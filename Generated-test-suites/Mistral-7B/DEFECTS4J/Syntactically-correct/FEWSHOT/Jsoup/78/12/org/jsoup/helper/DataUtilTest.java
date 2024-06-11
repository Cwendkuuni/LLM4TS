package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    private static Document createTestDocument() {
        String xmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        Document doc = new Parser().parseInput(xmlContent, "").outputSettings().charset("UTF-8");
        return doc;
    }

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test.html");
        Document doc = DataUtil.load(testFile, StandardCharsets.UTF_8.name(), "/");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testHtml = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(testInputStream, StandardCharsets.UTF_8.name(), "/");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String testHtml = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(testInputStream, "ISO-8859-1", "/"));
    }

    @Test
    public void testParseInputStream() throws IOException {
        String testHtml = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(testInputStream, StandardCharsets.UTF_8.name(), "/", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String testHtml = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseInputStream(testInputStream, "ISO-8859-1", "/", Parser.htmlParser()));
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testText = "Test text";
        InputStream testInputStream = new ByteArrayInputStream(testText.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(testInputStream, testOutputStream);
        String outputText = testOutputStream.toString(StandardCharsets.UTF_8.name());
        assertEquals(testText, outputText);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidContentType() {
        String contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("utf-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\t"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\n"));
        assertEquals(null, DataUtil.validateCharset(""));
        assertEquals(null, DataUtil.validateCharset("ISO-8859-1"));
        assertEquals(null, DataUtil.validateCharset("GB18030"));
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertFalse(mimeBoundary.isEmpty());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer testData = DataUtil.readFileToByteBuffer(new File("inputFileWithBom.html"));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(testData);
        assertEquals("UTF-32", bomCharset.charset);
        assertEquals(0, bomCharset.offset);

        testData = DataUtil.readFileToByteBuffer(new File("inputFileWithUtf16Bom.html"));
        bomCharset = DataUtil.detectCharsetFromBom(testData);
        assertEquals("UTF-16", bomCharset.charset);
        assertEquals(0, bomCharset.offset);

        testData = DataUtil.readFileToByteBuffer(new File("inputFileWithUtf8Bom.html"));
        bomCharset = DataUtil.detectCharsetFromBom(testData);
        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(3, bomCharset.offset);

        testData = DataUtil.readFileToByteBuffer(new File("inputFileWithoutBom.html"));
        bomCharset = DataUtil.detectCharsetFromBom(testData);
        assertNull(bomCharset);
    }
}