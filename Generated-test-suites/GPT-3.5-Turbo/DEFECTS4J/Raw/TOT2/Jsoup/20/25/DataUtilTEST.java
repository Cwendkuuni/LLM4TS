import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {
    // Test case for load(File in, String charsetName, String baseUri)
    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(document);
    }

    // Test case for load(InputStream in, String charsetName, String baseUri)
    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
    }

    // Test case for load(InputStream in, String charsetName, String baseUri, Parser parser)
    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String html = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(document);
    }

    // Test case for parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser)
    @Test
    public void testParseByteData() {
        // Test edge case where charsetName is null
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        Document document = DataUtil.parseByteData(byteData, null, "http://example.com", Parser.xmlParser());
        assertNotNull(document);
    }

    // Test case for readToByteBuffer(InputStream inStream)
    @Test
    public void testReadToByteBuffer() throws IOException {
        // Test error scenario where input stream is empty
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals(0, byteBuffer.remaining());
    }

    // Test case for getCharsetFromContentType(String contentType)
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}