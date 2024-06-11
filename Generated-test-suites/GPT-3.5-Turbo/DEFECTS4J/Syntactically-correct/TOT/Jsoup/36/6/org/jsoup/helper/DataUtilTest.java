package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DataUtilTest {

    // Test load(File in, String charsetName, String baseUri) method
    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);

        Assert.assertEquals("Title", doc.title());
    }

    // Test load(InputStream in, String charsetName, String baseUri) method
    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document doc = DataUtil.load(in, charsetName, baseUri);

        Assert.assertEquals("Title", doc.title());
    }

    // Test load(InputStream in, String charsetName, String baseUri, Parser parser) method
    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(in, charsetName, baseUri, parser);

        Assert.assertEquals("Title", doc.title());
    }

    // Test parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser) method
    @Test
    public void testParseByteData() {
        String html = "<html><head><title>Title</title></head><body></body></html>";
        byte[] byteData = html.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.wrap(byteData);

        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(buffer, charsetName, baseUri, parser);

        Assert.assertEquals("Title", doc.title());
    }

    // Test readToByteBuffer(InputStream inStream, int maxSize) method
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream in = new FileInputStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, 10);

        Assert.assertEquals(10, byteBuffer.limit());
    }

    // Test readToByteBuffer(InputStream inStream) method
    @Test
    public void testReadToByteBufferNoMaxSize() throws IOException {
        InputStream in = new FileInputStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in);

        Assert.assertNotNull(byteBuffer);
    }

    // Test getCharsetFromContentType(String contentType) method
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertEquals("UTF-8", charset);
    }

    // Test getCharsetFromContentType(String contentType) method with unsupported charset
    @Test
    public void testGetCharsetFromContentTypeUnsupportedCharset() {
        String contentType = "text/html; charset=ISO-8859-1";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertNull(charset);
    }
}