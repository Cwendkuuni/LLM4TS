import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

public class DataUtilTest {

    private static final String BASE_URI = "https://www.example.com";
    private static final File TEST_FILE = new File("test.html");
    private static final String CHARSET_NAME = "UTF-8";

    private Document testDocument;

    @Before
    public void setUp() {
        try {
            testDocument = DataUtil.load(TEST_FILE, CHARSET_NAME, BASE_URI);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadFromInputStream() {
        try (InputStream inputStream = new FileInputStream(TEST_FILE)) {
            Document document = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI);
            Assert.assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        try (InputStream inputStream = new FileInputStream(TEST_FILE)) {
            Document document = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());
            Assert.assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        byteData.put("Test byte data".getBytes());
        byteData.flip();
        Document document = DataUtil.parseByteData(byteData, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() {
        try (InputStream inputStream = new FileInputStream(TEST_FILE)) {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 1024);
            Assert.assertNotNull(byteData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }
}