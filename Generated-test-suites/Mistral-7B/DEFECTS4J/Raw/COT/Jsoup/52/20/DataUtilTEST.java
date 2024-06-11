package org.jsoup.helper; // make sure the test class is in the same package as the tested class

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class DataUtilTest {

    

    // Test load method with File input, default charset and baseUri
    @Test
    public void testLoadFileDefaultCharsetAndBaseUri() throws IOException {
        File file = new File("test.html"); // assuming test.html exists in the test resources
        Document doc = DataUtil.load(file, null, null);
        // Perform assertions on the Document object
    }

    // Test load method with File input, specified charset and baseUri
    @Test
    public void testLoadFileSpecifiedCharsetAndBaseUri() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        // Perform assertions on the Document object
    }

    // Test load method with InputStream input, default charset and baseUri
    @Test
    public void testLoadInputStreamDefaultCharsetAndBaseUri() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        // Write test data to the ByteArrayOutputStream
        byteStream.write("Test data".getBytes());
        InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());
        Document doc = DataUtil.load(inputStream, null, null);
        // Perform assertions on the Document object
    }

    // Test load method with InputStream input, specified charset and baseUri
    @Test
    public void testLoadInputStreamSpecifiedCharsetAndBaseUri() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write("Test data".getBytes());
        InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        // Perform assertions on the Document object
    }

    // Test load method with InputStream input, Parser and baseUri
    @Test
    public void testLoadInputStreamParserAndBaseUri() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write("Test data".getBytes());
        InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());
        Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        // Perform assertions on the Document object
    }

    // Test crossStreams method
    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayOutputStream byteStreamOut = new ByteArrayOutputStream();
        ByteArrayInputStream byteStreamIn = new ByteArrayInputStream("Test data".getBytes());
        DataUtil.crossStreams(byteStreamIn, byteStreamOut);
        byte[] expected = "Test data".getBytes();
        byte[] actual = byteStreamOut.toByteArray();
        assertEquals(expected.length, actual.length);
        assertEquals(expected, actual);
    }

    // Test parseByteData method with valid ByteBuffer, default charset and baseUri
    @Test
    public void testParseByteDataValidByteBufferDefaultCharsetAndBaseUri() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("test.html"));
        String charsetName = "UTF-8"; // or null for default charset
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        // Perform assertions on the Document object
    }

    // Test parseByteData method with valid ByteBuffer, specified charset and baseUri
    @Test
    public void testParseByteDataValidByteBufferSpecifiedCharsetAndBaseUri() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("test.html"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        // Perform assertions on the Document object
    }

    // Test parseByteData method with ByteBuffer containing BOM
    @Test
    public void testParseByteDataByteBufferWithBom() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap("UTF-32 BOM".getBytes()); // or UTF-16 or UTF-8 BOM
        String charsetName = "UTF-32";
        String baseUri = null;
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        // Perform assertions on the Document object
    }

    // Test parseByteData method with ByteBuffer not containing BOM
    @Test
    public void testParseByteDataByteBufferWithoutBom() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap("Test data".getBytes());
        String charsetName = null;
        String baseUri = null;
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        // Perform assertions on the Document object
    }

    // Test getCharsetFromContentType method
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String result = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", result);

        contentType = "text/html";
        result = DataUtil.getCharsetFromContentType(contentType);
        assertNull(result);

        contentType = "text/html; charset=invalidCharset";
        result = DataUtil.getCharsetFromContentType(contentType);
        assertNull(result);
    }

    // Test validateCharset method
    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validResult = DataUtil.validateCharset(charset);
        assertEquals(charset, validResult);

        charset = "UTF-8\t"; // with some whitespace
        validResult = DataUtil.validateCharset(charset);
        assertEquals(charset, validResult);

        charset = "UTF-8\n"; // with some newline
        validResult = DataUtil.validateCharset(charset);
        assertEquals(charset, validResult);

        charset = "invalidCharset";
        validResult = DataUtil.validateCharset(charset);
        assertNull(validResult);

        charset = "invalidCharset\t";
        validResult = DataUtil.validateCharset(charset);
        assertNull(validResult);

        charset = "invalidCharset\n";
        validResult = DataUtil.validateCharset(charset);
        assertNull(validResult);
    }

    // Test mimeBoundary method
    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        int length = boundary.length();
        assertEquals(32, length);

        for (int i = 0; i < length; i++) {
            char c = boundary.charAt(i);
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || c == '-' || c == '_');
        }
    }

    
}