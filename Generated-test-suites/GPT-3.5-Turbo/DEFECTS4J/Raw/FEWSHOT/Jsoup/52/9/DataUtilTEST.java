import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class DataUtilTest {
	
	@Test
	public void testLoadWithFile() throws IOException {
		File file = new File("test.html");
		Document document = DataUtil.load(file, "UTF-8", "");
		assertEquals("HTML", document.tagName());
		assertTrue(document.hasText());
	}
	
	@Test
	public void testLoadWithInputStreamAndCharset() throws IOException {
		String html = "<html><body><h1>Test</h1></body></html>";
		InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8")));
		Document document = DataUtil.load(inputStream, "UTF-8", "");
		assertEquals("HTML", document.tagName());
		assertTrue(document.hasText());
	}

	@Test
	public void testLoadWithInputStreamAndCharsetAndParser() throws IOException {
		String xml = "<xml><body><h1>Test</h1></body></xml>";
		InputStream inputStream = new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8")));
		Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());
		assertEquals("XML", document.tagName());
		assertTrue(document.hasText());
	}
	
	@Test
	public void testCrossStreams() throws IOException {
		String data = "This is a test";
		InputStream inputStream = new ByteArrayInputStream(data.getBytes(Charset.forName("UTF-8")));
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		DataUtil.crossStreams(inputStream, outputStream);
		String outputData = outputStream.toString(Charset.forName("UTF-8"));
		assertEquals(data, outputData);
	}
	
	@Test
	public void testParseByteData() {
		ByteBuffer byteData = ByteBuffer.allocate(0);
		String charsetName = "UTF-8";
		String baseUri = "";
		Parser parser = Parser.htmlParser();
		
		Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
		assertNull(document);
	}
	
	@Test
	public void testReadToByteBuffer() throws IOException {
		String data = "This is a test";
		InputStream inputStream = new ByteArrayInputStream(data.getBytes(Charset.forName("UTF-8")));
		ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
		String outputData = Charset.forName("UTF-8").decode(byteBuffer).toString();
		assertEquals(data, outputData);
	}
	
	@Test
	public void testEmptyByteBuffer() {
		ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
		assertEquals(0, byteBuffer.capacity());
	}
	
	@Test
	public void testGetCharsetFromContentType() {
		String contentType = "text/html; charset=UTF-8";
		String charset = DataUtil.getCharsetFromContentType(contentType);
		assertEquals("UTF-8", charset);
	}
	
	@Test
	public void testMimeBoundary() {
		String mimeBoundary = DataUtil.mimeBoundary();
		assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
	}
	
	@Test
	public void testDetectCharsetFromBom() {
		ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
		String charsetName = DataUtil.detectCharsetFromBom(byteData, null);
		assertEquals("UTF-8", charsetName);
	}

}