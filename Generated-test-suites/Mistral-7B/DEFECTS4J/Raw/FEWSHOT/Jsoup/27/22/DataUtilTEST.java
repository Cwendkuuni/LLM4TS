package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws Exception {
        String expectedHtml = "<html><head></head><body></body></html>";
        File testFile = new File("test.html");
        FileOutputStream testFileOutputStream = new FileOutputStream(testFile);
        testFileOutputStream.write(expectedHtml.getBytes());
        try {
            Document actualDocument = DataUtil.load(testFile, null);
            assertEquals(expectedHtml, actualDocument.body().html());
        } finally {
            testFile.delete();
        }
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws Exception {
        String expectedHtml = "<html><head></head><body></body></html>";
        File testFile = new File("test.html");
        FileOutputStream testFileOutputStream = new FileOutputStream(testFile);
        testFileOutputStream.write(expectedHtml.getBytes("UTF-8"));
        try {
            Document actualDocument = DataUtil.load(testFile, "UTF-8");
            assertEquals(expectedHtml, actualDocument.body().html());
        } finally {
            testFile.delete();
        }
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws Exception {
        String expectedHtml = "<html><head></head><body></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(expectedHtml.getBytes());
        try {
            Document actualDocument = DataUtil.load(testInputStream, null);
            assertEquals(expectedHtml, actualDocument.body().html());
        } finally {
            testInputStream.close();
        }
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws Exception {
        String expectedHtml = "<html><head></head><body></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(expectedHtml.getBytes("UTF-8"));
        try {
            Document actualDocument = DataUtil.load(testInputStream, "UTF-8");
            assertEquals(expectedHtml, actualDocument.body().html());
        } finally {
            testInputStream.close();
        }
    }

    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        String expectedHtml = "<html><head></head><body></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(expectedHtml.getBytes());
        try {
            Document actualDocument = DataUtil.load(testInputStream, null, null, Parser.xmlParser());
            assertEquals(expectedHtml, actualDocument.body().html());
        } finally {
            testInputStream.close();
        }
    }

    @Test
    public void testParseByteData() throws Exception {
        String expectedHtml = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(expectedHtml.getBytes("UTF-8"));
        Document actualDocument = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertEquals(expectedHtml, actualDocument.body().html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testString = "test string";
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testString.getBytes());
        InputStream in = DataUtil.readToByteBuffer(testInputStream);
        byte[] expectedByteData = testString.getBytes();
        byte[] actualByteData = new byte[in.available()];
        in.read(actualByteData);
        in.close();
        assertArrayEquals(expectedByteData, actualByteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", DataUtil.stripLeadingHyphens("-f"));
        assertEquals("foo", DataUtil.stripLeadingHyphens("--foo"));
        assertEquals("-foo", DataUtil.stripLeadingHyphens("---foo"));
        assertNull(DataUtil.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", DataUtil.stripLeadingAndTrailingQuotes("foo"));
        assertEquals("foo\" bar\"", DataUtil.stripLeadingAndTrailingQuotes("foo\" bar\""));
        assertEquals("\"foo\" bar", DataUtil.stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertEquals("\"foo\" and \"bar\"", DataUtil.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        assertEquals("", DataUtil.stripLeadingAndTrailingQuotes(""));
    }
}