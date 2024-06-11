package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "testFile.html";
        File file = new File(filePath);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        String inputData = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";

        String inputData = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));

        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, charsetName, baseUri));
    }

    @Test
    public void testParseInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        String inputData = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "Input data";
        String outputData = "Output data";

        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        assertEquals(inputData, outputData);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";

        String inputData = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));

        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser()));
    }

    @Test
    public void testParseInputStreamWithEmptyInput() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(0, document.childNodeSize());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String filePath = "testFile.html";
        File file = new File(filePath);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new FileInputStream(file), 0);

        assertNotNull(byteBuffer);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String filePath = "testFile.html";
        File file = new File(filePath);
        long fileSize = file.length();
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new FileInputStream(file), (int) fileSize);

        assertNotNull(byteBuffer);
        assertEquals(fileSize, byteBuffer.limit());
    }

    @Test
    public void testReadToByteBufferWithEmptyInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);

        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testReadToByteBufferWithInvalidMaxSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        assertThrows(NegativeArraySizeException.class, () -> DataUtil.readToByteBuffer(inputStream, -1));
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String filePath = "testFile.html";
        File file = new File(filePath);
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        assertNotNull(byteBuffer);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);

        assertEquals("UTF-8", validatedCharset);
    }

    @Test
    public void testValidateCharsetWithInvalidCharset() {
        String charset = "InvalidCharset";
        String validatedCharset = DataUtil.validateCharset(charset);

        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        String inputData = "\uFEFF<html><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(inputData.getBytes(StandardCharsets.UTF_8));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        assertNotNull(bomCharset);
        assertEquals("UTF-8", bomCharset.charset);
    }

    @Test
    public void testDetectCharsetFromBomWithNoBom() {
        String inputData = "<html><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(inputData.getBytes(StandardCharsets.UTF_8));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        assertNull(bomCharset);
    }

    @Test
    public void testDetectCharsetFromBomWithInvalidBom() {
        String inputData = "InvalidBOM<html><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(inputData.getBytes(StandardCharsets.UTF_8));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        assertNull(bomCharset);
    }
}