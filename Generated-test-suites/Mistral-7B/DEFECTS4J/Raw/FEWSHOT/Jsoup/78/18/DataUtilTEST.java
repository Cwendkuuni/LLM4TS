package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        Path filePath = Files.createTempFile("datautiltest", ".html");
        Files.write(filePath, fileContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(filePath.toFile(), null, null);
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>", doc.body().html());

        Files.delete(filePath);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, null, null);
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>", doc.body().html());
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>", doc.body().html());
        inputStream.close();
    }

    @Test
    public void testParseInputStream() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        byte[] inputStreamBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);

        InputStream inputStream = new ByteArrayInputStream(inputStreamBytes);
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>", doc.body().html());
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithCharset() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        byte[] inputStreamBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);

        InputStream inputStream = new ByteArrayInputStream(inputStreamBytes);
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>", doc.body().html());
        inputStream
.close();
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        byte[] inputStreamBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);

        InputStream inputStream = new ByteArrayInputStream(inputStreamBytes);
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.xmlParser());
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>", doc.body().html());
        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testInput = "Test Input";
        String testOutput = "Test Output";

        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] outputBytes = outputStream.toByteArray();
        assertEquals(testInput, new String(outputBytes, StandardCharsets.UTF_8));
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertFalse(boundary.isEmpty());
        for (int i = 0; i < boundary.length(); i++) {
            char c = boundary.charAt(i);
            assertTrue(java.util.Arrays.binarySearch(DataUtil.mimeBoundaryChars, c) >= 0);
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=gb2312";
        String expectedCharset = "gb2312";
        assertEquals(expectedCharset, DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        String charset1 = "UTF-8";
        String charset2 = "utf-8";
        String charset3 = "gb2312";
        String charset4 = "invalidCharset";

        assertEquals(charset1, DataUtil.validateCharset(charset1));
        assertEquals(charset1, DataUtil.validateCharset(charset2));
        assertNull(DataUtil.validateCharset(null));
        assertNull(DataUtil.validateCharset(""));
        assertNull(DataUtil.validateCharset(charset3)); // not supported
        assertNull(DataUtil.validateCharset(charset4)); // invalid
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputContent = "Test Content";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals(inputContent, StandardCharsets.UTF_8.decode(byteBuffer).toString());
        inputStream.close();
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String inputContent = "Test Content";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);
        assertEquals(10, byteBuffer.remaining());
        inputStream.close();
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String fileContent = "Test Content";
        Path filePath = Files.createTempFile("datautiltest", ".txt");
        Files.write(filePath, fileContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(filePath.toFile());
        assertEquals(fileContent, StandardCharsets.UTF_8.decode(byteBuffer).toString());
        Files.delete(filePath);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.remaining());
    }

    @Test
    public void testDetectCharsetFromBom() {
        byte[] utf32Bom = {0x00, 0x00, 0xFE, 0xFF};
        byte[] utf16LEBom = {0xFF, 0xFE, 0x00, 0x00};
        byte[] utf8Bom = {0xEF, 0xBB, 0xBF};

        ByteBuffer utf32ByteBuffer = ByteBuffer.wrap(utf32Bom);
        ByteBuffer utf16LEByteBuffer = ByteBuffer.wrap(utf16LEBom);
        ByteBuffer utf8ByteBuffer = ByteBuffer.wrap(utf8Bom);

        BomCharset utf32Result = DataUtil.detectCharsetFromBom(utf32ByteBuffer);
        assertEquals("UTF-32", utf32Result.charset);
        assertEquals(0, utf32Result.offset);

        BomCharset utf16LEResult = DataUtil.detectCharsetFromBom(utf16LEByteBuffer);
        assertEquals("UTF-16", utf16LEResult.charset);
        assertEquals(0, utf16LEResult.offset);

        BomCharset utf8Result = DataUtil.detectCharsetFromBom(utf8ByteBuffer);
        assertEquals("UTF-8", utf8Result.charset);
        assertEquals(3, utf8Result.offset);

        ByteBuffer invalidBomByteBuffer = ByteBuffer.allocate(1);
        fail("Expected a NullPointerException but did not receive one."); // NPE expected but not thrown
        BomCharset invalidResult = DataUtil.detectCharsetFromBom(invalidBomByteBuffer);
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