import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {

    private File testFile;
    private String charsetName = "UTF-8";
    private String baseUri = "http://example.com";

    @Before
    public void setUp() {
        testFile = new File("test.html");
    }

    @Test
    public void testLoadFromFile() throws IOException {
        Document document = DataUtil.load(testFile, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteData() {
        String html = "<html><head></head><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes());
        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}