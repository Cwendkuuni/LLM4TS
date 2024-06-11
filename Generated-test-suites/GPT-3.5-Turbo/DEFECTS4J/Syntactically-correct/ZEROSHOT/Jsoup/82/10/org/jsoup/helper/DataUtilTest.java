import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {

    private Document document;

    @Before
    public void setUp() {
        File file = new File("test.html");
        try {
            document = DataUtil.load(file, "UTF-8", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadFromFile() {
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStream() {
        InputStream inputStream = null;
        try {
            inputStream = getClass().getResourceAsStream("test.html");
            document = DataUtil.load(inputStream, "UTF-8", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(document);
    }
    
    @Test
    public void testCrossStreams() {
        InputStream inputStream = null;
        String content = "Hello, World!";
        ByteBuffer byteBuffer = ByteBuffer.wrap(content.getBytes());
        try {
            inputStream = DataUtil.class.getResourceAsStream("test.html");
            DataUtil.crossStreams(inputStream, System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseInputStream() {
        InputStream inputStream = null;
        try {
            inputStream = DataUtil.class.getResourceAsStream("test.html");
            document = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() {
        InputStream inputStream = null;
        ByteBuffer byteBuffer = null;
        try {
            inputStream = DataUtil.class.getResourceAsStream("test.html");
            byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024 * 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
    }

    @Test
    public void testCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertEquals(charset, validatedCharset);
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.put((byte)0xEF);
        byteBuffer.put((byte)0xBB);
        byteBuffer.put((byte)0xBF);
        byteBuffer.put((byte)0x00);
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        Assert.assertEquals("UTF-8", bomCharset.charset);
        Assert.assertTrue(bomCharset.offset);
    }
}