package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {

    private File testFile;

    @Before
    public void setUp() {
        testFile = new File("src/test/resources/test.html");
    }

    @Test
    public void testLoad_existingFile_returnDocument() throws IOException {
        Document document = DataUtil.load(testFile, "UTF-8", "");
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset().displayName());
    }

    @Test(expected = IOException.class)
    public void testLoad_nonExistingFile_throwIOException() throws IOException {
        File nonExistingFile = new File("nonexistent.html");
        DataUtil.load(nonExistingFile, "UTF-8", "");
    }

    @Test
    public void testLoad_NullCharset_returnDocumentWithDefaultCharset() throws IOException {
        Document document = DataUtil.load(testFile, null, "");
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset().displayName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoad_NullInputStream_throwIllegalArgumentException() throws IOException {
        InputStream inputStream = null;
        DataUtil.load(inputStream, "UTF-8", "");
    }

    @Test(expected = IOException.class)
    public void testLoad_InvalidCharset_throwIOException() throws IOException {
        Document document = DataUtil.load(testFile, "InvalidCharset", "");
    }

    @Test
    public void testLoad_validInputStream_returnDocument() throws IOException {
        String html = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(document);
        assertTrue(document.hasText());
        assertEquals("Test", document.body().select("h1").text());
    }

    @Test
    public void testLoad_withParser_returnDocument() throws IOException {
        String html = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());
        assertNotNull(document);
        assertTrue(document.hasText());
        assertEquals("Test", document.select("h1").text());
    }

    @Test
    public void testParseByteData_withNullCharset_returnDocumentWithDetectedCharset() {
        String html = "<html><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";

        Document document = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());

        assertNotNull(document);
        assertTrue(document.hasText());
        assertEquals("Test", document.body().select("h1").text());
    }

    @Test
    public void testParseByteData_withCharset_returnDocumentWithSpecifiedCharset() {
        String html = "<html><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";

        Document document = DataUtil.parseByteData(byteData, "UTF-8", baseUri, Parser.htmlParser());

        assertNotNull(document);
        assertTrue(document.hasText());
        assertEquals("Test", document.body().select("h1").text());
    }

    @Test
    public void testReadToByteBuffer_withNonZeroMaxSize_returnByteBufferWithLimitedSize() throws IOException {
        String html = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        int maxSize = 5;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        assertNotNull(byteBuffer);
        assertEquals(maxSize, byteBuffer.limit());
        assertEquals(byteBuffer.array().length, byteBuffer.limit());
    }

    @Test
    public void testReadToByteBuffer_withZeroMaxSize_returnFullByteBuffer() throws IOException {
        String html = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        int maxSize = 0;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        assertNotNull(byteBuffer);
        assertEquals(html.getBytes(StandardCharsets.UTF_8).length, byteBuffer.limit());
        assertEquals(byteBuffer.array().length, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType_withValidContentType_returnCharset() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_withInvalidContentType_returnNull() {
        String contentType = "text/html; charset=InvalidCharset";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

}