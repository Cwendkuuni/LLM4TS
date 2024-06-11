import org.junit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "testFile.html";
        File testFile = new File(filePath);
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document document = DataUtil.load(testFile, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamNullCharset() throws IOException {
        String testData = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        String baseUri = "http://test.com";
        Document document = DataUtil.load(inputStream, null, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamValidCharset() throws IOException {
        String testData = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("ISO-8859-1"));
        String baseUri = "http://test.com";
        Document document = DataUtil.load(inputStream, "ISO-8859-1", baseUri);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileIOException() throws IOException {
        String filePath = "testFileDoesNotExist.html";
        File testFile = new File(filePath);
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        DataUtil.load(testFile, charsetName, baseUri);
    }

    @Test
    public void testParseByteDataValidData() {
        String testData = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataInvalidCharset() {
        String testData = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes("UTF-8"));
        String charsetName = "ISO-8859-1";
        String baseUri = "http://test.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNotNull(document);
    }

    @Test
    public void testReadToByteBufferValidData() throws IOException {
        String testData = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteData);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferIOException() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharsetFromContentTypeNullContentType() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharsetFromContentTypeInvalidContentType() {
        String contentType = "text/plain";
        String charset = DataUtil.getCharsetFromContentType(contentType);
    }

    @Test
    public void testGetCharsetFromContentTypeValidContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("EUC-JP", charset);
    }

}