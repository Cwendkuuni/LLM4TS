import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.helper.DataUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("path/to/your/file.html"); // replace with the path to your test file
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidFile() throws IOException {
        File file = new File("path/to/invalid/file"); // replace with the path to a non-existent file
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "Test data for InputStream";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(testData, document.body().html());
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullInputStream() {
        InputStream inputStream = null;
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.load(inputStream, charsetName, baseUri);
    }

    @Test
    public void testParseByteData() throws IOException {
        String testData = "Test data for ByteBuffer";
        File file = new File("path/to/your/file.html"); // replace with the path to your test file
        ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(file));
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals(testData, document.body().html());
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithNullByteData() {
        ByteBuffer byteData = null;
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }
}