package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFileWithNullCharsetAndNullBaseUri() throws IOException {
        File inputFile = new File("test.html");
        assertNull(DataUtil.load(inputFile, null, null));
    }

    @Test
    public void testLoadFileWithEmptyCharsetAndNullBaseUri() throws IOException {
        File inputFile = new File("test.html");
        Document doc = DataUtil.load(inputFile, "", null);
        assertNotNull(doc);
        assertEquals("", doc.baseUri());
    }

    @Test
    public void testLoadFileWithValidCharsetAndNullBaseUri() throws IOException {
        File inputFile = new File("test.html");
        Document doc = DataUtil.load(inputFile, "UTF-8", null);
        assertNotNull(doc);
        assertEquals("", doc.baseUri());
    }

    @Test
    public void testLoadFileWithValidCharsetAndBaseUri() throws IOException {
        File inputFile = new File("test.html");
        Document doc = DataUtil.load(inputFile, "UTF-8", "/base/url");
        assertNotNull(doc);
        assertEquals("/base/url", doc.baseUri());
    }

    @Test
    public void testLoadInputStreamWithNullCharsetAndNullBaseUri() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
        assertNull(DataUtil.load(inputStream, null, null));
    }

    @Test
    public void testLoadInputStreamWithEmptyCharsetAndNullBaseUri() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
        Document doc = DataUtil.load(inputStream, "", null);
        assertNotNull(doc);
        assertEquals("", doc.baseUri());
    }

    @Test
    public void testLoadInputStreamWithValidCharsetAndNullBaseUri() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
        assertEquals("", doc.baseUri());
    }

    @Test
    public void testLoadInputStreamWithValidCharsetAndBaseUri() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", "/base/url");
        assertNotNull(doc);
        assertEquals("/base/url", doc.baseUri());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        assertNotNull(outputStream.toString());
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseInputStreamWithNullCharsetAndNullBaseUri() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("", doc.baseUri());
    }

    @Test
    public void testParseInputStreamWithEmptyCharsetAndNullBaseUri() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
        Document doc = DataUtil.parseInputStream(inputStream, "", null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("", doc.baseUri());
    }

    @Test
    public void testParseInputStreamWithValidCharsetAndNullBaseUri() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("", doc.baseUri());
    }

    @Test
    public void testParseInputStreamWithValidCharsetAndBaseUri() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "/base/url", Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("/base/url", doc.baseUri());
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithNegativeMaxSize() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
        assertThrows(IllegalArgumentException.class, () -> DataUtil.readToByteBuffer(inputStream, -1));
    }

    @Test
    public void testParseInputStreamWithValidMaxSize() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        assertNotNull(byteBuffer);
        assertNotNull(byteBuffer.array());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File inputFile = new File("test.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(inputFile);
        assertNotNull(byteBuffer);
        assertNotNull(byteBuffer.array());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testParseInputStreamDetectCharset() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n<root/>".getBytes());
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.xmlParser());
        assertEquals("UTF-16", doc.outputSettings().charset());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"X-ABC-123\"?>\n<root/>".getBytes());
        Document doc = assertThrows(IllegalArgumentException.class, () -> DataUtil.parseInputStream(inputStream, "X-ABC-123", null, Parser.xmlParser()));
        assertEquals("X-ABC-123", doc.outputSettings().charset());
        fail("Expected IllegalArgumentException to be thrown.");
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=ISO-8859-1";
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=gb2312";
        assertEquals("gb2312", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=ISO-8859-1, ISO-8859-2";
        assertEquals(null, DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentTypeInvalid() {
        String contentType = "text/html; invalid-charset=UTF-8";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertEquals("ISO-8859-1", DataUtil.validateCharset("ISO-8859-1"));
        assertEquals("gb2312", DataUtil.validateCharset("gb2312"));
        assertEquals("UTF-16", DataUtil.validateCharset("UTF-16"));
        assertEquals("UTF-32", DataUtil.validateCharset("UTF-32"));
        assertEquals(null, DataUtil.validateCharset("invalid-charset"));
    }

    @Test
    public void testValidateCharsetInvalid() {
        assertThrows(IllegalArgumentException.class, () -> DataUtil.validateCharset("invalid-charset"));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertFalse(boundary.isEmpty());
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap("EF BBBF".getBytes());
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-8", bomCharset.charset);
        assertTrue(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomInvalid() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap("0000".getBytes());
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertNull(bomCharset);
    }
}

class BomCharset {
    final String charset;
    final boolean offset;

    BomCharset(String charset, boolean offset) {
        this.charset = charset;
        this.offset = offset;
    }
}