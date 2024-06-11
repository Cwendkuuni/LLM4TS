package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.jsoup.helper.DataUtil.getCharsetFromContentType;
import static org.jsoup.parser.Parser.htmlParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import org.junit.Test;
import org.jsoup.nodes.Document;

public class DataUtilTest {

    @Test
    public void testLoadDocument() throws IOException {
        String contentType = "text/html; charset=UTF-8";
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8")));
        Document document = DataUtil.load(inputStream, contentType, null);
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testDetectCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String detectedCharset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", detectedCharset);

        contentType = "text/html; charset=ISO-8859-1";
        detectedCharset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("ISO-8859-1", detectedCharset);

        contentType = "text/html; charset=windows-1252";
        detectedCharset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("windows-1252", detectedCharset);

        contentType = "text/html";
        detectedCharset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(detectedCharset);
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String html = "<html><head><meta charset=\"" + charsetName + "\"></head><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(Charset.forName(charsetName)));
        Document document = DataUtil.parseByteData(byteData, charsetName, null, htmlParser());
        assertNotNull(document);
        assertEquals(charsetName, document.outputSettings().charset());

        charsetName = null;
        html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        byteData = ByteBuffer.wrap(html.getBytes(Charset.forName("UTF-8")));
        document = DataUtil.parseByteData(byteData, charsetName, null, htmlParser());
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String content = "This is a test.";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8")));

        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals(content.length(), buffer.limit());

        byte[] bufferData = outputStream.toByteArray();
        inputStream = new ByteArrayInputStream(bufferData);
        buffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals(content.length(), buffer.limit());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadToByteBufferNegativeMaxSize() {
        DataUtil.readToByteBuffer(null, -1);
    }
}