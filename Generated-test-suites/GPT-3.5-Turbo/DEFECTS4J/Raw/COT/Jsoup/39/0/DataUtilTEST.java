import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DataUtilTest {

    @Test
    public void testLoad1() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = DataUtil.load(file, charsetName, baseUri);
        
        // Test functionality
        assertNotNull(document);
    }

    @Test
    public void testLoad2() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        // Test functionality
        assertNotNull(document);
    }

    @Test
    public void testLoad3() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        // Test functionality
        assertNotNull(document);
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // Test functionality
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer1() throws IOException {
        FileInputStream inStream = new FileInputStream("test.html");
        int maxSize = 0;
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream, maxSize);
        
        // Test functionality
        assertNotNull(byteBuffer);
    }

    @Test
    public void testReadToByteBuffer2() throws IOException {
        FileInputStream inStream = new FileInputStream("test.html");
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream);
        
        // Test functionality
        assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Test functionality
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
}