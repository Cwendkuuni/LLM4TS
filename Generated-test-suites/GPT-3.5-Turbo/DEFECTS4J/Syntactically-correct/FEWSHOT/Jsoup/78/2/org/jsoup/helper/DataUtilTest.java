
package org.jsoup.helper;

import org.jsoup.internal.ConstrainableInputStream;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadWithFile() {
        try {
            File file = new File("test.html");
            Document doc = DataUtil.load(file, "UTF-8", "https://www.example.com");
            Assert.assertNotNull(doc);
            Assert.assertEquals("https://www.example.com", doc.baseUri());
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadWithInputStream() {
        try {
            InputStream in = new FileInputStream("test.html");
            Document doc = DataUtil.load(in, "UTF-8", "https://www.example.com");
            Assert.assertNotNull(doc);
            Assert.assertEquals("https://www.example.com", doc.baseUri());
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadWithInputStreamAndParser() {
        try {
            InputStream in = new FileInputStream("test.html");
            Document doc = DataUtil.load(in, "UTF-8", "https://www.example.com", Parser.htmlParser());
            Assert.assertNotNull(doc);
            Assert.assertEquals("https://www.example.com", doc.baseUri());
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = new ByteArrayInputStream("Hello World".getBytes());
            DataUtil.crossStreams(in, out);
            Assert.assertEquals("Hello World", out.toString());
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }
    }

    @Test
    public void testParseInputStream() {
        try {
            InputStream in = new FileInputStream("test.html");
            Document doc = DataUtil.parseInputStream(in, "UTF-8", "https://www.example.com", Parser.htmlParser());
            Assert.assertNotNull(doc);
            Assert.assertEquals("https://www.example.com", doc.baseUri());
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream in = new FileInputStream("test.html");
            ByteBuffer buffer = DataUtil.readToByteBuffer(in);
            Assert.assertNotNull(buffer);
            Assert.assertTrue(buffer.remaining() > 0);
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }
    }

    @Test
    public void testReadFileToByteBuffer() {
        try {
            File file = new File("test.html");
            ByteBuffer buffer = DataUtil.readFileToByteBuffer(file);
            Assert.assertNotNull(buffer);
            Assert.assertTrue(buffer.remaining() > 0);
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        Assert.assertEquals(0, buffer.capacity());
        Assert.assertEquals(0, buffer.remaining());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testValidateCharsetValidCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertEquals(charset, validatedCharset);
    }

    @Test
    public void testValidateCharsetInvalidCharset() {
        String charset = "InvalidCharset";
        String validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 'H', 'e', 'l', 'l', 'o'});
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        Assert.assertNotNull(bomCharset);
        Assert.assertEquals("UTF-8", bomCharset.charset);
        Assert.assertEquals(3, bomCharset.offset);
    }
}
