import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

	@Test
	public void testLoadFile() throws IOException {
		File file = new File("test.html");
		String charsetName = "UTF-8";
		String baseUri = "http://example.com";
		Document doc = DataUtil.load(file, charsetName, baseUri);
		
		// Test if document is loaded correctly
		assertNotNull(doc);
		
		// Test if document has correct base URI
		assertEquals(baseUri, doc.baseUri());
		
		// Test if document has correct charset
		assertEquals(charsetName, doc.outputSettings().charset().displayName());
	}

	@Test
	public void testLoadInputStream() throws IOException {
		InputStream in = new FileInputStream("test.html");
		String charsetName = "UTF-8";
		String baseUri = "http://example.com";
		Document doc = DataUtil.load(in, charsetName, baseUri);
		
		// Test if document is loaded correctly
		assertNotNull(doc);
		
		// Test if document has correct base URI
		assertEquals(baseUri, doc.baseUri());
		
		// Test if document has correct charset
		assertEquals(charsetName, doc.outputSettings().charset().displayName());
		
		// Test if input stream is closed
		assertTrue(in.available() == 0);
		in.close();
	}

	@Test
	public void testLoadInputStreamParser() throws IOException {
		InputStream in = new FileInputStream("test.html");
		String charsetName = "UTF-8";
		String baseUri = "http://example.com";
		Parser parser = Parser.htmlParser();
		Document doc = DataUtil.load(in, charsetName, baseUri, parser);
		
		// Test if document is loaded correctly
		assertNotNull(doc);
		
		// Test if document has correct base URI
		assertEquals(baseUri, doc.baseUri());
		
		// Test if document has correct charset
		assertEquals(charsetName, doc.outputSettings().charset().displayName());
		
		// Test if input stream is closed
		assertTrue(in.available() == 0);
		in.close();
	}
	
	@Test
	public void testCrossStreams() throws IOException {
		InputStream in = new FileInputStream("test.txt");
		OutputStream out = new ByteArrayOutputStream();
		DataUtil.crossStreams(in, out);
		
		// Test if all data is copied from input stream to output stream
		assertEquals(in.available(), out.size());
		
		// Test if input stream is closed
		assertTrue(in.available() == 0);
		in.close();
		
		// Test if output stream is not empty
		assertTrue(out.size() > 0);
		out.close();
	}
	
	@Test
	public void testParseByteData() {
		ByteBuffer byteData = ByteBuffer.allocate(1024);
		String charsetName = "UTF-8";
		String baseUri = "http://example.com";
		Parser parser = Parser.htmlParser();
		
		Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
		
		// Test if document exists
		assertNotNull(doc);
		
		// Test if document has correct base URI
		assertEquals(baseUri, doc.baseUri());
		
		// Test if document has correct charset
		assertEquals(charsetName, doc.outputSettings().charset().displayName());
	}
	
	@Test
	public void testReadToByteBuffer() throws IOException {
		InputStream in = new FileInputStream("test.txt");
		int maxSize = 1024;
		ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, maxSize);
		
		// Test if byte buffer is not null
		assertNotNull(byteBuffer);
		
		// Test if byte buffer has correct size
		assertEquals(maxSize, byteBuffer.limit());
		
		// Test if input stream is closed
		assertTrue(in.available() == 0);
		in.close();
	}

	@Test
	public void testReadToByteBufferUnlimited() throws IOException {
		InputStream in = new FileInputStream("test.txt");
		ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in);
		
		// Test if byte buffer is not null
		assertNotNull(byteBuffer);
		
		// Test if input stream is closed
		assertTrue(in.available() == 0);
		in.close();
	}
	
	@Test
	public void testReadFileToByteBuffer() throws IOException {
		File file = new File("test.txt");
		ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
		
		// Test if byte buffer is not null
		assertNotNull(byteBuffer);
		
		// Test if byte buffer has correct size
		assertEquals(file.length(), byteBuffer.limit());
	}
	
	@Test
	public void testEmptyByteBuffer() {
		ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
		
		// Test if byte buffer is not null
		assertNotNull(byteBuffer);
		
		// Test if byte buffer is empty
		assertEquals(0, byteBuffer.limit());
	}

	@Test
	public void testGetCharsetFromContentType() {
		String contentType = "text/html; charset=EUC-JP";
		String charset = DataUtil.getCharsetFromContentType(contentType);
		
		// Test if charset is not null
		assertNotNull(charset);
		
		// Test if charset is correct
		assertEquals("EUC-JP", charset);
	}

	@Test
	public void testGetCharsetFromContentTypeNotSupported() {
		String contentType = "text/html; charset=INVALID_CHARSET";
		String charset = DataUtil.getCharsetFromContentType(contentType);
		
		// Test if charset is null (because it is not supported)
		assertNull(charset);
	}

	@Test
	public void testMimeBoundary() {
		String boundary = DataUtil.mimeBoundary();
		
		// Test if boundary is not null
		assertNotNull(boundary);
		
		// Test if boundary has correct length
		assertEquals(DataUtil.boundaryLength, boundary.length());
	}

	@Test
	public void testDetectCharsetFromBomUTF16LE() {
		ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xFF, (byte) 0xFE, 'T', 0, 'e', 0, 's', 0, 't', 0});
		String charsetName = "UTF-8";
		String detectedCharset = DataUtil.detectCharsetFromBom(byteData, charsetName);
		
		// Test if detected charset is correct
		assertEquals("UTF-16", detectedCharset);
		
		// Test if byte data is rewound
		assertEquals(0, byteData.position());
	}

	@Test
	public void testDetectCharsetFromBomUTF8() {
		ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 'T', 'e', 's', 't'});
		String charsetName = "UTF-8";
		String detectedCharset = DataUtil.detectCharsetFromBom(byteData, charsetName);
		
		// Test if detected charset is correct
		assertEquals("UTF-8", detectedCharset);
		
		// Test if byte data is rewound
		assertEquals(3, byteData.position());
	}
}