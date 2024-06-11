package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("testFile.html"); // replace with actual test file
        String expected = "expected content";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(file, null, baseUri);
        assertEquals(expected, doc.text());
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("testFile.html"); // replace with actual test file
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertEquals("expected content", doc.text());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputContent = "expected content";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes());
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(inputStream, null, baseUri);
        assertEquals(inputContent, doc.text());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String inputContent = "expected content";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals(inputContent, doc.text());
    }

    @Test
    public void testLoadInputStreamWithAlternateParser() throws IOException {
        String inputContent = "expected content";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = org.jsoup.parser.Jsoup.xmlParser();

        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertEquals(inputContent, doc.text());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String charsetName = null;
        String baseUri = "http://example.com";
        ByteBuffer byteData = ByteBuffer.allocate(10);
        byteData.put("expected content".getBytes());
        byteData.flip();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertEquals("expected content", doc.text());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteBuffer byteData = ByteBuffer.allocate(10);
        byteData.put("expected content".getBytes());
        byteData.flip();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertEquals("expected content", doc.text());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() {
        String charsetName = null;
        String baseUri = "http://example.com";
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'M', 'T', ' ', 'H', 'T', 'M', 'L', ' ', 'c', 'h', 'a', 'r', 's', 'e', 't', '=', 'U', 'T', 'F', '-', '8'});

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertEquals("UTF-8", charsetName);
        assertEquals("expected content", doc.text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String input = "expected content";

        byte[] buffer = new byte[bufferSize];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());

        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }

        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(outputStream.toByteArray()));
        assertEquals(input, Charset.forName(defaultCharset).decode(byteData).toString());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidContentType() {
        String contentType = "text/plain";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }
}