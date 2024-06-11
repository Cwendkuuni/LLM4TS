import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        Document document = DataUtil.load(file, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals("Document base URI should match", baseUri, document.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals("Document base URI should match", baseUri, document.baseUri());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.xmlParser();
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        assertNotNull(document);
        assertEquals("Document base URI should match", baseUri, document.baseUri());
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Create a test input stream and output stream
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        DataUtil.crossStreams(inputStream, outputStream);
        
        // Check if output stream has content
        assertNotNull(outputStream.toByteArray());
    }

    @Test
    public void testParseByteData() {
        // Test for edge case where charset is null
        ByteBuffer byteData = ByteBuffer.allocate(0);
        String charsetName = null;
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(document);
        assertEquals("Document base URI should match", baseUri, document.baseUri());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        int maxSize = 1000;
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        
        assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test for edge case where content type is null
        String contentType = null;
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("Charset should be null", null, charset);
    }

    @Test
    public void testValidateCharset() {
        // Test for edge case where charset is null
        String charset = null;
        
        String validatedCharset = DataUtil.validateCharset(charset);
        
        assertEquals("Validated charset should be null", null, validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        // Test to ensure mime boundary is generated
        String mimeBoundary = DataUtil.mimeBoundary();
        
        assertNotNull(mimeBoundary);
        assertEquals("Mime boundary length should be 32", 32, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        // Test for edge case where byte data is empty
        ByteBuffer byteData = ByteBuffer.allocate(0);
        String charsetName = "UTF-8";
        
        String detectedCharset = DataUtil.detectCharsetFromBom(byteData, charsetName);
        
        assertEquals("Detected charset should be UTF-8", "UTF-8", detectedCharset);
    }
}