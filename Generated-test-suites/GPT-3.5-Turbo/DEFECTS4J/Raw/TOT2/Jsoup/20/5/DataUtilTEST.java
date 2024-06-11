import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        // Typical use case
        File file = new File("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document document = DataUtil.load(file, charset, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStream() {
        // Typical use case
        InputStream inputStream = new FileInputStream("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document document = DataUtil.load(inputStream, charset, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        // Typical use case
        InputStream inputStream = new FileInputStream("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        try {
            Document document = DataUtil.load(inputStream, charset, baseUri, parser);
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseByteData() {
        // Edge case - charsetName is null
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() {
        // Error scenario - invalid input stream
        InputStream inputStream = null;
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        } catch (IOException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Typical use case
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        // Edge case - no charset specified
        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charset);
    }
}