package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("testfile.html");
        try {
            Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
            Assert.assertNotNull(doc);
        } catch (IOException e) {
            Assert.fail("IOException thrown while loading file");
        }
    }

    @Test
    public void testLoadInputStream() {
        InputStream in = new FileInputStream("testfile.html");
        try {
            Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
            Assert.assertNotNull(doc);
        } catch (IOException e) {
            Assert.fail("IOException thrown while loading input stream");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        InputStream in = new FileInputStream("testfile.html");
        try {
            Document doc = DataUtil.load(in, "UTF-8", "http://example.com", Parser.htmlParser());
            Assert.assertNotNull(doc);
        } catch (IOException e) {
            Assert.fail("IOException thrown while loading input stream with parser");
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream in = new FileInputStream("testfile.html");
            ByteBuffer byteData = DataUtil.readToByteBuffer(in);
            Assert.assertNotNull(byteData);
        } catch (IOException e) {
            Assert.fail("IOException thrown while reading to byte buffer");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

}