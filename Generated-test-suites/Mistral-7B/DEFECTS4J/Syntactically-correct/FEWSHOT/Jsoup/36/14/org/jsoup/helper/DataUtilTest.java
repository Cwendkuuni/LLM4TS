package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoad() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charset = "UTF-8";
        String baseUri = null;
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.load(inputStream, charset, baseUri);
        assertNotNull(document);
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello</h1></body></html>", document.html());
    }

    @Test
    public void testLoadWithInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charset = "invalidCharset";
        String baseUri = null;
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.load(inputStream, charset, baseUri);
        assertNull(document);
    }

    @Test
    public void testParseByteDataWithUTF8() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello</h1></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String charset = "UTF-8";
        String baseUri = null;
        Parser parser = Parser.htmlParser();

        DataUtil.parseByteData(ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8)), charset, baseUri, parser);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document document = DataUtil.load(inputStream, charset, baseUri);
        assertNotNull(document);
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello</h1></body></html>", document.html());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String html = "<html><head><meta charset=\"gb2312\"></head><body><h1>你好</h1></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String charset = null;
        String baseUri = null;
        Parser parser = Parser.htmlParser();

        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        DataUtil.parseByteData(byteData, charset, baseUri, parser);

        // The charset should have been detected as "gb2312"
        assertEquals("gb2312", charset);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document document = DataUtil.load(inputStream, charset, baseUri);
        assertNotNull(document);
        assertEquals("<html><head><meta charset=\"gb2312\"></head><body><h1>你好</h1></body></html>", document.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedData = html.getBytes(StandardCharsets.UTF_8);
        byte[] actualData = new byte[expectedData.length];
        byteData.get(actualData);

        // Compare the byte arrays
        for (int i = 0; i < expectedData.length; i++) {
            assertEquals(expectedData[i], actualData[i]);
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=invalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }
}