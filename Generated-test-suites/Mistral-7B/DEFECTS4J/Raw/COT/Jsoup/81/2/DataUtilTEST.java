package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

public class DataUtilTest {
    /**
     * Test method for {@link DataUtil#load(File, String, String)}
     */
    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test.html"); // replace with actual test file
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(testFile, charset, baseUri);
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String testHtml = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes());
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charset, baseUri);
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}
     */
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testHtml = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes());
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charset, baseUri, Parser.xmlParser());
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#parseInputStream(InputStream, String, String, Parser)}
     */
    @Test
    public void testParseInputStream() throws IOException {
        String testHtml = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes());
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.parseInputStream(inputStream, charset, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#parseInputStream(InputStream, String, String, Parser)} with an unsupported charset
     */
    @Test(expected = UncheckedIOException.class)
    public void testParseInputStreamWithUnsupportedCharset() throws IOException {
        String testHtml = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes());
        String charset = "X-PASSED-THROUGH"; // unsupported charset
        String baseUri = "http://example.com";

        DataUtil.parseInputStream(inputStream, charset, baseUri, Parser.htmlParser());
    }

    /**
     * Test method for {@link DataUtil#parseInputStream(InputStream, String, String, Parser)} with an empty input stream
     */
    @Test
    public void testParseInputStreamWithEmptyInput() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.parseInputStream(new ByteArrayInputStream(new byte[0]), charset, baseUri, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(0, document.childNodeSize());
    }

    /**
     * Test method for {@link DataUtil#crossStreams(InputStream, OutputStream)}
     */
    @Test
    public void testCrossStreams() throws IOException {
        String testHtml = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        assertEquals(testHtml, outputStream.toString());
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream, int)}
     */
    @Test
    public void testReadToByteBuffer() throws IOException {
        String testHtml = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);

        assertEquals(testHtml.length(), byteBuffer.limit());
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream)}
     */
    @Test
    public void testReadToByteBufferWithDefaultBufferSize() throws IOException {
        String testHtml = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        assertEquals(testHtml.length(), byteBuffer.limit());
    }

    /**
     * Test method for {@link DataUtil#readFileToByteBuffer(File)}
     */
    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File testFile = new File("test.html"); // replace with actual test file
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);

        assertEquals(Files.size(testFile.toPath()), byteBuffer.limit());
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        assertEquals(expectedCharset, DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    /**
     * Test method for {@link DataUtil#validateCharset(String)}
     */
    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-8\u0000";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "";
        assertNull(DataUtil.validateCharset(charset));

        charset = "invalidCharset";
        assertNull(DataUtil.validateCharset(charset));
    }

    /**
     * Test method for {@link DataUtil#mimeBoundary()}
     */
    @Test
    public void testMimeBoundary() {
        String expectedMimeBoundary = new String(new Random().generateSeed(32), Locale.ROOT);
        assertEquals(expectedMimeBoundary, DataUtil.mimeBoundary());
    }

    /**
     * Test method for {@link DataUtil#detectCharsetFromBom(ByteBuffer)}
     */
    @Test
    public void testDetectCharsetFromBom() {
        byte[] bomUtf8 = {0xFF, 0xFE, 0x00, 0x00};
        byte[] bomUtf16BE = {0xFE, 0xFF, 0x00, 0x00};
        byte[] bomUtf16LE = {0xFF, 0xFE, 0x00, 0x00};
        byte[] bomUTF32 = {0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x00, 0x00};

        ByteBuffer byteBufferUtf8 = ByteBuffer.wrap(bomUtf8);
        ByteBuffer byteBufferUtf16BE = ByteBuffer.wrap(bomUtf16BE);
        ByteBuffer byteBufferUtf16LE = ByteBuffer.wrap(bomUtf16LE);
        ByteBuffer byteBufferUTF32 = ByteBuffer.wrap(bomUTF32);

        BomCharset utf8Bom = DataUtil.detectCharsetFromBom(byteBufferUtf8);
        BomCharset utf16BEBom = DataUtil.detectCharsetFromBom(byteBufferUtf16BE);
        BomCharset utf16LEBom = DataUtil.detectCharsetFromBom(byteBufferUtf16LE);
        BomCharset utf32Bom = DataUtil.detectCharsetFromBom(byteBufferUTF32);

        assertEquals("UTF-8", utf8Bom.charset);
        assertEquals(false, utf8Bom.offset);

        assertEquals("UTF-16", utf16BEBom.charset);
        assertEquals(false, utf16BEBom.offset);

        assertEquals("UTF-16", utf16LEBom.charset);
        assertEquals(true, utf16LEBom.offset);

        assertEquals("UTF-32", utf32Bom.charset);
        assertEquals(false, utf32Bom.offset);
    }
}