package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharsetSpecified() throws IOException {
        String filePath = "testFiles/validCharset.html";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(new File(filePath), charsetName, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertTrue(doc.outputSettings().contentType().startsWith("text/html;charset=" + charsetName));
    }

    @Test
    public void testLoadFileWithInvalidCharsetSpecified() throws IOException {
        String filePath = "testFiles/validCharset.html";
        String charsetName = "GBK"; // invalid charset
        String baseUri = "http://example.com";

        Document doc = null;
        try {
            doc = DataUtil.load(new File(filePath), charsetName, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }

        Assert.assertNull(doc);
    }

    @Test
    public void testLoadInputStreamWithValidCharsetSpecified() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        String inputData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Test</h1></body></html>";

        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(Charset.forName(charsetName)));

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertTrue(doc.outputSettings().contentType().startsWith("text/html;charset=" + charsetName));
    }

    @Test
    public void testLoadInputStreamWithInvalidCharsetSpecified() throws IOException {
        String charsetName = "GBK"; // invalid charset
        String baseUri = "http://example.com";
        String inputData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Test</h1></body></html>";

        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(Charset.forName("UTF-8")));

        try {
            DataUtil.load(inputStream, charsetName, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamWithDetectedCharset() throws IOException {
        String baseUri = "http://example.com";
        String inputData = "<html lang=\"zh-CN\"><head><meta charset=\"gb2312\"></head><body><h1>Test</h1></body></html>";

        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(Charset.forName("UTF-8")));

        Document doc = DataUtil.load(inputStream, null, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("gb2312", doc.outputSettings().charset());
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        String inputData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Test</h1></body></html>";

        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(Charset.forName(charsetName)));
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.xmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "gb2312"; // invalid charset
        String baseUri = "http://example.com";
        String inputData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Test</h1></body></html>";

        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(Charset.forName("UTF-8")));

        try {
            DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.xmlParser());
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseInputStreamWithDetectedCharset() throws IOException {
        String baseUri = "http://example.com";
        String inputData = "<html lang=\"zh-CN\"><head><meta charset=\"gb2312\"></head><body><h1>Test</h1></body></html>";

        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(Charset.forName("UTF-8")));
        Document doc = DataUtil.parseInputStream(inputStream, null, baseUri, Parser.xmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("gb2312", doc.outputSettings().charset());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "Hello, World!";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(Charset.forName("UTF-8")));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        String outputData = outputStream.toString();
        Assert.assertEquals(inputData, outputData);
    }
}