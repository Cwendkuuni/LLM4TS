import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    private static final String EXAMPLE_HTML = "<html><head></head><body><h1>Hello, World!</h1></body></html>";
    private static final String CHARSET_UTF8 = "UTF-8";
    private static final String CHARSET_ISO8859_1 = "ISO-8859-1";
    private static final String BASE_URI = "http://example.com";

    @Test
    public void testLoad_WithValidFile_ShouldReturnDocument() throws IOException {
        File file = new File("test.html");
        String charsetName = CHARSET_UTF8;
        String baseUri = BASE_URI;

        Document document = DataUtil.load(file, charsetName, baseUri);

        Assert.assertNotNull(document);
    }    

    @Test
    public void testLoad_WithInvalidFile_ShouldThrowIOException() throws IOException {
        File file = new File("invalid_file.html");
        String charsetName = CHARSET_UTF8;
        String baseUri = BASE_URI;

        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            Assert.assertTrue(e instanceof IOException);
        }
    }    
    
    @Test
    public void testLoad_WithInputStream_ShouldReturnDocument() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = CHARSET_UTF8;
        String baseUri = BASE_URI;

        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        Assert.assertNotNull(document);
    }
    
    @Test
    public void testLoad_WithInvalidInputStream_ShouldThrowIOException() throws IOException {
        InputStream inputStream = new FileInputStream("invalid_file.html");
        String charsetName = CHARSET_UTF8;
        String baseUri = BASE_URI;

        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            Assert.assertTrue(e instanceof IOException);
        }
    }
    
    @Test
    public void testLoad_WithAlternateParser_ShouldReturnDocument() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = CHARSET_UTF8;
        String baseUri = BASE_URI;
        Parser parser = Parser.xmlParser();

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        Assert.assertNotNull(document);
    }
    
    @Test
    public void testLoad_WithInvalidAlternateParser_ShouldThrowIOException() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = CHARSET_UTF8;
        String baseUri = BASE_URI;
        Parser parser = null;

        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            Assert.assertTrue(e instanceof IOException);
        }
    }
    
    @Test
    public void testParseByteData_WithValidCharset_ShouldReturnDocument() {
        ByteBuffer byteData = StandardCharsets.UTF_8.encode(EXAMPLE_HTML);
        String charsetName = CHARSET_UTF8;
        String baseUri = BASE_URI;
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        Assert.assertNotNull(document);
    }
    
    @Test
    public void testParseByteData_WithInvalidCharset_ShouldReturnDocumentWithFallbackCharset() {
        ByteBuffer byteData = StandardCharsets.ISO_8859_1.encode(EXAMPLE_HTML);
        String charsetName = CHARSET_ISO8859_1;
        String baseUri = BASE_URI;
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        Assert.assertNotNull(document);
        Assert.assertEquals(CHARSET_UTF8, document.outputSettings().charset().name());
    }
    
    @Test
    public void testReadToByteBuffer_WithValidInputStream_ShouldReturnByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        
        Assert.assertNotNull(byteBuffer);
        Assert.assertTrue(byteBuffer.capacity() > 0);
    }
    
    @Test
    public void testGetCharsetFromContentType_WithValidContentType_ShouldReturnCharset() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        Assert.assertNotNull(charset);
        Assert.assertEquals(CHARSET_UTF8, charset);
    }
    
    @Test
    public void testGetCharsetFromContentType_WithInvalidContentType_ShouldReturnNull() {
        String contentType = "text/html";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        Assert.assertNull(charset);
    }

}