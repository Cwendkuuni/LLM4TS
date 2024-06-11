import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("test.html"); // example file
        try {
            Document doc = DataUtil.load(file, "UTF-8", "http://www.example.com");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", "http://www.example.com");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", "http://www.example.com", Parser.xmlParser());
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testCrossStreams() {
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            DataUtil.crossStreams(in, out);
            assertEquals("test", out.toString());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "http://www.example.com", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() {
        ByteArrayInputStream inStream = new ByteArrayInputStream("test".getBytes());
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream, 10);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadToByteBufferUnlimited() {
        ByteArrayInputStream inStream = new ByteArrayInputStream("test".getBytes());
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadFileToByteBuffer() {
        File file = new File("test.html"); // example file
        try {
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html; charset=EUC-JP";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("EUC-JP", charset);

        contentType = "text/html; charset=invalid";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String mime = DataUtil.mimeBoundary();
        assertNotNull(mime);
        assertEquals(32, mime.length());
    }
}