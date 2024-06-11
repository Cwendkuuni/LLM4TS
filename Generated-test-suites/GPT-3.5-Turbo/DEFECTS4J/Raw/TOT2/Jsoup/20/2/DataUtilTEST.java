import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.Before;
import java.io.IOException;
import java.io.File;
import java.io.ByteArrayInputStream;
import static org.junit.Assert.assertEquals;

public class DataUtilTest {

    private Document testDocument;

    @Before
    public void setup() {
        testDocument = new Document("");
    }

    @Test
    public void testLoadFromFile() throws IOException {
        File testFile = new File("testFile.html");
        Document loadedDocument = DataUtil.load(testFile, "UTF-8", "http://example.com");
        assertEquals(testDocument, loadedDocument);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String htmlData = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlData.getBytes());
        Document loadedDocument = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertEquals(testDocument, loadedDocument);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String htmlData = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlData.getBytes());
        Document loadedDocument = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertEquals(testDocument, loadedDocument);
    }

    @Test
    public void testParseByteDataWithCharset() {
        String htmlData = "<html><head></head><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(htmlData.getBytes());
        Document parsedDocument = DataUtil.parseByteData(byteBuffer, "UTF-8", "http://example.com", Parser.htmlParser());
        assertEquals(testDocument, parsedDocument);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}