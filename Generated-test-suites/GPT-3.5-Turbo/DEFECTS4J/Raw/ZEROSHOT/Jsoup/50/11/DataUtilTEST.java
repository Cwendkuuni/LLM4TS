
package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DataUtilTest {

    private File testFile;
    private InputStream testInputStream;
    private Parser testParser;

    @Before
    public void setUp() {
        testFile = new File("test.html");
        testInputStream = new FileInputStream(testFile);
        testParser = Parser.htmlParser();
    }

    @Test
    public void testLoadFromFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(testFile, charsetName, baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(testInputStream, charsetName, baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(testInputStream, charsetName, baseUri, testParser);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = mock(InputStream.class);
        OutputStream out = mock(OutputStream.class);
        DataUtil.crossStreams(in, out);
        verify(in, atLeastOnce()).read(any(byte[].class));
        verify(out, atLeastOnce()).write(any(byte[].class), anyInt(), anyInt());
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(charsetName, document.outputSettings().charset().name());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        int maxSize = 1000;
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream, maxSize);
        assertEquals(maxSize, byteBuffer.capacity());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream);
        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.capacity() > 0);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.capacity() > 0);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }
}
