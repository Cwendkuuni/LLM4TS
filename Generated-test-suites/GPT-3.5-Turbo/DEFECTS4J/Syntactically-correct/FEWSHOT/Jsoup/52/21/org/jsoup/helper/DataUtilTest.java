package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {
    private static final String CHARSET_UTF8 = "UTF-8";
    private static final String CHARSET_ISO8859_1 = "ISO-8859-1";

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        String charsetName = CHARSET_UTF8;
        String baseUri = "";
        
        Document doc = DataUtil.load(file, charsetName, baseUri);

        assertNotNull(doc);
        assertEquals("test.html", doc.title());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = CHARSET_UTF8;
        String baseUri = "";
        
        Document doc = DataUtil.load(in, charsetName, baseUri);

        assertNotNull(doc);
        assertEquals("test.html", doc.title());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = CHARSET_UTF8;
        String baseUri = "";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.load(in, charsetName, baseUri, parser);

        assertNotNull(doc);
        assertEquals("test.html", doc.title());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new ByteArrayInputStream("test".getBytes(CHARSET_UTF8));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        DataUtil.crossStreams(in, out);

        byte[] bytes = out.toByteArray();
        assertEquals("test", new String(bytes, CHARSET_UTF8));
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes(CHARSET_UTF8));
        String charsetName = CHARSET_UTF8;
        String baseUri = "";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(doc);
        assertEquals("test", doc.text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream in = new ByteArrayInputStream("test".getBytes(CHARSET_UTF8));
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in);

        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        assertEquals("test", new String(bytes, CHARSET_UTF8));
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream in = new ByteArrayInputStream("test".getBytes(CHARSET_UTF8));
        int maxSize = 2;
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, maxSize);

        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        assertEquals("te", new String(bytes, CHARSET_UTF8));
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        assertTrue(byteBuffer.remaining() > 0);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        assertEquals(0, byteBuffer.remaining());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testValidateCharset() {
        String cs = "UTF-8";
        
        String validatedCharset = DataUtil.validateCharset(cs);

        assertEquals("UTF-8", validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap("\uFEFFtest".getBytes(CHARSET_UTF8));
        String charsetName = CHARSET_UTF8;
        
        String detectedCharset = DataUtil.detectCharsetFromBom(byteData, charsetName);

        assertEquals(CHARSET_UTF8, detectedCharset);
    }
}