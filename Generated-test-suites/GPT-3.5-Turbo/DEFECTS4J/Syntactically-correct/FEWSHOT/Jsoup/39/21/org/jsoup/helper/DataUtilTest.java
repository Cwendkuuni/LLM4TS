package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    private File file;
    private String baseUri;
    private Parser parser;

    @Before
    public void setup() {
        file = new File("test.html");
        baseUri = "http://example.com";
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoadFromFile() throws IOException {
        Document document = DataUtil.load(file, null, baseUri);

        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        byte[] data = "<html><head></head><body></body></html>".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(data);

        Document document = DataUtil.load(inputStream, null, baseUri);

        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test
    public void testLoadFromInputStreamWithCharset() throws IOException {
        byte[] data = "<html><head></head><body></body></html>".getBytes(StandardCharsets.ISO_8859_1);
        InputStream inputStream = new ByteArrayInputStream(data);

        Document document = DataUtil.load(inputStream, "ISO-8859-1", baseUri);

        assertNotNull(document);
        assertEquals("ISO-8859-1", document.outputSettings().charset().name());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        byte[] data = "<html><head></head><body></body></html>".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(data);

        Document document = DataUtil.load(inputStream, null, baseUri, parser);

        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test
    public void testParseByteDataWithCharset() {
        byte[] data = "<html><head></head><body></body></html>".getBytes(StandardCharsets.ISO_8859_1);
        Document document = DataUtil.parseByteData(ByteBuffer.wrap(data), "ISO-8859-1", baseUri, parser);

        assertNotNull(document);
        assertEquals("ISO-8859-1", document.outputSettings().charset().name());
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        byte[] data = "<html><head></head><body></body></html>".getBytes(StandardCharsets.UTF_8);
        Document document = DataUtil.parseByteData(ByteBuffer.wrap(data), null, baseUri, parser);

        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        byte[] data = "<html><head></head><body></body></html>".getBytes(StandardCharsets.UTF_8);
        Document document = DataUtil.parseByteData(ByteBuffer.wrap(data), "INVALID_CHARSET", baseUri, parser);

        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        byte[] data = new byte[DataUtil.bufferSize * 2];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (i % 256);
        }
        InputStream inputStream = new ByteArrayInputStream(data);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, DataUtil.bufferSize);

        assertNotNull(byteBuffer);
        assertEquals(DataUtil.bufferSize, byteBuffer.limit());
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        byte[] data = new byte[DataUtil.bufferSize * 2];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (i % 256);
        }
        InputStream inputStream = new ByteArrayInputStream(data);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        assertNotNull(byteBuffer);
        assertEquals(data.length, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=utf-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=invalid_charset";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithoutCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

}