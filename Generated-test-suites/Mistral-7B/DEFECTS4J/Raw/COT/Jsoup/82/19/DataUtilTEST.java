package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Test class for DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() {
        String filePath = "path/to/your/file.html"; // Replace with the actual path to your HTML file
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        try {
            Document doc = DataUtil.load(new File(filePath), charset, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("Failed to load document from file: " + e.getMessage());
        }
    }

    @Test
    public void testLoadDocumentFromInputStream() {
        String htmlData = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlData.getBytes(StandardCharsets.UTF_8));
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        try {
            Document doc = DataUtil.load(inputStream, charset, baseUri);
            assertNotNull(doc);
            assertTrue(doc.body().tagName().equals("body"));
            assertEquals("Test", doc.select("h1").text());
        } catch (IOException e) {
            fail("Failed to load document from input stream: " + e.getMessage());
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    @Test(expected = IOException.class)
    public void testLoadDocumentFromInputStreamWithInvalidCharset() {
        String htmlData = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlData.getBytes(StandardCharsets.UTF_8));
        String charset = "InvalidCharset";
        String baseUri = "http://example.com";

        try {
            DataUtil.load(inputStream, charset, baseUri);
            fail("Should have thrown IOException");
        } catch (IOException e) {
            // Expected
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    @Test
    public void testParseInputStreamWithBOM() {
        String htmlDataWithUtf8Bom = "\uFEFF<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlDataWithUtf8Bom.getBytes(StandardCharsets.UTF_8));
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        try {
            Document doc = DataUtil.parseInputStream(inputStream, charset, baseUri, parser);
            assertNotNull(doc);
            assertTrue(doc.body().tagName().equals("body"));
            assertEquals("Test", doc.select("h1").text());
        } catch (IOException e) {
            fail("Failed to parse input stream with BOM: " + e.getMessage());
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() {
        String htmlData = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlData.getBytes(StandardCharsets.UTF_8));
        String charset = "InvalidCharset";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        try {
            Document doc = DataUtil.parseInputStream(inputStream, charset, baseUri, parser);
            fail("Should have thrown IOException");
        } catch (IOException e) {
            // Expected
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }
}