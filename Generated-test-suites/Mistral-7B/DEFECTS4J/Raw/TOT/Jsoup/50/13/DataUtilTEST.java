package org.jsoup.helper.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null, "");
        assertEquals(getDefaultContentType(file), doc.outputSettings().contentType());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test-gb2312.html");
        Document doc = DataUtil.load(file, "GB2312", "");
        assertEquals("text/html;charset=GB2312", doc.outputSettings().contentType());
        assertEquals("GB2312", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String html = "<html><head><meta charset='UTF-8'></head><body><p>Test</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, null, "");
        assertEquals("text/html;charset=UTF-8", doc.outputSettings().contentType());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String html = "<html><head><meta charset='GB2312'></head><body><p>Test</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, "GB2312", "");
        assertEquals("text/html;charset=GB2312", doc.outputSettings().contentType());
        assertEquals("GB2312", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String xml = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        Document doc = DataUtil.load(inputStream, null, "", Parser.xmlParser());
        assertEquals("application/xml", doc.outputSettings().contentType());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testData = "Test Data";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        InputStream inStream = new ByteArrayInputStream(testData.getBytes());

        DataUtil.crossStreams(inStream, outStream);

        byte[] expected = testData.getBytes();
        byte[] actual = outStream.toByteArray();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test Data";
        InputStream inStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);

        byte[] expected = testData.getBytes();
        byte[] actual = new byte[byteData.limit()];
        byteData.get(actual);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String testData = "Test Data";
        File file = new File("test.txt");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(testData);
        fileWriter.close();

        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);

        byte[] expected = testData.getBytes();
        byte[] actual = new byte[byteData.limit()];
        byteData.get(actual);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String testData = "Test Data";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        Document doc = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());

        assertEquals("text/html", doc.outputSettings().contentType());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String testData = "Test Data";
        String charset = "GB2312";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        Document doc = DataUtil.parseByteData(byteData, charset, "", Parser.htmlParser());

        assertEquals("text/html;charset=" + charset, doc.outputSettings().contentType());
        assertEquals(charset, doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String testData = "Test Data";
        String charset = "X-Invalid-Charset";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, charset, "", Parser.htmlParser()));
    }

    @Test
    public void testGetCharsetFromContentType() throws IOException {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == boundaryLength);
        for (int i = 0; i < boundaryLength; i++) {
            char c = boundary.charAt(i);
            assertTrue(java.util.Arrays.asList(mimeBoundaryChars).contains(c));
        }
    }

    private static String getDefaultContentType(File file) {
        String ext = file.getName().substring(file.getName().lastIndexOf("."));
        String contentType = "";
        switch (ext) {
            case ".html":
            case ".htm":
                contentType = "text/html";
                break;
            case ".xml":
                contentType = "application/xml";
                break;
            default:
                contentType = "application/octet-stream";
                break;
        }
        return contentType;
    }

    private static void assertArrayEquals(byte[] expected, byte[] actual) {
        Assertions.assertArrayEquals(expected, actual, "Byte arrays must be equal");
    }
}