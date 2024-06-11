package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document doc = DataUtil.load(file, charsetName, baseUri);

        assertEquals("Expected Document to not be null", doc, DataUtil.parseByteData(readToByteBuffer(file), charsetName, baseUri, Parser.htmlParser()));
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream in = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document doc = DataUtil.load(in, charsetName, baseUri);

        assertEquals("Expected Document to not be null", doc, DataUtil.parseByteData(readToByteBuffer(in), charsetName, baseUri, Parser.htmlParser()));
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream in = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = mock(Parser.class);

        when(parser.parseInput(anyString(), anyString())).thenReturn(mock(Document.class));

        Document doc = DataUtil.load(in, charsetName, baseUri, parser);

        assertEquals("Expected Document to not be null", doc, DataUtil.parseByteData(readToByteBuffer(in), charsetName, baseUri, parser).get());
    }

    @Test
    public void testParseByteData() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertEquals("Expected Document to not be null", doc, DataUtil.parseByteData(byteData, null, baseUri, parser).get());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-16\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes("UTF-8"));
        String baseUri = "http://test.com";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, null, baseUri, parser);

        assertEquals("Expected Document to not be null and have charset UTF-16", doc, DataUtil.parseByteData(byteData, "UTF-16", baseUri, parser).get());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes("UTF-8"));
        String charsetName = "InvalidCharset";
        String baseUri = "http://test.com";
        Parser parser = Parser.htmlParser();

        assertNull("Expected Document to be null", DataUtil.parseByteData(byteData, charsetName, baseUri, parser).get());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedData = htmlContent.getBytes("UTF-8");
        byte[] actualData = new byte[byteData.limit()];
        byteData.get(actualData);

        assertArrayEquals("Expected byte arrays to be equal", expectedData, actualData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("Expected charset to be UTF-8", "UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull("Expected charset to be null", charset);
    }
}