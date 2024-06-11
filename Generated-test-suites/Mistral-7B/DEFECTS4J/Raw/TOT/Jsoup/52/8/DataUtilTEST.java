package org.jsoup.helper.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

import org.junit.Test;

/**
 * Unit tests for the DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException, HelperException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Test loading a file with a valid charset in the XML declaration
        File file = new File("testfiles/valid_charset.html");
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("Valid Charset", doc.title());

        // Test loading a file with no charset in the XML declaration or meta tags
        file = new File("testfiles/no_charset.html");
        doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("No Charset", doc.title());

        // Test loading a file with an invalid charset in the XML declaration
        file = new File("testfiles/invalid_charset.html");
        doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("Invalid Charset", doc.title());

        // Test loading a file with an unknown charset in the XML declaration
        file = new File("testfiles/unknown_charset.html");
        doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("Unknown Charset", doc.title());

        // Test loading a file with a specified charset different from the XML declaration
        charsetName = "ISO-8859-1";
        doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("Unknown Charset", doc.title());
    }

    @Test
    public void testLoadStream() throws IOException, HelperException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Test loading a stream with a valid charset in the XML declaration
        InputStream in = getClass().getResourceAsStream("/testfiles/valid_charset.html");
        Document doc = DataUtil.load(in, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("Valid Charset", doc.title());

        // Test loading a stream with no charset in the XML declaration or meta tags
        in = getClass().getResourceAsStream("/testfiles/no_charset.html");
        doc = DataUtil.load(in, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("No Charset", doc.title());

        // Test loading a stream with an invalid charset in the XML declaration
        in = getClass().getResourceAsStream("/testfiles/invalid_charset.html");
        doc = DataUtil.load(in, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("Invalid Charset", doc.title());

        // Test loading a stream with an unknown charset in the XML declaration
        in = getClass().getResourceAsStream("/testfiles/unknown_charset.html");
        doc = DataUtil.load(in, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("Unknown Charset", doc.title());

        // Test loading a stream with a specified charset different from the XML declaration
        charsetName = "ISO-8859-1";
        doc = DataUtil.load(in, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("Unknown Charset", doc.title());
    }

    @Test
    public void testParseByteData() throws IOException, HelperException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Test parsing byte data with a valid charset in the XML declaration
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Valid Charset</h1></body></html>".getBytes(StandardCharsets.UTF_8));
        DataUtil.crossStreams(in, out);
        ByteBuffer byteData = DataUtil.parseByteData(ByteBuffer.wrap(out.toByteArray()), charsetName, baseUri, Parser.htmlParser());
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser()).body();
        assertEquals("Valid Charset", doc.text());

        // Test parsing byte data with no charset in the XML declaration or meta tags
        out = new ByteArrayOutputStream();
        in = new ByteArrayInputStream("<html><body><h1>No Charset</h1></body></html>".getBytes(StandardCharsets.UTF_8));
        DataUtil.crossStreams(in, out);
        byteData = DataUtil.parseByteData(ByteBuffer.wrap(out.toByteArray()), charsetName, baseUri, Parser.htmlParser());
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser()).body();
        assertEquals("No Charset", doc.text());

        // Test parsing byte data with an invalid charset in the XML declaration
        out = new ByteArrayOutputStream();
        in = new ByteArrayInputStream("<html xmlns:x=\"http://www.w3.org/2000/xmlns/\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\"></head><body><h1>Invalid Charset</h1></body></html>".getBytes(StandardCharsets.UTF_8));
        DataUtil.crossStreams(in, out);
        byteData = DataUtil.parseByteData(ByteBuffer.wrap(out.toByteArray()), charsetName, baseUri, Parser.htmlParser());
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser()).body();
        assertEquals("Invalid Charset", doc.text());

        // Test parsing byte data with an unknown charset in the XML declaration
        out = new ByteArrayOutputStream();
        in = new ByteArrayInputStream("<html xmlns:x=\"http://www.w3.org/2000/xmlns/\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=x-charset\"></head><body><h1>Unknown Charset</h1></body></html>".getBytes(StandardCharsets.UTF_8));
        DataUtil.crossStreams(in, out);
        byteData = DataUtil.parseByteData(ByteBuffer.wrap(out.toByteArray()), charsetName, baseUri, Parser.htmlParser());
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser()).body();
        assertEquals("Unknown Charset", doc.text());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();

        // Test detecting UTF-32 BOM
        byteData.put((byte) 0x00).put((byte) 0x00).put((byte) 0xFE).put((byte) 0xFF);
        String charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-32", charsetName);

        // Test detecting UTF-16 BE BOM
        byteData.put((byte) 0xFE).put((byte) 0xFF).put((byte) 0x00).put((byte) 0x00);
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-16 BE", charsetName);

        // Test detecting UTF-16 LE BOM
        byteData.put((byte) 0xFF).put((byte) 0xFE).put((byte) 0x00).put((byte) 0x00);
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-16 LE", charsetName);

        // Test detecting UTF-8 BOM
        byteData.put((byte) 0xEF).put((byte) 0xBB).put((byte) 0xBF);
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-8", charsetName);

        // Test detecting no BOM
        byteData.clear();
        byteData.put((byte) 'H'.toByte());
        byteData.put((byte) 'e'.toByte());
        byteData.put((byte) 'l'.toByte());        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertNull(charsetName);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());

        // Test creating the same boundary multiple times
        String boundary1 = DataUtil.mimeBoundary();
        String boundary2 = DataUtil.mimeBoundary();
        assertEquals(boundary1, boundary2);
    }
}