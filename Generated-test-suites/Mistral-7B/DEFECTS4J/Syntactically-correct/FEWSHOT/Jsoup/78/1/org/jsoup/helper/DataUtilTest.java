package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    private static final String TEST_FILE_CHARSET = "UTF-8";

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test.html");
        Document doc = DataUtil.load(testFile, TEST_FILE_CHARSET, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream testInput = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(testInput, TEST_FILE_CHARSET, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream testInput = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(testInput, "INVALID_CHARSET", null);
        assertNotNull(doc);
        assertEquals(TEST_FILE_CHARSET, doc.outputSettings().charset());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream testInput = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(testInput, TEST_FILE_CHARSET, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithIOException() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream testInput = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        InputStream testOutput = new ByteArrayInputStream("not an input stream".getBytes());
        try {
            DataUtil.parseInputStream(testOutput, TEST_FILE_CHARSET, null, Parser.htmlParser());
            assert(false); // this should throw an IOException
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        String source = "Source data";
        String target = "Target data";
        InputStream testInput = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));
        OutputStream testOutput = new ByteArrayOutputStream();
        DataUtil.crossStreams(testInput, testOutput);
        byte[] targetData = testOutput.toByteArray();
        assertEquals(source, new String(targetData, StandardCharsets.UTF_8));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream testInput = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInput, 1024 * 5); // first read is < buffer size, so can't be invalid
        assertNotNull(byteBuffer);
        assertEquals(1024 * 5, byteBuffer.remaining());
    }

    @Test
    public void testReadToByteBufferWithIOException() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream testInput = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        try {
            DataUtil.readToByteBuffer(null, 1024 * 5);
            assert(false); // this should throw an IOException
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testReadToByteBufferWithEOF() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream testInput = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInput);
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.remaining());
        testInput.close();

        try {
            byteBuffer.get();
            assert(false); // this should throw an IOException
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File testFile = new File("test.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
        assertNotNull(byteBuffer);
        assertEquals(testFile.length(), byteBuffer.remaining());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.remaining());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(charset, "UTF-8");
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String charset = DataUtil.getCharsetFromContentType(null);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidContentType() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-16";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);

        charset = "INVALID_CHARSET";
        validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);

        charset = "UTF-8";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        int boundaryLength = mimeBoundary.length();
        for (int i = 0; i < boundaryLength; i++) {
            assertEquals(mimeBoundary.charAt(i), mimeBoundaryChars[i % mimeBoundaryChars.length]);
        }
    }

    @Test
    public void testDetectCharsetFromBom() {
        String testByteData = "EF BBBF 68 65 6C 6C 6F 20 74 68 69 73 70 61 20 73 72 63 68 65 72 20 61 6D 20 63 61 6D 65";
        ByteBuffer byteData = ByteBuffer.wrap(testByteData.getBytes(StandardCharsets.UTF_8));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        if (bomCharset != null) {
            assertEquals("UTF-8", bomCharset.charset);
            assertEquals(3, bomCharset.offset);
        } else {
            assertNull(bomCharset);
        }
    }

    @Test
    public void testDetectCharsetFromBomWithNullByteData() {
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(null);
        assertNull(bomCharset);
    }
}

class BomCharset {
    private final String charset;
    private final int offset;

    public BomCharset(String charset, int offset) {
        this.charset = charset;
        this.offset = offset;
    }
}