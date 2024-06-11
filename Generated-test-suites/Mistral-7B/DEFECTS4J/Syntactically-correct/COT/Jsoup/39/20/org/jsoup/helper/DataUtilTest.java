package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for DataUtil.
 *
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     */
    @Test
    public void testLoadFile() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("test.html");
        Document doc = DataUtil.load(file, charset, baseUri);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     */
    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String charset = "INVALID_CHARSET";
        String baseUri = "http://example.com";
        File file = new File("test.html");
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(file, charset, baseUri));
    }

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     */
    @Test
    public void testLoadFileWithNullBaseUri() throws IOException {
        String charset = "UTF-8";
        File file = new File("test.html");
        Document doc = DataUtil.load(file, charset, null);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = DataUtil.load(inputStream, charset, baseUri);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     */
    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String baseUri = "http://example.com";
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        assertThrows(NullPointerException.class, () -> DataUtil.load(inputStream, null, baseUri));
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}.
     */
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = DataUtil.load(inputStream, charset, baseUri, Parser.xmlParser());
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     */
    @Test
    public void testParseByteData() {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        Document doc = DataUtil.parseByteData(byteData, charset, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataWithNullCharset() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
        assertNull(doc);
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream, int)}.
     */
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 0);
        assertNotNull(byteData);
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream)}.
     */
    @Test
    public void testReadToByteBufferWithMaxSizeZero() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals(charset, "UTF-8");
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
     */
    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=INVALID_CHARSET";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
     */
    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
     */
    @Test
    public void testGetCharsetFromContentTypeWithUppercaseCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals(charset, "UTF-8");

        contentType = "text/html; charset=UTF-8";
        charset = DataUtil.getCharsetFromContentType(contentType.toUpperCase(Locale.ENGLISH));
        assertNotNull(charset);
        assertEquals(charset, "UTF-8");
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
     */
    @Test(expected = IllegalCharsetNameException.class)
    public void testGetCharsetFromContentTypeWithUnsupportedCharset() {
        String contentType = "text/html; charset=INVALID_CHARSET";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

}