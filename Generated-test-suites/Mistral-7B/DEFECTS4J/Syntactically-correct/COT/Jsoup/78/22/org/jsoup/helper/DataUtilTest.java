package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>";
        File testFile = new File("testFile.html");
        Mockito.when(testFile.exists()).thenReturn(true);
        Mockito.when(testFile.length()).thenReturn(fileContent.getBytes().length);
        Mockito.when(testFile.getAbsolutePath()).thenReturn("/path/to/testFile.html");

        Document document = DataUtil.load(testFile, "UTF-8", null);
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>", document.html());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testInputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testInputStreamContent.getBytes());

        Document document = DataUtil.load(testInputStream, "UTF-8", null);
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>", document.html());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String testInputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testInputStreamContent.getBytes());

        assertThrows(IOException.class, () -> DataUtil.load(testInputStream, "ISO-8859-1", null));
    }

    @Test
    public void testLoadInputStreamWithEmptyContent() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(new byte[0]);

        Document document = DataUtil.load(testInputStream, null, null);
        assertEquals("<html></html>", document.html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "Test Input";
        String outputContent = "Test Output";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputContent.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(inputContent, outputStream.toString());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String testInputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testInputStreamContent.getBytes());

        Document document = DataUtil.parseInputStream(testInputStream, "UTF-8", null, Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>", document.html());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String testInputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testInputStreamContent.getBytes());

        assertThrows(IOException.class, () -> DataUtil.parseInputStream(testInputStream, "ISO-8859-1", null, Parser.htmlParser()));
    }

    @Test
    public void testParseInputStreamWithEmptyContent() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(new byte[0]);

        Document document = DataUtil.parseInputStream(testInputStream, null, null, Parser.htmlParser());
        assertEquals("<html></html>", document.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testInputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testInputStreamContent.getBytes());

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream);
        assertEquals(testInputStreamContent.getBytes().length, byteBuffer.limit());
    }

    @Test
    public void testReadToByteBufferWithInvalidInputStream() {
        InputStream testInputStream = null;

        assertThrows(NullPointerException.class, () -> DataUtil.readToByteBuffer(testInputStream));
    }

    @Test
    public void testReadToByteBufferWithMaxSizeGreaterThanInputStreamLength() throws IOException {
        String testInputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testInputStreamContent.getBytes());

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream, 1024 * 64);
        assertEquals(testInputStreamContent.getBytes().length, byteBuffer.limit());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>";
        File testFile = new File("testFile.html");
        Mockito.when(testFile.exists()).thenReturn(true);
        Mockito.when(testFile.length()).thenReturn(fileContent.getBytes().length);
        Mockito.when(testFile.getAbsolutePath()).thenReturn("/path/to/testFile.html");

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
        assertEquals(fileContent.getBytes().length, byteBuffer.limit());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String testContentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(testContentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String testContentType = "text/html; charset=UTF-16BE";

        assertNull(DataUtil.getCharsetFromContentType(testContentType));
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharsetSpecified() {
        String testContentType = "text/html";

        assertNull(DataUtil.getCharsetFromContentType(testContentType));
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";

        assertEquals(charset, DataUtil.validateCharset(charset));
    }

    @Test
    public void testValidateCharsetWithInvalidCharset() {
        String charset = "invalidCharset";

        assertNull(DataUtil.validateCharset(charset));
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(new File("src/test/resources/UTF-8.html"));

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(0, bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomWithInvalidBom() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(new File("src/test/resources/UTF-16LE.html"));

        assertNull(DataUtil.detectCharsetFromBom(byteBuffer));
    }

    @Test
    public void testDetectCharsetFromBomWithEmptyContent() throws IOException {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        assertNull(DataUtil.detectCharsetFromBom(byteBuffer));
    }
}