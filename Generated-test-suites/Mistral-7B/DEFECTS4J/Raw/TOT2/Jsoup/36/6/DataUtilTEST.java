import org.junit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {
    // Test case for load(File, String, String) method
    @org.junit.Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("test.html");
        Document doc = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    // Test case for load(InputStream, String, String) method
    @org.junit.Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = new ByteArrayInputStream("HTML content".getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    // Test case for load(InputStream, String, String, Parser) method
    @org.junit.Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = new ByteArrayInputStream("HTML content".getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    // Test case for getCharsetFromContentType method with valid content type
    @org.junit.Test
    public void testGetCharsetFromContentTypeValid() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expectedCharset, charset);
    }

    // Test case for getCharsetFromContentType method with invalid content type
    @org.junit.Test
    public void testGetCharsetFromContentTypeInvalid() {
        String contentType = "text/html; charset=invalid-charset";
        String expectedCharset = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expectedCharset, charset);
    }
}