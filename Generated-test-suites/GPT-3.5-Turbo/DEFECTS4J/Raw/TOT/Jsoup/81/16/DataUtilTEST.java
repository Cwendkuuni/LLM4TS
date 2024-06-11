package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class DataUtilTest {
    private File testFile;
    private String charsetName;
    private String baseUri;
    private Parser parser;

    @Before
    public void setUp() {
        testFile = new File("test.html");
        charsetName = "UTF-8";
        baseUri = "http://example.com";
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoadFile_withExistingFile() throws IOException {
        Document doc = DataUtil.load(testFile, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFile_withNonExistingFile() throws IOException {
        File nonExistingFile = new File("non_existing.html");
        DataUtil.load(nonExistingFile, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStream_withValidInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStream_withInvalidInputStream() throws IOException {
        InputStream invalidInputStream = new ByteArrayInputStream("invalid".getBytes());
        DataUtil.load(invalidInputStream, charsetName, baseUri);
    }

    @Test
    public void testLoadAlternateParser_withValidInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadAlternateParser_withInvalidInputStream() throws IOException {
        InputStream invalidInputStream = new ByteArrayInputStream("invalid".getBytes());
        DataUtil.load(invalidInputStream, charsetName, baseUri, Parser.xmlParser());
    }

    @Test
    public void testCrossStreams_withValidInputStreamAndOutputStream() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        byte[] outputBytes = outputStream.toByteArray();
        assertTrue(outputBytes.length > 0);
    }

    @Test(expected = IOException.class)
    public void testCrossStreams_withInvalidInputStream() throws IOException {
        InputStream invalidInputStream = new ByteArrayInputStream("invalid".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(invalidInputStream, outputStream);
    }

    @Test
    public void testParseInputStream_withValidInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testParseInputStream_withInvalidInputStream() throws IOException {
        InputStream invalidInputStream = new ByteArrayInputStream("invalid".getBytes());
        DataUtil.parseInputStream(invalidInputStream, charsetName, baseUri, Parser.htmlParser());
    }

    @Test
    public void testReadToByteBuffer_withValidInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        int maxSize = 100000;
        byte[] bytes = DataUtil.readToByteBuffer(inputStream, maxSize).array();
        assertTrue(bytes.length > 0);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBuffer_withInvalidInputStream() throws IOException {
        InputStream invalidInputStream = new ByteArrayInputStream("invalid".getBytes());
        DataUtil.readToByteBuffer(invalidInputStream);
    }

    @Test
    public void testReadFileToByteBuffer_withExistingFile() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
        assertNotNull(byteBuffer);
    }

    @Test(expected = IOException.class)
    public void testReadFileToByteBuffer_withNonExistingFile() throws IOException {
        File nonExistingFile = new File("non_existing.html");
        DataUtil.readFileToByteBuffer(nonExistingFile);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType_withValidContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_withInvalidContentType() {
        String contentType = "text/html; charset=INVALID";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset_withSupportedCharset() {
        String cs = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(cs);
        assertNotNull(validatedCharset);
        assertEquals(cs, validatedCharset);
    }

    @Test
    public void testValidateCharset_withUnsupportedCharset() {
        String cs = "INVALID";
        String validatedCharset = DataUtil.validateCharset(cs);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }
}