import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    private static final String FILE_PATH = "test.html";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "http://example.com";

    private File testFile;

    @Before
    public void setup() {
        testFile = new File(FILE_PATH);
    }

    @Test
    public void testLoadFromFile() throws Exception {
        Document doc = DataUtil.load(testFile, CHARSET_NAME, BASE_URI);

        assertNotNull(doc);
        assertEquals(doc.baseUri(), BASE_URI);
    }

    @Test
    public void testLoadFromInputStream() throws Exception {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI);

        assertNotNull(doc);
        assertEquals(doc.baseUri(), BASE_URI);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws Exception {
        String xml = "<root><item>Test</item></root>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI, Parser.xmlParser());

        assertNotNull(doc);
        assertEquals(doc.baseUri(), BASE_URI);
        assertEquals(doc.title(), "");
    }

    @Test
    public void testParseByteData() {
        byte[] data = "<html><body><h1>Hello, World!</h1></body></html>".getBytes(StandardCharsets.UTF_8);
        Document doc = DataUtil.parseByteData(ByteBuffer.wrap(data), CHARSET_NAME, BASE_URI, Parser.htmlParser());

        assertNotNull(doc);
        assertEquals(doc.baseUri(), BASE_URI);
        assertEquals(doc.body().getElementsByTag("h1").text(), "Hello, World!");
    }

    @Test
    public void testReadToByteBuffer() {
        byte[] data = "<html><body><h1>Hello, World!</h1></body></html>".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);

        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

            assertNotNull(byteData);
            assertEquals(byteData.array().length, data.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals(charset, "UTF-8");
    }

    @Test
    public void testGetCharsetFromContentTypeNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }
}