import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.jsoup.helper.DataUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.*;

public class DataUtilTest {
    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "http://www.example.com/";
    private DataUtil dataUtil;

    @Before
    public void setUp() {
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoadFile() {
        try {
            File file = new File("test.html");
            Document doc = DataUtil.load(file, CHARSET_NAME, BASE_URI);

            Assert.assertNotNull(doc);
            Assert.assertEquals(CHARSET_NAME, doc.charset().name());
            Assert.assertEquals(BASE_URI, doc.baseUri());
        } catch (IOException e) {
            Assert.fail("IOException caught: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            InputStream in = new FileInputStream("test.html");
            Document doc = DataUtil.load(in, CHARSET_NAME, BASE_URI);

            Assert.assertNotNull(doc);
            Assert.assertEquals(CHARSET_NAME, doc.charset().name());
            Assert.assertEquals(BASE_URI, doc.baseUri());
        } catch (IOException e) {
            Assert.fail("IOException caught: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream in = new FileInputStream("test.xml");
            Document doc = DataUtil.load(in, CHARSET_NAME, BASE_URI, Parser.xmlParser());

            Assert.assertNotNull(doc);
            Assert.assertEquals(CHARSET_NAME, doc.charset().name());
            Assert.assertEquals(BASE_URI, doc.baseUri());
            Assert.assertTrue(doc.parser() instanceof Parser);
        } catch (IOException e) {
            Assert.fail("IOException caught: " + e.getMessage());
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            InputStream in = new ByteArrayInputStream("test string".getBytes());
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            DataUtil.crossStreams(in, out);

            String result = out.toString();

            Assert.assertEquals("test string", result);
        } catch (IOException e) {
            Assert.fail("IOException caught: " + e.getMessage());
        }
    }

    @Test
    public void testParseInputStream() {
        try {
            InputStream in = new FileInputStream("test.html");
            Document doc = DataUtil.parseInputStream(in, CHARSET_NAME, BASE_URI, Parser.htmlParser());

            Assert.assertNotNull(doc);
            Assert.assertEquals(CHARSET_NAME, doc.charset().name());
            Assert.assertEquals(BASE_URI, doc.baseUri());
        } catch (IOException e) {
            Assert.fail("IOException caught: " + e.getMessage());
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            File file = new File("test.html");
            InputStream in = new FileInputStream(file);
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in);

            Assert.assertNotNull(byteBuffer);
            Assert.assertEquals(file.length(), byteBuffer.remaining());
        } catch (IOException e) {
            Assert.fail("IOException caught: " + e.getMessage());
        }
    }

    @Test
    public void testReadFileToByteBuffer() {
        try {
            File file = new File("test.html");
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

            Assert.assertNotNull(byteBuffer);
            Assert.assertEquals(file.length(), byteBuffer.remaining());
        } catch (IOException e) {
            Assert.fail("IOException caught: " + e.getMessage());
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(0, byteBuffer.remaining());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertNotNull(charset);
        Assert.assertEquals(CHARSET_NAME, charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        Assert.assertNotNull(boundary);
        Assert.assertEquals(DataUtil.boundaryLength, boundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byte[] bomBE = {0x00, 0x00, (byte) 0xFE, (byte) 0xFF};
        byteBuffer.put(bomBE);

        DataUtil.BomCharset bomBECharset = DataUtil.detectCharsetFromBom(byteBuffer);

        Assert.assertNotNull(bomBECharset);
        Assert.assertEquals("UTF-32", bomBECharset.charset);
        Assert.assertEquals(0, bomBECharset.offset);

        byteBuffer.clear();
        byte[] bomLE = {(byte) 0xFF, (byte) 0xFE, 0x00, 0x00};
        byteBuffer.put(bomLE);

        DataUtil.BomCharset bomLECharset = DataUtil.detectCharsetFromBom(byteBuffer);

        Assert.assertNotNull(bomLECharset);
        Assert.assertEquals("UTF-32", bomLECharset.charset);
        Assert.assertEquals(0, bomLECharset.offset);

        byteBuffer.clear();
        byte[] bomUTF8 = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        byteBuffer.put(bomUTF8);

        DataUtil.BomCharset bomUTF8Charset = DataUtil.detectCharsetFromBom(byteBuffer);

        Assert.assertNotNull(bomUTF8Charset);
        Assert.assertEquals("UTF-8", bomUTF8Charset.charset);
        Assert.assertEquals(3, bomUTF8Charset.offset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-32";
        String validCharset = DataUtil.validateCharset(charset);

        Assert.assertNotNull(validCharset);
        Assert.assertEquals(charset, validCharset);

        charset = "InvalidCharset";
        validCharset = DataUtil.validateCharset(charset);

        Assert.assertNull(validCharset);
    }
}