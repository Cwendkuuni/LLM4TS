
import org.jsoup.nodes.Document;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
        // Add additional assertions as needed
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
        // Add additional assertions as needed
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
        // Add additional assertions as needed
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new FileInputStream("input.txt");
        OutputStream outputStream = new FileOutputStream("output.txt");
        DataUtil.crossStreams(inputStream, outputStream);
        // Add assertions to check if the input stream is written to the output stream correctly
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("input.txt");
        int maxSize = 1024;
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        assertNotNull(byteBuffer);
        // Add additional assertions as needed
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
        // Add additional assertions as needed
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        // Add additional assertions as needed
    }
}

