package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {
    @Test
    public void testLoad() throws IOException {
        File file = new File("test_file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        // Test case when file exists
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertEquals("Document title", document.title());
        
        // Test case when file does not exist
        File nonExistingFile = new File("non_existing_file.html");
        try {
            DataUtil.load(nonExistingFile, charsetName, baseUri);
        } catch (IOException e) {
            assertEquals("File not found: " + nonExistingFile.getPath(), e.getMessage());
        }
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html><head><title>Test Document</title></head><body><h1>Hello, world!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        // Test case when input stream contains valid HTML
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals("Test Document", document.title());

        // Test case when input stream is empty
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        try {
            DataUtil.load(emptyInputStream, charsetName, baseUri);
        } catch (IOException e) {
            assertEquals("Stream is empty", e.getMessage());
        }
    }

    @Test
    public void testLoadFromInputStreamAndParser() throws IOException {
        String html = "<html><head><title>Test Document</title></head><body><h1>Hello, world!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();

        // Test case when input stream contains valid HTML
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertEquals("Test Document", document.title());

        // Test case when input stream is empty
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        try {
            DataUtil.load(emptyInputStream, charsetName, baseUri, parser);
        } catch (IOException e) {
            assertEquals("Stream is empty", e.getMessage());
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals("test", document.text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "test data";
        byte[] buffer = testData.getBytes();
        InputStream inputStream = new ByteArrayInputStream(buffer);

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(testData, new String(byteData.array()));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
        
        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}