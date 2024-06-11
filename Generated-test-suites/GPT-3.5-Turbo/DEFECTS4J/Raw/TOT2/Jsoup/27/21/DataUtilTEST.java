import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoadWithParser() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseByteData() {
        // Test parsing byte data with different charsets and base URIs
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream inputStream = new ByteArrayInputStream(data);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        assertNotNull(byteBuffer);
        assertEquals(data.length, byteBuffer.array().length);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }
}