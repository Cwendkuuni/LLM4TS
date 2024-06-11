package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadDocumentFromInputStreamWithParser() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = mock(Parser.class);
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
        when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(document);
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = mock(Parser.class);
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String docData = "<html><body></body></html>";

        DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(mockDocument());

        assertEquals(mockDocument(), DataUtil.parseByteData(byteData, charsetName, baseUri, parser).get());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(ByteBuffer.wrap("test".getBytes()), byteData);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("testtesttesttesttest".getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 10);
        assertEquals(ByteBuffer.wrap("test".getBytes()), byteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    private Document mockDocument() {
        Document document = mock(Document.class);
        return document;
    }
}