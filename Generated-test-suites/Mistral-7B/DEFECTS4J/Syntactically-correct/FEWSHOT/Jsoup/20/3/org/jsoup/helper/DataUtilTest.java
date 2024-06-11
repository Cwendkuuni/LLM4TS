package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String charset = "UTF-8";
        File file = new File("test.html");
        String expectedHtml = "<html><body><h1>Hello World</h1></body></html>";

        Document document = DataUtil.load(file, charset, null);
        assertNotNull(document);
        assertEquals(expectedHtml, document.html());
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String charset = "ISO-8859-1";
        File file = new File("test.html");
        String expectedHtml = "<html><body><h1>Hello World</h1></body></html>";

        Document document = DataUtil.load(file, charset, null);
        assertNotNull(document);
        assertEquals(expectedHtml, document.html());
    }

    @Test
    public void testLoadFileWithNullCharset() throws IOException {
        File file = new File("test.html");
        String expectedHtml = "<html><body><h1>Hello World</h1></body></html>";

        Document document = DataUtil.load(file, null, null);
        assertNotNull(document);
        assertEquals(expectedHtml, document.html());
    }

    @Test
    public void testLoadFileWithEmptyCharset() throws IOException {
        File file = new File("test.html");
        String expectedHtml = "<html><body><h1>Hello World</h1></body></html>";

        Document document = DataUtil.load(file, "", null);
        assertNotNull(document);
        assertEquals(expectedHtml, document.html());
    }

    @Test
    public void testLoadFileWithNoCharset() throws IOException {
        File file = new File("test.html");
        String expectedHtml = "<html><body><h1>Hello World</h1></body></html>";

        Document document = DataUtil.load(file, null, null);
        assertNotNull(document);
        assertEquals(expectedHtml, document.html());
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String charset = "UTF-8";
        String inputStreamData = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(Charset.forName(charset)));
        String expectedHtml = "<html><body><h1>Hello World</h1></body></html>";

        Document document = DataUtil.load(inputStream, charset, null);
        assertNotNull(document);
        assertEquals(expectedHtml, document.html());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charset = "ISO-8859-1";
        String inputStreamData = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(Charset.forName("UTF-8")));

        Document document = DataUtil.load(inputStream, charset, null);
        assertNotNull(document);
        assertEquals(inputStreamData, document.html());
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String inputStreamData = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(Charset.forName("UTF-8")));

        Document document = DataUtil.load(inputStream, null, null);
        assertNotNull(document);
        assertEquals(inputStreamData, document.html());
    }

    @Test
    public void testLoadInputStreamWithEmptyCharset() throws IOException {
        String inputStreamData = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(Charset.forName("UTF-8")));

        Document document = DataUtil.load(inputStream, "", null);
        assertNotNull(document);
        assertEquals(inputStreamData, document.html());
    }

    @Test
    public void testLoadInputStreamWithNoCharset() throws IOException {
        String inputStreamData = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(Charset.forName("UTF-8")));

        Document document = DataUtil.load(inputStream, null, null);
        assertNotNull(document);
        assertEquals(inputStreamData, document.html());
    }

    @Test
    public void testParseByteDataWithValidCharset() throws IOException {
        String charset = "UTF-8";
        String inputStreamData = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(inputStreamData.getBytes(Charset.forName(charset)));

        Document document = DataUtil.parseByteData(byteBuffer, charset, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(inputStreamData, document.html());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String charset = "ISO-8859-1";
        String inputStreamData = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(inputStreamData.getBytes(Charset.forName("UTF-8")));

        Document document = DataUtil.parseByteData(byteBuffer, charset, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(inputStreamData, document.html());
    }

    @Test
    public void testParseByteDataWithNullCharset() throws IOException {
        String inputStreamData = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(inputStreamData.getBytes(Charset.forName("UTF-8")));

        Document document = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(inputStreamData, document.html());
    }

    @Test
    public void testParseByteDataWithEmptyCharset() throws IOException {
        String inputStreamData = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(inputStreamData.getBytes(Charset.forName("UTF-8")));

        Document document = DataUtil.parseByteData(byteBuffer, "", null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(inputStreamData, document.html());
    }

    @Test
    public void testParseByteDataWithNoCharset() throws IOException {
        String inputStreamData = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(inputStreamData.getBytes(Charset.forName("UTF-8")));

        Document document = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(inputStreamData, document.html());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String inputStreamData = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(inputStreamData.getBytes(Charset.forName("UTF-8")));

        Document document = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(inputStreamData, document.html());

        ByteBuffer byteBufferWithBom = ByteBuffer.wrap(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }.concat(inputStreamData.getBytes(Charset.forName("UTF-8"))));
        Document documentWithBom = DataUtil.parseByteData(byteBufferWithBom, null, null, Parser.htmlParser());
        assertNotNull(documentWithBom);
        assertEquals(inputStreamData, documentWithBom.html());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String inputStreamData = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(inputStreamData.getBytes(Charset.forName("UTF-8")));

        Document document = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(inputStreamData, document.html());

        ByteBuffer byteBufferWithBom = ByteBuffer.wrap(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }.concat(inputStreamData.getBytes(Charset.forName("UTF-8"))));
        Document documentWithBom = DataUtil.parseByteData(byteBufferWithBom, null, null, Parser.htmlParser());
        assertNotNull(documentWithBom);
        assertEquals(inputStreamData, documentWithBom.html());
    }

    @Test
    public void testDetectCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String detectedCharset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, detectedCharset);
    }

    @Test
    public void testDetectCharsetFromContentTypeEmpty() {
        String contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testDetectCharsetFromContentTypeNull() {
        assertNull(DataUtil.getCharsetFromContentType(null));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamData = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(Charset.forName("UTF-8")));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedBytes = inputStreamData.getBytes(Charset.forName("UTF-8"));
        assertEquals(expectedBytes.length, byteBuffer.limit());

        byte[] buffer = new byte[byteBuffer.limit()];
        byteBuffer.get(buffer);

        assertEquals(expectedBytes, buffer);
    }
}