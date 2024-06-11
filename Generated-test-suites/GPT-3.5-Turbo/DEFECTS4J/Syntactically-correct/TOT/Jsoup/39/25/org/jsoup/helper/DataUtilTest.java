package org.jsoup.helper;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.BeforeClass;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Locale;

public class DataUtilTest {

    private static String baseUri;
    private static File testFile;
    private static String charsetName;

    @BeforeClass
    public static void setUp() {
        baseUri = "http://example.com";
        testFile = new File("test.html");
        charsetName = "UTF-8";
    }

    @Test
    public void testLoadFromValidFile() {
        try {
            Document doc = DataUtil.load(testFile, charsetName, baseUri);
            assertNotNull(doc);
            // Additional assertions to validate the document
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadFromNonExistentFile() throws IOException {
        File nonExistentFile = new File("nonexistent.html");
        Document doc = DataUtil.load(nonExistentFile, charsetName, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadFromNullInputStream() throws IOException {
        InputStream in = null;
        Document doc = DataUtil.load(in, charsetName, baseUri);
    }

    @Test
    public void testLoadFromValidInputStream() {
        InputStream in = getClass().getResourceAsStream("/test.html");
        try {
            Document doc = DataUtil.load(in, charsetName, baseUri);
            assertNotNull(doc);
            // Additional assertions to validate the document
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromInvalidCharset() throws IOException {
        String invalidCharset = "invalid";
        Document doc = DataUtil.load(testFile, invalidCharset, baseUri);
    }

    @Test
    public void testLoadWithAlternateParser() {
        Parser parser = Parser.xmlParser();
        try {
            Document doc = DataUtil.load(testFile, charsetName, baseUri, parser);
            assertNotNull(doc);
            // Additional assertions to validate the document
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }

    @Test
    public void testParseByteDataWithValidCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body>Test</body></html>".getBytes(Charset.forName(charsetName)));
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        // Additional assertions to validate the document
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body>Test</body></html>".getBytes(Charset.forName(charsetName)));
        String invalidCharset = "invalid";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, invalidCharset, baseUri, parser);
        assertNotNull(doc);
        // Additional assertions to validate the document
    }

    @Test
    public void testParseByteDataWithNullCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body>Test</body></html>".getBytes(Charset.forName(charsetName)));
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertNotNull(doc);
        // Additional assertions to validate the document
    }

    @Test
    public void testReadToByteBufferWithValidInputStream() {
        InputStream in = getClass().getResourceAsStream("/test.html");
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(in);
            assertNotNull(byteData);
            // Additional assertions to validate the byte data
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadToByteBufferWithNegativeMaxSize() throws IOException {
        InputStream in = getClass().getResourceAsStream("/test.html");
        int maxSize = -1;
        ByteBuffer byteData = DataUtil.readToByteBuffer(in, maxSize);
    }

    @Test
    public void testGetCharsetFromContentTypeWithValidContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithAlternateCharset() {
        String contentType = "text/html; charset=ISO-8859-1";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("ISO-8859-1", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=invalid";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

}