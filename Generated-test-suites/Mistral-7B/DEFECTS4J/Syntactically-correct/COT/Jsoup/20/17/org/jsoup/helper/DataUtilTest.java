package org.jsoup.helper;

import org.junit.Assert; import org.junit.Test; import org.jsoup.helper.DataUtil; import org.jsoup.nodes.Document; import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream; import java.io.ByteArrayOutputStream; import java.io.File; import java.io.IOException; import java.nio.ByteBuffer; import java.nio.charset.Charset;

/**
 * Test class for DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Assuming test.html exists in the classpath
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document doc = DataUtil.load(file, charsetName, baseUri);

        // Add assertions to check the loaded Document
    }

    @Test
    public void testLoadInputStreamNullCharset() throws IOException {
        String inputData = "<html><head></head><body></body></html>";
        ByteArrayInputStream in = new ByteArrayInputStream(inputData.getBytes());
        String baseUri = "http://test.com";

        try {
            Document doc = DataUtil.load(in, null, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamValidCharset() throws IOException {
        String inputData = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head></head><body></body></html>";
        ByteArrayInputStream in = new ByteArrayInputStream(inputData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document doc = DataUtil.load(in, charsetName, baseUri);

        // Add assertions to check the loaded Document
    }

    @Test
    public void testParseByteDataDefaultCharset() {
        String charsetName = null;
        String baseUri = "http://test.com";
        String inputData = "<html><head></head><body></body></html>";

        ByteArrayInputStream in = new ByteArrayInputStream(inputData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());

        // Add assertions to check the loaded Document
    }

    @Test
    public void testParseByteDataSpecifiedCharset() {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        String inputData = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head></head><body></body></html>";

        ByteArrayInputStream in = new ByteArrayInputStream(inputData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());

        // Add assertions to check the loaded Document
    }

    @Test
    public void testParseByteDataInvalidCharset() {
        String charsetName = " invalidCharset ";
        String baseUri = "http://test.com";
        String inputData = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head></head><body></body></html>";

        ByteArrayInputStream in = new ByteArrayInputStream(inputData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);

        try {
            Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputData = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head></head><body></body></html>";
        ByteArrayInputStream in = new ByteArrayInputStream(inputData.getBytes());

        ByteBuffer byteData = DataUtil.readToByteBuffer(in);

        // Add assertions to check the byte buffer
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNullContentType() {
        String contentType = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertNull(charset);
    }
}