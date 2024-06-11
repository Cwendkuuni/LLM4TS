import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    @Test
    public void testLoad_withFile_validInputs() throws IOException {
        // Arrange
        File file = new File("test.html");
        String charset = "UTF-8";
        String baseUri = "https://www.example.com";

        // Act
        Document document = DataUtil.load(file, charset, baseUri);

        // Assert
        assertNotNull(document);
        // Additional assertions specific to the loaded document
    }

    @Test(expected = IOException.class)
    public void testLoad_withFile_invalidFile() throws IOException {
        // Arrange
        File file = new File("nonexistent.html");
        String charset = "UTF-8";
        String baseUri = "https://www.example.com";

        // Act
        DataUtil.load(file, charset, baseUri);
    }

    @Test
    public void testLoad_withInputStream_validInputs() throws IOException {
        // Arrange
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charset = "UTF-8";
        String baseUri = "https://www.example.com";

        // Act
        Document document = DataUtil.load(inputStream, charset, baseUri);

        // Assert
        assertNotNull(document);
        // Additional assertions specific to the loaded document
    }

    @Test(expected = IOException.class)
    public void testLoad_withInputStream_nullInputStream() throws IOException {
        // Arrange
        ByteArrayInputStream inputStream = null;
        String charset = "UTF-8";
        String baseUri = "https://www.example.com";

        // Act
        DataUtil.load(inputStream, charset, baseUri);
    }

    @Test
    public void testLoad_withInputStreamAndParser_validInputs() throws IOException {
        // Arrange
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charset = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.load(inputStream, charset, baseUri, parser);

        // Assert
        assertNotNull(document);
        // Additional assertions specific to the loaded document
    }

    @Test(expected = IOException.class)
    public void testLoad_withInputStreamAndParser_nullInputStream() throws IOException {
        // Arrange
        ByteArrayInputStream inputStream = null;
        String charset = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        // Act
        DataUtil.load(inputStream, charset, baseUri, parser);
    }

    @Test
    public void testParseByteData_withCharsetInMeta_validCharset() {
        // Arrange
        String html = "<html><head><meta charset=\"ISO-8859-1\"></head><body></body></html>";
        String charset = null;
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.parseByteData(ByteBuffer.wrap(html.getBytes(Charset.forName("ISO-8859-1"))), charset, baseUri, parser);

        // Assert
        assertNotNull(document);
        // Additional assertions specific to the parsed document
    }

    @Test
    public void testParseByteData_withCharsetInMeta_invalidCharset() {
        // Arrange
        String html = "<html><head><meta charset=\"ISO-8859-1\"></head><body></body></html>";
        String charset = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.parseByteData(ByteBuffer.wrap(html.getBytes(Charset.forName("ISO-8859-1"))), charset, baseUri, parser);

        // Assert
        assertNotNull(document);
        // Additional assertions specific to the parsed document
    }

    @Test
    public void testParseByteData_withCharsetSpecified_validCharset() {
        // Arrange
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String charset = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.parseByteData(ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8)), charset, baseUri, parser);

        // Assert
        assertNotNull(document);
        // Additional assertions specific to the parsed document
    }

    @Test
    public void testReadToByteBuffer_validInputStream() throws IOException {
        // Arrange
        String content = "Hello World!";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        // Assert
        assertEquals(content, new String(byteBuffer.array(), StandardCharsets.UTF_8));
    }

    @Test(expected = IOException.class)
    public void testReadToByteBuffer_nullInputStream() throws IOException {
        // Arrange
        ByteArrayInputStream inputStream = null;

        // Act
        DataUtil.readToByteBuffer(inputStream);
    }

    @Test
    public void testGetCharsetFromContentType_validContentType() {
        // Arrange
        String contentType = "text/html; charset=ISO-8859-1";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertEquals("ISO-8859-1", charset);
    }

    @Test
    public void testGetCharsetFromContentType_invalidContentType() {
        // Arrange
        String contentType = "text/html";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNull(charset);
    }

}