
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

public class DataUtilTest {
    
    // Test load(File in, String charsetName, String baseUri)
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        
        assertEquals("http://example.com", doc.baseUri());
    }
    
    // Test load(InputStream in, String charsetName, String baseUri)
    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("Hello, World!", doc.select("h1").text());
    }
    
    // Test load(InputStream in, String charsetName, String baseUri, Parser parser)
    @Test
    public void testLoadWithParser() throws IOException {
        String xml = "<xml><name>John Doe</name></xml>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("John Doe", doc.select("name").text());
    }
    
    // Test parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser)
    @Test
    public void testParseByteData() {
        // Test edge case: invalid charsetName
        ByteBuffer byteData = ByteBuffer.wrap("<html><head></head><body></body></html>".getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseByteData(byteData, "InvalidCharset", "http://example.com", Parser.htmlParser());
        
        assertEquals("http://example.com", doc.baseUri());
    }
    
    // Test parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser)
    @Test
    public void testParseByteDataWithMetaCharset() {
        // Test edge case: charset specified in meta charset tag
        ByteBuffer byteData = ByteBuffer.wrap((
                "<html><head><meta charset=\"ISO-8859-1\"></head><body></body></html>"
                ).getBytes(StandardCharsets.UTF_8));
        
        Document doc = DataUtil.parseByteData(byteData, null, "http://example.com", Parser.htmlParser());
        
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("ISO-8859-1", doc.outputSettings().charset().name());
    }
    
    // Test readToByteBuffer(InputStream inStream)
    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello, World!".getBytes(StandardCharsets.UTF_8));
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        
        assertEquals("Hello, World!", new String(byteData.array(), StandardCharsets.UTF_8));
    }
    
    // Test getCharsetFromContentType(String contentType)
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("EUC-JP", charset);
    }
}

