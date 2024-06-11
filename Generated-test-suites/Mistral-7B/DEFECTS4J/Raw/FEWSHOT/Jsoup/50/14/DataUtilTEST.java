package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

import java.io.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("US-ASCII", DataUtil.getCharsetFromContentType("text/plain; charset=US-ASCII"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("UTF-16BE", DataUtil.getCharsetFromContentType("application/octet-stream; charset=UTF-16BE"));
        assertEquals("UTF-16LE", DataUtil.getCharsetFromContentType("application/octet-stream; charset=UTF-16LE"));
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType("text/html; charset=ISO-8859-1"));
        assertEquals("EUC-JP", DataUtil.getCharsetFromContentType("text/html; charset=EUC-JP"));
        assertEquals("GB2312", DataUtil.getCharsetFromContentType("text/html; charset=GB2312"));
        assertNull(DataUtil.getCharsetFromContentType("text/html"));
        assertNull(DataUtil.getCharsetFromContentType("text/html; charset="));
        assertNull(DataUtil.getCharsetFromContentType("text/html; charset=   "));
        assertNull(DataUtil.getCharsetFromContentType("text/html; charset=foo"));
    }

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(testFile, charset, baseUri);

        assertNotNull(doc);
        assertEquals(charset, doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputString = "Hello World!";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputString.getBytes(Charset.forName("UTF-8")));
        InputStream testInput = new ByteArrayInputStream(outputStream.toByteArray());
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(testInput, charset, baseUri);

        assertNotNull(doc);
        assertEquals(charset, doc.outputSettings().charset());
        assertEquals(inputString, doc.body().text());
    }

    @Test
    public void testParseByteData() {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        byte[] bytes = "Hello World!".getBytes(Charset.forName("UTF-8"));
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

        Document doc = DataUtil.parseByteData(byteBuffer, charset, baseUri, Parser.htmlParser());

        assertNotNull(doc);
        assertEquals(charset, doc.outputSettings().charset());
        assertEquals("Hello World!", doc.body().text());

        charset = null;
        doc = DataUtil.parseByteData(byteBuffer, charset, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());

        charset = "ISO-8859-1";
        byteBuffer.rewind();
        doc = DataUtil.parseByteData(byteBuffer, charset, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().text());
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputString = "Hello World!";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputString.getBytes(Charset.forName("UTF-8")));
        InputStream testInput = new ByteArrayInputStream(outputStream.toByteArray());

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInput);

        assertEquals(outputStream.size(), byteBuffer.limit());
        ByteBuffer expected = ByteBuffer.wrap(outputStream.toByteArray());
        byteBuffer.rewind();
        assertEquals(expected.get(), byteBuffer.get());
        for (int i = 0; i < outputStream.size() - 1; i++) {
            assertEquals(expected.get(), byteBuffer.get());
        }
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File testFile = new File("test.html");

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);

        RandomAccessFile randomAccessFile = new RandomAccessFile(testFile, "r");
        long fileSize = randomAccessFile.length();
        randomAccessFile.close();

        assertEquals(fileSize, byteBuffer.limit());
        assertEquals(testFile.getBytes(), byteBuffer.array());
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        assertEquals(32, boundary.length());
        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(Character.isDefinedIsALphanum(boundary.charAt(i)));
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        File testFile = new File("test.html");
        RandomAccessFile randomAccessFile = new RandomAccessFile(testFile, "r");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(randomAccessFile, outputStream);

        randomAccessFile.close();

        assertEquals(testFile.length(), outputStream.size());
        assertArrayEquals(testFile.getBytes(), outputStream.toByteArray());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        String charset = "invalidCharset";
        ByteBuffer byteBuffer = ByteBuffer.wrap("Hello World!".getBytes(Charset.forName("UTF-8")));

        assertThrows(IllegalArgumentException.class, () -> {
            DataUtil.parseByteData(byteBuffer, charset, null, Parser.htmlParser());
        });
    }
}