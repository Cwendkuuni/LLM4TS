package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {
    @Test
    public void testLoadFile() {
        try {
            File file = new File("test.html");
            Document doc = DataUtil.load(file, "UTF-8", file.getAbsolutePath());
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document doc = DataUtil.load(inputStream, "UTF-8", "test.html");
            assertNotNull(doc);
            inputStream.close();
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testLoadWithParser() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document doc = DataUtil.load(inputStream, "UTF-8", "test.html", Parser.htmlParser());
            assertNotNull(doc);
            inputStream.close();
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            OutputStream outputStream = new FileOutputStream("test_copy.html");
            DataUtil.crossStreams(inputStream, outputStream);

            File originalFile = new File("test.html");
            File copiedFile = new File("test_copy.html");
            assertTrue(originalFile.length() == copiedFile.length());

            inputStream.close();
            outputStream.close();

            copiedFile.delete();
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testParseInputStream() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "test.html", Parser.htmlParser());
            assertNotNull(doc);
            inputStream.close();
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadToByteBufferWithMaxSize() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
            assertNotNull(byteBuffer);
            inputStream.close();
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
            assertNotNull(byteBuffer);
            inputStream.close();
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadFileToByteBuffer() {
        try {
            File file = new File("test.html");
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String charset = "text/html; charset=UTF-8";
        String result = DataUtil.getCharsetFromContentType(charset);
        assertEquals("UTF-8", result);
    }

    @Test
    public void testGetCharsetFromContentTypeNoCharset() {
        String charset = "text/html";
        String result = DataUtil.getCharsetFromContentType(charset);
        assertNull(result);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String result = DataUtil.validateCharset(charset);
        assertEquals("UTF-8", result);
    }

    @Test
    public void testValidateCharsetNotSupported() {
        String charset = "InvalidCharset";
        String result = DataUtil.validateCharset(charset);
        assertNull(result);
    }

    @Test
    public void testMimeBoundary() {
        String result = DataUtil.mimeBoundary();
        assertNotNull(result);
        assertEquals(32, result.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("\uFEFFtest".getBytes());
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNotNull(bomCharset);
        assertEquals("UTF-16", bomCharset.charset);
        assertFalse(bomCharset.offset);
    }
}