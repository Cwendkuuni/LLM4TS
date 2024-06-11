import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

class DataUtilTest {

    @Test
    void load_file_validCharset_baseUri_provided_success() throws IOException {
        // Arrange
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        String expectedTitle = "Test Document";

        // Act
        Document document = DataUtil.load(file, charsetName, baseUri);
        String actualTitle = document.title();

        // Assert
        Assertions.assertEquals(expectedTitle, actualTitle);
    }

    @Test
    void load_file_invalidCharset_baseUri_provided_throwsIOException() {
        // Arrange
        File file = new File("test.html");
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";

        // Act and Assert
        Assertions.assertThrows(IOException.class, () -> DataUtil.load(file, charsetName, baseUri));
    }

    @Test
    void load_inputStream_validCharset_baseUri_provided_success() throws IOException {
        // Arrange
        String html = "<html><head><title>Test Document</title></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        String expectedTitle = "Test Document";

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        String actualTitle = document.title();

        // Assert
        Assertions.assertEquals(expectedTitle, actualTitle);
    }

    @Test
    void load_inputStream_invalidCharset_baseUri_provided_throwsIOException() {
        // Arrange
        String html = "<html><head><title>Test Document</title></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";

        // Act and Assert
        Assertions.assertThrows(IOException.class, () -> DataUtil.load(inputStream, charsetName, baseUri));
    }

    @Test
    void load_inputStream_validCharset_baseUri_parser_provided_success() throws IOException {
        // Arrange
        String html = "<html><head><title>Test Document</title></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        String expectedTitle = "Test Document";

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        String actualTitle = document.title();

        // Assert
        Assertions.assertEquals(expectedTitle, actualTitle);
    }

    @Test
    void load_inputStream_invalidCharset_baseUri_parser_provided_throwsIOException() {
        // Arrange
        String html = "<html><head><title>Test Document</title></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Act and Assert
        Assertions.assertThrows(IOException.class, () -> DataUtil.load(inputStream, charsetName, baseUri, parser));
    }

    @Test
    void parseByteData_charsetName_null_baseUri_provided_success() {
        // Arrange
        String html = "<html><head><title>Test Document</title></head><body></body></html>";
        ByteBuffer byteData = StandardCharsets.UTF_8.encode(html);
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        String expectedTitle = "Test Document";

        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        String actualTitle = document.title();

        // Assert
        Assertions.assertEquals(expectedTitle, actualTitle);
    }

    @Test
    void parseByteData_charsetName_null_baseUri_invalidCharset_provided_success() {
        // Arrange
        String html = "<html><head><title>Test Document</title></head><body></body></html>";
        ByteBuffer byteData = StandardCharsets.UTF_8.encode(html);
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        String expectedTitle = "Test Document";

        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        String actualTitle = document.title();

        // Assert
        Assertions.assertEquals(expectedTitle, actualTitle);
    }

    @Test
    void readToByteBuffer_inputStream_validMaxSize_success() throws IOException {
        // Arrange
        String html = "<html><head><title>Test Document</title></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        int maxSize = 1024;

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        String actualHtml = new String(byteBuffer.array(), StandardCharsets.UTF_8);

        // Assert
        Assertions.assertEquals(html, actualHtml);
    }

    @Test
    void readToByteBuffer_inputStream_invalidMaxSize_throwsIllegalArgumentException() {
        // Arrange
        String html = "<html><head><title>Test Document</title></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        int maxSize = -1;

        // Act and Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> DataUtil.readToByteBuffer(inputStream, maxSize));
    }

    @Test
    void getCharsetFromContentType_validContentType_charsetExists_success() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        Assertions.assertEquals(expectedCharset, charset);
    }

    @Test
    void getCharsetFromContentType_validContentType_charsetNotExists_returnsNull() {
        // Arrange
        String contentType = "text/html; encoding=UTF-8";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        Assertions.assertNull(charset);
    }

}