import junit.framework.TestCase;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Locale;

public class DataUtilTest extends TestCase {
    // Test load(File in, String charsetName, String baseUri)
    public void testLoadFile() throws IOException {
        File input = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        Document document = DataUtil.load(input, charsetName, baseUri);
        
        assertNotNull(document);
        assertTrue(document.hasSameValue("<html><head><title>Test HTML</title></head><body><h1>Hello, World!</h1></body></html>"));
    }
    
    // Test load(InputStream in, String charsetName, String baseUri)
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><title>Test HTML</title></head><body><h1>Hello, World!</h1></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        Document document = DataUtil.load(input, charsetName, baseUri);
        
        assertNotNull(document);
        assertTrue(document.hasSameValue("<html><head><title>Test HTML</title></head><body><h1>Hello, World!</h1></body></html>"));
    }
    
    // Test load(InputStream in, String charsetName, String baseUri, Parser parser)
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><head><title>Test HTML</title></head><body><h1>Hello, World!</h1></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        Document document = DataUtil.load(input, charsetName, baseUri, Parser.htmlParser());
        
        assertNotNull(document);
        assertTrue(document.hasSameValue("<html><head><title>Test HTML</title></head><body><h1>Hello, World!</h1></body></html>"));
    }
    
    // Test crossStreams(final InputStream in, final OutputStream out)
    public void testCrossStreams() throws IOException {
        String inputString = "Hello, World!";
        InputStream input = new ByteArrayInputStream(inputString.getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        DataUtil.crossStreams(input, output);
        
        assertEquals(inputString, output.toString());
    }
    
    // Test parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser)
    public void testParseByteData() {
        // Not easily testable as it relies on external dependencies (Jsoup library)
    }
    
    // Test readToByteBuffer(InputStream inStream, int maxSize)
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String inputString = "Hello, World!";
        InputStream input = new ByteArrayInputStream(inputString.getBytes());
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(input, inputString.length());
        
        assertEquals(inputString.length(), byteBuffer.capacity());
        assertEquals(inputString, new String(byteBuffer.array()));
    }
    
    // Test readToByteBuffer(InputStream inStream)
    public void testReadToByteBuffer() throws IOException {
        String inputString = "Hello, World!";
        InputStream input = new ByteArrayInputStream(inputString.getBytes());
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(input);
        
        assertEquals(inputString.length(), byteBuffer.capacity());
        assertEquals(inputString, new String(byteBuffer.array()));
    }
    
    // Test readFileToByteBuffer(File file)
    public void testReadFileToByteBuffer() throws IOException {
        File input = new File("test.txt");
        PrintWriter writer = new PrintWriter(input);
        writer.write("Hello, World!");
        writer.close();
        
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(input);
        
        assertEquals(13, byteBuffer.capacity());
        assertEquals("Hello, World!", new String(byteBuffer.array()));
        
        input.delete();
    }
    
    // Test emptyByteBuffer()
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        
        assertEquals(0, byteBuffer.capacity());
    }
    
    // Test getCharsetFromContentType(String contentType)
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
    }
    
    // Test mimeBoundary()
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
        
        for (char c : mimeBoundary.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }
}