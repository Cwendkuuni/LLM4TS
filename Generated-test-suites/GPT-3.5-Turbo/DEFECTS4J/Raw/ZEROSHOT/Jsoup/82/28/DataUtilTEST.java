import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {
    
    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        Assert.assertNotNull(doc);
    }
    
    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        Assert.assertNotNull(doc);
    }
    
    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.xml");
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Jsoup.parser());
        Assert.assertNotNull(doc);
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new FileInputStream("input.txt");
        OutputStream outputStream = System.out;
        
        DataUtil.crossStreams(inputStream, outputStream);
        
        // Verify the output
        // ...
    }
    
    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "http://example.com", Jsoup.parser());
        Assert.assertNotNull(doc);
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("input.txt");
        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream, 1024);
        Assert.assertTrue(buffer.limit() > 0);
    }
    
    @Test
    public void testGetCharsetFromContentType() {
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
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertEquals(DataUtil.boundaryLength, boundary.length());
    }
    
    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        Assert.assertNull(bomCharset);
    }
}