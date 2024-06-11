package org.jsoup.helper;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {
    @Test
    public void testLoad_FileCharset_BaseUri() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testLoad_InputCharset_BaseUri() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testLoad_InputCharset_BaseUri_Parser() throws IOException {
        InputStream inputStream = new FileInputStream("test.xml");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Test setup
        InputStream inputStream = new FileInputStream("input.txt");
        OutputStream outputStream = new ByteArrayOutputStream();

        // Run method
        DataUtil.crossStreams(inputStream, outputStream);

        // Assertions
        String expectedOutput = "This is a test input stream.";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testParseInputStream_ValidCharset() throws IOException {
        InputStream inputStream = new FileInputStream("input.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseInputStream_NullCharset() throws IOException {
        InputStream inputStream = new FileInputStream("input.html");
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testGetCharsetFromContentType_ValidContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_NullContentType() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

}