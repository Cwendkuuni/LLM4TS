package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", null);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testParseInputStreamWithXmlParser() throws IOException {
        String xmlContent = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<element name=\"test\">Hello World</element>\n</root>\n</xml>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
        XmlDeclaration xmlDeclaration = doc.child(0);
        assertEquals("1.0", xmlDeclaration.version());
        assertEquals("UTF-8", xmlDeclaration.encoding());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "Test Data";
        String outputContent = "";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        byte[] content = outputStream.toByteArray();
        assertEquals(inputContent, new String(content, StandardCharsets.UTF_8));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == 32);
        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(DataUtil.mimeBoundaryChars.contains(boundary.charAt(i)));
        }
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(new File("test.html")), 0);
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-8", bomCharset.charset);
        assertFalse(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomWithUtf16() throws IOException {
        byte[] utf16Content = {(byte) 0xFF, (byte) 0xFE, 0, 0, 1, 0, 0, 0, 72, 101, 108, 108, 111, 0, 0};
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(utf16Content), 0);
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-16", bomCharset.charset);
        assertTrue(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomWithUtf32() throws IOException {
        byte[] utf32Content = {(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF, 0, 0, 72, 101, 108, 108, 111, 0, 0, 0, 0};
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(utf32Content), 0);
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-32", bomCharset.charset);
        assertTrue(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomWithInvalidBom() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(new File("test.html")), 0);
        byteData.position(byteData.position() + 1);
        byteData.mark();
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertNull(bomCharset);
    }
}