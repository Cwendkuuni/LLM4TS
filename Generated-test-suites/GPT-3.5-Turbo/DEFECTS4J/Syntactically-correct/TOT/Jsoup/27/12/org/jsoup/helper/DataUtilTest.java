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

public class DataUtilTest {

    // Test load(File in, String charsetName, String baseUri) method
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/test/";

        Document document = DataUtil.load(file, charsetName, baseUri);

        Assert.assertNotNull(document);
        // Assert some properties of the document
        // ...
    }

    // Test load(InputStream in, String charsetName, String baseUri) method
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/test/";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        Assert.assertNotNull(document);
        // Assert some properties of the document
        // ...
    }

    // Test load(InputStream in, String charsetName, String baseUri, Parser parser) method
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/test/";
        Parser parser = Parser.xmlParser();

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        Assert.assertNotNull(document);
        // Assert some properties of the document
        // ...
    }

    // Test parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser) method
    @Test
    public void testParseByteData() throws IOException {
        String htmlData = "<html><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlData.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/test/";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        Assert.assertNotNull(document);
        // Assert some properties of the document
        // ...
    }

    // Test readToByteBuffer(InputStream inStream) method
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        Assert.assertNotNull(byteData);
        // Assert the size of the byteData
        // ...
    }

    // Test getCharsetFromContentType(String contentType) method
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertEquals("UTF-8", charset);
    }

}