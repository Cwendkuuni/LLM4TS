package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        // Mock FileInputStream
        FileInputStream mockInStream = mock(FileInputStream.class);
        File mockFile = mock(File.class);
        when(mockFile.getPath()).thenReturn("test.html");
        when(mockFile.isFile()).thenReturn(true);
        when(mockFile.canRead()).thenReturn(true);
        when(mockFile.exists()).thenReturn(true);
        whenNew(FileInputStream.class).withAnyArguments().thenReturn(mockInStream);

        // Mock ByteBuffer
        ByteBuffer mockByteData = mock(ByteBuffer.class);
        when(DataUtil.readToByteBuffer(any(InputStream.class))).thenReturn(mockByteData);

        // Mock Parser and Document
        Parser mockParser = mock(Parser.class);
        Document mockDocument = mock(Document.class);
        when(DataUtil.parseByteData(mockByteData, "UTF-8", "http://example.com", mockParser)).thenReturn(mockDocument);

        // Test load with valid file
        Document result = DataUtil.load(mockFile, "UTF-8", "http://example.com");
        verify(mockInStream).close();
        verify(mockParser).parseInput(any(String.class), eq("http://example.com"));
        assertEquals(mockDocument, result);
    }

    @Test(expected = IOException.class)
    public void testLoadFileException() throws IOException {
        // Mock FileInputStream
        FileInputStream mockInStream = mock(FileInputStream.class);
        File mockFile = mock(File.class);
        when(mockFile.getPath()).thenReturn("test.html");
        when(mockFile.isFile()).thenReturn(true);
        when(mockFile.canRead()).thenReturn(true);
        when(mockFile.exists()).thenReturn(true);
        whenNew(FileInputStream.class).withAnyArguments().thenReturn(mockInStream);
        doThrow(new IOException("Mock IO Exception")).when(mockInStream).close();

        // Mock ByteBuffer
        ByteBuffer mockByteData = mock(ByteBuffer.class);
        when(DataUtil.readToByteBuffer(any(InputStream.class))).thenReturn(mockByteData);

        // Mock Parser and Document
        Parser mockParser = mock(Parser.class);
        Document mockDocument = mock(Document.class);
        when(DataUtil.parseByteData(mockByteData, "UTF-8", "http://example.com", mockParser)).thenReturn(mockDocument);

        try {
            // Test load with valid file, should throw IOException
            DataUtil.load(mockFile, "UTF-8", "http://example.com");
        } finally {
            verify(mockInStream).close();
            verify(mockParser, never()).parseInput(any(String.class), eq("http://example.com"));
        }
    }

    @Test
    public void testLoadInputStream() throws IOException {
        // Mock InputStream
        InputStream mockInputStream = mock(InputStream.class);

        // Mock ByteBuffer
        ByteBuffer mockByteData = mock(ByteBuffer.class);
        when(DataUtil.readToByteBuffer(mockInputStream)).thenReturn(mockByteData);

        // Mock Parser and Document
        Parser mockParser = mock(Parser.class);
        Document mockDocument = mock(Document.class);
        when(DataUtil.parseByteData(mockByteData, "UTF-8", "http://example.com", mockParser)).thenReturn(mockDocument);

        // Test load with valid InputStream
        Document result = DataUtil.load(mockInputStream, "UTF-8", "http://example.com");
        verify(mockInputStream).close();
        verify(mockParser).parseInput(any(String.class), eq("http://example.com"));
        assertEquals(mockDocument, result);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamException() throws IOException {
        // Mock InputStream
        InputStream mockInputStream = mock(InputStream.class);
        doThrow(new IOException("Mock IO Exception")).when(mockInputStream).close();

        // Mock ByteBuffer
        ByteBuffer mockByteData = mock(ByteBuffer.class);
        when(DataUtil.readToByteBuffer(mockInputStream)).thenReturn(mockByteData);

        // Mock Parser and Document
        Parser mockParser = mock(Parser.class);
        Document mockDocument = mock(Document.class);
        when(DataUtil.parseByteData(mockByteData, "UTF-8", "http://example.com", mockParser)).thenReturn(mockDocument);

        try {
            // Test load with valid InputStream, should throw IOException
            DataUtil.load(mockInputStream, "UTF-8", "http://example.com");
        } finally {
            verify(mockInputStream).close();
            verify(mockParser, never()).parseInput(any(String.class), eq("http://example.com"));
        }
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        // Mock InputStream
        InputStream mockInputStream = mock(InputStream.class);

        // Mock ByteBuffer
        ByteBuffer mockByteData = mock(ByteBuffer.class);
        when(DataUtil.readToByteBuffer(mockInputStream)).thenReturn(mockByteData);

        // Mock Parser and Document
        Parser mockParser = mock(Parser.class);
        Document mockDocument = mock(Document.class);
        when(DataUtil.parseByteData(mockByteData, "UTF-8", "http://example.com", mockParser)).thenReturn(mockDocument);

        // Test load with valid InputStream and Parser
        Document result = DataUtil.load(mockInputStream, "UTF-8", "http://example.com", mockParser);
        verify(mockInputStream).close();
        verify(mockParser).parseInput(any(String.class), eq("http://example.com"));
        assertEquals(mockDocument, result);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithParserException() throws IOException {
        // Mock InputStream
        InputStream mockInputStream = mock(InputStream.class);
        doThrow(new IOException("Mock IO Exception")).when(mockInputStream).close();

        // Mock ByteBuffer
        ByteBuffer mockByteData = mock(ByteBuffer.class);
        when(DataUtil.readToByteBuffer(mockInputStream)).thenReturn(mockByteData);

        // Mock Parser and Document
        Parser mockParser = mock(Parser.class);
        Document mockDocument = mock(Document.class);
        when(DataUtil.parseByteData(mockByteData, "UTF-8", "http://example.com", mockParser)).thenReturn(mockDocument);

        try {
            // Test load with valid InputStream and Parser, should throw IOException
            DataUtil.load(mockInputStream, "UTF-8", "http://example.com", mockParser);
        } finally {
            verify(mockInputStream).close();
            verify(mockParser, never()).parseInput(any(String.class), eq("http://example.com"));
        }
    }

    @Test
    public void testParseByteDataWithCharset() {
        // Mock ByteBuffer
        ByteBuffer mockByteData = mock(ByteBuffer.class);

        // Mock Parser and Document
        Parser mockParser = mock(Parser.class);
        Document mockDocument = mock(Document.class);
        when(DataUtil.parseByteData(mockByteData, "UTF-8", "http://example.com", mockParser)).thenReturn(mockDocument);

        // Test parseByteData with charset
        Document result = DataUtil.parseByteData(mockByteData, "UTF-8", "http://example.com", mockParser);
        assertEquals(mockDocument, result);
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        // Mock ByteBuffer
        ByteBuffer mockByteData = mock(ByteBuffer.class);

        // Mock Parser and Document
        Parser mockParser = mock(Parser.class);
        Document mockDocument = mock(Document.class);
        when(DataUtil.parseByteData(mockByteData, null, "http://example.com", mockParser)).thenReturn(mockDocument);

        // Test parseByteData without charset
        Document result = DataUtil.parseByteData(mockByteData, null, "http://example.com", mockParser);
        assertEquals(mockDocument, result);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Mock InputStream
        InputStream mockInputStream = mock(InputStream.class);

        // Mock byte buffer
        ByteBuffer mockByteData = mock(ByteBuffer.class);
        when(mockByteData.array()).thenReturn(new byte[]{});

        // Test readToByteBuffer
        ByteBuffer result = DataUtil.readToByteBuffer(mockInputStream);
        verify(mockInputStream, never()).close();
        assertEquals(mockByteData, result);
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test getCharsetFromContentType with valid content type
        String result = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", result);

        // Test getCharsetFromContentType with invalid content type
        result = DataUtil.getCharsetFromContentType("text/html");
        assertNull(result);
    }
}