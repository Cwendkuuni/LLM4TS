package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    /**
     * Test for method load(File, String, String).
     * @throws IOException on IO error
     */
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("testData.html");
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    /**
     * Test for method load(InputStream, String, String).
     * @throws IOException on IO error
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    /**
     * Test for method load(InputStream, String, String, Parser).
     * @throws IOException on IO error
     */
    @Test
    public void testLoadInputStreamParser() throws IOException {
        String html = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = Parser.xmlParser();
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    /**
     * Test for method parseByteData(ByteBuffer, String, String, Parser).
     */
    @Test
    public void testParseByteData() {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = Parser.htmlParser();

        // Test with a valid byte buffer and charset.
        ByteBuffer byteData = ByteBuffer.wrap("testData.html".getBytes(Charset.forName("UTF-8")));
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);

        // Test with a valid byte buffer but unknown charset.
        byteData = ByteBuffer.wrap("testData.html".getBytes(Charset.forName("GBK")));
        document = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertNotNull(document);

        // Test with a valid byte buffer and invalid charset.
        byteData = ByteBuffer.wrap("testData.html".getBytes(Charset.forName("ABCDE")));
        document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);

        // Test with an empty byte buffer.
        ByteBuffer emptyByteBuffer = DataUtil.emptyByteBuffer();
        document = DataUtil.parseByteData(emptyByteBuffer, charsetName, baseUri, parser);
        assertNull(document);
    }

    /**
     * Test for method readToByteBuffer(InputStream, int).
     * @throws IOException on IO error
     */
    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
        assertEquals(html.getBytes("UTF-8").length, byteBuffer.limit());
    }

    /**
     * Test for method readToByteBuffer(InputStream).
     * @throws IOException on IO error
     */
    @Test
    public void testReadToByteBufferNoLimit() throws IOException {
        String html = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals(html.getBytes("UTF-8").length, byteBuffer.limit());
    }

    /**
     * Test for method readFileToByteBuffer(File).
     * @throws IOException on IO error
     */
    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("testData.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        long expectedFileSize = file.length();
        assertEquals(expectedFileSize, byteBuffer.limit());
    }

    /**
     * Test for method emptyByteBuffer().
     */
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.limit());
    }

    /**
     * Test for method getCharsetFromContentType(String).
     * @throws IOException on IO error
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html; charset=GBK";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("GBK", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    /**
     * Test for method mimeBoundary().
     */
    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        int boundaryLength = boundary.length();
        for (int i = 0; i < boundaryLength; i++) {
            Character c = boundary.charAt(i);
            assertTrue(DataUtil.mimeBoundaryChars[new Random().nextInt(DataUtil.mimeBoundaryChars.length)].equals(c));
        }
    }
}