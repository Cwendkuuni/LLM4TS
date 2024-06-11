import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        try {
            Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testLoadInputStream() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    // Edge case: charsetName is null
    @Test
    public void testParseByteDataWithNullCharsetName() {
        ByteBuffer byteData = ByteBuffer.wrap("Sample data".getBytes());
        Document doc = DataUtil.parseByteData(byteData, null, "http://example.com", Parser.htmlParser());
        assertNotNull(doc);
    }

    // Exception handling: IOException while reading from InputStream
    @Test
    public void testReadToByteBufferWithIOException() {
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Failed to read from InputStream");
            }
        };
        try {
            DataUtil.readToByteBuffer(inputStream);
            fail("IOException was not thrown");
        } catch (IOException e) {
            assertEquals("Failed to read from InputStream", e.getMessage());
        }
    }

    // Edge case: contentType does not contain charset
    @Test
    public void testGetCharsetFromContentTypeWithoutCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    // Edge case: contentType contains charset
    @Test
    public void testGetCharsetFromContentTypeWithCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    // Edge case: contentType contains charset with leading and trailing whitespace
    @Test
    public void testGetCharsetFromContentTypeWithWhitespace() {
        String contentType = "text/html; charset=  UTF-8  ";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

}