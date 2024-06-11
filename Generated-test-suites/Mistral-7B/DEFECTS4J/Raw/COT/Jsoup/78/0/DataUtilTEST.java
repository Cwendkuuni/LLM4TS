package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     * @throws IOException on IO error
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
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = this.getClass().getResourceAsStream("/test.html");
        Document doc = DataUtil.load(inputStream, charset, baseUri);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = this.getClass().getResourceAsStream("/test.html");
        Parser parser = Parser.xmlParser(); // or htmlParser()
        Document doc = DataUtil.load(inputStream, charset, baseUri, parser);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#parseInputStream(InputStream, String, String, Parser)}.
     * @throws IOException on IO error
     */
    @Test
    public void testParseInputStream() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes(Charset.forName("UTF-8")));
        Document doc = DataUtil.parseInputStream(inputStream, charset, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#crossStreams(InputStream, OutputStream)}.
     * @throws IOException on IO error
     */
    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test data".getBytes(Charset.forName("UTF-8")));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        String result = outputStream.toString();
        assertEquals("test data", result);
    }

    /**
     * Test method for edge case: empty input stream.
     * @throws IOException on IO error
     */
    @Test(expected = IOException.class)
    public void testParseEmptyInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseInputStream(inputStream, charset, baseUri, parser);
    }

    /**
     * Test method for edge case: null input stream.
     * @throws IOException on IO error
     */
    @Test(expected = IOException.class)
    public void testParseNullInputStream() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseInputStream(null, charset, baseUri, parser);
    }

    /**
     * Test method for edge case: invalid charset.
     * @throws IOException on IO error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes(Charset.forName("UTF-8")));
        String charset = "INVALID_CHARSET";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseInputStream(inputStream, charset, baseUri, parser);
    }

    /**
     * Test method for edge case: null base URI.
     * @throws IOException on IO error
     */
    @Test
    public void testParseInputStreamWithNullBaseUri() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes(Charset.forName("UTF-8")));
        String charset = "UTF-8";
        String baseUri = null;
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseInputStream(inputStream, charset, baseUri, parser);
        assertNotNull(doc);
    }

    /**
     * Test method for edge case: null parser.
     * @throws IOException on IO error
     */
    @Test(expected = NullPointerException.class)
    public void testParseInputStreamWithNullParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes(Charset.forName("UTF-8")));
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = null;
        Document doc = DataUtil.parseInputStream(inputStream, charset, baseUri, parser);
    }

    /**
     * Test method for edge case: BOM in input stream.
     * @throws IOException on IO error
     */
    @Test
    public void testParseInputStreamWithBOM() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0xFF, (byte) 0xFE, 0, 0});
        String charset = "UTF-16";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseInputStream(inputStream, charset, baseUri, parser);
        assertNotNull(doc);
    }

}