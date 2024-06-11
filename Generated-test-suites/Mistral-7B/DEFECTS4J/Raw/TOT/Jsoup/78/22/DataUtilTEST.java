package org.jsoup.helper.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    private static final String TEST_FILE = "test.html";
    private Document normalDoc;
    private Document charsetDoc;
    private Document noBomDoc;
    private Document noInputStreamDoc;
    private Document emptyDoc;
    private Document mimeDoc;
    private Document xmlDoc;

    @Before
    public void setup() throws IOException {
        String htmlContent = "<html lang=\"en\"><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        normalDoc = DataUtil.load(new File(TEST_FILE), "UTF-8", "/");
        if (normalDoc == null) {
            fail("load(File, String, String) should not return null");
        }

        String charsetHtmlContent = "<html lang=\"en\"><head><meta charset=\"ISO-8859-1\"></head><body><h1>Test</h1></body></html>";
        charsetDoc = DataUtil.load(new ByteArrayInputStream(charsetHtmlContent.getBytes(StandardCharsets.UTF_8)), "ISO-8859-1", "/");

        String noBomHtmlContent = "<html lang=\"en\"><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>"; // no BOM
        ByteArrayInputStream noBomInputStream = new ByteArrayInputStream(noBomHtmlContent.getBytes(StandardCharsets.UTF_8));
        noBomDoc = DataUtil.load(noBomInputStream, null, "/", Parser.htmlParser());

        String noInputStreamContent = ""; // no input stream
        InputStream noInputStream = new ByteArrayInputStream(noInputStreamContent.getBytes(StandardCharsets.UTF_8));
        noInputStreamDoc = DataUtil.load(noInputStream, null, "/", Parser.htmlParser());

        String emptyContent = "";
        emptyDoc = DataUtil.load(new ByteArrayInputStream(emptyContent.getBytes(StandardCharsets.UTF_8)), null, "/", Parser.htmlParser());

        String mimeContent = "-- boundary\r\nContent-Type: text/plain\r\nContent-Disposition: attachment; filename=\"test.txt\"\r\n\r\nTest\r\n-- boundary--";
        ByteArrayInputStream mimeInputStream = new ByteArrayInputStream(mimeContent.getBytes(StandardCharsets.UTF_8));
        mimeDoc = DataUtil.load(mimeInputStream, null, "/", Parser.textParser());

        String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><test>Hello World</test></root>";
        ByteArrayInputStream xmlInputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
        xmlDoc = DataUtil.load(xmlInputStream, null, "/", Parser.xmlParser());
    }

    @Test
    public void testLoadFile() throws IOException {
        Document doc = DataUtil.load(new File(TEST_FILE), null, "/");
        assertEquals(normalDoc, doc);
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        Document doc = DataUtil.load(new ByteArrayInputStream(charsetHtmlContent.getBytes(StandardCharsets.UTF_8)), "ISO-8859-1", "/");
        assertEquals(charsetDoc, doc);
    }

    @Test
    public void testLoadInputStreamWithoutCharset() throws IOException {
        Document doc = DataUtil.load(new ByteArrayInputStream(noBomHtmlContent.getBytes(StandardCharsets.UTF_8)), null, "/", Parser.htmlParser());
        assertEquals(noBomDoc, doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException, IOException {
        Document doc = DataUtil.load(new ByteArrayInputStream(noBomHtmlContent.getBytes(StandardCharsets.UTF_8)), "invalidCharset", "/", Parser.htmlParser());
        fail("loadInputStreamWithInvalidCharset should throw IOException");
    }

    @Test
    public void testLoadEmptyFile() throws IOException {
        Document doc = DataUtil.load(new File(""), null, "/", Parser.htmlParser());
        assertEquals(emptyDoc, doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInvalidFile() throws IOException {
        Document doc = DataUtil.load(new File("invalidFile"), null, "/", Parser.htmlParser());
        fail("loadInvalidFile should throw IOException");
    }

    @Test
    public void testLoadMimeTypeFile() throws IOException {
        Document doc = DataUtil.load(new File(TEST_FILE), null, "/", Parser.textParser());
        assertEquals(noBomDoc, doc);
    }

    @Test
    public void testLoadXmlFile() throws IOException {
        Document doc = DataUtil.load(new File(TEST_FILE), null, "/", Parser.xmlParser());
        assertEquals(xmlDoc, doc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadNullFile() throws IOException {
        Document doc = DataUtil.load(null, null, "/", Parser.htmlParser());
        fail("loadNullFile should throw IllegalArgumentException");
    }

    @Test
    public void testParseInputStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(new ByteArrayInputStream(noBomHtmlContent.getBytes(StandardCharsets.UTF_8)), outputStream);
        ByteArrayInputStream parsedInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.parseInputStream(parsedInputStream, null, "/", Parser.htmlParser());
        assertEquals(noBomDoc, doc);
    }

    @Test(expected = IllegalStateException.class)
    public void testParseInputStreamWithClosedInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(noBomHtmlContent.getBytes(StandardCharsets.UTF_8));
        DataUtil.parseInputStream(inputStream, null, "/", Parser.htmlParser());
        inputStream.close();
        Document doc = DataUtil.parseInputStream(inputStream, null, "/", Parser.htmlParser());
        fail("parseInputStreamWithClosedInputStream should throw IllegalStateException");
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(noBomHtmlContent.getBytes(StandardCharsets.UTF_8));
        DataUtil.parseInputStream(inputStream, "invalidCharset", "/", Parser.htmlParser());
        fail("parseInputStreamWithInvalidCharset should throw IOException");
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(noBomHtmlContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        // Check byte buffer content
        byte[] expectedBytes = noBomHtmlContent.getBytes(StandardCharsets.UTF_8);
        byte[] actualBytes = new byte[byteBuffer.limit()];
        byteBuffer.get(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File(TEST_FILE);
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        // Check byte buffer content
        byte[] expectedBytes = Files.readAllBytes(file.toPath());
        byte[] actualBytes = new byte[byteBuffer.limit()];
        byteBuffer.get(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html"));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/plain"));
    }

    @Test
    public void testValidateCharset() {
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("utf-8"));
        assertEquals(null, DataUtil.validateCharset("invalidCharset"));
        assertEquals(null, DataUtil.validateCharset("invalidCharset123"));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == boundaryLength);
        for (int i = 0; i < boundaryLength; i++) {
            assertTrue(Character.isAlphabetic(boundary.charAt(i)) || Character.isDigit(boundary.charAt(i)) || boundary.charAt(i) == '-');
        }
    }
}