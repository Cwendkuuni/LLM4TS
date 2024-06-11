package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testgetDefaultCharset() {
        assertEquals("UTF-8", DataUtil.defaultCharset);
    }

    @Test
    public void testBufferSize() {
        assertEquals(0x20000, DataUtil.bufferSize);
    }

    @Test
    public void testParseByteData_nullCharsetName() throws IOException {
        assertNull(DataUtil.parseByteData(DataUtil.emptyByteBuffer(), null, null, Parser.htmlParser()));
    }

    @Test
    public void testParseByteData_validCharsetName() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("src/test/resources/test.html"));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteData_noCharsetSpecified() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("src/test/resources/test.html"));
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(doc);
        Element meta = doc.select("meta[http-equiv=content-type], meta[charset]").first();
        if (meta != null) {
            String foundCharset = DataUtil.getCharsetFromContentType(meta.attr("content"));
            assertEquals("UTF-8", foundCharset);
        }
    }

    @Test
    public void testParseByteData_validCharsetName_noBOM() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("src/test/resources/test.html"));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
        assertFalse(docDataContainsBOM(doc.body().html()));
    }

    @Test
    public void testParseByteData_validCharsetName_withBOM() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("src/test/resources/test_utf16be.html"));
        Document doc = DataUtil.parseByteData(byteData, "UTF-16BE", null, Parser.htmlParser());
        assertNotNull(doc);
        assertTrue(docDataContainsBOM(doc.body().html()));
    }

    @Test
    public void testParseByteData_invalidCharsetName() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("src/test/resources/test.html"));
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, "INVALID_CHARSET", null, Parser.htmlParser()));
    }

    @Test
    public void testParseByteData_nullBaseUri() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("src/test/resources/test.html"));
        assertNull(DataUtil.parseByteData(byteData, null, null, Parser.htmlParser()));
    }

    @Test
    public void testParseByteData_validBaseUri() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("src/test/resources/test.html"));
        Document doc = DataUtil.parseByteData(byteData, null, "/test/resources", Parser.htmlParser());
        assertNotNull(doc);
        Element img = doc.select("img").first();
        assertEquals("/test/resources/img.png", img.attr("src"));
    }

    @Test
    public void testParseByteData_nullParser() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("src/test/resources/test.html"));
        assertNull(DataUtil.parseByteData(byteData, null, null, null));
    }

    @Test
    public void testParseByteData_emptyByteBuffer() {
        assertNull(DataUtil.parseByteData(DataUtil.emptyByteBuffer(), null, null, Parser.htmlParser()));
    }

    @Test
    public void testLoadFile() throws IOException {
        Document doc = DataUtil.load(new File("src/test/resources/test.html"), null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFile_nullFile() throws IOException {
        assertNull(DataUtil.load(null, null, null));
    }

    @Test
    public void testLoadFile_nullCharsetName() throws IOException {
        Document doc = DataUtil.load(new File("src/test/resources/test.html"), null, null);
        assertNotNull(doc);
        Element meta = doc.select("meta[http-equiv=content-type], meta[charset]").first();
        if (meta != null) {
            String foundCharset = DataUtil.getCharsetFromContentType(meta.attr("content"));
            assertEquals("UTF-8", foundCharset);
        }
    }

    @Test
    public void testLoadFile_validCharsetName() throws IOException {
        Document doc = DataUtil.load(new File("src/test/resources/test.html"), "UTF-8", null);
        assertNotNull(doc);
        assertFalse(docDataContainsBOM(doc.body().html()));
    }

    @Test
    public void testLoadFile_validBaseUri() throws IOException {
        Document doc = DataUtil.load(new File("src/test/resources/test.html"), null, "/test/resources");
        assertNotNull(doc);
        Element img = doc.select("img").first();
        assertEquals("/test/resources/img.png", img.attr("src"));
    }

    @Test
    public void testLoadFile_nullBaseUriAndCharsetName() throws IOException {
        Document doc = DataUtil.load(new File("src/test/resources/test.html"), null, null);
        assertNotNull(doc);
        Element meta = doc.select("meta[http-equiv=content-type], meta[charset]").first();
        if (meta != null) {
            String foundCharset = DataUtil.getCharsetFromContentType(meta.attr("content"));
            assertEquals("UTF-8", foundCharset);
        }
    }

    @Test
    public void testLoadInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        Document doc = DataUtil.load(inputStream, null, null);
        assertNotNull(doc);
        assertEquals("test data", doc.body().text());
    }

    @Test
    public void testLoadInputStream_nullInputStream() throws IOException {
        assertNull(DataUtil.load(null, null, null));
    }

    @Test
    public void testLoadInputStream_nullCharsetName() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        Document doc = DataUtil.load(inputStream, null, null);
        assertNotNull(doc);
        Element meta = doc.select("meta[http-equiv=content-type], meta[charset]").first();
        if (meta != null) {
            String foundCharset = DataUtil.getCharsetFromContentType(meta.attr("content"));
            assertEquals("UTF-8", foundCharset);
        }
    }

    @Test
    public void testLoadInputStream_validCharsetName() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test data".getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
        assertFalse(docDataContainsBOM(doc.body().html()));
    }

    @Test
    public void testLoadInputStream_validBaseUri() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        Document doc = DataUtil.load(inputStream, null, "/test/resources");
        assertNotNull(doc);
        Element img = doc.select("img").first();
        assertEquals("/test/resources/img.png", img.attr("src"));
    }

    @Test
    public void testLoadInputStream_nullBaseUriAndCharsetName() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        Document doc = DataUtil.load(inputStream, null, null);
        assertNotNull(doc);
        Element meta = doc.select("meta[http-equiv=content-type], meta[charset]").first();
        if (meta != null) {
            String foundCharset = DataUtil.getCharsetFromContentType(meta.attr("content"));
            assertEquals("UTF-8", foundCharset);
        }
    }

    @Test
    public void testLoadInputStream_parser() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertNotNull(doc);
        assertEquals("test data", doc.body().text());
    }

    @Test
    public void testLoadInputStream_parserNull() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        assertNull(DataUtil.load(inputStream, null, null, null));
    }

    @Test
    public void testLoadInputStream_parserInvalid() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, null, null, new Parser() {}));
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals("test data", outputStream.toString());
    }

    @Test
    public void testGetCharsetFromContentType_nullContentType() {
        assertNull(DataUtil.getCharsetFromContentType(null));
    }

    @Test
    public void testGetCharsetFromContentType_validContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentType_validContentTypeUppercase() {
        String contentType = "TEXT/HTML; CHARSET=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentType_noCharsetSpecified() {
        String contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
        assertEquals(13, byteBuffer.limit());
    }

    @Test
    public void testReadToByteBuffer_nullInputStream() throws IOException {
        assertNull(DataUtil.readToByteBuffer(null));
    }

    @Test
    public void testReadToByteBuffer_emptyInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("src/test/resources/test.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        assertNotNull(byteBuffer);
        assertEquals(14881, byteBuffer.limit());
    }

    @Test
    public void testReadFileToByteBuffer_nullFile() throws IOException {
        assertNull(DataUtil.readFileToByteBuffer(null));
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.limit());
    }

    private boolean docDataContainsBOM(String docData) {
        return docData.length() > 0 && docData.charAt(0) == DataUtil.UNICODE_BOM;
    }
}