package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RunWith(JUnit4.class)
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String xmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        File file = new File("testFile.html");
        FileUtils.writeStringToFile(file, xmlContent, StandardCharsets.UTF_8);

        Document document = DataUtil.load(file, "UTF-8", "/");
        assertEquals(xmlContent, document.html());

        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String xmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "UTF-8", "/");
        assertEquals(xmlContent, document.html());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String xmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "UTF-8", "/", DataUtil.Parser.xmlParser());
        assertEquals(xmlContent, document.xml());
    }

    @Test(expected = IOException.class)
    public void testLoadNullInputStream() {
        InputStream inputStream = null;
        Document document = DataUtil.load(inputStream, "UTF-8", "/");
    }

    @Test
    public void testParseInputStream() throws IOException {
        String xmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "/", DataUtil.Parser.htmlParser());
        assertEquals(xmlContent, document.html());
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithNullInputStream() {
        InputStream inputStream = null;
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "/", DataUtil.Parser.htmlParser());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String source = "Source content";
        String target = "Target content";
        InputStream inputSource = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputTarget = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputSource, outputTarget);
        assertEquals(source, outputTarget.toString(StandardCharsets.UTF_8.displayName()));
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer emptyByteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0L, emptyByteBuffer.limit());
        assertEquals(0, emptyByteBuffer.position());
        emptyByteBuffer.clear();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String xmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedByteArray = xmlContent.getBytes(StandardCharsets.UTF_8);
        byte[] resultByteArray = new byte[byteBuffer.limit()];
        byteBuffer.get(resultByteArray);

        assertArrayEquals(expectedByteArray, resultByteArray);
    }

    @Test
    public void testReadToByteBufferWithFile() throws IOException {
        String xmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        File file = new File("testFile.html");
        FileUtils.writeStringToFile(file, xmlContent, StandardCharsets.UTF_8);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(file);
        byte[] expectedByteArray = xmlContent.getBytes(StandardCharsets.UTF_8);
        byte[] resultByteArray = new byte[byteBuffer.limit()];
        byteBuffer.get(resultByteArray);

        assertArrayEquals(expectedByteArray, resultByteArray);

        FileUtils.deleteQuietly(file);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithNullInputStream() {
        InputStream inputStream = null;
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithNegativeMaxSize() {
        InputStream inputStream = new ByteArrayInputStream("Test content".getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, -1);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentTypeText = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentTypeText);

        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentTypeText = "text/html; charset=UTF-8, otherParameter=value";
        String charset = DataUtil.getCharsetFromContentType(contentTypeText);

        assertEquals(null, charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String expectedCharset = "UTF-8";
        String charsetFromValidation = DataUtil.validateCharset(charset);

        assertEquals(expectedCharset, charsetFromValidation);
    }

    @Test
    public void testValidateCharsetWithInvalidCharset() {
        String charset = "UTF-8Invalid";
        String expectedCharset = null;
        String charsetFromValidation = DataUtil.validateCharset(charset);

        assertEquals(expectedCharset, charsetFromValidation);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        int boundaryLength = 32;

        assertTrue(mimeBoundary.length() == boundaryLength);
    }

    @Test
    public void testDetectCharsetFromBom() {
        String xmlContentUtf8 = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        String xmlContentUtf16 = "<html><head><meta charset=\"UTF-16\"></head><body></body></html>";
        String xmlContentUtf32 = "<html><head><meta charset=\"UTF-32\"></head><body></body></html>";
        String xmlContentUtf8WithBom = "\ufeff\u0000\u0000\u00fe\u00ff" + xmlContentUtf8;
        String xmlContentUtf16WithBom = "\ufeff\u00fe\u00ff" + xmlContentUtf16;
        String xmlContentUtf32WithBom = "\ufeff\u0000\u0000\u00fe\u00ff" + xmlContentUtf32;

        ByteBuffer xmlContentUtf8ByteBuffer = DataUtil.readToByteBuffer(
                new ByteArrayInputStream(xmlContentUtf8.getBytes(StandardCharsets.UTF_8)));
        ByteBuffer xmlContentUtf16ByteBuffer = DataUtil.readToByteBuffer(
                new ByteArrayInputStream(xmlContentUtf16.getBytes(StandardCharsets.UTF_16)));
        ByteBuffer xmlContentUtf32ByteBuffer = DataUtil.readToByteBuffer(
                new ByteArrayInputStream(xmlContentUtf32.getBytes(StandardCharsets.UTF_32)));
        ByteBuffer xmlContentUtf8WithBomByteBuffer = DataUtil.readToByteBuffer(
                new ByteArrayInputStream(xmlContentUtf8WithBom.getBytes(StandardCharsets.UTF_8)));
        ByteBuffer xmlContentUtf16WithBomByteBuffer = DataUtil.readToByteBuffer(
                new ByteArrayInputStream(xmlContentUtf16WithBom.getBytes(StandardCharsets.UTF_16)));
        ByteBuffer xmlContentUtf32WithBomByteBuffer = DataUtil.readToByteBuffer(
                new ByteArrayInputStream(xmlContentUtf32WithBom.getBytes(StandardCharsets.UTF_32)));

        BomCharset bomCharsetUtf8 = DataUtil.detectCharsetFromBom(xmlContentUtf8WithBomByteBuffer);
        BomCharset bomCharsetUtf16 = DataUtil.detectCharsetFromBom(xmlContentUtf16WithBomByteBuffer);
        BomCharset bomCharsetUtf32 = DataUtil.detectCharsetFromBom(xmlContentUtf32WithBomByteBuffer);

        String utf8Charset = "UTF-8";
        String utf16Charset = "UTF-16";
        String utf32Charset = "UTF-32";

        assertEquals(utf8Charset, bomCharsetUtf8.charset);
        assertEquals(0, bomCharsetUtf8.offset);

        assertEquals(utf16Charset, bomCharsetUtf16.charset);
        assertEquals(0, bomCharsetUtf16.offset);

        assertEquals(utf32Charset, bomCharsetUtf32.charset);
        assertEquals(0, bomCharsetUtf32.offset);
    }
}

class FileUtils {
    public static void writeStringToFile(File file, String text, Charset charset) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(text.getBytes(charset));
        }
    }

    public static void deleteQuietly(File file) {
        if (file.exists() && file.isFile())
            file.delete();
    }
}