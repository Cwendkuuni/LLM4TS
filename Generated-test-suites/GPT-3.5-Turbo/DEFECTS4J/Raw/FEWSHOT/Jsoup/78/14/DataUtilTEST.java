import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {
    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        
        Elements h1 = doc.select("h1");
        assertEquals(1, h1.size());
        assertEquals("Hello World!", h1.text());
    }
    
    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("testFile.html");
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        FileUtils.writeStringToFile(file, html, StandardCharsets.UTF_8);
        
        Document doc = DataUtil.load(file, "UTF-8", "");
        
        Elements h1 = doc.select("h1");
        assertEquals(1, h1.size());
        assertEquals("Hello World!", h1.text());
        
        FileUtils.deleteQuietly(file);
    }
    
    @Test
    public void testLoadWithParser() throws IOException {
        String xml = "<root><element>Value</element></root>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());
        
        Elements element = doc.select("element");
        assertEquals(1, element.size());
        assertEquals("Value", element.text());
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        String input = "Hello World!";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        DataUtil.crossStreams(inputStream, outputStream);
        
        String output = outputStream.toString("UTF-8");
        assertEquals(input, output);
    }
    
    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String input = "Hello World!";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 5);
        
        assertEquals(5, byteBuffer.limit());
        assertEquals(5, byteBuffer.capacity());
        assertEquals("Hello", new String(byteBuffer.array(), "UTF-8"));
    }
    
    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        String input = "Hello World!";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        
        assertEquals(12, byteBuffer.limit());
        assertEquals(12, byteBuffer.capacity());
        assertEquals("Hello World!", new String(byteBuffer.array(), "UTF-8"));
    }
    
    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("testFile.txt");
        String input = "Hello World!";
        FileUtils.writeStringToFile(file, input, StandardCharsets.UTF_8);
        
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        
        assertEquals(12, byteBuffer.limit());
        assertEquals(12, byteBuffer.capacity());
        assertEquals("Hello World!", new String(byteBuffer.array(), "UTF-8"));
        
        FileUtils.deleteQuietly(file);
    }
    
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        
        assertEquals(32, mimeBoundary.length());
        assertTrue(mimeBoundary.matches("[a-zA-Z0-9_-]+"));
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=Invalid");
        
        assertNull(charset);
    }
    
    @Test
    public void testValidateCharset() {
        String validCharset = DataUtil.validateCharset("UTF-8");
        
        assertEquals("UTF-8", validCharset);
    }
    
    @Test
    public void testValidateCharsetWithInvalidCharset() {
        String invalidCharset = DataUtil.validateCharset("Invalid");
        
        assertNull(invalidCharset);
    }
    
    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
        
        Elements h1 = doc.select("h1");
        assertEquals(1, h1.size());
        assertEquals("Hello World!", h1.text());
    }
}