package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testParseByteData() {
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // test with null charsetName
        ByteBuffer byteData = emptyByteBuffer();
        String docData = "This is some HTML data.";
        Document doc = parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals(docData, doc.html());

        // test with specified charsetName
        charsetName = "UTF-8";
        byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName(charsetName)));
        doc = parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals(docData, doc.html());

        // test with BOM and default charset
        charsetName = null;
        byteData = ByteBuffer.wrap(new byte[] { UNICODE_BOM }.concat(docData.getBytes(Charset.forName(defaultCharset))));
        doc = parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals(docData.substring(1), doc.html());

        // test with specified charsetName that is not supported
        charsetName = "invalidCharset";
        byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName(charsetName)));
        assertThrows(IllegalCharsetNameException.class, () -> parseByteData(byteData, charsetName, baseUri, parser));

        // test with incorrect charsetName in content type header
        charsetName = "text/html; charset=invalidCharset";
        byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName(defaultCharset)));
        doc = parseByteData(byteData, charsetName, baseUri, parser);
        assertNull(doc);

        // test with correct charsetName in content type header
        charsetName = "text/html; charset=UTF-8";
        byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName(defaultCharset)));
        doc = parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals(docData, doc.html());
    }

    @Test
    public void testLoad() throws IOException {
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // test with File
        File file = new File("test.html");
        Document doc = load(file, charsetName, baseUri);
        assertNotNull(doc);

        // test with InputStream
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});
        doc = load(inputStream, charsetName, baseUri);
        assertNull(doc);

        byte[] data = "Test data".getBytes(Charset.forName(defaultCharset));
        inputStream = new ByteArrayInputStream(data);
        doc = load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("Test data", doc.html());

        // test with specified charsetName
        charsetName = "UTF-8";
        inputStream = new ByteArrayInputStream(data);
        doc = load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("Test data", doc.html());
        doc.outputSettings().charset(charsetName);

        // test with BOM and default charset
        charsetName = null;
        inputStream = new ByteArrayInputStream(new byte[] { UNICODE_BOM }.concat(data));
        doc = load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("Test data", doc.html());

        // test with incorrect charsetName in content type header
        charsetName = "text/html; charset=invalidCharset";
        inputStream = new ByteArrayInputStream(data);
        doc = load(inputStream, charsetName, baseUri);
        assertNull(doc);

        // test with correct charsetName in content type header
        charsetName = "text/html; charset=UTF-8";
        inputStream = new ByteArrayInputStream(data);
        doc = load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("Test data", doc.html());
        doc.outputSettings().charset(charsetName);
    }

    @Test
    public void testLoadWithParser() throws IOException {
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();

        // test with File
        File file = new File("test.xml");
        Document doc = load(file, charsetName, baseUri, parser);
        assertNotNull(doc);

        // test with InputStream
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});
        doc = load(inputStream, charsetName, baseUri, parser);
        assertNull(doc);

        byte[] data = "Test xml data".getBytes(Charset.forName(defaultCharset));
        inputStream = new ByteArrayInputStream(data);
        doc = load(inputStream, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals("Test xml data", doc.xml());

        // test with specified charsetName
        charsetName = "UTF-8";
        inputStream = new ByteArrayInputStream(data);
        doc = load(inputStream, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals("Test xml data", doc.xml());
        doc.outputSettings().charset(charsetName);

        // test with BOM and default charset
        charsetName = null;
        inputStream = new ByteArrayInputStream(new byte[] { UNICODE_BOM }.concat(data));
        doc = load(inputStream, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals("Test xml data", doc.xml());

        // test with incorrect charsetName in content type header
        charsetName = "text/xml; charset=invalidCharset";
        inputStream = new ByteArrayInputStream(data);
        doc = load(inputStream, charsetName, baseUri, parser);
        assertNull(doc);

        // test with correct charsetName in content type header
        charsetName = "text/xml; charset=UTF-8";
        inputStream = new ByteArrayInputStream(data);
        doc = load(inputStream, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals("Test xml data", doc.xml());
        doc.outputSettings().charset(charsetName);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html; charset=gb2312";
        charset = getCharsetFromContentType(contentType);
        assertEquals("gb2312", charset);

        contentType = "text/html";
        charset = getCharsetFromContentType(contentType);
        assertNull(charset);

        contentType = "application/octet-stream";
        charset = getCharsetFromContentType(contentType);
        assertNull(charset);

        contentType = "text/html; charset= invalid-charset ";
        charset = getCharsetFromContentType(contentType);
        assertNull(charset);

        contentType = "text/html; charset=Utf-8";
        charset = getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = mimeBoundary();
        assertNotNull(boundary);
        assertEquals(boundary.length(), boundaryLength);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});
        OutputStream outputStream = new ByteArrayOutputStream();
        crossStreams(inputStream, outputStream);
        assertArrayEquals(new byte[0], outputStream.toByteArray());

        inputStream = new ByteArrayInputStream("Test data".getBytes(Charset.forName(defaultCharset)));
        outputStream = new ByteArrayOutputStream();
        crossStreams(inputStream, outputStream);
        assertArrayEquals("Test data".getBytes(Charset.forName(defaultCharset)), outputStream.toByteArray());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteData = readFileToByteBuffer(file);
        assertNotNull(byteData);
        assertEquals(file.length(), byteData.limit());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});
        ByteBuffer byteData = readToByteBuffer(inputStream);
        assertNotNull(byteData);
        assertEquals(0, byteData.limit());

        byte[] data = "Test data".getBytes(Charset.forName(defaultCharset));
        inputStream = new ByteArrayInputStream(data);
        byteData = readToByteBuffer(inputStream, data.length);
        assertNotNull(byteData);
        assertEquals(data.length, byteData.limit());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteData = emptyByteBuffer();
        assertNotNull(byteData);
        assertEquals(0, byteData.limit());
    }
}