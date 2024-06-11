package org.jsoup.helper;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document expected = Jsoup.parse(file, "UTF-8", "http://example.com");
        Document actual = DataUtil.load(file, "UTF-8", "http://example.com");
        assertEquals(expected, actual);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        Document expected = Jsoup.parse(inputStream, "UTF-8", "http://example.com");
        Document actual = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertEquals(expected, actual);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        Document expected = Jsoup.parse(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        Document actual = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        assertEquals(expected, actual);
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Test case goes here
    }

    @Test
    public void testParseInputStream() throws IOException {
        // Test case goes here
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Test case goes here
    }

    @Test
    public void testMimeBoundary() {
        // Test case goes here
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test case goes here
    }

    @Test
    public void testValidateCharset() {
        // Test case goes here
    }

    @Test
    public void testDetectCharsetFromBom() {
        // Test case goes here
    }
}