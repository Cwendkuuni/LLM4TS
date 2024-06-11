package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFromFile_typical() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        // Additional assertions for the content of the document
    }

    @Test
    public void testLoadFromFile_edge() throws IOException {
        File file = new File("empty.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        // Additional assertions for the content of the document
    }

    @Test(expected = IOException.class)
    public void testLoadFromFile_error() throws IOException {
        File file = new File("nonexistent.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testLoadFromInputStream_typical() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        // Additional assertions for the content of the document
    }

    @Test
    public void testLoadFromInputStream_edge() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("empty.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        // Additional assertions for the content of the document
    }

    @Test(expected = IOException.class)
    public void testLoadFromInputStream_error() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("nonexistent.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.load(inputStream, charsetName, baseUri);
    }

    @Test
    public void testLoadFromInputStreamAndParser_typical() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.xml");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        // Additional assertions for the content of the document
    }

    @Test
    public void testLoadFromInputStreamAndParser_edge() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("empty.xml");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        // Additional assertions for the content of the document
    }

    @Test(expected = IOException.class)
    public void testLoadFromInputStreamAndParser_error() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("nonexistent.xml");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        DataUtil.load(inputStream, charsetName, baseUri, parser);
    }
}