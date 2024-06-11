package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.parser.Parser.HtmlParser;
import org.jsoup.parser.Parser.XmlParser;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.junit.Test;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    private static final String DEFAULT_CHARSET = "UTF-8";

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        // Given
        String fileContent = "This is a test";
        File testFile = new File("testFile.html");
        MockitoAnnotations.initMocks(this);
        InputStream inputStreamMock = mock(InputStream.class);
        when(inputStreamMock.readAllBytes()).thenReturn(fileContent.getBytes());
        when(testFile.length()).thenReturn(fileContent.length());
        when(testFile.exists()).thenReturn(true);
        when(testFile.isFile()).thenReturn(true);
        when(testFile.getAbsolutePath()).thenReturn("testFile.html");
        when(testFile.getAbsoluteFile()).thenReturn(testFile);
        when(testFile.getParentFile()).thenReturn(new File("testDirectory"));
        when(testFile.getParent()).thenReturn("testDirectory");

        Document document = DataUtil.load(testFile, DEFAULT_CHARSET, null);

        // Then
        assertSame("Expected a Document instance", Document.class, document.getClass());
        assertNull("Expected the baseUri to be null", document.baseURI());
        assertSame("Expected the content of the testFile", fileContent, document.body().html());
    }

    @Test
    public void testLoadFileWithProvidedCharset() throws IOException {
        // Given
        String fileContent = "This is a test";
        String charset = "UTF-16";
        File testFile = new File("testFile.html");
        MockitoAnnotations.initMocks(this);
        InputStream inputStreamMock = mock(InputStream.class);
        when(inputStreamMock.readAllBytes()).thenReturn(fileContent.getBytes());
        when(testFile.length()).thenReturn(fileContent.length());
        when(testFile.exists()).thenReturn(true);
        when(testFile.isFile()).thenReturn(true);
        when(testFile.getAbsolutePath()).thenReturn("testFile.html");
        when(testFile.getAbsoluteFile()).thenReturn(testFile);
        when(testFile.getParentFile()).thenReturn(new File("testDirectory"));
        when(testFile.getParent()).thenReturn("testDirectory");

        Document document = DataUtil.load(testFile, charset, null);

        // Then
        assertSame("Expected a Document instance", Document.class, document.getClass());
        assertNull("Expected the baseUri to be null", document.baseURI());
        assertSame("Expected the content of the testFile", fileContent, document.body().html());
        assertSame("Expected the provided charset", charset, document.outputSettings().charset().name());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        // Given
        String inputStreamContent = "This is a test";
        byte[] inputStreamContentBytes = inputStreamContent.getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputStreamContentBytes);
        Document document = null;

        // When
        document = DataUtil.load(inputStream, DEFAULT_CHARSET, null);

        // Then
        assertSame("Expected a Document instance", Document.class, document.getClass());
        assertNull("Expected the baseUri to be null", document.baseURI());
        assertSame("Expected the content of the input stream", inputStreamContent, document.body().html());
    }

    @Test
    public void testLoadInputStreamWithProvidedCharset() throws IOException {
        // Given
        String inputStreamContent = "This is a test";
        String charset = "UTF-16";
        byte[] inputStreamContentBytes = inputStreamContent.getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputStreamContentBytes);
        Document document = null;

        // When
        document = DataUtil.load(inputStream, charset, null);

        // Then
        assertSame("Expected a Document instance", Document.class, document.getClass());
        assertNull("Expected the baseUri to be null", document.baseURI());
        assertSame("Expected the content of the input stream", inputStreamContent, document.body().html());
        assertSame("Expected the provided charset", charset, document.outputSettings().charset().name());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        // Given
        String inputStreamContent = "<html><head><title>Test</title></head><body>This is a test</body></html>";
        byte[] inputStreamContentBytes = inputStreamContent.getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputStreamContentBytes);
        Parser parser = mock(Parser.class);
        Document document = null;

        // When
        document = DataUtil.load(inputStream, DEFAULT_CHARSET, null, parser);

        // Then
        assertSame("Expected a Document instance", Document.class, document.getClass());
        assertNull("Expected the baseUri to be null", document.baseURI());
        assertSame("Expected the content of the input stream as parsed HTML", inputStreamContent, document.html());
        assertSame("Expected the provided parser", parser, document.parser());
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Given
        String testData = "This is a test";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // When
        DataUtil.crossStreams(inputStream, outputStream);

        // Then
        byte[] result = outputStream.toByteArray();
        assertSame("Expected the size of the result to be the same as the testData", testData.length(), result.length);
        assertSame("Expected the result to be equal to the testData", testData, new String(result, DEFAULT_CHARSET));
    }
}