import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    private Document doc;

    @Before
    public void setUp() throws Exception {
        File file = new File("test.html");
        doc = DataUtil.load(file, "UTF-8", "");
    }

    @After
    public void tearDown() throws Exception {
        doc = null;
    }

    @Test
    public void testLoad() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        File file = new File("test.html");
        FileInputStream inStream = new FileInputStream(file);
        Document doc = DataUtil.load(inStream, "UTF-8", "");
        assertNotNull(doc);
        inStream.close();
    }

    @Test
    public void testLoadInputStreamParser() throws IOException {
        File file = new File("test.html");
        FileInputStream inStream = new FileInputStream(file);
        Document doc = DataUtil.load(inStream, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
        inStream.close();
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(10);
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File file = new File("test.html");
        FileInputStream inStream = new FileInputStream(file);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        assertNotNull(byteData);
        inStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String contentType2 = "text/html";
        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        assertNotNull(charset1);
        assertNull(charset2);
        assertEquals(charset1, "UTF-8");
    }
}