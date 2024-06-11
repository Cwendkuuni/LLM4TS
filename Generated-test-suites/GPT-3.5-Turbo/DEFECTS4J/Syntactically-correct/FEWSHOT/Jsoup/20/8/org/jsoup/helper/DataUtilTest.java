package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    private static final String BASE_URI = "https://example.com";

    @Test
    public void testLoad_File_ValidCharset() throws IOException {
        File file = new File("path/to/file.html");
        Document document = DataUtil.load(file, StandardCharsets.UTF_8.name(), BASE_URI);
        assertNotNull(document);
        // add assertions for the loaded document
    }

    @Test
    public void testLoad_File_NullCharset() throws IOException {
        File file = new File("path/to/file.html");
        Document document = DataUtil.load(file, null, BASE_URI);
        assertNotNull(document);
        // add assertions for the loaded document
    }

    @Test
    public void testLoad_File_EmptyCharset() throws IOException {
        File file = new File("path/to/file.html");
        Document document = DataUtil.load(file, "", BASE_URI);
        assertNotNull(document);
        // add assertions for the loaded document
    }

    // Add more test cases for different files with different charsets to cover all possibilities

    @Test
    public void testLoad_InputStream_ValidCharset() throws IOException {
        String html = "<html><head></head><body>Hello World!</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), BASE_URI);
        assertNotNull(document);
        // add assertions for the loaded document
    }

    @Test
    public void testParseByteData_ValidCharset() {
        String html = "<html><head></head><body>Hello World!</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, StandardCharsets.UTF_8.name(), BASE_URI, Parser.htmlParser());
        assertNotNull(document);
        // add assertions for the parsed document
    }

    @Test
    public void testParseByteData_NullCharset() {
        String html = "<html><head></head><body>Hello World!</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, null, BASE_URI, Parser.htmlParser());
        assertNotNull(document);
        // add assertions for the parsed document
    }

    @Test
    public void testParseByteData_EmptyCharset() {
        String html = "<html><head></head><body>Hello World!</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, "", BASE_URI, Parser.htmlParser());
        assertNotNull(document);
        // add assertions for the parsed document
    }

    // Add more test cases to cover different scenarios when parsing byte data

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] bytes = new byte[]{0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x57, 0x6F, 0x72, 0x6C, 0x64, 0x21};
        InputStream inputStream = new ByteArrayInputStream(bytes);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
        assertEquals(12, byteBuffer.limit());
        // add assertions for the byte buffer content
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(StandardCharsets.UTF_8.name(), charset);
    }

    @Test
    public void testGetCharsetFromContentType_NoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_EmptyCharset() {
        String contentType = "text/html; charset=";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    // Add more test cases to cover different scenarios when extracting charset from content type

}