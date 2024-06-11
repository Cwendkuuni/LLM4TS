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
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document expectedDocument = Parser.htmlParser().parseInput(new File("test.html"), baseUri);
        Document actualDocument = DataUtil.load(inputFile, charsetName, baseUri);
        Assert.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "Test Data";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testData.getBytes(Charset.forName("UTF-8")));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document expectedDocument = Parser.htmlParser().parseInput(new ByteArrayInputStream(testData.getBytes(Charset.forName("UTF-8"))), baseUri);
        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException, IllegalCharsetNameException {
        String testData = "Test Data";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testData.getBytes(Charset.forName("InvalidCharset")));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        Document actualDocument = null;
        try {
            actualDocument = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        Assert.assertNull(actualDocument);
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String testData = "Test Data";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(Charset.forName("UTF-8")));
        Document expectedDocument = Parser.htmlParser().parseInput(new ByteArrayInputStream(testData.getBytes(Charset.forName("UTF-8"))), "");
        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, "", Parser.htmlParser());
        Assert.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException, IllegalCharsetNameException {
        String charsetName = "InvalidCharset";
        String testData = "Test Data";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(Charset.forName("UTF-8")));
        Document actualDocument = null;
        try {
            actualDocument = DataUtil.parseByteData(byteData, charsetName, "", Parser.htmlParser());
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        Assert.assertNull(actualDocument);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testData = "Test Data";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(Charset.forName("UTF-8")));
        OutputStream outputStream2 = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream2);
        String actualData = outputStream2.toString();
        Assert.assertEquals(testData, actualData);
    }
}