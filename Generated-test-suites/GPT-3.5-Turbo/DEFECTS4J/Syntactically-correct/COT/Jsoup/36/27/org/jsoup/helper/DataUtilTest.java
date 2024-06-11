import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import static org.junit.Assert.*;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() throws IOException {
        File in = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document result = DataUtil.load(in, charsetName, baseUri);
        assertNotNull(result);
        // additional assertions
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document result = DataUtil.load(in, charsetName, baseUri);
        assertNotNull(result);
        // additional assertions
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document result = DataUtil.load(in, charsetName, baseUri, parser);
        assertNotNull(result);
        // additional assertions
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(10);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(result);
        // additional assertions
    }
    
    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inStream = getClass().getResourceAsStream("test.html");
        int maxSize = 1000;
        ByteBuffer result = DataUtil.readToByteBuffer(inStream, maxSize);
        assertNotNull(result);
        // additional assertions
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = getClass().getResourceAsStream("test.html");
        ByteBuffer result = DataUtil.readToByteBuffer(inStream);
        assertNotNull(result);
        // additional assertions
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String result = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", result);
        // additional assertions
    }
    
}