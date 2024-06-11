package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    private DataUtil dataUtil;
    private Parser parser;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        parser = mock(Parser.class);
        dataUtil = new DataUtil();
    }

    @After
    public void tearDown() {
        parser = null;
        dataUtil = null;
    }

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<html><head></head><body></body></html>";
        File inputFile = new File("test.html");
        OutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        // Given
        when(parser.parseInput(any(InputStreamReader.class), any(String.class))).thenReturn(new Document(""));

        // When
        Document doc = dataUtil.load(inputFile, "UTF-8", "");

        // Then
        assertNotNull(doc);
        verify(parser).parseInput(any(InputStreamReader.class), any(String.class));
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String fileContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        // Given
        when(parser.parseInput(any(InputStream.class), any(String.class))).thenReturn(new Document(""));

        // When
        Document doc = dataUtil.load(inputStream, "UTF-8", "");

        // Then
        assertNotNull(doc);
        verify(parser).parseInput(any(InputStream.class), any(String.class));
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String fileContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        // Given
        when(parser.parseInput(any(InputStream.class), any(String.class))).thenReturn(new Document(""));

        // When
        Document doc = dataUtil.load(inputStream, "UTF-8", "", parser);

        // Then
        assertNotNull(doc);
        verify(parser).parseInput(any(InputStream.class), any(String.class));
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullInputStream() throws IOException {
        // Given
        Document doc = null;

        // When & Then
        dataUtil.load(null, "UTF-8", "");
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithEmptyInputStream() throws IOException {
        File inputFile = new File("test.html");

        // Given
        when(parser.parseInput(any(InputStreamReader.class), any(String.class))).thenReturn(new Document(""));

        // When & Then
        assertThrows(IOException.class, () -> dataUtil.load(inputFile, "", ""));
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test input".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Given

        // When
        dataUtil.crossStreams(inputStream, outputStream);

        // Then
        assertEquals("Test input", outputStream.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void testParseInputStream() throws IOException {
        String inputContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "";
        Document expectedDocument = new Document("");

        // Given
        when(parser.parseInput(any(InputStream.class), eq(baseUri))).thenReturn(expectedDocument);

        // When
        Document doc = dataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);

        // Then
        assertEquals(expectedDocument, doc);
        verify(parser).parseInput(inputStream, baseUri);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test input".getBytes(StandardCharsets.UTF_8));
        int maxSize = 10;

        // Given

        // When
        ByteBuffer byteBuffer = dataUtil.readToByteBuffer(inputStream, maxSize);

        // Then
        assertNotNull(byteBuffer);
        byte[] expectedContent = "Test input".getBytes(StandardCharsets.UTF_8);
        byte[] bufferContent = new byte[byteBuffer.limit()];
        byteBuffer.get(bufferContent);
        assertArrayEquals(expectedContent, bufferContent);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        // Given

        // When
        String charset = dataUtil.getCharsetFromContentType(contentType);

        // Then
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testValidateCharset() {
        String validCharset = "UTF-8";
        String invalidCharset = "invalidCharset";

        // Given

        // When
        String validatedCharset = dataUtil.validateCharset(validCharset);
        String invalidValidatedCharset = dataUtil.validateCharset(invalidCharset);

        // Then
        assertEquals(validCharset, validatedCharset);
        assertNull(invalidValidatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        // Given

        // When
        String boundary = dataUtil.mimeBoundary();

        // Then
        assertNotNull(boundary);
        assertFalse(boundary.isEmpty());
    }
}