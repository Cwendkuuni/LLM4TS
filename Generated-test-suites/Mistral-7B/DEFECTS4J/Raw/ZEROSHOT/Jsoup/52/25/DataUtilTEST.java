package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteBuffer;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("path/to/your/file.html");
        Document doc = DataUtil.load(file, null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("path/to/your/file.html");
        Document doc = DataUtil.load(file, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("path/to/your/file.html");
        Document doc = DataUtil.load(file, "XPQZ123", null);
        assertNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String testData = "Test data";
        byte[] testBytes = testData.getBytes();
        OutputStream out = outputStream;
        DataUtil.crossStreams(new ByteArrayInputStream(testBytes), out);
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(outputStream.toByteArray()));
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String testData = "Test data";
        byte[] testBytes = testData.getBytes("UTF-8");
        OutputStream out = outputStream;
        DataUtil.crossStreams(new ByteArrayInputStream(testBytes), out);
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(outputStream.toByteArray()));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>\r\n<html>\r\n<head>\r\n<meta charset=\"UTF-8\"></head>\r\n<body>\r\nTest data\r\n</body>\r\n</html>";
        byteData.put(testData.getBytes("UTF-16"));
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithDetectedCharsetFromMetaTag() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String testData = "<html>\r\n<head>\r\n<meta charset=\"Shift_JIS\"></head>\r\n<body>\r\nTest data\r\n</body>\r\n</html>";
        byteData.put(testData.getBytes());
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("Shift_JIS", doc.outputSettings().charset());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithDetectedCharsetFromXmlDeclaration() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String testData = "<html xmlns:xml=\"http://www.w3.org/1999/xhtml\" xml:lang=\"ja\" xml:base=\"/\">\r\n<?xml version=\"1.0\" encoding=\"EUC-JP\"?>\r\n<head>\r\n<meta charset=\"UTF-8\"></head>\r\n<body>\r\nTest data\r\n</body>\r\n</html>";
        byteData.put(testData.getBytes());
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("EUC-JP", doc.outputSettings().charset());
        assertNotNull(doc);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html; charset=EUC-JP";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("EUC-JP", charset);

        contentType = "text/html; charset=\"Shift_JIS\"";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("Shift_JIS", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(boundary.length(), 32);
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byteData.put((byte) 0xFF);
        byteData.put((byte) 0xFE);
        byteData.put((byte) 0x00);
        byteData.put((byte) 0x00);
        String charset = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-16", charset);

        byteData.clear();
        byteData.put((byte) 0x00);
        byteData.put((byte) 0x00);
        byteData.put((byte) 0xFE);
        byteData.put((byte) 0xFF);
        charset = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-32", charset);

        byteData.clear();
        byteData.put((byte) 0xEF);
        byteData.put((byte) 0xBB);
        byteData.put((byte) 0xBF);
        charset = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-8", charset);
    }
}