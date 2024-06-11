import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = DataUtil.load(file, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals("utf-8", document.outputSettings().charset().name());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals("utf-8", document.outputSettings().charset().name());
    }

    @Test
    public void testLoadWithParser() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        assertNotNull(document);
        assertEquals("utf-8", document.outputSettings().charset().name());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String input = "Hello, World!";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        DataUtil.crossStreams(inputStream, outputStream);
        
        assertEquals(input, outputStream.toString());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String input = "Hello, World!";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        
        assertNotNull(byteBuffer);
        assertEquals(input, Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        
        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.capacity() > 0);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        
        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte)0xEF, (byte)0xBB, (byte)0xBF, 'a', 'b', 'c'});
        String charsetName = "UTF-8";
        
        String detectedCharset = DataUtil.detectCharsetFromBom(byteData, charsetName);
        
        assertNotNull(detectedCharset);
        assertEquals("UTF-8", detectedCharset);
    }

}