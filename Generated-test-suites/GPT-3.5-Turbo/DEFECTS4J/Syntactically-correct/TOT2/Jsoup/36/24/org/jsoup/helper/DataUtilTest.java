import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        try {
            File file = new File("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";

            Document doc = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(doc);

        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            String html = "<html><head></head><body></body></html>";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";

            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(doc);

        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            String html = "<html><head></head><body></body></html>";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Parser parser = Parser.htmlParser();

            Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(doc);

        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testReadToByteBufferWithMaxSize() {
        try {
            String content = "Test content for byte buffer reading";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
            int maxSize = 10;

            DataUtil.readToByteBuffer(inputStream, maxSize);

        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            String content = "Test content for byte buffer reading";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

            DataUtil.readToByteBuffer(inputStream);

        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}