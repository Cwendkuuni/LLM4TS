import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {
    
    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write("<html><head><title>Test</title></head><body></body></html>".getBytes());
        fileOutputStream.close();
        
        Document document = DataUtil.load(file, "UTF-8", "https://www.example.com");
        
        Assert.assertEquals("https://www.example.com", document.baseUri());
        Assert.assertEquals("Test", document.title());
    }
    
    @Test
    public void testLoadFromStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html><head><title>Test</title></head><body></body></html>".getBytes());
        
        Document document = DataUtil.load(inputStream, "UTF-8", "https://www.example.com");
        
        Assert.assertEquals("https://www.example.com", document.baseUri());
        Assert.assertEquals("Test", document.title());
    }
    
    @Test
    public void testLoadFromStreamWithParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html><head><title>Test</title></head><body></body></html>".getBytes());
        
        Document document = DataUtil.load(inputStream, "UTF-8", "https://www.example.com", Parser.htmlParser());
        
        Assert.assertEquals("https://www.example.com", document.baseUri());
        Assert.assertEquals("Test", document.title());
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello, World!".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        DataUtil.crossStreams(inputStream, outputStream);
        
        Assert.assertEquals("Hello, World!", new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
    }
    
    @Test
    public void testParseByteData() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html><head><title>Test</title></head><body></body></html>".getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        
        Document document = DataUtil.parseByteData(byteData, "UTF-8", "https://www.example.com", Parser.htmlParser());
        
        Assert.assertEquals("https://www.example.com", document.baseUri());
        Assert.assertEquals("Test", document.title());
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello, World!".getBytes());
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 20);
        
        Assert.assertEquals("Hello, World!", new String(byteBuffer.array(), StandardCharsets.UTF_8));
    }
    
    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write("<html><head><title>Test</title></head><body></body></html>".getBytes());
        fileOutputStream.close();
        
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        
        Assert.assertEquals("<html><head><title>Test</title></head><body></body></html>", new String(byteBuffer.array(), StandardCharsets.UTF_8));
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
        
        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }
    
    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte)0xEF, (byte)0xBB, (byte)0xBF, '<', 'h', 't', 'm', 'l', '>', '<', '/', 'h', 't', 'm', 'l', '>'});
        
        String charsetName = DataUtil.detectCharsetFromBom(byteData, "UTF-8");
        
        Assert.assertEquals("UTF-8", charsetName);
    }
}