
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;


public class DataUtilTest {

    @Test
    public void testLoadWithFile() throws IOException {
        // Arrange
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        
        // Act
        Document document = DataUtil.load(file, charsetName, baseUri);
        
        // Assert
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadWithInputStream() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        
        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        // Assert
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadWithInputStreamAndParser() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream("test.xml");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.xmlParser();
        
        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        // Assert
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithCharset() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // Assert
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("data".getBytes());
        String charsetName = null;
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // Assert
        Assert.assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream("test.html");
        
        // Act
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        
        // Assert
        Assert.assertNotNull(byteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";
        
        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        Assert.assertEquals("UTF-8", charset);
    }
}
