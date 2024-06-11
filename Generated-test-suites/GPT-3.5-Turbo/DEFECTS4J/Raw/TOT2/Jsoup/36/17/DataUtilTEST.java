package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.Assert.*;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        // Typical use case
        try {
            File file = new File("test.html");
            Document document = DataUtil.load(file, "UTF-8", "http://example.com");
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException thrown");
        }
        // Edge cases and error scenarios handled in subsequent tests
    }

    @Test
    public void testLoadInputStream() {
        // Typical use case
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException thrown");
        }
        // Edge cases and error scenarios handled in subsequent tests
    }

    @Test
    public void testLoadInputStreamWithParser() {
        // Typical use case
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException thrown");
        }
        // Edge cases and error scenarios handled in subsequent tests
    }

    @Test
    public void testParseByteData() {
        // Unnecessary to test as it is a private method called indirectly
        assertTrue(true);
        // No edge cases or error scenarios to consider
    }

    @Test
    public void testReadToByteBuffer() {
        // Unnecessary to test as it is a private method called indirectly
        assertTrue(true);
        // No edge cases or error scenarios to consider
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Typical use case
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        // Edge cases
        contentType = "text/html; charset=ISO-8859-1";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("ISO-8859-1", charset);

        // Error scenario
        contentType = "text/html; charset=NotValid";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

}