package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String filePath = "testFileWithValidCharset.html";
        Document document = DataUtil.load(new File(filePath), charsetName, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        String filePath = "testFileWithValidCharset.html";
        try {
            DataUtil.load(new File(filePath), charsetName, null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        InputStream inputStream = getClass().getResourceAsStream("testFileWithValidCharset.html");
        Document document = DataUtil.load(inputStream, charsetName, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        InputStream inputStream = getClass().getResourceAsStream("testFileWithValidCharset.html");
        try {
            DataUtil.load(inputStream, charsetName, null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charsetName = "UTF-8";
        InputStream inputStream = getClass().getResourceAsStream("testFileWithValidCharset.html");
        Parser parser = DataUtil.Parser.xmlParser();
        Document document = DataUtil.load(inputStream, charsetName, null, parser);
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String sourceFile = "sourceFile.txt";
        String targetFile = "targetFile.txt";
        File source = new File(sourceFile);
        File target = new File(targetFile);

        try (InputStream inputStream = new FileInputStream(source);
             OutputStream outputStream = new FileOutputStream(target)) {

            DataUtil.crossStreams(inputStream, outputStream);
            assertTrue(source.length() == target.length());
        }

        byte[] sourceContent = new byte[source.length()];
        try (InputStream inputStream = new FileInputStream(source)) {
            inputStream.read(sourceContent);
        }

        byte[] targetContent = new byte[target.length()];
        try (InputStream inputStream = new FileInputStream(target);
             OutputStream outputStream = new ByteArrayOutputStream()) {
            DataUtil.crossStreams(inputStream, outputStream);
            targetContent = outputStream.toByteArray();
        }

        assertArrayEquals(sourceContent, targetContent);
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String inputData = "Test data.";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, charsetName, null, DataUtil.Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        String inputData = "Test data.";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        try {
            DataUtil.parseInputStream(inputStream, charsetName, null, DataUtil.Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        String charsetName = "UTF-8";
        String inputData = "Test data.";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Parser parser = DataUtil.Parser.xmlParser();
        Document document = DataUtil.parseInputStream(inputStream, charsetName, null, parser);
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputData = "Test data.";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals(inputData.getBytes(StandardCharsets.UTF_8).length, byteBuffer.limit());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String filePath = "testFileWithValidCharset.html";
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(new File(filePath));
        File inputFile = new File(filePath);
        assertEquals(inputFile.length(), byteBuffer.limit());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        String contentType2 = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType2));
    }

    @Test
    public void testValidateCharset() {
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8 "));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\n"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\t"));

        assertNull(DataUtil.validateCharset("InvalidCharset"));
        assertNull(DataUtil.validateCharset(""));
        assertNull(DataUtil.validateCharset(null));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(32, boundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        byte[] bomUtf32 = {0x00, 0x00, (byte) 0xFE, (byte) 0xFF};
        byte[] bomUtf16 = {(byte) 0xFF, (byte) 0xFE, 0x00, 0x00};
        byte[] bomUtf8 = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

        BomCharset bomUtf32Result = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(bomUtf32));
        BomCharset bomUtf16Result = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(bomUtf16));
        BomCharset bomUtf8Result = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(bomUtf8));

        assertEquals("UTF-32", bomUtf32Result.charset);
        assertEquals(false, bomUtf32Result.offset);

        assertEquals("UTF-16", bomUtf16Result.charset);
        assertEquals(false, bomUtf16Result.offset);

        assertEquals("UTF-8", bomUtf8Result.charset);
        assertEquals(true, bomUtf8Result.offset);

        assertNull(DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[4])));
    }
}