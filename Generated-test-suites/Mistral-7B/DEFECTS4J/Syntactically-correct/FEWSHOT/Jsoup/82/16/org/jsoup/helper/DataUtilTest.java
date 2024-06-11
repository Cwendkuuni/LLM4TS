package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        String fileContent = "<html lang='en'><head><meta charset='UTF-8'></head><body><h1>Hello</h1></body></html>";
        File tempFile = File.createTempFile("testDataUtilLoadDocumentFromFile", null);
        writeFileContent(tempFile, fileContent);

        Document document = DataUtil.load(tempFile, null, "http://test.com");
        assertEquals("http://test.com", document.baseUri());
        assertEquals(fileContent, document.html());
        tempFile.delete();
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String fileContent = "<html lang='en'><head><meta charset='UTF-8'></head><body><h1>Hello</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "UTF-8", "http://test.com");
        assertEquals("http://test.com", document.baseUri());
        assertEquals(fileContent, document.html());
    }

    @Test
    public void testParseInputStreamWithCharset() throws IOException {
        String fileContent = "<html lang='en'><head><meta charset='UTF-8'></head><body><h1>Hello</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "http://test.com", Parser.htmlParser());
        assertEquals("http://test.com", document.baseUri());
        assertEquals(fileContent, document.html());
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String fileContent = "<html lang='en'><head><meta charset='ISO-8859-1'></head><body><h1>Hello</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, null, "http://test.com", Parser.htmlParser());
        assertEquals("http://test.com", document.baseUri());
        assertEquals(fileContent, document.html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String sourceContent = "Source";
        String targetContent = "Target";
        byte[] buffer = new byte[512];

        InputStream inputStreamSource = new ByteArrayInputStream(sourceContent.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStreamTarget = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStreamSource, outputStreamTarget);
        assertEquals(sourceContent, outputStreamTarget.toString(StandardCharsets.UTF_8.name()));

        inputStreamSource.close();
        outputStreamTarget.close();
    }

    @Test
    public void testLoadDocumentWithInvalidCharset() throws IOException, UncheckedIOException {
        String fileContent = "<html lang='en'><head><meta charset='x-invalid'></head><body><h1>Hello</h1></body></html>";
        File tempFile = File.createTempFile("testDataUtilLoadDocumentWithInvalidCharset", null);
        writeFileContent(tempFile, fileContent);

        assertThrows(UncheckedIOException.class, () -> DataUtil.load(tempFile, "x-invalid", "http://test.com"));
        tempFile.delete();
    }

    @Test
    public void testDetectCharsetFromBom() {
        String bomUTF8 = "\uFEFF";
        String bomUTF16BE = "\uFFFE\u0000";
        String bomUTF16LE = "\u0000\uFFFE";
        String bomUTF32BE = "\u0000\u0000\u0000\u0000\uFEFF";
        String bomUTF32LE = "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\uFFFE";

        ByteBuffer byteDataUTF8 = ByteBuffer.wrap(bomUTF8.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteDataUTF16BE = ByteBuffer.wrap(bomUTF16BE.getBytes(StandardCharsets.UTF_16BE));
        ByteBuffer byteDataUTF16LE = ByteBuffer.wrap(bomUTF16LE.getBytes(StandardCharsets.UTF_16LE));
        ByteBuffer byteDataUTF32BE = ByteBuffer.wrap(bomUTF32BE.getBytes(StandardCharsets.UTF_32BE));
        ByteBuffer byteDataUTF32LE = ByteBuffer.wrap(bomUTF32LE.getBytes(StandardCharsets.UTF_32LE));

        assertEquals("UTF-8", DataUtil.detectCharsetFromBom(byteDataUTF8).charset);
        assertEquals(false, DataUtil.detectCharsetFromBom(byteDataUTF8).offset);

        assertEquals("UTF-16", DataUtil.detectCharsetFromBom(byteDataUTF16BE).charset);
        assertEquals(false, DataUtil.detectCharsetFromBom(byteDataUTF16BE).offset);

        assertEquals("UTF-16", DataUtil.detectCharsetFromBom(byteDataUTF16LE).charset);
        assertEquals(true, DataUtil.detectCharsetFromBom(byteDataUTF16LE).offset);

        assertNull(DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[0])));
        assertNull(DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[1])));
        assertNull(DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[2])));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentTypeTextPlain = "text/plain";
        String contentTypeHtml = "text/html; charset=UTF-8";
        String contentTypeXml = "application/xml; charset=UTF-16";

        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentTypeHtml));
        assertEquals("UTF-16", DataUtil.getCharsetFromContentType(contentTypeXml));
        assertEquals(null, DataUtil.getCharsetFromContentType(contentTypeTextPlain));
    }

    @Test
    public void testValidateCharset() {
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertEquals("UTF-16", DataUtil.validateCharset("UTF-16"));
        assertEquals("UTF-32", DataUtil.validateCharset("UTF-32"));
        assertEquals("ISO-8859-1", DataUtil.validateCharset("ISO-8859-1"));
        assertEquals("UTF-8", DataUtil.validateCharset("utf-8"));
        assertEquals("UTF-16", DataUtil.validateCharset("utf-16"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8 ".trim()));
        assertEquals("UTF-16", DataUtil.validateCharset("UTF-16 ".trim()));
        assertEquals(null, DataUtil.validateCharset("x-invalid"));
        assertEquals(null, DataUtil.validateCharset("UTF-16 BE "));
    }

    @Test
    public void testMimeBoundary() {
        String expectedMimeBoundary = "Random_String_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        assertEquals(expectedMimeBoundary, DataUtil.mimeBoundary());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String fileContent = "Test Content";
        File inputFile = File.createTempFile("testDataUtilReadToByteBuffer", null);
        writeFileContent(inputFile, fileContent);

        InputStream inputStream = new FileInputStream(inputFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 0);
        inputStream.close();
        inputFile.delete();

        assertEquals(fileContent.getBytes(StandardCharsets.UTF_8).length, byteData.limit());
        byteData.clear();
        byteData.get(new byte[fileContent.getBytes(StandardCharsets.UTF_8).length]);
        assertArrayEquals(fileContent.getBytes(StandardCharsets.UTF_8), byteData.array());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer expectedByteBuffer = ByteBuffer.allocate(0);
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        assertEquals(expectedByteBuffer, byteBuffer);
    }

    private void assertThrows(Class<?> expectedExceptionClass, Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            if (expectedExceptionClass.isInstance(throwable)) {
                return;
            }
            throw new AssertionError("Expected exception of type " + expectedExceptionClass.getName() + " but got " + throwable.getClass().getName() + ": " + throwable.getMessage());
        }

        throw new AssertionError("Expected exception but none thrown");
    }

    private void writeFileContent(File file, String content) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(file)) {
            byte[] fileContentBytes = content.getBytes(StandardCharsets.UTF_8);
            outputStream.write(fileContentBytes);
            outputStream.flush();
        }
    }
}