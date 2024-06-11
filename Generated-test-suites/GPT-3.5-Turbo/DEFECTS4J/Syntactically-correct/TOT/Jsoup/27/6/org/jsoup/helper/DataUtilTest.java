import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class DataUtilTest {

    private static final String CHARSET_NAME = "UTF-8";
    private static final String WRONG_CHARSET_NAME = "WRONG-CHARSET";

    @Test
    public void load_File_ValidCharset_ReturnsDocument() throws IOException {
        // Arrange
        File file = new File("test.html");

        // Act
        Document document = DataUtil.load(file, CHARSET_NAME, "");

        // Assert
        assertNotNull(document);
        assertNotNull(document.html());
    }

    @Test(expected = IOException.class)
    public void load_File_InvalidCharset_ThrowsIOException() throws IOException {
        // Arrange
        File file = new File("test.html");

        // Act
        Document document = DataUtil.load(file, WRONG_CHARSET_NAME, "");
    }

    @Test
    public void load_InputStream_ValidCharset_ReturnsDocument() throws IOException {
        // Arrange
        File file = new File("test.html");
        InputStream inputStream = DataUtil.class.getResourceAsStream(file.getPath());

        // Act
        Document document = DataUtil.load(inputStream, CHARSET_NAME, "");

        // Assert
        assertNotNull(document);
        assertNotNull(document.html());
    }

    @Test(expected = IOException.class)
    public void load_InputStream_InvalidCharset_ThrowsIOException() throws IOException {
        // Arrange
        File file = new File("test.html");
        InputStream inputStream = DataUtil.class.getResourceAsStream(file.getPath());

        // Act
        Document document = DataUtil.load(inputStream, WRONG_CHARSET_NAME, "");
    }

    @Test
    public void load_InputStream_WithParser_ValidCharset_ReturnsDocument() throws IOException {
        // Arrange
        File file = new File("test.html");
        InputStream inputStream = DataUtil.class.getResourceAsStream(file.getPath());
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.load(inputStream, CHARSET_NAME, "", parser);

        // Assert
        assertNotNull(document);
        assertNotNull(document.html());
    }

    @Test(expected = IOException.class)
    public void load_InputStream_WithParser_InvalidCharset_ThrowsIOException() throws IOException {
        // Arrange
        File file = new File("test.html");
        InputStream inputStream = DataUtil.class.getResourceAsStream(file.getPath());
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.load(inputStream, WRONG_CHARSET_NAME, "", parser);
    }

    @Test
    public void parseByteData_ValidDataWithoutCharset_ReturnsDocument() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("<html><head></head><body></body></html>".getBytes());
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.parseByteData(byteData, null, baseUri, parser);

        // Assert
        assertNotNull(document);
        assertNotNull(document.html());
    }

    @Test
    public void parseByteData_ValidDataWithCharset_ReturnsDocument() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("<html><head></head><body></body></html>".getBytes());
        String charsetName = CHARSET_NAME;
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Assert
        assertNotNull(document);
        assertNotNull(document.html());
    }

    @Test
    public void readToByteBuffer_ValidInputStream_ReturnsByteBuffer() throws IOException {
        // Arrange
        InputStream inputStream = DataUtil.class.getResourceAsStream("test.html");

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        // Assert
        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.remaining() > 0);
    }

    @Test
    public void getCharsetFromContentType_ValidContentType_ReturnsCharset() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNotNull(charset);
        assertEquals(CHARSET_NAME, charset);
    }

    @Test
    public void getCharsetFromContentType_NoCharset_ReturnsNull() {
        // Arrange
        String contentType = "text/html";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNull(charset);
    }

    @Test
    public void getCharsetFromContentType_InvalidCharset_ReturnsNull() {
        // Arrange
        String contentType = "text/html; charset=INVALID-CHARSET";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNull(charset);
    }

}