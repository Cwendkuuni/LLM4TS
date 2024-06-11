
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, null, "");
        Assert.assertEquals("html", document.tagName());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = DataUtil.load(inputStream, null, "");
        Assert.assertEquals("html", document.tagName());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.xml");
        Document document = DataUtil.load(inputStream, null, "", Parser.xmlParser());
        Assert.assertEquals("root", document.tagName());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("input.txt");
        ByteBuffer outputBuffer = ByteBuffer.allocate(1024);
        DataUtil.crossStreams(inputStream, outputBuffer);
        outputBuffer.flip();
        String output = new String(outputBuffer.array(), 0, outputBuffer.limit());
        Assert.assertEquals("Hello, World!", output);
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = DataUtil.parseInputStream(inputStream, null, "", Parser.htmlParser());
        Assert.assertEquals("html", document.tagName());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("input.txt");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        String content = new String(bytes);
        Assert.assertEquals("Hello, World!", content);
    }

    @Test
    public void testReadToByteBufferUnlimited() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("input.txt");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        String content = new String(bytes);
        Assert.assertEquals("Hello, World!", content);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File(getClass().getResource("input.txt").getFile());
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        String content = new String(bytes);
        Assert.assertEquals("Hello, World!", content);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertEquals(0, byteBuffer.remaining());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testValidateCharset() {
        String validCharset = "ISO-8859-1";
        String invalidCharset = "xyz";
        String validatedCharset = DataUtil.validateCharset(validCharset);
        Assert.assertEquals("ISO-8859-1", validatedCharset);
        String invalidValidatedCharset = DataUtil.validateCharset(invalidCharset);
        Assert.assertNull(invalidValidatedCharset);
    }
    
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer bomByteBuffer = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 'h', 'e', 'l', 'l', 'o'});
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(bomByteBuffer);
        Assert.assertEquals("UTF-8", bomCharset.charset);
    }
}
