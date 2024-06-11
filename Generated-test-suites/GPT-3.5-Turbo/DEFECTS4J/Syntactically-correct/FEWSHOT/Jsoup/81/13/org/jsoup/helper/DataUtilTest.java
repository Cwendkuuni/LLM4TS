package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DataUtilTest {
    private File testFile;
    private InputStream testInputStream;
    private Parser testParser;
    private String testCharsetName;
    private String testBaseUri;
    private String testContentType;
    private String testCharset;

    @Before
    public void setUp() {
        testFile = new File("test.html");
        testInputStream = new ByteArrayInputStream("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>".getBytes());
        testParser = Parser.htmlParser();
        testCharsetName = "UTF-8";
        testBaseUri = "http://example.com";
        testContentType = "text/html; charset=UTF-8";
        testCharset = "UTF-8";
    }

    @Test
    public void testLoadWithFile() throws IOException {
        Document expectedDoc = Jsoup.parse(testFile, testCharsetName, testBaseUri);

        Document actualDoc = DataUtil.load(testFile, testCharsetName, testBaseUri);

        assertEquals(expectedDoc, actualDoc);
    }

    @Test
    public void testLoadWithInputStream() throws IOException {
        Document expectedDoc = Jsoup.parse(testInputStream, testCharsetName, testBaseUri);

        Document actualDoc = DataUtil.load(testInputStream, testCharsetName, testBaseUri);

        assertEquals(expectedDoc, actualDoc);
    }

    @Test
    public void testLoadWithInputStreamAndParser() throws IOException {
        Document expectedDoc = Jsoup.parse(testInputStream, testCharsetName, testBaseUri, testParser);

        Document actualDoc = DataUtil.load(testInputStream, testCharsetName, testBaseUri, testParser);

        assertEquals(expectedDoc, actualDoc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        String expectedOutput = "Hello World";
        String actualOutput = outputStream.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testParseInputStream() throws IOException {
        Document expectedDoc = Jsoup.parse(testInputStream, testCharsetName, testBaseUri, testParser);

        Document actualDoc = DataUtil.parseInputStream(testInputStream, testCharsetName, testBaseUri, testParser);

        assertEquals(expectedDoc, actualDoc);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String expectedCharset = testCharset;

        String actualCharset = DataUtil.getCharsetFromContentType(testContentType);

        assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoContentType() {
        String expectedCharset = null;

        String actualCharset = DataUtil.getCharsetFromContentType(null);

        assertEquals(expectedCharset, actualCharset);
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteBuffer expectedBuffer = ByteBuffer.wrap("Hello World".getBytes());

        ByteBuffer actualBuffer = DataUtil.readToByteBuffer(testInputStream);

        assertEquals(expectedBuffer, actualBuffer);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        int maxSize = 5;
        ByteBuffer expectedBuffer = ByteBuffer.wrap("Hello".getBytes());

        ByteBuffer actualBuffer = DataUtil.readToByteBuffer(testInputStream, maxSize);

        assertEquals(expectedBuffer, actualBuffer);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        ByteBuffer expectedBuffer = ByteBuffer.wrap("Hello World".getBytes());

        ByteBuffer actualBuffer = DataUtil.readFileToByteBuffer(testFile);

        assertEquals(expectedBuffer, actualBuffer);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer expectedBuffer = ByteBuffer.allocate(0);

        ByteBuffer actualBuffer = DataUtil.emptyByteBuffer();

        assertEquals(expectedBuffer, actualBuffer);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";

        String validatedCharset = DataUtil.validateCharset(charset);

        assertEquals(charset, validatedCharset);
    }

    @Test
    public void testValidateCharsetWithInvalidCharset() {
        String charset = "InvalidCharset";

        String validatedCharset = DataUtil.validateCharset(charset);

        assertNull(validatedCharset);
    }
}