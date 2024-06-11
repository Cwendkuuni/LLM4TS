// imports

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

// class under test
import org.jsoup.helper.DataUtil;

public class DataUtilTest {

    @Before
    public void setUp() {
        // setup code
    }

    @Test
    public void testLoad_withFileCharsetBaseUri() {
        // Test the load method with a file, charset, and baseUri
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        File file = new File("test.html");

        // Act
        Document result = DataUtil.load(file, charsetName, baseUri);

        // Assert
        assertNotNull(result);
        // additional assertions
    }

    @Test
    public void testLoad_withInputStreamCharsetBaseUri() throws IOException {
        // Test the load method with an input stream, charset, and baseUri
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        InputStream inputStream = new FileInputStream("test.html");

        // Act
        Document result = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert
        assertNotNull(result);
        // additional assertions
    }

    @Test
    public void testLoad_withInputStreamCharsetBaseUriAndParser() throws IOException {
        // Test the load method with an input stream, charset, baseUri, and parser
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        InputStream inputStream = new FileInputStream("test.html");
        Parser parser = Parser.htmlParser();

        // Act
        Document result = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert
        assertNotNull(result);
        // additional assertions
    }

    @Test
    public void testParseByteData_withCharsetNull() {
        // Test the parseByteData method with charsetName as null
        // Arrange
        ByteBuffer byteData = ByteBuffer.allocate(10);
        String charsetName = null;
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Assert
        assertNotNull(result);
        // additional assertions
    }

    @Test
    public void testParseByteData_withCharsetNotNull() {
        // Test the parseByteData method with a specific charsetName
        // Arrange
        ByteBuffer byteData = ByteBuffer.allocate(10);
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Assert
        assertNotNull(result);
        // additional assertions
    }

    @Test
    public void testReadToByteBuffer_withInputStream() throws IOException {
        // Test the readToByteBuffer method with an input stream
        // Arrange
        InputStream inputStream = new FileInputStream("test.html");

        // Act
        ByteBuffer result = DataUtil.readToByteBuffer(inputStream);

        // Assert
        assertNotNull(result);
        // additional assertions
    }

    @Test
    public void testGetCharsetFromContentType_withValidContentType() {
        // Test the getCharsetFromContentType method with a valid content type
        // Arrange
        String contentType = "text/html; charset=UTF-8";

        // Act
        String result = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertEquals("UTF-8", result);
    }

    @Test
    public void testGetCharsetFromContentType_withInvalidContentType() {
        // Test the getCharsetFromContentType method with an invalid content type
        // Arrange
        String contentType = "text/html";

        // Act
        String result = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNull(result);
    }
}