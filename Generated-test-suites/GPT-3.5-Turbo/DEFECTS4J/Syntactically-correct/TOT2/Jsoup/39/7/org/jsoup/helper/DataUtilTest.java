import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        try {
            Document document = DataUtil.load(file, "UTF-8", "https://www.example.com");
            assertNotNull(document);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            URL url = new URL("https://www.example.com");
            InputStream inputStream = url.openStream();
            Document document = DataUtil.load(inputStream, "UTF-8", "https://www.example.com");
            assertNotNull(document);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            URL url = new URL("https://www.example.com");
            InputStream inputStream = url.openStream();
            Document document = DataUtil.load(inputStream, "UTF-8", "https://www.example.com", Parser.xmlParser());
            assertNotNull(document);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File("test.txt"));
            DataUtil.readToByteBuffer(fileInputStream, 0);
            assertTrue(true);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}