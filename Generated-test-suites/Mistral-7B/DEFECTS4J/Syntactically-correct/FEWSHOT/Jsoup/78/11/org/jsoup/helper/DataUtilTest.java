package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Test utility class for the DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadFileWithCharset() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "/path/to/test";
        File file = new File("test.html");
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithoutCharset() throws IOException {
        String baseUri = "/path/to/test";
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "/path/to/test";
        byte[] inputData = "test data".getBytes(Charset.forName("UTF-8"));
        InputStream input = new ByteArrayInputStream(inputData);
        Document doc = DataUtil.load(input, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithoutCharset() throws IOException {
        String baseUri = "/path/to/test";
        byte[] inputData = "test data".getBytes();
        InputStream input = new ByteArrayInputStream(inputData);
        Document doc = DataUtil.load(input, null, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithCharset() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "/path/to/test";
        byte[] inputData = "test data".getBytes(Charset.forName("UTF-8"));
        InputStream input = new ByteArrayInputStream(inputData);
        Document doc = DataUtil.parseInputStream(input, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithoutCharset() throws IOException {
        String baseUri = "/path/to/test";
        byte[] inputData = "test data".getBytes();
        InputStream input = new ByteArrayInputStream(inputData);
        Document doc = DataUtil.parseInputStream(input, null, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "/path/to/test";
        byte[] inputData = "test data".getBytes(Charset.forName("UTF-8"));
        InputStream input = new ByteArrayInputStream(inputData);
        Document doc = DataUtil.parseInputStream(input, charsetName, baseUri, Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputFilePath = "input.txt";
        String outputFilePath = "output.txt";
        String charsetName = "UTF-8";

        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);

        InputStream input = new FileInputStream(inputFile);
        OutputStream output = new FileOutputStream(outputFile);

        DataUtil.crossStreams(input, output);

        input.close();
        output.close();

        assertEquals(new String(Files.readAllBytes(Paths.get(inputFilePath)), Charset.forName(charsetName)),
                new String(Files.readAllBytes(Paths.get(outputFilePath)), Charset.forName(charsetName)));
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        ByteBuffer testBuffer = ByteBuffer.allocate(4);
        testBuffer.put((byte) 0xFF);
        testBuffer.put((byte) 0xFE);
        testBuffer.put((byte) 0x00);
        testBuffer.put((byte) 0x00);
        testBuffer.flip();

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(testBuffer);
        assertEquals("UTF-16", bomCharset.charset);
        assertEquals(0, bomCharset.offset);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertEquals("UTF-16", DataUtil.validateCharset("UTF-16"));
        assertEquals("UTF-32", DataUtil.validateCharset("UTF-32"));
        assertEquals("ISO-8859-1", DataUtil.validateCharset("ISO-8859-1"));
        assertEquals("UTF-32BE", DataUtil.validateCharset("UTF-32BE"));
        assertEquals("UTF-32LE", DataUtil.validateCharset("UTF-32LE"));
        assertEquals(null, DataUtil.validateCharset(""));
        assertEquals(null, DataUtil.validateCharset("unknown"));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == 32);
        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(DataUtil.mimeBoundaryChars.contains(boundary.charAt(i)));
        }
    }
}