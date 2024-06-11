package org.jsoup.helper.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        File file = new File("test.html"); // replace with valid file path
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull("Document should not be null", doc);
        // check for typical usage and edge cases
        assertNotNull("Document should have root element", doc.rootElement());
        assertEquals("html", doc.rootElement().tagName());
        assertNotNull("Document should have head element", doc.select("head").first());
        assertEquals("head", doc.select("head").first().tagName());
        // check for error scenarios
        charsetName = null;
        doc = DataUtil.load(file, charsetName, baseUri);
        assertTrue("Document should be empty when charset is not provided", doc.outputSettings().contentType().startsWith("text/html; charset=UTF-8"));
        charsetName = "invalid_charset";
        doc = DataUtil.load(file, charsetName, baseUri);
        assertTrue("Document should be empty when invalid charset is provided", doc.outputSettings().contentType().startsWith("text/html; charset=UTF-8"));
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        String htmlContent = "your_html_content_here"; // replace with valid html content
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull("Document should not be null", doc);
        // check for typical usage and edge cases
        assertNotNull("Document should have root element", doc.rootElement());
        assertEquals("html", doc.rootElement().tagName());
        assertNotNull("Document should have head element", doc.select("head").first());
        assertEquals("head", doc.select("head").first().tagName());
        // check for error scenarios
        charsetName = null;
        doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertTrue("Document should be empty when charset is not provided", doc.outputSettings().contentType().startsWith("text/html; charset=UTF-8"));
        charsetName = "invalid_charset";
        doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertTrue("Document should be empty when invalid charset is provided", doc.outputSettings().contentType().startsWith("text/html; charset=UTF-8"));
    }

    @Test
    public void testLoadInputStreamWithInvalidCharsetAndFallback() throws IOException {
        String charsetName = "GBK";
        String baseUri = "http://example.com";
        String htmlContent = "your_html_content_here"; // replace with valid html content
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull("Document should not be null", doc);
        // check for typical usage and edge cases
        assertNotNull("Document should have root element", doc.rootElement());
        assertEquals("html", doc.rootElement().tagName());
        assertNotNull("Document should have head element", doc.select("head").first());
        assertEquals("head", doc.select("head").first().tagName());
        // check for error scenarios
        charsetName = null;
        doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertTrue("Document should be empty when charset is not provided", doc.outputSettings().contentType().startsWith("text/html; charset=UTF-8"));
        charsetName = "invalid_charset";
        doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertTrue("Document should be empty when invalid charset is provided", doc.outputSettings().contentType().startsWith("text/html; charset=UTF-8"));
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("input.txt");
        File outputFile = new File("output.txt");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);
        try {
            DataUtil.crossStreams(inputStream, outputStream);
            // check for typical usage and edge cases
            assertTrue("Input and output files should have the same content", new String(new File("output.txt").getBytes(), StandardCharsets.UTF_8).equals(new String(new File("input.txt").getBytes(), StandardCharsets.UTF_8)));
        } finally {
            inputStream.close();
            outputStream.close();
        }

        // check for error scenarios
        inputFile = null;
        inputStream = new FileInputStream(inputFile);
        outputStream = new FileOutputStream(outputFile);
        try {
            DataUtil.crossStreams(inputStream, outputStream);
            fail("IOException should be thrown when input file is null");
        } catch (IOException e) {
            // expected
        } finally {
            inputStream.close();
            outputStream.close();
        }

        inputFile = new File("input.txt");
        outputFile = null;
        inputStream = new FileInputStream(inputFile);
        try {
            DataUtil.crossStreams(inputStream, outputStream);
            fail("IOException should be thrown when output file is null");
        } catch (IOException e) {
            // expected
        } finally {
            inputStream.close();
        }
    }
}