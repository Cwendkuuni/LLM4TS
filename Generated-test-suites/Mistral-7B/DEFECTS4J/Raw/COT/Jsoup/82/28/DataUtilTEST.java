package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import static org.jsoup.helper.DataUtil.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        File testFile = new File("testFile.html");
        FileUtils.writeStringToFile(testFile, fileContent, StandardCharsets.UTF_8);

        Document document = DataUtil.load(testFile, "UTF-8", "");
        assertNotNull(document);
        assertTrue(document.body().text().equals(fileContent));

        FileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        byte[] contentBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(contentBytes);

        Document document = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(document);
        assertTrue(document.body().text().equals(inputStreamContent));
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "<html><body>This is a test input stream.</body></html>";
        byte[] contentBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(contentBytes);

        Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());
        assertNotNull(document);
        assertTrue(document.body().text().equals(inputStreamContent));
    }

    @Test(expected = IOException.class)
    public void testLoadNullInputStream() throws IOException {
        Document document = DataUtil.load(null, "UTF-8", "");
    }

    @Test(expected = IOException.class)
    public void testLoadEmptyInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document document = DataUtil.load(inputStream, "UTF-8", "");
    }

    @Test
    public void testCrossStreams() throws IOException {
        String fileContent = "This is a test file.";
        File testFile = new File("testFile.html");
        FileUtils.writeStringToFile(testFile, fileContent, StandardCharsets.UTF_8);

        FileInputStream inputFile = new FileInputStream(testFile);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputFile, outputStream);
        inputFile.close();

        String outputContent = outputStream.toString(StandardCharsets.UTF_8.name());
        assertTrue(outputContent.equals(fileContent));

        FileUtils.deleteQuietly(testFile);
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithNullInputStream() throws IOException {
        InputStream nullInputStream = null;
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(nullInputStream, outputStream);
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithNullOutputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes(StandardCharsets.UTF_8));
        OutputStream nullOutputStream = null;

        DataUtil.crossStreams(inputStream, nullOutputStream);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String inputStreamContent = "<html><body>This is a test input stream.</body></html>";
        byte[] contentBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(contentBytes);

        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
        assertNotNull(document);
        assertTrue(document.body().text().equals(inputStreamContent));
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithNullInputStream() throws IOException {
        Document document = DataUtil.parseInputStream(null, "UTF-8", "", Parser.htmlParser());
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithEmptyInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
    }

    @Test
    public void testParseInputStreamWithXmlParser() throws IOException {
        String inputStreamContent = "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><body>This is a test input stream.</body></html>";
        byte[] contentBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(contentBytes);

        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.xmlParser());
        assertNotNull(document);
        assertTrue(document.body().text().equals(inputStreamContent));
    }

    @Test(expected = UncheckedIOException.class)
    public void testParseInputStreamWithIOException() throws IOException, UncheckedIOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
        } catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == 32);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputContent = "This is a test string to read from the input stream.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 100);
        assertTrue(byteBuffer.capacity() >= inputContent.length());
        assertTrue(byteBuffer.position() == 0);

        byte[] expectedBytes = inputContent.getBytes(StandardCharsets.UTF_8);
        byteBuffer.position(0);
        byte[] actualBytes = new byte[inputContent.length()];
        byteBuffer.get(actualBytes, 0, inputContent.length());

        assertTrue(new String(actualBytes, StandardCharsets.UTF_8).equals(inputContent));
    }

    @Test
    public void testReadToByteBufferWithEmptyInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 100);

        assertTrue(byteBuffer.capacity() == 0);
        assertTrue(byteBuffer.position() == 0);
    }

    @Test
    public void testReadToByteBufferWithLargeSize() throws IOException {
        String inputContent = "This is a test string to read from the input stream.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, Integer.MAX_VALUE);
        assertTrue(byteBuffer.capacity() >= inputContent.length());
        assertTrue(byteBuffer.position() == 0);

        byte[] expectedBytes = inputContent.getBytes(StandardCharsets.UTF_8);
        byteBuffer.position(0);
        byte[] actualBytes = new byte[inputContent.length()];
        byteBuffer.get(actualBytes, 0, inputContent.length());

        assertTrue(new String(actualBytes, StandardCharsets.UTF_8).equals(inputContent));
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertTrue(byteBuffer.capacity() == 0);
        assertTrue(byteBuffer.position() == 0);
    }

    @Test(expected = IOException.class)
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=invalid_charset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
    }

    @Test
    public void testGetCharsetFromContentTypeWithValidCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertTrue(charset.equalsIgnoreCase("UTF-8"));
    }

    @Test
    public void testValidateCharsetWithValidCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertTrue(validatedCharset.equalsIgnoreCase("UTF-8"));
    }

    @Test
    public void testValidateCharsetWithInvalidCharset() {
        String charset = "invalid_charset";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertTrue(validatedCharset == null);
    }

    @Test
    public void testDetectCharsetFromBomWithUTF8Bom() {
        ByteBuffer byteData = ByteBuffer.allocate(3);
        byteData.put((byte) 0xEF);
        byteData.put((byte) 0xBB);
        byteData.put((byte) 0xBF);
        byteData.flip();

        BomCharset charset = DataUtil.detectCharsetFromBom(byteData);
        assertTrue(charset != null);
        assertTrue(charset.charset.equalsIgnoreCase("UTF-8"));
    }

    @Test
    public void testDetectCharsetFromBomWithUTF16Bom() {
        ByteBuffer byteData = ByteBuffer.allocate(2);
        byteData.put((byte) 0xFF);
        byteData.put((byte) 0xFE);
        byteData.flip();

        BomCharset charset = DataUtil.detectCharsetFromBom(byteData);
        assertTrue(charset != null);
        assertTrue(charset.charset.equalsIgnoreCase("UTF-16"));
    }

    @Test
    public void testDetectCharsetFromBomWithUTF32Bom() {
        ByteBuffer byteData = ByteBuffer.allocate(4);
        byteData.put((byte) 0x00);
        byteData.put((byte) 0x00);
        byteData.put((byte) 0xFE);
        byteData.put((byte) 0xFF);
        byteData.flip();

        BomCharset charset = DataUtil.detectCharsetFromBom(byteData);
        assertTrue(charset != null);
        assertTrue(charset.charset.equalsIgnoreCase("UTF-32"));
    }

    @Test
    public void testDetectCharsetFromBomWithInvalidBom() {
        ByteBuffer byteData = ByteBuffer.allocate(1);
        byteData.put((byte) 0x01);
        byteData.flip();

        BomCharset charset = DataUtil.detectCharsetFromBom(byteData);
        assertTrue(charset == null);
    }
}