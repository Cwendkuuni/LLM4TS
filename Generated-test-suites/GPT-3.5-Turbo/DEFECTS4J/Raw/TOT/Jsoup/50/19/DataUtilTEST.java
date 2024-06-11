
package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.junit.*;
import java.io.*;

public class DataUtilTest {

    private File testFile;

    @Before
    public void setUp() throws IOException {
        // Create a test file
        testFile = new File("test.html");
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write("<html><head><title>Test Document</title></head><body><h1>Hello, World!</h1></body></html>");
        fileWriter.close();
    }

    @After
    public void tearDown() {
        // Delete the test file
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testLoadFile() throws IOException {
        // Test loading a file to a Document
        Document document = DataUtil.load(testFile, "UTF-8", "http://example.com");
        
        Assert.assertEquals("Test Document", document.title());
        Assert.assertEquals("Hello, World!", document.select("h1").text());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        // Test parsing a Document from an input stream
        FileInputStream inputStream = new FileInputStream(testFile);
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        inputStream.close();

        Assert.assertEquals("Test Document", document.title());
        Assert.assertEquals("Hello, World!", document.select("h1").text());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        // Test parsing a Document from an input stream with alternate parser
        FileInputStream inputStream = new FileInputStream(testFile);
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        inputStream.close();

        Assert.assertEquals("Test Document", document.title());
        Assert.assertEquals("Hello, World!", document.select("h1").text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Test crossing input and output streams
        FileInputStream inputStream = new FileInputStream(testFile);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        inputStream.close();
        outputStream.close();

        String expected = "<html><head><title>Test Document</title></head><body><h1>Hello, World!</h1></body></html>";
        Assert.assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testParseByteData() throws IOException {
        // Test parsing byte data into a Document
        FileInputStream inputStream = new FileInputStream(testFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Document document = DataUtil.parseByteData(byteData, "UTF-8", "http://example.com", Parser.htmlParser());
        inputStream.close();

        Assert.assertEquals("Test Document", document.title());
        Assert.assertEquals("Hello, World!", document.select("h1").text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Test reading input stream into a byte buffer
        FileInputStream inputStream = new FileInputStream(testFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        inputStream.close();

        Assert.assertEquals("<html><head><title>Test Document</title></head><body><h1>Hello, World!</h1></body></html>", new String(byteData.array()));
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        // Test reading a file into a byte buffer
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(testFile);

        Assert.assertEquals("<html><head><title>Test Document</title></head><body><h1>Hello, World!</h1></body></html>", new String(byteData.array()));
    }

    @Test
    public void testEmptyByteBuffer() {
        // Test creating an empty byte buffer
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        Assert.assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test getting charset from content type header
        String charset1 = "text/html; charset=UTF-8";
        String charset2 = "text/html; charset=ISO-8859-1";
        String charset3 = "text/html";
        String charset4 = "text/html; charset=unknown";

        Assert.assertEquals("UTF-8", DataUtil.getCharsetFromContentType(charset1));
        Assert.assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType(charset2));
        Assert.assertNull(DataUtil.getCharsetFromContentType(charset3));
        Assert.assertNull(DataUtil.getCharsetFromContentType(charset4));
    }

    @Test
    public void testMimeBoundary() {
        // Test generating a mime boundary
        String boundary = DataUtil.mimeBoundary();

        Assert.assertEquals(32, boundary.length());
    }
}

