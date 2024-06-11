package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        File file = new File("test.html"); // Replace with a valid file path
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        // Add assertions to check the loaded document's contents here
    }

    @Test
    public void testLoadDocumentFromInvalidFile() throws IOException {
        File file = new File("non-existent-file.html"); // Replace with an invalid file path
        String charsetName = null;
        String baseUri = "http://example.com";
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            Assert.fail("Expected IOException, but no exception was thrown.");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test
    public void testLoadDocumentFromInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, null, baseUri);
        // Add assertions to check the loaded document's contents here
    }

    @Test
    public void testLoadDocumentFromInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"GBK\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, "GBK", baseUri);
        // Add assertions to check the loaded document's contents here
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String charsetName = null;
        String baseUri = "http://example.com";
        ByteBuffer byteData = ByteBuffer.allocate(30); // Replace with a valid byte buffer
        String docData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Test</h1></body></html>";
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        // Add assertions to check the parsed document's contents here
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String charsetName = "GBK";
        String baseUri = "http://example.com";
        ByteBuffer byteData = ByteBuffer.allocate(30); // Replace with a valid byte buffer
        String docData = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n<html><body><h1>Test</h1></body></html>";
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        // Add assertions to check the parsed document's contents here
    }

    @Test
    public void testDetectCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String detectedCharset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", detectedCharset);

        contentType = "text/html";
        detectedCharset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(detectedCharset);
    }

    @Test
    public void testReadInputStreamToByteBuffer() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        byte[] expectedByteArray = htmlContent.getBytes("UTF-8");
        byte[] actualByteArray = byteData.array();
        Assert.assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test
    public void testReadInputStreamToByteBufferWithMaxSize() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 10);
        Assert.assertEquals(10, byteData.limit());
        byte[] expectedByteArray = htmlContent.substring(0, 10).getBytes("UTF-8");
        byte[] actualByteArray = byteData.array();
        Assert.assertArrayEquals(expectedByteArray, actualByteArray);
    }
}