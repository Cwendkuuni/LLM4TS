package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {
    private Parser parser;

    @Before
    public void setUp() {
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoadFromFile() throws IOException {
        // Arrange
        File file = mock(File.class);
        FileInputStream inStream = mock(FileInputStream.class);
        Document expectedOutput = mock(Document.class);

        when(file.exists()).thenReturn(true);
        when(file.canRead()).thenReturn(true);
        whenNew(FileInputStream.class).withArguments(file).thenReturn(inStream);
        when(DataUtil.parseByteData(any(ByteBuffer.class), anyString(), anyString(), any(Parser.class))).thenReturn(expectedOutput);

        // Act
        Document actualOutput = DataUtil.load(file, "UTF-8", "http://example.com");

        // Assert
        ArgumentCaptor<ByteBuffer> byteDataCaptor = ArgumentCaptor.forClass(ByteBuffer.class);
        ArgumentCaptor<String> charsetNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> baseUriCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Parser> parserCaptor = ArgumentCaptor.forClass(Parser.class);

        verify(DataUtil.class, times(1)).parseByteData(byteDataCaptor.capture(), charsetNameCaptor.capture(), baseUriCaptor.capture(), parserCaptor.capture());
        assertEquals("http://example.com", baseUriCaptor.getValue());
        assertEquals("UTF-8", charsetNameCaptor.getValue());
        assertEquals(parser, parserCaptor.getValue());

        ByteBuffer byteData = byteDataCaptor.getValue();
        byte[] expectedBuffer = new byte[DataUtil.bufferSize];
        byteData.get(expectedBuffer);
        byte[] actualBuffer = new byte[DataUtil.bufferSize];
        ((ByteBuffer) byteData.rewind()).get(actualBuffer);
        assertArrayEquals(expectedBuffer, actualBuffer);

        assertEquals(expectedOutput, actualOutput);
        verify(inStream, times(1)).close();
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        // Arrange
        InputStream inStream = mock(InputStream.class);
        Document expectedOutput = mock(Document.class);

        when(DataUtil.parseByteData(any(ByteBuffer.class), anyString(), anyString(), any(Parser.class))).thenReturn(expectedOutput);

        // Act
        Document actualOutput = DataUtil.load(inStream, "UTF-8", "http://example.com");
        
        // Assert
        ArgumentCaptor<ByteBuffer> byteDataCaptor = ArgumentCaptor.forClass(ByteBuffer.class);
        ArgumentCaptor<String> charsetNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> baseUriCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Parser> parserCaptor = ArgumentCaptor.forClass(Parser.class);

        verify(DataUtil.class, times(1)).parseByteData(byteDataCaptor.capture(), charsetNameCaptor.capture(), baseUriCaptor.capture(), parserCaptor.capture());
        assertEquals("http://example.com", baseUriCaptor.getValue());
        assertEquals("UTF-8", charsetNameCaptor.getValue());
        assertEquals(parser, parserCaptor.getValue());

        ByteBuffer byteData = byteDataCaptor.getValue();
        byte[] expectedBuffer = new byte[DataUtil.bufferSize];
        byteData.get(expectedBuffer);
        byte[] actualBuffer = new byte[DataUtil.bufferSize];
        ((ByteBuffer) byteData.rewind()).get(actualBuffer);
        assertArrayEquals(expectedBuffer, actualBuffer);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        // Arrange
        InputStream inStream = mock(InputStream.class);
        Document expectedOutput = mock(Document.class);
        Parser customParser = mock(Parser.class);

        when(DataUtil.parseByteData(any(ByteBuffer.class), anyString(), anyString(), any(Parser.class))).thenReturn(expectedOutput);

        // Act
        Document actualOutput = DataUtil.load(inStream, StandardCharsets.ISO_8859_1.name(), "http://example.com", customParser);

        // Assert
        ArgumentCaptor<ByteBuffer> byteDataCaptor = ArgumentCaptor.forClass(ByteBuffer.class);
        ArgumentCaptor<String> charsetNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> baseUriCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Parser> parserCaptor = ArgumentCaptor.forClass(Parser.class);

        verify(DataUtil.class, times(1)).parseByteData(byteDataCaptor.capture(), charsetNameCaptor.capture(), baseUriCaptor.capture(), parserCaptor.capture());
        assertEquals("http://example.com", baseUriCaptor.getValue());
        assertEquals(StandardCharsets.ISO_8859_1.name(), charsetNameCaptor.getValue());
        assertEquals(customParser, parserCaptor.getValue());

        ByteBuffer byteData = byteDataCaptor.getValue();
        byte[] expectedBuffer = new byte[DataUtil.bufferSize];
        byteData.get(expectedBuffer);
        byte[] actualBuffer = new byte[DataUtil.bufferSize];
        ((ByteBuffer) byteData.rewind()).get(actualBuffer);
        assertArrayEquals(expectedBuffer, actualBuffer);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testParseByteDataWithNullCharset() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        String baseUri = "http://example.com";
        Document expectedOutput = mock(Document.class);

        when(DataUtil.parseByteData(byteData, null, baseUri, parser)).thenReturn(expectedOutput);

        // Act
        Document actualOutput = DataUtil.parseByteData(byteData, null, baseUri, parser);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testParseByteDataWithCharset() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document expectedOutput = mock(Document.class);

        when(DataUtil.parseByteData(byteData, charsetName, baseUri, parser)).thenReturn(expectedOutput);

        // Act
        Document actualOutput = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Arrange
        InputStream inStream = new ByteArrayInputStream("test".getBytes());
        byte[] expectedBuffer = new byte[DataUtil.bufferSize];
        inStream.read(expectedBuffer, 0, DataUtil.bufferSize);

        // Act
        ByteBuffer actualOutput = DataUtil.readToByteBuffer(inStream);

        // Assert
        byte[] actualBuffer = new byte[actualOutput.capacity()];
        actualOutput.get(actualBuffer);
        assertArrayEquals(expectedBuffer, actualBuffer);
    }

    @Test
    public void testGetCharsetFromContentTypeWithCharset() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";
        String expectedOutput = "UTF-8";

        // Act
        String actualOutput = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGetCharsetFromContentTypeWithoutCharset() {
        // Arrange
        String contentType = "text/html";
        String expectedOutput = null;

        // Act
        String actualOutput = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }
}