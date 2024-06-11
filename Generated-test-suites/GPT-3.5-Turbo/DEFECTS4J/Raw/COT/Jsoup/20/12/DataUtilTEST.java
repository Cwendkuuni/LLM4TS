
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.*;
import org.jsoup.Jsoup;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataUtilTest {
    
    @Test
    public void testLoadFromFile() throws Exception {
        // Create a temporary file for testing
        Path tempFile = Files.createTempFile("test", ".html");
        
        String htmlContent = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        Files.write(tempFile, htmlContent.getBytes());
        
        // Load the file using the DataUtil.load() method
        Document doc = DataUtil.load(tempFile.toFile(), "UTF-8", "");
        
        // Verify that the loaded document contains the expected HTML content
        String expectedHtml = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        Assert.assertEquals(expectedHtml, doc.html());
    }
    
    @Test
    public void testLoadFromInputStream() throws Exception {
        // Create an input stream from a string
        String htmlContent = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        
        // Load the input stream using the DataUtil.load() method
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        
        // Verify that the loaded document contains the expected HTML content
        String expectedHtml = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        Assert.assertEquals(expectedHtml, doc.html());
    }
    
    @Test
    public void testLoadFromInputStreamWithParser() throws Exception {
        // Create an input stream from a string
        String xmlContent = "<root><element>Hello World!</element></root>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes());
        
        // Load the input stream using the DataUtil.load() method with a custom parser
        Document doc = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());
        
        // Verify that the loaded document contains the expected XML content
        String expectedXml = "<root><element>Hello World!</element></root>";
        Assert.assertEquals(expectedXml, doc.xml());
    }
    
    @Test
    public void testParseByteDataWithoutCharset() throws Exception {
        // Create a byte buffer with UTF-8 encoded byte data
        String htmlContent = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes());
        
        // Parse the byte data using the DataUtil.parseByteData() method without specifying a charset
        Document doc = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        
        // Verify that the parsed document contains the expected HTML content
        String expectedHtml = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        Assert.assertEquals(expectedHtml, doc.html());
    }
    
    @Test
    public void testParseByteDataWithCharset() throws Exception {
        // Create a byte buffer with ISO-8859-1 encoded byte data
        String htmlContent = "<html><head></head><body><h1>HÃ«llÃ¶ WÃ¶rld!</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes("ISO-8859-1"));
        
        // Parse the byte data using the DataUtil.parseByteData() method with specifying the correct charset
        Document doc = DataUtil.parseByteData(byteData, "ISO-8859-1", "", Parser.htmlParser());
        
        // Verify that the parsed document contains the expected HTML content
        String expectedHtml = "<html><head></head><body><h1>Hëllö Wörld!</h1></body></html>";
        Assert.assertEquals(expectedHtml, doc.html());
    }
    
    @Test
    public void testReadToByteBuffer() throws Exception {
        // Create an input stream from a string
        String htmlContent = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        
        // Read the input stream to a byte buffer using the DataUtil.readToByteBuffer() method
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        
        // Verify that the byte buffer contains the expected byte data
        Assert.assertEquals(byteData.remaining(), htmlContent.getBytes().length);
        byte[] data = new byte[byteData.remaining()];
        byteData.get(data);
        Assert.assertArrayEquals(htmlContent.getBytes(), data);
    }
    
    @Test
    public void testGetCharsetFromContentType() throws Exception {
        // Test with a content type that contains a charset
        String contentType1 = "text/html; charset=UTF-8";
        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        Assert.assertEquals("UTF-8", charset1);
        
        // Test with a content type that does not contain a charset
        String contentType2 = "text/html";
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        Assert.assertNull(charset2);
    }
    
    
}

