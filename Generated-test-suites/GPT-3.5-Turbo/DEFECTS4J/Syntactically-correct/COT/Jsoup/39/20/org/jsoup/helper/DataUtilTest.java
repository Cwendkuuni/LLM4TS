import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        // Arrange
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);

            // Assert
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);

            // Assert
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Act
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

            // Assert
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testParseByteData() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Assert
        assertNull(document);
    }

    @Test
    public void testReadToByteBuffer() {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        int maxSize = 1024;

        // Act
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);

            // Assert
            assertNotNull(byteData);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test.html");

        // Act
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

            // Assert
            assertNotNull(byteData);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertEquals("UTF-8", charset);
    }
}