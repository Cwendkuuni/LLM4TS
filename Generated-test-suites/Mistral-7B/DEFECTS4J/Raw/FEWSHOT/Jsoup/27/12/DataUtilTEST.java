package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.parser.Parser.ParserType;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFileCharsetDetected() throws IOException {
        String charset = "UTF-8";
        String fileContent = "Content-Type: text/html; charset=" + charset;
        File file = new File("testFile.html");
        InputStream inStream = mock(FileInputStream.class);
        when(inStream.read()).thenAnswer((i) -> fileContent.charAt((int) i));
        when(inStream.available()).thenReturn((int) fileContent.length());

        Document doc = DataUtil.load(file, null, "/baseUri");
        assertEquals(charset, doc.outputSettings().charset());
    }

    @Test
    public void testLoadFileDefaultCharset() throws IOException {
        String fileContent = "<html><head><meta charset=\"ISO-8859-1\"></head><body><div>Hello World</div></body></html>";
        File file = new File("testFile.html");
        InputStream inStream = new FileInputStream(file);

        Document doc = DataUtil.load(file, null, "/baseUri");
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamCharsetDetected() throws IOException {
        String charset = "UTF-8";
        String inputStreamContent = "Content-Type: text/html; charset=" + charset;
        InputStream in = new ByteArrayInputStream(inputStreamContent.getBytes(Charset.forName(charset)));

        Document doc = DataUtil.load(in, null, "/baseUri");
        assertEquals(charset, doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamDefaultCharset() throws IOException {
        String inputStreamContent = "Content-Type: text/html";
        InputStream in = new ByteArrayInputStream("Hello World".getBytes(Charset.forName("ISO-8859-1")));

        Document doc = DataUtil.load(in, null, "/baseUri");
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String inputStreamContent = "Content-Type: text/html";
        InputStream in = new ByteArrayInputStream("Hello World".getBytes(Charset.forName("UTF-8")));

        Document doc = DataUtil.load(in, "UTF-8", "/baseUri");
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedParser() throws IOException {
        String inputStreamContent = "Content-Type: text/xml";
        InputStream in = new ByteArrayInputStream("<xml><data>Hello World</data></xml>".getBytes(Charset.forName("UTF-8")));

        Document doc = DataUtil.load(in, "UTF-8", "/baseUri", ParserType.xmlParser());
        assertEquals(ParserType.xmlParser(), doc.parser());
    }

    @Test
    public void testLoadByteDataCharsetDetected() throws IOException {
        String charset = "UTF-8";
        String byteDataContent = "Content-Type: text/html; charset=" + charset;
        ByteBuffer byteData = ByteBuffer.wrap(byteDataContent.getBytes(Charset.forName(charset)));

        Document doc = DataUtil.parseByteData(byteData, null, "/baseUri", ParserType.htmlParser());
        assertEquals(charset, doc.outputSettings().charset());
    }

    @Test
    public void testLoadByteDataDefaultCharset() {
        String byteDataContent = "<html><head><meta charset=\"ISO-8859-1\"></head><body><div>Hello World</div></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(byteDataContent.getBytes(Charset.forName("UTF-8")));

        Document doc = DataUtil.parseByteData(byteData, null, "/baseUri", ParserType.htmlParser());
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
    }

    @Test
    public void testLoadByteDataWithSpecifiedCharset() {
        String byteDataContent = "<html><head><meta charset=\"ISO-8859-1\"></head><body><div>Hello World</div></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(byteDataContent.getBytes(Charset.forName("UTF-8")));

        Document doc = DataUtil.parseByteData(byteData, "ISO-8859-1", "/baseUri", ParserType.htmlParser());
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "This is a test.";
        InputStream in = new ByteArrayInputStream(inputStreamContent.getBytes(Charset.forName("UTF-8")));
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);

        byte[] expectedBytes = inputStreamContent.getBytes(Charset.forName("UTF-8"));
        assertEquals(expectedBytes.length, byteData.limit());
        byte[] actualBytes = new byte[byteData.limit()];
        byteData.get(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}