package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        // Test data
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Test method
        Document doc = DataUtil.load(file, charsetName, baseUri);

        // Assertions
        assertNotNull(doc);
        // Add more assertions
    }

    @Test
    public void testLoadInputStream() throws IOException {
        // Test data
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Test method
        Document doc = DataUtil.load(in, charsetName, baseUri);

        // Assertions
        assertNotNull(doc);
        // Add more assertions
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        // Test data
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Test method
        Document doc = DataUtil.load(in, charsetName, baseUri, parser);

        // Assertions
        assertNotNull(doc);
        // Add more assertions
    }

    @Test
    public void testParseByteData() {
        // Test data
        ByteBuffer byteData = ByteBuffer.wrap("Test".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Test method
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Assertions
        assertNotNull(doc);
        // Add more assertions
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Test data
        InputStream in = getClass().getResourceAsStream("test.html");

        // Test method
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);

        // Assertions
        assertNotNull(byteData);
        // Add more assertions
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test data
        String contentType = "text/html; charset=UTF-8";

        // Test method
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assertions
        assertEquals("UTF-8", charset);
        // Add more assertions
    }
}