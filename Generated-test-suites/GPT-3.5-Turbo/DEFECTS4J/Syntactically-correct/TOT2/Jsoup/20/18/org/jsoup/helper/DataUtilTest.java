import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(document);
        // Add more assertions as needed
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream stream = new FileInputStream("test.html");
        Document document = DataUtil.load(stream, "UTF-8", "http://example.com");
        assertNotNull(document);
        // Add more assertions as needed
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream stream = new FileInputStream("test.html");
        Document document = DataUtil.load(stream, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(document);
        // Add more assertions as needed
    }

    @Test
    public void testParseByteData() {
        // Test edge cases and error scenarios for parseByteData method
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream stream = new FileInputStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(stream);
        assertNotNull(byteBuffer);
        // Add more assertions as needed
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
        // Add more assertions as needed
    }

}