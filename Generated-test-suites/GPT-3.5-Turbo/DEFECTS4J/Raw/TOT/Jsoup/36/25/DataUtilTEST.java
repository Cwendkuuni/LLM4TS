import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@RunWith(JUnit4.class)
public class DataUtilTest {

    @Test
    public void load_withFile_returnsDocument() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);

        // Assert document is not null
        Assert.assertNotNull(document);

        // Assert document has expected title
        Assert.assertEquals("Test HTML", document.title());
    }

    @Test
    public void load_withInputStream_returnsDocument() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert document is not null
        Assert.assertNotNull(document);

        // Assert document has expected title
        Assert.assertEquals("Test HTML", document.title());
    }

    @Test
    public void load_withInputStreamAndParser_returnsDocument() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert document is not null
        Assert.assertNotNull(document);

        // Assert document has expected title
        Assert.assertEquals("Test HTML", document.title());
    }

    @Test
    public void parseByteData_withNullCharsetName_returnsDocument() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><head><title>Test HTML</title></head><body>Hello World!</body></html>".getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Assert document is not null
        Assert.assertNotNull(document);

        // Assert document has expected title
        Assert.assertEquals("Test HTML", document.title());
    }

    @Test
    public void parseByteData_withCharsetName_returnsDocument() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><head><title>Test HTML</title></head><body>Hello World!</body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Assert document is not null
        Assert.assertNotNull(document);

        // Assert document has expected title
        Assert.assertEquals("Test HTML", document.title());
    }

    @Test
    public void readToByteBuffer_withMaxSize_returnsByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        int maxSize = 1000;
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        // Assert byteBuffer is not null
        Assert.assertNotNull(byteBuffer);

        // Assert byteBuffer has expected capacity
        Assert.assertTrue(byteBuffer.capacity() <= maxSize);
    }

    @Test
    public void readToByteBuffer_withInputStream_returnsByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        // Assert byteBuffer is not null
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void getCharsetFromContentType_withValidContentType_returnsCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert charset is not null
        Assert.assertNotNull(charset);

        // Assert charset matches expected value
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void getCharsetFromContentType_withInvalidCharset_returnsNull() {
        String contentType = "text/html; charset=INVALID_CHARSET";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert charset is null
        Assert.assertNull(charset);
    }

    @Test
    public void getCharsetFromContentType_withEmptyContentType_returnsNull() {
        String contentType = "";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert charset is null
        Assert.assertNull(charset);
    }

}