import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import static org.junit.Assert.*;

public class DataUtilTest {

    private static final String BASE_URL = "http://example.com";
    private static final String CHARSET_UTF_8 = "UTF-8";

    private File file;
    private InputStream inputStream;

    @Before
    public void setUp() {
        file = new File("test.html");
        inputStream = null;
    }

    @Test
    public void testLoadFromFile() throws IOException {
        String expected = "<html><body><h1>Hello World!</h1></body></html>";
        Document document = DataUtil.load(file, CHARSET_UTF_8, BASE_URL);
        assertNotNull(document);
        assertEquals(expected, document.html());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String expected = "<html><body><h1>Hello World!</h1></body></html>";
        FileInputStream inStream = new FileInputStream(file);
        Document document = DataUtil.load(inStream, CHARSET_UTF_8, BASE_URL);
        assertNotNull(document);
        assertEquals(expected, document.html());
        inStream.close();
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String expected = "<html><body><h1>Hello World!</h1></body></html>";
        FileInputStream inStream = new FileInputStream(file);
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.load(inStream, CHARSET_UTF_8, BASE_URL, parser);
        assertNotNull(document);
        assertEquals(expected, document.html());
        inStream.close();
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body><h1>Hello World!</h1></body></html>".getBytes());
        String charsetName = CHARSET_UTF_8;
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, BASE_URL, parser);
        assertNotNull(document);
        assertEquals("<h1>Hello World!</h1>", document.select("h1").first().outerHtml());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        FileInputStream inStream = new FileInputStream(file);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        assertNotNull(byteData);
        inStream.close();
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        int maxSize = 1024;
        FileInputStream inStream = new FileInputStream(file);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream, maxSize);
        assertNotNull(byteData);
        inStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(CHARSET_UTF_8, charset);
    }
}