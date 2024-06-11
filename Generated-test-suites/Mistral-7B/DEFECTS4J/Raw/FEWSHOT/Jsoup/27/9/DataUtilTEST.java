package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.InputElement;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadSimpleFile() throws IOException {
        String filePath = "test.html";
        File testFile = new File(filePath);
        Document doc = DataUtil.load(testFile, null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadWithInvalidFile() throws IOException {
        File testFile = new File("non-existent-file.html");
        assertThrows(FileNotFoundException.class, () -> DataUtil.load(testFile, null, null));
    }

    @Test
    public void testLoadWithInvalidCharset() throws IOException {
        File testFile = new File("test.html");
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(testFile, "invalidCharset", null));
    }

    @Test
    public void testLoadWithBaseUri() throws IOException {
        String filePath = "test.html";
        File testFile = new File(filePath);
        String baseUri = "https://example.com";
        Document doc = DataUtil.load(testFile, null, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testLoadWithInputstreamAndNullCharset() throws IOException {
        String docData = "<html><body><h1>Hello World</h1></body></html>";
        InputStream testStream = new ByteArrayInputStream(docData.getBytes());
        Document doc = DataUtil.load(testStream, null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadWithInputstreamAndValidCharset() throws IOException {
        String docData = "<html><body><h1>Hello World</h1></body></html>";
        InputStream testStream = new ByteArrayInputStream(docData.getBytes("UTF-8"));
        Document doc = DataUtil.load(testStream, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadWithInputstreamAndInvalidCharset() throws IOException {
        String docData = "<html><body><h1>Hello World</h1></body></html>";
        InputStream testStream = new ByteArrayInputStream(docData.getBytes());
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(testStream, "invalidCharset", null));
    }

    @Test
    public void testLoadWithInputstreamAndBaseUri() throws IOException {
        String docData = "<html><body><h1>Hello World</h1></body></html>";
        InputStream testStream = new ByteArrayInputStream(docData.getBytes());
        String baseUri = "https://example.com";
        Document doc = DataUtil.load(testStream, null, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testLoadWithInputStreamParser() throws IOException {
        String docData = "<html><body><h1>Hello World</h1></body></html>";
        InputStream testStream = new ByteArrayInputStream(docData.getBytes());
        Parser parser = mock(Parser.class);
        when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(createTestDocument());
        Document doc = DataUtil.load(testStream, null, null, parser);
        assertNotNull(doc);
    }

    @Test
    public void testParseByteData() throws IOException {
        String docData = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = mock(Parser.class);
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals(charsetName, doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String docData = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
        String charsetName = "invalidCharset";
        String baseUri = null;
        Parser parser = mock(Parser.class);
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, charsetName, baseUri, parser));
    }

    @Test
    public void testParseByteDataWithNullByteData() throws IOException {
        ByteBuffer byteData = ByteBuffer.allocate(0);
        String charsetName = "UTF-8";
        String baseUri = null;
        Parser parser = mock(Parser.class);
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNull(doc);
    }

    @Test
    public void testParseByteDataWithNullCharset() throws IOException {
        String docData = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
        String charsetName = null;
        String baseUri = null;
        Parser parser = mock(Parser.class);
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithBOM() throws IOException {
        String docData = "\ufeff<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = null;
        Parser parser = mock(Parser.class);
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String docData = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inStream = new ByteArrayInputStream(docData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        byte[] expectedBytes = docData.getBytes();
        assertArrayEquals(expectedBytes, byteData.array());
    }

    @Test
    public void testReadToByteBufferWithInvalidStream() throws IOException {
        InputStream inStream = new ByteArrayInputStream("invalid".getBytes());
        assertThrows(IOException.class, () -> DataUtil.readToByteBuffer(inStream));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(null);
        assertNull(charset);
    }

    private Document createTestDocument() {
        String docData = "<html><body><h1>Hello World</h1></body></html>";
        return new InputElement(docData).parse();
    }
}