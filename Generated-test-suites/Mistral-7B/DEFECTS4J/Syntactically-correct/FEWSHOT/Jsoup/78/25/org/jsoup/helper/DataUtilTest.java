package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadDocument() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "https://example.com/";

        // Test loading a file with a valid charset in the header
        File inputFile = new File("test_file_with_charset.html");
        Document doc = DataUtil.load(inputFile, charsetName, baseUri);
        assertNotNull(doc);

        // Test loading a file with no charset in the header or meta tag
        File inputFile2 = new File("test_file_no_charset.html");
        Document doc2 = DataUtil.load(inputFile2, null, baseUri);
        assertNotNull(doc2);
        assertEquals(StandardCharsets.UTF_8.displayName(), doc2.outputSettings().charset());

        // Test loading a file with an invalid charset in the header
        File inputFile3 = new File("test_file_invalid_charset.html");
        Document doc3 = DataUtil.load(inputFile3, "invalidCharset", baseUri);
        assertNotNull(doc3);
        assertEquals(StandardCharsets.UTF_8.displayName(), doc3.outputSettings().charset());

        // Test loading a file with a valid charset in the meta tag
        File inputFile4 = new File("test_file_meta_charset.html");
        Document doc4 = DataUtil.load(inputFile4, null, baseUri);
        assertNotNull(doc4);
        assertEquals("MetaCharset", doc4.outputSettings().charset());

        // Test loading a file with a valid charset in the XML declaration
        File inputFile5 = new File("test_file_xml_declaration.xml");
        Document doc5 = DataUtil.load(inputFile5, null, baseUri, Parser.xmlParser());
        assertNotNull(doc5);
        assertEquals("UTF-8", doc5.outputSettings().charset());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "https://example.com/";

        InputStream inputStream = new ByteArrayInputStream("Test content".getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(StandardCharsets.UTF_8.displayName(), doc.outputSettings().charset());

        inputStream = new ByteArrayInputStream("Test content".getBytes());
        Document doc2 = DataUtil.parseInputStream(inputStream, null, baseUri, Parser.htmlParser());
        assertNotNull(doc2);
        assertEquals(StandardCharsets.UTF_8.displayName(), doc2.outputSettings().charset());

        inputStream = new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>Test content".getBytes());
        Document doc3 = DataUtil.parseInputStream(inputStream, null, baseUri, Parser.xmlParser());
        assertNotNull(doc3);
        assertEquals("UTF-8", doc3.outputSettings().charset());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        assertEquals(expectedCharset, DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=ISO-8859-1";
        expectedCharset = "ISO-8859-1";
        assertEquals(expectedCharset, DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-8";
        charset = charset.trim();
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-8";
        charset = charset.replace("charset=", "");
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "ISO-8859-1";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "ISO-8859-1";
        charset = charset.toUpperCase(Locale.ENGLISH);
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "ISO-8859-1";
        charset = charset.trim();
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "ISO-8859-1";
        charset = charset.replace("\"", "");
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "ISO-8859-1";
        charset = charset.replace("'", "");
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "unknownCharset";
        assertNull(DataUtil.validateCharset(charset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertFalse(boundary.isEmpty());
    }
}