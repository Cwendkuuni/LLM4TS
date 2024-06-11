package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    

    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("input.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputFile, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidFile() throws IOException {
        File inputFile = new File("does_not_exist.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.load(inputFile, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-16\"></head><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-32";
        String baseUri = "http://example.com";
        DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-16\"></head><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-32";
        String baseUri = "http://example.com";
        DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "Input Data";
        String outputData = "Output Data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        Assert.assertEquals(outputData, new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
    }

    
}