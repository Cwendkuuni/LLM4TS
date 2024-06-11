import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoad_withFile_Success() throws IOException {
        // Arrange
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        Document document = DataUtil.load(file, charsetName, baseUri);

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoad_withInvalidFile_ExceptionThrown() {
        // Arrange
        File file = new File("nonexistent.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act & Assert
        Assert.assertThrows(IOException.class, () -> {
            DataUtil.load(file, charsetName, baseUri);
        });
    }

    @Test
    public void testLoad_withInputStream_Success() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoad_withInvalidInputStream_ExceptionThrown() {
        // Arrange
        InputStream inputStream = null;
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act & Assert
        Assert.assertThrows(IOException.class, () -> {
            DataUtil.load(inputStream, charsetName, baseUri);
        });
    }

    @Test
    public void testLoad_withCustomParser_Success() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoad_withInvalidCharset_DefaultCharsetUsed() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals(DataUtil.defaultCharset, document.outputSettings().charset().name());
    }

    @Test
    public void testParseByteData_withNullCharset_DetectCharsetFromMeta() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test_with_charset_meta.html");
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals("EUC-JP", document.outputSettings().charset().name());
    }

    @Test
    public void testParseByteData_withInvalidCharset_DefaultCharsetUsed() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test_with_charset_meta.html");
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals(DataUtil.defaultCharset, document.outputSettings().charset().name());
    }

    @Test
    public void testReadToByteBuffer_withMaxSizeUnlimited_Success() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test.html");

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);

        // Assert
        Assert.assertNotNull(byteBuffer);
        Assert.assertTrue(byteBuffer.capacity() > 0);
    }

    @Test
    public void testReadToByteBuffer_withCappedMaxSize_Success() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        int maxSize = 1024;

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        // Assert
        Assert.assertNotNull(byteBuffer);
        Assert.assertTrue(byteBuffer.capacity() > 0);
        Assert.assertEquals(maxSize, byteBuffer.capacity());
    }

    @Test
    public void testReadToByteBuffer_withInvalidMaxSize_ExceptionThrown() {
        // Arrange
        InputStream inputStream = null;
        int maxSize = -1;

        // Act & Assert
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            DataUtil.readToByteBuffer(inputStream, maxSize);
        });
    }

    @Test
    public void testGetCharsetFromContentType_withValidCharset_Success() {
        // Arrange
        String contentType = "text/html; charset=EUC-JP";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        Assert.assertNotNull(charset);
        Assert.assertEquals("EUC-JP", charset);
    }

    @Test
    public void testGetCharsetFromContentType_withInvalidCharset_NullReturned() {
        // Arrange
        String contentType = "text/html; charset=InvalidCharset";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        Assert.assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_withNullContentType_NullReturned() {
        // Arrange
        String contentType = null;

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        Assert.assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_withEmptyCharset_NullReturned() {
        // Arrange
        String contentType = "text/html; charset=";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        Assert.assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_withCharsetInUpperCase_Success() {
        // Arrange
        String contentType = "text/html; charset=ISO-8859-1";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        Assert.assertNotNull(charset);
        Assert.assertEquals("ISO-8859-1", charset);
    }
}