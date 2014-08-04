package edu.isi.karma.web.services.rdf;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class TestRDFGeneratorServlet {

//@Test
	public void testR2RMLRDF() {
		ClientConfig clientConfig = new DefaultClientConfig();
		Client client = Client.create(clientConfig);

		WebResource webRes = client.resource(UriBuilder.fromUri(
				"http://localhost:8080/rdf/r2rml/rdf").build());

		MultivaluedMap<String, String> formParams = new MultivaluedMapImpl();
		formParams.add(FormParameters.R2RML_URL,
				getTestResource("metadata.json-model.ttl").toString());
		formParams
				.add(FormParameters.RAW_DATA,
						"{\"metadata\":{\"GPSTimeStamp\":\"NOT_AVAILABLE\",\"ISOSpeedRatings\":\"100\",\"Orientation\":\"6\",\"Model\":\"GT-N7100\",\"WhiteBalance\":\"0\",\"GPSLongitude\":\"NOT_AVAILABLE\",\"ImageLength\":\"2448\",\"FocalLength\":\"3.7\",\"HasFaces\":\"1\",\"ImageName\":\"20140707_134558.jpg\",\"GPSDateStamp\":\"NOT_AVAILABLE\",\"Flash\":\"0\",\"DateTime\":\"2014:07:07 13:45:58\",\"NumberOfFaces\":\"1\",\"ExposureTime\":\"0.020\",\"GPSProcessingMethod\":\"NOT_AVAILABLE\",\"FNumber\":\"2.6\",\"ImageWidth\":\"3264\",\"GPSLatitude\":\"NOT_AVAILABLE\",\"GPSAltitudeRef\":\"-1\",\"Make\":\"SAMSUNG\",\"GPSAltitude\":\"-1.0\"}}");
		
		formParams.add(FormParameters.CONTENT_TYPE, FormParameters.CONTENT_TYPE_JSON);
		String response = webRes.type(MediaType.APPLICATION_FORM_URLENCODED)
				.post(String.class, formParams);
		System.out.print(response);
		String sampleTriple = "<20140707_134558.jpg> <http://www.semanticdesktop.org/ontologies/2007/05/10/nexif#make> \"SAMSUNG\" .";
		int idx = response.indexOf(sampleTriple);
		assert(idx != -1);
		
		String[] lines = response.split(System.getProperty("line.separator"));
		assertEquals(17, lines.length);
	}
	
//@Test
	public void testR2RMLRDFVirtuoso() throws IOException,
			MalformedURLException, ProtocolException {
		ClientConfig clientConfig = new DefaultClientConfig();
		Client client = Client.create(clientConfig);

		WebResource webRes = client.resource(UriBuilder.fromUri(
				"http://localhost:8080/rdf/r2rml/rdf/sparql").build());

		MultivaluedMap<String, String> formParams = new MultivaluedMapImpl();

		formParams.add(FormParameters.SPARQL_ENDPOINT,
				"http://fusion-sqid.isi.edu:8890/sparql-graph-crud-auth/");
		formParams.add(FormParameters.GRAPH_URI,
				"http://fusion-sqid.isi.edu:8890/image-metadata");
		formParams.add(FormParameters.TRIPLE_STORE,
				FormParameters.TRIPLE_STORE_VIRTUOSO);
		formParams.add(FormParameters.OVERWRITE, "True");
		formParams.add(FormParameters.R2RML_URL,
				getTestResource("metadata.json-model.ttl").toString());
		// formParams.add("DataURL", "");
		formParams
				.add(FormParameters.RAW_DATA,
						"{\"metadata\":{\"GPSTimeStamp\":\"NOT_AVAILABLE\",\"ISOSpeedRatings\":\"100\",\"Orientation\":\"6\",\"Model\":\"GT-N7100\",\"WhiteBalance\":\"0\",\"GPSLongitude\":\"NOT_AVAILABLE\",\"ImageLength\":\"2448\",\"FocalLength\":\"3.7\",\"HasFaces\":\"1\",\"ImageName\":\"20140707_134558.jpg\",\"GPSDateStamp\":\"NOT_AVAILABLE\",\"Flash\":\"0\",\"DateTime\":\"2014:07:07 13:45:58\",\"NumberOfFaces\":\"1\",\"ExposureTime\":\"0.020\",\"GPSProcessingMethod\":\"NOT_AVAILABLE\",\"FNumber\":\"2.6\",\"ImageWidth\":\"3264\",\"GPSLatitude\":\"NOT_AVAILABLE\",\"GPSAltitudeRef\":\"-1\",\"Make\":\"SAMSUNG\",\"GPSAltitude\":\"-1.0\"}}");
		formParams.add(FormParameters.CONTENT_TYPE, FormParameters.CONTENT_TYPE_JSON);
		formParams.add(FormParameters.USERNAME, "finimg");
		formParams.add(FormParameters.PASSWORD, "isi");

		String response = webRes.type(MediaType.APPLICATION_FORM_URLENCODED)
				.post(String.class, formParams);
		System.out.print(response);
		String sampleTriple = "<20140707_134558.jpg> <http://www.semanticdesktop.org/ontologies/2007/05/10/nexif#make> \"SAMSUNG\" .";
		int idx = response.indexOf(sampleTriple);
		assert(idx != -1);
		
		String[] lines = response.split(System.getProperty("line.separator"));
		assertEquals(17, lines.length);
	}

//	@Test
	public void testR2RMLRDFSesame() {
		//TODO: Add testcase for Sesame
	}

//	@Test
	public void sendPOSTRequestTextPlain() throws IOException,
			MalformedURLException, ProtocolException {
		HttpURLConnection urlCon;
		URL url = new URL("http://localhost:8080/rdf/metadata/images");
		String strJSON = "{\"metadata\":{\"GPSTimeStamp\":\"NOT_AVAILABLE\",\"ISOSpeedRatings\":\"100\",\"Orientation\":\"6\",\"Model\":\"GT-N7100\",\"WhiteBalance\":\"0\",\"GPSLongitude\":\"NOT_AVAILABLE\",\"ImageLength\":\"2448\",\"FocalLength\":\"3.7\",\"HasFaces\":\"1\",\"ImageName\":\"20140707_134558.jpg\",\"GPSDateStamp\":\"NOT_AVAILABLE\",\"Flash\":\"0\",\"DateTime\":\"2014:07:07 13:45:58\",\"NumberOfFaces\":\"1\",\"ExposureTime\":\"0.020\",\"GPSProcessingMethod\":\"NOT_AVAILABLE\",\"FNumber\":\"2.6\",\"ImageWidth\":\"3264\",\"GPSLatitude\":\"NOT_AVAILABLE\",\"GPSAltitudeRef\":\"-1\",\"Make\":\"SAMSUNG\",\"GPSAltitude\":\"-1.0\"}}";

		urlCon = (HttpURLConnection) url.openConnection();

		urlCon.setDoOutput(true); // its a POST request

		urlCon.setRequestMethod("POST"); // is not needed, but still...

		urlCon.setRequestProperty("Content-Type", "text/plain");

		// byte[] data = strJSON.getBytes("UTF-8");
		urlCon.setFixedLengthStreamingMode(strJSON.length());

		OutputStream os = urlCon.getOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,
				"UTF-8"));

		writer.write(strJSON);
		writer.flush();
		writer.close();
		os.close();

		urlCon.connect();

		System.out.print("run");
	}

	private URL getTestResource(String name) {
		return getClass().getClassLoader().getResource(name);
	}

}