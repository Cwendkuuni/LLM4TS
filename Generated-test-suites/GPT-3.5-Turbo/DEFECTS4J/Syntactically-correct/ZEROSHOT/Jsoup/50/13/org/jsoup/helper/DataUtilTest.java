package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class DataUtilTest {
    private File file;
    private InputStream inputStream;
    private String charsetName;
    private String baseUri;
    private Parser parser;

    @Before
    public void setUp() {
        file = new File("test.html");
        inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        charsetName = "UTF-8";
        baseUri = "http://example.com";
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoadFile() throws IOException {
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        assertEquals(0, out.size());
    }

    @Test
    public void testParseByteDataWithCharset() {
        ByteBuffer byteData = ByteBuffer.allocate(0);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        ByteBuffer byteData = ByteBuffer.allocate(0);
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 100);
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=invalid";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }

}