package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import java.io.*;
import java.nio.ByteBuffer;

public class DataUtilTest {

    @Mock
    private File fileMock;
    @Mock
    private InputStream inputStreamMock;
    @Mock
    private ByteBuffer byteBufferMock;
    @Mock
    private Parser parserMock;
    @Mock
    private Document documentMock;

    @BeforeClass
    public static void setUp() {
        // Set up mocks if needed
    }

    @Test
    public void testLoadFile() throws IOException {
        when(fileMock.exists()).thenReturn(true);
        when(fileMock.canRead()).thenReturn(true);
        when(fileMock.isFile()).thenReturn(true);
        when(fileMock.length()).thenReturn(10L);

        when(parserMock.htmlParser()).thenReturn(parserMock);
        when(parserMock.parseInput(anyString(), any())).thenReturn(documentMock);
        when(documentMock.outputSettings()).thenReturn(documentMock);

        when(byteBufferMock.rewind()).thenReturn(byteBufferMock);

        assertEquals(documentMock, DataUtil.load(fileMock, "UTF-8", "http://www.example.com"));
        verify(fileMock, times(1)).exists();
        verify(fileMock, times(1)).canRead();
        verify(fileMock, times(1)).isFile();
        verify(fileMock, times(1)).length();
        verify(parserMock, times(1)).htmlParser();
        verify(parserMock, times(1)).parseInput(anyString(), any());
        verify(documentMock, times(1)).outputSettings();
        verify(byteBufferMock, times(1)).rewind();
    }

    @Test(expected = IOException.class)
    public void testLoadFileThrowsIOException() throws IOException {
        when(fileMock.exists()).thenReturn(true);
        when(fileMock.canRead()).thenReturn(false);

        DataUtil.load(fileMock, "UTF-8", "http://www.example.com");
    }

    @Test
    public void testLoadInputStream() throws IOException {
        when(parserMock.htmlParser()).thenReturn(parserMock);
        when(parserMock.parseInput(anyString(), any())).thenReturn(documentMock);

        assertEquals(documentMock, DataUtil.load(inputStreamMock, "UTF-8", "http://www.example.com"));
        verify(parserMock, times(1)).htmlParser();
        verify(parserMock, times(1)).parseInput(anyString(), any());
        verify(documentMock, times(1)).outputSettings();
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamThrowsIOException() throws IOException {
        when(inputStreamMock.read(any())).thenThrow(IOException.class);

        DataUtil.load(inputStreamMock, "UTF-8", "http://www.example.com");
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        when(parserMock.parseInput(anyString(), any())).thenReturn(documentMock);

        assertEquals(documentMock, DataUtil.load(inputStreamMock, "UTF-8", "http://www.example.com", parserMock));
        verify(parserMock, times(1)).parseInput(anyString(), any());
        verify(documentMock, times(1)).outputSettings();
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithParserThrowsIOException() throws IOException {
        when(inputStreamMock.read(any())).thenThrow(IOException.class);

        DataUtil.load(inputStreamMock, "UTF-8", "http://www.example.com", parserMock);
    }

    @Test
    public void testParseByteData() {
        when(parserMock.parseInput(anyString(), any())).thenReturn(documentMock);
        when(documentMock.outputSettings()).thenReturn(documentMock);

        assertEquals(documentMock, DataUtil.parseByteData(byteBufferMock, "UTF-8", "http://www.example.com", parserMock));
        verify(parserMock, times(1)).parseInput(anyString(), any());
        verify(documentMock, times(1)).outputSettings();
    }

    @Test
    public void testParseByteDataWithNullCharset() {
        when(parserMock.parseInput(anyString(), any())).thenReturn(documentMock);
        when(documentMock.outputSettings()).thenReturn(documentMock);

        assertEquals(documentMock, DataUtil.parseByteData(byteBufferMock, null, "http://www.example.com", parserMock));
        verify(parserMock, times(1)).parseInput(anyString(), any());
        verify(documentMock, times(1)).outputSettings();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] buffer = new byte[DataUtil.bufferSize];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(DataUtil.bufferSize);

        when(inputStreamMock.read(buffer)).thenReturn(10);
        when(byteBufferMock.wrap(any())).thenReturn(byteBufferMock);

        assertEquals(byteBufferMock, DataUtil.readToByteBuffer(inputStreamMock));
        verify(inputStreamMock, times(1)).read(buffer);
        verify(byteBufferMock, times(1)).wrap(any());
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferThrowsIOException() throws IOException {
        when(inputStreamMock.read(any())).thenThrow(IOException.class);

        DataUtil.readToByteBuffer(inputStreamMock);
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("EUC-JP", DataUtil.getCharsetFromContentType("text/html; charset=EUC-JP"));
        assertNull(DataUtil.getCharsetFromContentType("text/plain"));
    }
}