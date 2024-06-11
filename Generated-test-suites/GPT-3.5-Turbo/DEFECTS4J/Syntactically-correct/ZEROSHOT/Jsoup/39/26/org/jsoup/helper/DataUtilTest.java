import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Locale;

import static org.junit.Assert.*;

public class DataUtilTest {

    private static final String CHARSET_UTF8 = "UTF-8";
    
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("example.html");
        Document document = DataUtil.load(file, CHARSET_UTF8, "");
        assertNotNull(document);
        // add more assertions
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("example.html");
        Document document = DataUtil.load(inputStream, CHARSET_UTF8, "");
        assertNotNull(document);
        // add more assertions
    }

    @Test
    public void testLoadParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("example.html");
        Document document = DataUtil.load(inputStream, CHARSET_UTF8, "", Parser.htmlParser());
        assertNotNull(document);
        // add more assertions
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(100);
        String charsetName = CHARSET_UTF8;
        String baseUri = "";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        // add more assertions
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("example.html");
        int maxSize = 100000;
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        assertNotNull(byteBuffer);
        assertEquals(maxSize, byteBuffer.capacity());
        // add more assertions
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("example.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
        // add more assertions
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
        // add more assertions
    }
}