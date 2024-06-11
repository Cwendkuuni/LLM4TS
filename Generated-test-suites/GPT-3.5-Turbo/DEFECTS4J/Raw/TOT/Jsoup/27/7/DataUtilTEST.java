package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {

    private File testFile;
    private String testFileBaseUri;
    private String expectedDefaultCharset = "UTF-8";

    @Before
    public void setUp() {
        testFile = new File("test/test.html");
        testFileBaseUri = "https://example.com";
    }

    @Test
    public void testLoad_withValidFile_returnsDocument() throws IOException {
        Document document = DataUtil.load(testFile, "UTF-8", testFileBaseUri);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoad_withInvalidFile_throwsIOException() throws IOException {
        Document document = DataUtil.load(new File("invalid.html"), "UTF-8", testFileBaseUri);
    }

    @Test
    public void testLoad_withNullCharsetName_usesDefaultCharset() throws IOException {
        Document document = DataUtil.load(testFile, null, testFileBaseUri);
        assertNotNull(document);
        assertEquals(expectedDefaultCharset, document.outputSettings().charset().name());
    }

    @Test
    public void testLoad_withInputStream_returnsDocument() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        Document document = DataUtil.load(inputStream, "UTF-8", testFileBaseUri);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoad_withInvalidInputStream_throwsIOException() throws IOException {
        InputStream inputStream = new FileInputStream(new File("invalid.html"));
        Document document = DataUtil.load(inputStream, "UTF-8", testFileBaseUri);
    }

    @Test
    public void testLoad_withParser_returnsDocument() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        Document document = DataUtil.load(inputStream, "UTF-8", testFileBaseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoad_withInvalidParser_throwsIOException() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        Document document = DataUtil.load(inputStream, "UTF-8", testFileBaseUri, Parser.xmlParser());
    }

    @Test
    public void testParseByteData_withValidData_returnsDocument() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("<html><head></head><body></body></html>".getBytes());
        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", testFileBaseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test(expected = NullPointerException.class)
    public void testParseByteData_withNullByteData_throwsNullPointerException() {
        Document document = DataUtil.parseByteData(null, "UTF-8", testFileBaseUri, Parser.htmlParser());
    }

    @Test
    public void testReadToByteBuffer_withValidInputStream_returnsByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.limit() > 0);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBuffer_withInvalidInputStream_throwsIOException() throws IOException {
        InputStream inputStream = new FileInputStream(new File("invalid.html"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
    }

    @Test
    public void testGetCharsetFromContentType_withValidContentType_returnsValidCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_withInvalidContentType_returnsNull() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

}