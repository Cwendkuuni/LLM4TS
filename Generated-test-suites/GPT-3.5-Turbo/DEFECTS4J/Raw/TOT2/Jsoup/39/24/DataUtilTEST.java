import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("testFile.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream in = new FileInputStream("testFile.html");
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream in = new FileInputStream("testFile.html");
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteData() {
        // Edge case: charsetName is null
        // Should still return a document without throwing exceptions
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        Document doc = DataUtil.parseByteData(byteData, null, "http://example.com", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream in = new FileInputStream("testFile.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, 100);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Typical case: content type has charset specified
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        // Edge case: content type does not have charset specified
        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);

        // Error scenario: invalid charset
        contentType = "text/html; charset=invalid";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}