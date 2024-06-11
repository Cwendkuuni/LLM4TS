import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws Exception {
        // Test normal case
        Document document = DataUtil.load(new File("file.html"), "UTF-8", "http://example.com");
        // Assert the expected result
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testLoadInputStream() throws Exception {
        // Test normal case
        InputStream inputStream = new FileInputStream(new File("file.html"));
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        // Assert the expected result
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        // Test normal case
        InputStream inputStream = new FileInputStream(new File("file.html"));
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        // Assert the expected result
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testParseByteData() throws Exception {
        // Test normal case
        ByteBuffer byteBuffer = ByteBuffer.wrap("<html></html>".getBytes());
        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", "http://example.com", Parser.htmlParser());
        // Assert the expected result
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        // Test normal case
        InputStream inputStream = new FileInputStream(new File("file.html"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        // Assert the expected result
        assertEquals(1024, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() throws Exception {
        // Test normal case
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        // Assert the expected result
        assertEquals("UTF-8", charset);
    }
}