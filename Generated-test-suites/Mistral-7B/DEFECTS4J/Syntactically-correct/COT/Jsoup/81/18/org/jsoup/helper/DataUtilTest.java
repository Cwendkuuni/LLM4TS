package org.jsoup.helper; // make sure the package name matches the class

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.helper.DataUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class DataUtilTest {
    

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        // Given
        File inputFile = new File("test-file.html");

        // When
        Document document = DataUtil.load(inputFile, null);

        // Then
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        // Given
        File inputFile = new File("test-file.html");
        String charsetName = "UTF-8";

        // When
        Document document = DataUtil.load(inputFile, charsetName);

        // Then
        assertNotNull(document);
        assertEquals(charsetName, document.outputSettings().charset());
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidInput() throws IOException {
        // Given
        File inputFile = new File("nonexistent-file.html");

        // When & Then
        DataUtil.load(inputFile, null);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        // Given
        File inputFile = new File("test-file.html");
        InputStream inputStream = new FileInputStream(inputFile);

        // When
        Document document = DataUtil.load(inputStream, null);

        // Then
        assertNotNull(document);
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        // Given
        File inputFile = new File("test-file.html");
        InputStream inputStream = new FileInputStream(inputFile);
        String charsetName = "UTF-8";

        // When
        Document document = DataUtil.load(inputStream, charsetName);

        // Then
        assertNotNull(document);
        assertEquals(charsetName, document.outputSettings().charset());
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullInput() throws IOException {
        // Given & When
        Document document = DataUtil.load(null, null);

        // Then
        fail("Should throw IOException for null input stream.");
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        // Given
        File inputFile = new File("test-file.html");
        InputStream inputStream = new FileInputStream(inputFile);
        String charsetName = "invalidCharset";

        // When
        try {
            Document document = DataUtil.load(inputStream, charsetName);
            fail("Should throw UncheckedIOException for invalid charset.");
        } catch (UncheckedIOException e) {
            // When & Then
            assertEquals("Invalid charset: invalidCharset", e.getMessage());
            inputStream.close();
        }
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        // Given
        String testContent = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testContent.getBytes());

        // When
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());

        // Then
        assertNotNull(document);
        assertEquals(testContent, document.html());
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        // Given
        String testContent = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testContent.getBytes());
        String charsetName = "UTF-8";

        // When
        Document document = DataUtil.parseInputStream(inputStream, charsetName, null, Parser.htmlParser());

        // Then
        assertNotNull(document);
        assertEquals(testContent, document.html());
        assertEquals(charsetName, document.outputSettings().charset());
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithNullInput() throws IOException {
        // Given & When
        Document document = DataUtil.parseInputStream(null, null, null, Parser.htmlParser());

        // Then
        fail("Should throw IOException for null input stream.");
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        // Given
        String testContent = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testContent.getBytes());
        String charsetName = "invalidCharset";

        // When
        try {
            Document document = DataUtil.parseInputStream(inputStream, charsetName, null, Parser.htmlParser());
            fail("Should throw UncheckedIOException for invalid charset.");
        } catch (UncheckedIOException e) {
            // When & Then
            assertEquals("Invalid charset: invalidCharset", e.getMessage());
            inputStream.close();
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Given
        InputStream inputStream = new FileInputStream(new File("test-file.html"));
        OutputStream outputStream = new FileOutputStream(new File("output-file.html"));

        // When
        DataUtil.crossStreams(inputStream, outputStream);

        // Then
        try (InputStream readStream = new FileInputStream(new File("output-file.html"));
             ByteArrayOutputStream expectedContent = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[bufferSize];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                expectedContent.write(buffer, 0, len);
            }
            String expected = new String(expectedContent.toByteArray(), Charset.forName("UTF-8"));
            String actual = new String(readStream.readAllBytes(), Charset.forName("UTF-8"));
            assertEquals(expected, actual);
        }
        outputStream.close();
        inputStream.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Given
        File inputFile = new File("test-file.html");
        InputStream inputStream = new FileInputStream(inputFile);

        // When
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);

        // Then
        try (InputStream readStream = new FileInputStream(inputFile)) {
            byte[] expected = new byte[inputFile.length()];
            readStream.read(expected);
            byte[] actual = byteBuffer.array();
            assertArrayEquals(expected, actual);
        }
        inputStream.close();
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        // Given
        File inputFile = new File("test-file.html");

        // When
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(inputFile);

        // Then
        try (InputStream inputStream = new FileInputStream(inputFile)) {
            byte[] expected = new byte[inputFile.length()];
            inputStream.read(expected);
            byte[] actual = byteBuffer.array();
            assertArrayEquals(expected, actual);
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        // Given
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        // Then
        assertEquals(0, byteBuffer.limit());
        assertEquals(0, byteBuffer.position());
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Given
        String contentType = "text/html; charset=UTF-8";

        // When
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Then
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testValidateCharset() {
        // Given
        String charset = "UTF-8";

        // When
        String validatedCharset = DataUtil.validateCharset(charset);

        // Then
        assertEquals(charset, validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        // Given

        // When
        String boundary = DataUtil.mimeBoundary();

        // Then
        assertTrue(boundary.length() == boundaryLength);
    }

    
}