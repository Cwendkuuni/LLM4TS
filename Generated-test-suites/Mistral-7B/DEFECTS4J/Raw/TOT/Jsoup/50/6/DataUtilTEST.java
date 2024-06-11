package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
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
    public void testLoadFileWithValidCharset() throws IOException {
        File file = new File("path/to/valid/file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("path/to/file.html");
        String charsetName = "invalidCharset";
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.load(file, charsetName, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"invalidCharset\"></meta></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8")));
        String charsetName = "invalidCharset";
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8")));
        String charsetName = null;
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseByteDataValidCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(Charset.forName("UTF-8")));
        String charsetName = "invalidCharset";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        try {
            Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
            Assert.fail("Expected IllegaArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        Assert.assertEquals("Test data", outputStream.toString());
    }
}