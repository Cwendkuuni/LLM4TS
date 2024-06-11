import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        Document doc = DataUtil.load(testFile, charsetName, baseUri);

        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "<html><body>Test Data</body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("Test Data", doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String testData = "<html><body>Test Data</body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com/";

        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.fail("Expected IOException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String testData = "<html><body>Test Data</body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = null;
        String baseUri = "http://example.com/";

        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.fail("Expected IOException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseByteData() throws IOException {
        String testData = "<html><body>Test Data</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());

        Assert.assertNotNull(doc);
        Assert.assertEquals("Test Data", doc.body().text());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        String testData = "<html><body>Test Data</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com/";

        try {
            Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("test.html");
        File outputFile = new File("output.html");

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        ByteArrayOutputStream inputStreamAsByteArray = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;

        while ((length = inputStream.read(buffer)) > 0) {
            inputStreamAsByteArray.write(buffer, 0, length);
        }

        ByteBuffer byteData = ByteBuffer.wrap(inputStreamAsByteArray.toByteArray());
        String charsetName = null;
        String baseUri = null;

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());

        Assert.assertNotNull(doc);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        contentType = "text/html; charset=ISO-8859-1";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("ISO-8859-1", charset);

        contentType = "text/html; charset=GB2312";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("GB2312", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertEquals(boundary.length(), 32);
    }
}