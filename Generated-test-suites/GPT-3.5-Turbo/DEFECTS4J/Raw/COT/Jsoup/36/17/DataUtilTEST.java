import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFromInputStream() throws IOException {
        String htmlString = "<html><body><p>Hello, World!</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlString.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "UTF-8", "");

        assertNotNull(document);
        assertEquals("Hello, World!", document.body().text());
    }

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");

        Document document = DataUtil.load(file, "UTF-8", "");

        assertNotNull(document);
        assertEquals("Hello, World!", document.body().text());
    }

    @Test(expected = IOException.class)
    public void testLoadFromFileThrowsIOException() throws IOException {
        File file = new File("nonexistent.html");

        DataUtil.load(file, "UTF-8", "");
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String xmlString = "<root><element>Hello, World!</element></root>";
        InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());

        assertNotNull(document);
        assertEquals("Hello, World!", document.select("element").text());
    }

    @Test
    public void testParseByteData() {
        // Test with null charsetName
        ByteBuffer byteBuffer = ByteBuffer.wrap("<html><body><p>Hello, World!</p></body></html>".getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteBuffer, null, "", Parser.htmlParser());
        assertNotNull(document);
        assertEquals("Hello, World!", document.body().text());

        // Test with specified charsetName
        byteBuffer = ByteBuffer.wrap("<html><body><p>Hello, World!</p></body></html>".getBytes(StandardCharsets.UTF_8));
        document = DataUtil.parseByteData(byteBuffer, "UTF-8", "", Parser.htmlParser());
        assertNotNull(document);
        assertEquals("Hello, World!", document.body().text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlString = "<html><body><p>Hello, World!</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlString.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        String decodedString = new String(byteBuffer.array(), StandardCharsets.UTF_8);
        assertEquals(htmlString, decodedString);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String htmlString = "<html><body><p>Hello, World!</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlString.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);

        String decodedString = new String(byteBuffer.array(), StandardCharsets.UTF_8);
        assertEquals("<html><bod", decodedString);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html; charset=EUC-JP";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("EUC-JP", charset);

        contentType = "text/html; charset=nonexistent";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charset);

        contentType = null;
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charset);
    }
}