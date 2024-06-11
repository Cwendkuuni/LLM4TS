import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File testFile = new File("test.html");
        try {
            Document doc = DataUtil.load(testFile, "UTF-8", "http://example.com");
            assertNotNull(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
            assertNotNull(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
            assertNotNull(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadToByteBufferCapped() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 100);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadToByteBufferUncapped() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

}