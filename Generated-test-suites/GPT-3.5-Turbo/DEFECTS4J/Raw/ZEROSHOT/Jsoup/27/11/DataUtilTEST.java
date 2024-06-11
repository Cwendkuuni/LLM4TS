import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Locale;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadWithFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }
    
    @Test
    public void testLoadWithInputStream() throws IOException {
        FileInputStream inStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        Document document = DataUtil.load(inStream, charsetName, baseUri);
        assertNotNull(document);
        inStream.close();
    }
    
    @Test
    public void testLoadWithInputStreamAndParser() throws IOException {
        FileInputStream inStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.load(inStream, charsetName, baseUri, parser);
        assertNotNull(document);
        inStream.close();
    }
    
    @Test
    public void testParseByteDataWithCharsetName() {
        ByteBuffer byteData = ByteBuffer.wrap("Test Data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals(charsetName, document.outputSettings().charset());
    }
    
    @Test
    public void testParseByteDataWithNoCharsetName() {
        ByteBuffer byteData = ByteBuffer.wrap("Test Data".getBytes());
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertNotNull(document);
        assertNotEquals("", document.outputSettings().charset());
    }
    
    @Test
    public void testParseByteDataWithBOM() {
        ByteBuffer byteData = ByteBuffer.wrap("\uFEFFTest Data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals(charsetName, document.outputSettings().charset());
        assertEquals("Test Data", document.text());
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        FileInputStream inStream = new FileInputStream("test.html");
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        assertNotNull(byteData);
        inStream.close();
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}