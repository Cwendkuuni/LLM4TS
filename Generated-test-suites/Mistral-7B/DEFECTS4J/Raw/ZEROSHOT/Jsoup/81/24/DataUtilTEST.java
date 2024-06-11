package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.HelperException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Test class for DataUtil.java
 */
public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String filePath = "path/to/your/file.html";

        Document document = DataUtil.load(new File(filePath), charsetName, null);
        assertNotNull(document);
        assertEquals(charsetName, document.outputSettings().charset());
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        String filePath = "path/to/your/file.html";

        try {
            Document document = DataUtil.load(new File(filePath), charsetName, null);
            fail("Expected HelperException but got Document");
        } catch (HelperException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String inputStreamContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(charsetName));

        Document document = DataUtil.load(inputStream, charsetName, null);
        assertNotNull(document);
        assertEquals(charsetName, document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        String inputStreamContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        try {
            Document document = DataUtil.load(inputStream, charsetName, null);
            fail("Expected HelperException but got Document");
        } catch (HelperException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamWithUnsupportedCharset() throws IOException {
        String charsetName = "X-PASCAL";
        String inputStreamContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        try {
            Document document = DataUtil.load(inputStream, charsetName, null);
            fail("Expected HelperException but got Document");
        } catch (HelperException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String inputStreamContent = "<html><body></body></html>";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(charsetName));
        Document document = DataUtil.parseInputStream(inputStream, charsetName, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(charsetName, document.outputSettings().charset());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        String inputStreamContent = "<html><body></body></html>";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        try {
            Document document = DataUtil.parseInputStream(inputStream, charsetName, null, Parser.htmlParser());
            fail("Expected HelperException but got Document");
        } catch (HelperException e) {
            // Expected
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputStreamContent = "Hello, World!";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        String outputStreamContent = outputStream.toString();
        assertEquals(inputStreamContent, outputStreamContent);
    }
}