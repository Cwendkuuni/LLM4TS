import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadWithFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testLoadWithInputStream() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(fileInputStream, "UTF-8", "http://example.com");
        assertNotNull(doc);
        fileInputStream.close();
    }

    @Test
    public void testLoadWithInputStreamAndParser() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(fileInputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(doc);
        fileInputStream.close();
    }

    @Test
    public void testParseByteData() {
        // Test with null charsetName
        Document doc = DataUtil.parseByteData(null, null, "http://example.com", Parser.htmlParser());
        assertNull(doc);

        // Test with specified charsetName
        ByteBuffer byteBuffer = ByteBuffer.wrap("Test document".getBytes());
        doc = DataUtil.parseByteData(byteBuffer, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test input stream".getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
        assertEquals("Test input stream", Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test with null content type
        String charset = DataUtil.getCharsetFromContentType(null);
        assertNull(charset);

        // Test with valid content type
        charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", charset);

        // Test with unsupported charset
        charset = DataUtil.getCharsetFromContentType("text/html; charset=unknown");
        assertNull(charset);
    }
}