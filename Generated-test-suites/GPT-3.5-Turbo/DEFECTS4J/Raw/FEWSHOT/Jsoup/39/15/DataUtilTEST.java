package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {
    @Mock
    private File file;
    @Mock
    private FileInputStream inStream;
    @Mock
    private InputStream inputStream;
    @Mock
    private Parser parser;
    @Mock
    private Document document;

    private String baseUri;
    private String charsetName;
    private String contentType;
    private ByteBuffer byteBuffer;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        baseUri = "http://example.com";
        charsetName = "UTF-8";
        contentType = "text/html; charset=UTF-8";
        byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("Test Data".getBytes());
        byteBuffer.flip();
    }

    @Test
    public void testLoadWithFile() throws IOException {
        when(file.getName()).thenReturn("test.html");
        when(file.getAbsolutePath()).thenReturn("/path/to/test.html");
        when(file.exists()).thenReturn(true);
        when(file.canRead()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.length()).thenReturn(1024L);

        when(inStream.read()).thenReturn(-1); // End of stream

        Document result = DataUtil.load(file, charsetName, baseUri);
        assertNull(result);
    }

    @Test
    public void testLoadWithInputStream() throws IOException {
        when(inputStream.read()).thenReturn(-1); // End of stream

        Document result = DataUtil.load(inputStream, charsetName, baseUri);
        assertNull(result);
    }

    @Test
    public void testLoadWithInputStreamAndCustomParser() throws IOException {
        when(inputStream.read()).thenReturn(-1); // End of stream

        Document result = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNull(result);
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        when(parser.parseInput("Test Data", baseUri)).thenReturn(document);

        Document result = DataUtil.parseByteData(byteBuffer, null, baseUri, parser);
        assertEquals(document, result);
    }

    @Test
    public void testParseByteDataWithCharset() {
        when(parser.parseInput("Test Data", baseUri)).thenReturn(document);

        Document result = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);
        assertEquals(document, result);
    }

    @Test
    public void testReadToByteBufferUnlimited() throws IOException {
        ByteArrayOutputStream outStream = mock(ByteArrayOutputStream.class);
        byte[] buffer = "Test Data".getBytes();

        when(inputStream.read()).thenReturn(-1); // End of stream
        when(outStream.toByteArray()).thenReturn(buffer);

        ByteBuffer result = DataUtil.readToByteBuffer(inputStream, 0);
        assertEquals(ByteBuffer.wrap(buffer), result);
    }

    @Test
    public void testReadToByteBufferCapped() throws IOException {
        ByteArrayOutputStream outStream = mock(ByteArrayOutputStream.class);
        byte[] buffer = "Test Data".getBytes();

        when(inputStream.read()).thenReturn(-1); // End of stream
        when(outStream.toByteArray()).thenReturn(buffer);

        ByteBuffer result = DataUtil.readToByteBuffer(inputStream, 1024);
        assertEquals(ByteBuffer.wrap(buffer), result);
    }

    @Test
    public void testGetCharsetFromContentTypeWithCharset() {
        String result = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", result);
    }

    @Test
    public void testGetCharsetFromContentTypeWithoutCharset() {
        String result = DataUtil.getCharsetFromContentType("text/html");
        assertNull(result);
    }
}