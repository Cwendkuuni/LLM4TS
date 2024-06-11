import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;

public class DataUtilTest {

    @Test
    public void testLoad() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadWithInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadWithCustomParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseByteData() {
        // Edge case: charsetName is null
        ByteBuffer byteData = ByteBuffer.wrap("This is a test".getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        int maxSize = 0;
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
        assertNotNull(byteData);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        int maxSize = 1024;
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
        assertNotNull(byteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}