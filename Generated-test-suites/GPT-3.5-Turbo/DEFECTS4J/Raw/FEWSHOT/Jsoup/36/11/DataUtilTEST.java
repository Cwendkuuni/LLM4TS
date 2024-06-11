package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;

public class DataUtilTest {

    private File testFile;

    @Before
    public void setUp() throws IOException {
        testFile = new File("test.html");
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write("<html><body><h1>Hello, World!</h1></body></html>");
        fileWriter.close();
    }

    @Test
    public void testLoadWithCharset() throws IOException {
        Document document = DataUtil.load(testFile, "UTF-8", "http://example.com");
        assertEquals("<html><head></head><body><h1>Hello, World!</h1></body></html>", document.html());
    }

    @Test
    public void testLoadWithoutCharset() throws IOException {
        Document document = DataUtil.load(testFile, null, "http://example.com");
        assertEquals("<html><head></head><body><h1>Hello, World!</h1></body></html>", document.html());
    }

    @Test
    public void testLoadFromInputStreamWithCharset() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(testFile);
        Document document = DataUtil.load(fileInputStream, "UTF-8", "http://example.com");
        fileInputStream.close();
        assertEquals("<html><head></head><body><h1>Hello, World!</h1></body></html>", document.html());
    }

    @Test
    public void testLoadFromInputStreamWithoutCharset() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(testFile);
        Document document = DataUtil.load(fileInputStream, null, "http://example.com");
        fileInputStream.close();
        assertEquals("<html><head></head><body><h1>Hello, World!</h1></body></html>", document.html());
    }

    @Test
    public void testLoadFromInputStreamWithCharsetAndParser() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(testFile);
        Document document = DataUtil.load(fileInputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        fileInputStream.close();
        assertEquals("<html><head></head><body><h1>Hello, World!</h1></body></html>", document.html());
    }

    @Test
    public void testLoadFromInputStreamWithoutCharsetAndParser() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(testFile);
        Document document = DataUtil.load(fileInputStream, null, "http://example.com", Parser.htmlParser());
        fileInputStream.close();
        assertEquals("<html><head></head><body><h1>Hello, World!</h1></body></html>", document.html());
    }

    @Test
    public void testParseByteDataWithCharset() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("<html><body><h1>Hello, World!</h1></body></html>".getBytes(Charset.forName("UTF-8")));
        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", "http://example.com", Parser.htmlParser());
        assertEquals("<html><head></head><body><h1>Hello, World!</h1></body></html>", document.html());
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("<html><body><h1>Hello, World!</h1></body></html>".getBytes(Charset.forName("UTF-8")));
        Document document = DataUtil.parseByteData(byteBuffer, null, "http://example.com", Parser.htmlParser());
        assertEquals("<html><head></head><body><h1>Hello, World!</h1></body></html>", document.html());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(testFile);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(fileInputStream, 100);
        fileInputStream.close();
        assertEquals("<html><body><h1>Hello, World!</h1></body></html>", Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(testFile);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(fileInputStream);
        fileInputStream.close();
        assertEquals("<html><body><h1>Hello, World!</h1></body></html>", Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", charset);

        charset = DataUtil.getCharsetFromContentType("text/html");
        assertNull(charset);

        charset = DataUtil.getCharsetFromContentType(null);
        assertNull(charset);
    }
}