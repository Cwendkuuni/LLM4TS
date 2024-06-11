package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import static org.junit.Assert.*;

public class DataUtilTest {
    // Declare variables for test
    private File testFile;
    private String testFileCharset;
    private String testBaseUri;

    @Before
    public void setUp() throws Exception {
        // Set up test environment
        testFile = new File("test.html");
        testFileCharset = "UTF-8";
        testBaseUri = "https://example.com/";
    }

    @After
    public void tearDown() throws Exception {
        // Clean up test environment
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    // Test case for load(File in, String charsetName, String baseUri) method
    @Test
    public void testLoadFile() throws Exception {
        // Create a test file
        testFile.createNewFile();

        // Write test content to the file
        String testContent = "<html><body><h1>Hello, World!</h1></body></html>";
        FileUtils.writeStringToFile(testFile, testContent, Charset.forName(testFileCharset));

        // Load the test file using DataUtil.load(File in, String charsetName, String baseUri)
        Document document = DataUtil.load(testFile, testFileCharset, testBaseUri);

        // Verify the loaded document
        assertNotNull(document);
        assertEquals(testContent, document.html());
        assertEquals(testBaseUri, document.baseUri());
    }

    // Test case for load(InputStream in, String charsetName, String baseUri) method
    @Test
    public void testLoadInputStream() throws Exception {
        // Create a test file
        testFile.createNewFile();

        // Write test content to the file
        String testContent = "<html><body><h1>Hello, World!</h1></body></html>";
        FileUtils.writeStringToFile(testFile, testContent, Charset.forName(testFileCharset));

        // Load the test file using DataUtil.load(InputStream in, String charsetName, String baseUri)
        InputStream inputStream = new FileInputStream(testFile);
        Document document = DataUtil.load(inputStream, testFileCharset, testBaseUri);

        // Verify the loaded document
        assertNotNull(document);
        assertEquals(testContent, document.html());
        assertEquals(testBaseUri, document.baseUri());

        // Close the input stream
        inputStream.close();
    }

    // Test case for load(InputStream in, String charsetName, String baseUri, Parser parser) method
    @Test
    public void testLoadWithParser() throws Exception {
        // Create a test file
        testFile.createNewFile();

        // Write test content to the file
        String testContent = "<html><body><h1>Hello, World!</h1></body></html>";
        FileUtils.writeStringToFile(testFile, testContent, Charset.forName(testFileCharset));

        // Load the test file using DataUtil.load(InputStream in, String charsetName, String baseUri, Parser parser)
        InputStream inputStream = new FileInputStream(testFile);
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.load(inputStream, testFileCharset, testBaseUri, parser);

        // Verify the loaded document
        assertNotNull(document);
        assertEquals(testContent, document.html());
        assertEquals(testBaseUri, document.baseUri());

        // Close the input stream
        inputStream.close();
    }

    // Test case for parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser) method
    @Test
    public void testParseByteData() throws Exception {
        // Create a test file
        testFile.createNewFile();

        // Write test content to the file
        String testContent = "<html><body><h1>Hello, World!</h1></body></html>";
        FileUtils.writeStringToFile(testFile, testContent, Charset.forName(testFileCharset));

        // Read test file to byte buffer
        FileInputStream inStream = new FileInputStream(testFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        inStream.close();

        // Parse byte data using DataUtil.parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser)
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, testFileCharset, testBaseUri, parser);

        // Verify the loaded document
        assertNotNull(document);
        assertEquals(testContent, document.html());
        assertEquals(testBaseUri, document.baseUri());
    }

    // Test case for readToByteBuffer(InputStream inStream, int maxSize) method
    @Test
    public void testReadToByteBuffer() throws Exception {
        // Create a test file
        testFile.createNewFile();

        // Write test content to the file
        String testContent = "<html><body><h1>Hello, World!</h1></body></html>";
        FileUtils.writeStringToFile(testFile, testContent, Charset.forName(testFileCharset));

        // Read test file to byte buffer using DataUtil.readToByteBuffer(InputStream inStream, int maxSize)
        InputStream inputStream = new FileInputStream(testFile);
        int maxSize = 500; // Set maximum size to 500 bytes
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        // Verify the byte buffer size
        assertEquals(maxSize, byteBuffer.capacity());

        // Close the input stream
        inputStream.close();
    }

    // Test case for readToByteBuffer(InputStream inStream) method
    @Test
    public void testReadToByteBufferUnlimited() throws Exception {
        // Create a test file
        testFile.createNewFile();

        // Write test content to the file
        String testContent = "<html><body><h1>Hello, World!</h1></body></html>";
        FileUtils.writeStringToFile(testFile, testContent, Charset.forName(testFileCharset));

        // Read test file to byte buffer using DataUtil.readToByteBuffer(InputStream inStream)
        InputStream inputStream = new FileInputStream(testFile);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        // Verify the byte buffer size
        assertEquals(testContent.length(), byteBuffer.capacity());

        // Close the input stream
        inputStream.close();
    }

    // Test case for getCharsetFromContentType(String contentType) method with supported charset
    @Test
    public void testGetCharsetFromContentTypeSupported() {
        // Test with supported charset
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Verify the charset
        assertEquals("UTF-8", charset);
    }

    // Test case for getCharsetFromContentType(String contentType) method with unsupported charset
    @Test
    public void testGetCharsetFromContentTypeUnsupported() {
        // Test with unsupported charset
        String contentType = "text/html; charset=ISO-8859-13";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Verify the charset
        assertNull(charset);
    }

    // Test case for getCharsetFromContentType(String contentType) method with invalid content type
    @Test
    public void testGetCharsetFromContentTypeInvalid() {
        // Test with invalid content type
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Verify the charset
        assertNull(charset);
    }

    // Test case for getCharsetFromContentType(String contentType) method with multiple charsets
    @Test
    public void testGetCharsetFromContentTypeMultipleCharsets() {
        // Test with multiple charsets
        String contentType = "text/html; charset=UTF-8; charset=ISO-8859-1";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Verify the charset
        assertEquals("UTF-8", charset);
    }
}