import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String testFilePath = "testFile.html";
        File testFile = new File(testFilePath);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(testFile, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileIOError() throws IOException {
        String testFilePath = "nonExistentFile.html";
        File testFile = new File(testFilePath);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.load(testFile, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(testInputStream, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamIOError() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayInputStream testInputStream = new ByteArrayInputStream("Invalid data".getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";
        DataUtil.load(testInputStream, charsetName, baseUri, Parser.htmlParser());
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(testInputStream, charsetName, baseUri, Parser.xmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteData() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String testData = "<html><head><meta charset=\"gb2312\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        String charsetName = "invalidCharset";
        String baseUri = "http://example.com";
        DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test data to read.";
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);
        byte[] expectedBytes = testData.getBytes();
        byte[] readBytes = new byte[byteData.limit()];
        byteData.get(readBytes);
        Assert.assertArrayEquals(expectedBytes, readBytes);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferIOError() throws IOException {
        ByteArrayInputStream testInputStream = new ByteArrayInputStream("Invalid data".getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);
    }
}