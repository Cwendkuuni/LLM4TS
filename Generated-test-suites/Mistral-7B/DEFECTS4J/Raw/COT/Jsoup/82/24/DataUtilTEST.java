package org.jsoup.helper;

import org.jsoup.HelperException;
import org.jsoup.UncheckedIOException;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
public final class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "test.html"; // Replace with the path to an actual HTML file
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File(filePath);

        Document actualDocument = DataUtil.load(file, charsetName, baseUri);

        Document expectedDocument = // Initialize an expected Document here

        Assert.assertEquals("Expected document does not match the actual one", expectedDocument, actualDocument);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><head><meta charset='UTF-8'></head><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document actualDocument = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);

        Document expectedDocument = // Initialize an expected Document here

        Assert.assertEquals("Expected document does not match the actual one", expectedDocument, actualDocument);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><head><meta charset='X-Invalid-Charset'></head><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        try {
            Document actualDocument = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
            Assert.fail("An IOException should have been thrown.");
        } catch (IOException e) {
            // No need to assert anything here as IOException is the expected outcome
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        String source = "Source data";
        String target = "Target data";
        InputStream inputStream = new ByteArrayInputStream(source.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] expectedData = target.getBytes();
        byte[] actualData = outputStream.toByteArray();

        Assert.assertArrayEquals("Expected data does not match the actual one", expectedData, actualData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String charsetExpected1 = "UTF-8";

        String contentType2 = "text/html; charset=ISO-8859-1";
        String charsetExpected2 = "ISO-8859-1";

        String contentType3 = "text/html";
        String charsetExpected3 = null;

        String contentType4 = "application/xml; charset=UTF-16";
        String charsetExpected4 = "UTF-16";

        for (int i = 1; i <= 4; i++) {
            String contentType = "contentType" + i;
            String charsetActual = DataUtil.getCharsetFromContentType(contentType);
            String charsetExpected = "charsetExpected" + i;

            Assert.assertEquals("Expected charset for content type " + contentType + " is incorrect.", charsetExpected, charsetActual);
        }
    }

    @Test
    public void testValidateCharset() {
        String charset1 = "UTF-8";
        String charset2 = "EUC-JP";
        String charset3 = "ISO-8859-1";
        String charset4 = "UTF-16";
        String charset5 = "X-Invalid-Charset";

        String expected1 = "UTF-8";
        String expected2 = "EUC-JP";
        String expected3 = "ISO-8859-1";
        String expected4 = "UTF-16";
        String expected5 = null;

        for (int i = 1; i <= 5; i++) {
            String charset = "charset" + i;
            String charsetValidated = DataUtil.validateCharset(charset);
            String charsetExpected = "charsetExpected" + i;

            Assert.assertEquals("Expected charset for " + charset + " is incorrect.", charsetExpected, charsetValidated);
        }
    }

    @Test
    public void testMimeBoundary() {
        String expected = DataUtil.mimeBoundary();

        String actual = DataUtil.mimeBoundary();

        Assert.assertEquals("Expected mime boundary is incorrect.", expected, actual);
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        String testFileContent = "UTF-8: \uFEFFTest"; // This content will have a UTF-8 BOM
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testFileContent.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);

        BomCharset expectedBomCharset = new BomCharset("UTF-8", true);
        BomCharset actualBomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        Assert.assertEquals("Expected BOM charset is incorrect.", expectedBomCharset, actualBomCharset);
    }

    @Test
    public void testDetectCharsetFromBomWithInvalidBom() throws IOException {
        String testFileContent = "Invalid-BOM: Test"; // This content will have an invalid BOM
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testFileContent.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);

        BomCharset expectedBomCharset = null;
        BomCharset actualBomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        Assert.assertEquals("Expected BOM charset is incorrect.", expectedBomCharset, actualBomCharset);
    }

    @Test(expected = UncheckedIOException.class)
    public void testParseInputStreamWithIOException() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]); // An empty input stream should throw IOException
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer actualByteBuffer = DataUtil.emptyByteBuffer();
        ByteBuffer expectedByteBuffer = ByteBuffer.allocate(0);

        Assert.assertEquals("Expected empty byte buffer is incorrect.", expectedByteBuffer, actualByteBuffer);
    }

    private static class BomCharset {
        private final String charset;
        private final boolean offset;

        public BomCharset(String charset, boolean offset) {
            this.charset = charset;
            this.offset = offset;
        }
    }
}