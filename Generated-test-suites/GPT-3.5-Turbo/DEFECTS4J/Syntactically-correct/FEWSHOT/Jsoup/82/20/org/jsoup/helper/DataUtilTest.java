package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoad_File_CharsetName_BaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoad_InputStream_CharsetName_BaseUri() throws IOException {
        String html = "<html><body><h1>Hello, world!</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoad_InputStream_CharsetName_BaseUri_Parser() throws IOException {
        String html = "<html><body><h1>Hello, world!</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello, world!".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        Assert.assertEquals("Hello, world!", outputStream.toString());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><body><h1>Hello, world!</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);

        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals(html, document.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String text = "Hello, world!";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        int maxSize = 1024;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(text, new String(byteBuffer.array(), StandardCharsets.UTF_8));
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(32, mimeBoundary.length());
        Assert.assertTrue(mimeBoundary.matches("^[\\-_0-9a-zA-Z]{32}$"));
    }
}