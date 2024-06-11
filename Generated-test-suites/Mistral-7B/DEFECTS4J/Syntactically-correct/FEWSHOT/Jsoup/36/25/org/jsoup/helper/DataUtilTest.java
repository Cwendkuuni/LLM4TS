package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test cases for DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws Exception {
        File inputFile = new File("test.html");
        String baseUri = "http://example.com/";
        Document document = DataUtil.load(inputFile, null, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws Exception {
        File inputFile = new File("test.html");
        String charsetName = "UTF-16";
        String baseUri = "http://example.com/";
        Document document = DataUtil.load(inputFile, charsetName, baseUri);
        assertNotNull(document);
        assertEquals(charsetName, document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws Exception {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.available()).thenReturn(1024); // 1 KB for testing
        String baseUri = "http://example.com/";
        Document document = DataUtil.load(inputStream, null, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws Exception {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.available()).thenReturn(1024); // 1 KB for testing
        String charsetName = "UTF-16";
        String baseUri = "http://example.com/";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
        assertEquals(charsetName, document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.available()).thenReturn(1024); // 1 KB for testing
        Parser parser = mock(Parser.class);
        String charsetName = "UTF-16";
        String baseUri = "http://example.com/";
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
        verify(parser).parseInput(anyString(), eq(baseUri));
        assertEquals(charsetName, document.outputSettings().charset());
    }

    @Test
    public void testParseByteData() throws Exception {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Parser parser = mock(Parser.class);
        ByteBuffer byteData = ByteBuffer.allocate(1024); // 1 KB for testing
        String docData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head></head><body></body></html>";
        byteData.put(docData.getBytes());
        byteData.flip();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        verify(parser).parseInput(eq(docData), eq(baseUri));
        assertEquals(charsetName, document.outputSettings().charset());

        // Test with null charsetName and detecting charset from header or meta tag
        byteData.clear();
        byteData.put("Data: text/html;charset=gb2312\n".getBytes());
        byteData.put(docData.getBytes());
        byteData.flip();
        document = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertNotNull(document);
        verify(parser).parseInput(eq(docData), eq(baseUri));
        assertEquals("gb2312", document.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals("Test data", Charset.forName("UTF-8").decode(byteData).toString());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 10); // Read only 10 bytes
        assertEquals("Test data", Charset.forName("UTF-8").decode(byteData).toString().substring(0, 10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadToByteBufferWithNegativeMaxSize() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        DataUtil.readToByteBuffer(inputStream, -1);
    }
}