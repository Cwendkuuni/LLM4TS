package org.jsoup.helper.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    private Document doc;

    @Before
    public void setUp() {
        doc = null;
    }

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        File file = new File("test.html");
        Charset charset = Charset.forName("UTF-8");
        String baseUri = "http://example.com/";
        doc = DataUtil.load(file, charset.name(), baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        String baseUri = "http://example.com/";
        doc = DataUtil.load(file, "invalidCharset", baseUri);
        assertNull(doc);
    }

    @Test
    public void testLoadFileWithNoCharset() throws IOException {
        File file = new File("test.html");
        String baseUri = "http://example.com/";
        doc = DataUtil.load(file, null, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        InputStream input = new FileInputStream(new File("test.html"));
        Charset charset = Charset.forName("UTF-8");
        String baseUri = "http://example.com/";
        doc = DataUtil.load(input, charset.name(), baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        InputStream input = new FileInputStream(new File("test.html"));
        String baseUri = "http://example.com/";
        doc = DataUtil.load(input, "invalidCharset", baseUri);
        assertNull(doc);
    }

    @Test
    public void testLoadInputStreamWithNoCharset() throws IOException {
        InputStream input = new FileInputStream(new File("test.html"));
        String baseUri = "http://example.com/";
        doc = DataUtil.load(input, null, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream input = new FileInputStream(new File("test.html"));
        Charset charset = Charset.forName("UTF-8");
        String baseUri = "http://example.com/";
        Parser parser = Parser.xmlParser(); // test a different parser than htmlParser()
        doc = DataUtil.load(input, charset.name(), baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        byte[] inputData = new byte[bufferSize];
        InputStream in = new ByteArrayInputStream(inputData);
        OutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        byte[] expectedOutput = new byte[bufferSize];
        System.arraycopy(inputData, 0, expectedOutput, 0, bufferSize);
        assertEquals(expectedOutput, out.toByteArray());
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[bufferSize]);
        Charset charset = Charset.forName("UTF-8");
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();
        doc = DataUtil.parseInputStream(input, charset.name(), baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[bufferSize]);
        String baseUri = "http://example.com/";
        doc = DataUtil.parseInputStream(input, "invalidCharset", baseUri, Parser.htmlParser());
        assertNull(doc);
    }

    @Test
    public void testParseInputStreamWithNoCharset() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[bufferSize]);
        String baseUri = "http://example.com/";
        doc = DataUtil.parseInputStream(input, null, baseUri, Parser.htmlParser());
        assertNull(doc);
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[bufferSize]);
        Charset charset = Charset.forName("UTF-8");
        String baseUri = "http://example.com/";
        Parser parser = Parser.xmlParser();
        doc = DataUtil.parseInputStream(input, charset.name(), baseUri, parser);
        assertNull(doc);
    }

    @Test
    public void testReadToByteBufferFromFile() throws IOException {
        File file = new File("test.html");
        ByteBuffer buffer = DataUtil.readToByteBuffer(file);
        assertNotNull(buffer);
    }

    @Test
    public void testReadToByteBufferFromInputStream() throws IOException {
        InputStream input = new FileInputStream(new File("test.html"));
        ByteBuffer buffer = DataUtil.readToByteBuffer(input);
        assertNotNull(buffer);
    }

    @Test
    public void testReadToByteBufferFromEmptyInputStream() throws IOException {
        ByteBuffer buffer = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[0]));
        assertEquals(0, buffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        assertEquals(expectedCharset, DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-8";
        charset = charset.toUpperCase(Locale.ENGLISH);
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "ISO-8859-1";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "InvalidCharset";
        assertNull(DataUtil.validateCharset(charset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        int length = boundary.length();
        assertEquals(boundaryLength, length);
    }

    @Test
    public void testDetectCharsetFromBom() {
        byte[] bom = new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00};
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(bom));
        assertEquals("UTF-16", bomCharset.charset);
        assertEquals(0, bomCharset.offset);

        bom = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF};
        bomCharset = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(bom));
        assertEquals("UTF-16", bomCharset.charset);
        assertEquals(0, bomCharset.offset);

        bom = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        bomCharset = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(bom));
        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(3, bomCharset.offset);
    }
}