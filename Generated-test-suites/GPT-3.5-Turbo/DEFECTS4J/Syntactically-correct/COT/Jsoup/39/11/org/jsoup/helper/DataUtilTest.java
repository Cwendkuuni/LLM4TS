
import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        try {
            File file = new File("path/to/file");
            String charsetName = "UTF-8";
            String baseUri = "https://example.com";
            Document document = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            InputStream in = getClass().getResourceAsStream("input.html");
            String charsetName = "UTF-8";
            String baseUri = "https://example.com";
            Document document = DataUtil.load(in, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream in = getClass().getResourceAsStream("input.html");
            String charsetName = "UTF-8";
            String baseUri = "https://example.com";
            Parser parser = Parser.htmlParser();
            Document document = DataUtil.load(in, charsetName, baseUri, parser);
            assertNotNull(document);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testParseByteData() {
        try {
            ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
            String charsetName = "UTF-8";
            String baseUri = "https://example.com";
            Parser parser = Parser.htmlParser();
            Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
            assertNotNull(document);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream in = getClass().getResourceAsStream("input.html");
            int maxSize = 0;
            ByteBuffer byteData = DataUtil.readToByteBuffer(in, maxSize);
            assertNotNull(byteData);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testReadToByteBufferWithMaxSize() {
        try {
            InputStream in = getClass().getResourceAsStream("input.html");
            int maxSize = 1024;
            ByteBuffer byteData = DataUtil.readToByteBuffer(in, maxSize);
            assertNotNull(byteData);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}