import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(document);
        // Add more assertions here if needed
    }

    @Test
    public void testLoadInputStream() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(fileInputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
        // Add more assertions here if needed
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(fileInputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(document);
        // Add more assertions here if needed
    }

    @Test
    public void testParseByteData() {
        // Add test cases for parseByteData method with edge cases and exceptions
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(fileInputStream, 100);
        assertNotNull(byteBuffer);
        // Add more assertions here if needed
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

}