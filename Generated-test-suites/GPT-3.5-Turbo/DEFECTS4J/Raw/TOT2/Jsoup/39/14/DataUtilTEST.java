import org.jsoup.nodes.Document;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.Charset;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        try {
            Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            FileInputStream fileInputStream = new FileInputStream("test.html");
            Document doc = DataUtil.load(fileInputStream, "UTF-8", "http://example.com");
            assertNotNull(doc);
            fileInputStream.close();
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            FileInputStream fileInputStream = new FileInputStream("test.html");
            Document doc = DataUtil.load(fileInputStream, "UTF-8", "http://example.com", Parser.htmlParser());
            assertNotNull(doc);
            fileInputStream.close();
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("test data".getBytes());
        Document doc = DataUtil.parseByteData(byteBuffer, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() {
        byte[] testData = "test data".getBytes();
        InputStream inputStream = new ByteArrayInputStream(testData);
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
            assertEquals(testData.length, byteBuffer.remaining());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html; charset=ISO-8859-1";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("ISO-8859-1", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}