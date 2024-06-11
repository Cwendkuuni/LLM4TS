import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("UTF-8", doc.charset().name());
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("UTF-8", doc.charset().name());
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.xml");
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("UTF-8", doc.charset().name());
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = getClass().getResourceAsStream("/test.html");
        OutputStream out = System.out;
        
        DataUtil.crossStreams(in, out);
        // Check output manually
    }
    
    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("UTF-8", doc.charset().name());
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        Assert.assertNotNull(byteBuffer);
    }
    
    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        Assert.assertNotNull(byteBuffer);
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertEquals(0, byteBuffer.capacity());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }
    
    @Test
    public void testValidateCharset() {
        String cs = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(cs);
        Assert.assertEquals("UTF-8", validatedCharset);
    }
    
    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{(byte)0xEF, (byte)0xBB, (byte)0xBF, 'H', 'e', 'l', 'l', 'o'});
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        Assert.assertEquals("UTF-8", bomCharset.charset);
        Assert.assertEquals(3, bomCharset.offset);
    }
}