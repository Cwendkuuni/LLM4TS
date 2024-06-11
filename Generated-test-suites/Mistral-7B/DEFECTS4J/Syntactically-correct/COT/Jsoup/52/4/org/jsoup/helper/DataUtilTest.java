package org.jsoup.helper; // update the test package if needed

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
public final class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("testFile.html"); // replace with the actual file path
        Document doc = DataUtil.load(file, null, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("testFile.html"); // replace with the actual file path
        Document doc = DataUtil.load(file, "UTF-8", null);
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullInput() {
        Document doc = null;
        try {
            doc = DataUtil.load(null, "UTF-8", null);
        } catch (IOException e) {
            // expected exception
        }
        Assert.assertNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String testData = "Test data to load.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Document doc = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String testData = "Test data to load.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullInput() {
        Document doc = null;
        try {
            doc = DataUtil.load(null, "UTF-8", null);
        } catch (IOException e) {
            // expected exception
        }
        Assert.assertNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testData = "Test data to load.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("inputFile.txt");
        File outputFile = new File("outputFile.txt");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        Assert.assertTrue(outputFile.length() > 0);
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String testData = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<body>\nTest data to parse.\n</body>\n</html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String testData = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<body>\nTest data to parse.\n</body>\n</html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithEmptyInput() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assert.assertNull(doc);
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithNullInput() {
        ByteBuffer byteData = null;
        Document doc = null;
        try {
            doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        } catch (IOException e) {
            // expected exception
        }
        Assert.assertNull(doc);
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

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        Assert.assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "euc-jp";
        Assert.assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "";
        Assert.assertNull(DataUtil.validateCharset(charset));

        charset = "unknown-charset";
        Assert.assertNull(DataUtil.validateCharset(charset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        int length = boundary.length();
        Assert.assertEquals(boundaryLength, length);
    }
}