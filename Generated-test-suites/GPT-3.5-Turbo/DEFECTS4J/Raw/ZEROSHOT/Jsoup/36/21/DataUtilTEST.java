package org.jsoup.helper;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        // Test case for loading a file to a Document
        File file = new File("test.html");
        Document document = null;
        try {
            document = DataUtil.load(file, "UTF-8", "http://example.com");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assert that the document is not null
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStream1() {
        // Test case for parsing a Document from an input stream
        InputStream inputStream = new ByteArrayInputStream("<html><head></head><body></body></html>".getBytes());
        Document document = null;
        try {
            document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assert that the document is not null
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStream2() {
        // Test case for parsing a Document from an input stream using the provided Parser
        InputStream inputStream = new ByteArrayInputStream("<html><head></head><body></body></html>".getBytes());
        Document document = null;
        try {
            document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assert that the document is not null
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteData() {
        // Test case for parsing byte data into a Document
        ByteBuffer byteData = ByteBuffer.wrap("<html><head></head><body></body></html>".getBytes());
        Document document = DataUtil.parseByteData(byteData, "UTF-8", "http://example.com", Parser.htmlParser());

        // Assert that the document is not null
        Assert.assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() {
        // Test case for reading an input stream into a byte buffer
        InputStream inputStream = new ByteArrayInputStream("<html><head></head><body></body></html>".getBytes());
        ByteBuffer byteBuffer = null;
        try {
            byteBuffer = DataUtil.readToByteBuffer(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assert that the byte buffer is not null
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test case for extracting charset from content type header
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert that the charset is equal to "UTF-8"
        Assert.assertEquals("UTF-8", charset);
    }
}