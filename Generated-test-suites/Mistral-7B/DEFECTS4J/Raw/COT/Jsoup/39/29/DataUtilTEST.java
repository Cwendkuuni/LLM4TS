package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import static org.junit.Assert.*;

/**
 * Test class for DataUtil class.
 */
public class DataUtilTest {

    private DataUtil dataUtil;
    private Parser parser;
    private Document document;
    private ByteArrayOutputStream outputStream;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataUtil = new DataUtil();
        parser = org.jsoup.parser.Jsoup.htmlParser();
        outputStream = new ByteArrayOutputStream();
    }

    // Test for load(File, String, String) method

    @Test
    public void testLoadFileWithValidArguments() throws IOException {
        String fileContent = "This is a test file.";
        File testFile = new File("testFile.html");
        testFile.createNewFile();
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document expectedDocument = parser.parse(fileContent);
        Document actualDocument = dataUtil.load(testFile, "UTF-8", null);

        assertEquals(expectedDocument, actualDocument);

        testFile.delete();
    }

    @Test
    public void testLoadFileWithNullCharset() throws IOException {
        File testFile = new File("testFile.html");
        Document actualDocument = dataUtil.load(testFile, null, null);

        assertEquals(Parser.htmlParser().parseInput(""), actualDocument);
        testFile.delete();
    }

    @Test
    public void testLoadFileWithEmptyCharset() throws IOException {
        File testFile = new File("testFile.html");
        Document actualDocument = dataUtil.load(testFile, "", null);

        assertEquals(Parser.htmlParser().parseInput(""), actualDocument);
        testFile.delete();
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File testFile = new File("testFile.html");
        Document actualDocument = dataUtil.load(testFile, "InvalidCharset", null);

        assertEquals(Parser.htmlParser().parseInput(""), actualDocument);
        testFile.delete();
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidFile() throws IOException {
        File testFile = new File("InvalidFile.html");
        dataUtil.load(testFile, "UTF-8", null);
    }

    @Test
    public void testLoadFileWithBaseUri() throws IOException {
        String fileContent = "<html><head><base href='/test'></head><body><p>This is a test file.</p></body></html>";
        File testFile = new File("testFile.html");
        testFile.createNewFile();
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document expectedDocument = parser.parse(fileContent);
        String baseUri = "/test";
        Document actualDocument = dataUtil.load(testFile, "UTF-8", baseUri);

        assertEquals(expectedDocument, actualDocument);
        testFile.delete();
    }

    @Test
    public void testLoadInputStreamWithValidArguments() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        Document expectedDocument = parser.parse(inputStreamContent);
        Document actualDocument = dataUtil.load(inputStream, "UTF-8", null);

        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        Document actualDocument = dataUtil.load(inputStream, null, null);

        assertEquals(Parser.htmlParser().parseInput(inputStreamContent), actualDocument);
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithEmptyCharset() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        Document actualDocument = dataUtil.load(inputStream, "", null);

        assertEquals(Parser.htmlParser().parseInput(inputStreamContent), actualDocument);
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        Document actualDocument = dataUtil.load(inputStream, "InvalidCharset", null);

        assertEquals(Parser.htmlParser().parseInput(inputStreamContent), actualDocument);
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullInputStream() {
        Document actualDocument = dataUtil.load(null, "UTF-8", null);
        // No need to call inputStream.close() because it's null
    }

    // Test for parseByteData(ByteBuffer, String, String, Parser) method

    @Test
    public void testParseByteDataWithValidArguments() {
        String byteDataContent = "<html><head></head><body><p>This is a test byte buffer.</p></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(byteDataContent.getBytes(Charset.forName("UTF-8")));

        Document expectedDocument = parser.parseInput(byteDataContent);
        Document actualDocument = dataUtil.parseByteData(byteData, "UTF-8", null, parser);

        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataWithNullCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("This is a test byte buffer.".getBytes());
        Document actualDocument = dataUtil.parseByteData(byteData, null, null, parser);

        assertEquals(Parser.htmlParser().parseInput(""), actualDocument);
    }

    @Test
    public void testParseByteDataWithEmptyCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("This is a test byte buffer.".getBytes());
        Document actualDocument = dataUtil.parseByteData(byteData, "", null, parser);

        assertEquals(Parser.htmlParser().parseInput(""), actualDocument);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("This is a test byte buffer.".getBytes());
        Document actualDocument = dataUtil.parseByteData(byteData, "InvalidCharset", null, parser);

        assertEquals(Parser.htmlParser().parseInput(""), actualDocument);
    }

    @Test
    public void testParseByteDataWithBaseUri() {
        String byteDataContent = "<html><head><base href='/test'></head><body><p>This is a test byte buffer.</p></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(byteDataContent.getBytes(Charset.forName("UTF-8")));

        Document expectedDocument = parser.parseInput(byteDataContent);
        String baseUri = "/test";
        Document actualDocument = dataUtil.parseByteData(byteData, "UTF-8", baseUri, parser);

        assertEquals(expectedDocument, actualDocument);
    }

    // Test for readToByteBuffer(InputStream, int) method

    @Test
    public void testReadToByteBufferWithValidArguments() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("This is a test input stream".getBytes());
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap("This is a test input stream".getBytes());
        ByteBuffer actualByteBuffer = dataUtil.readToByteBuffer(inputStream, 0);

        assertEquals(expectedByteBuffer, actualByteBuffer);
        inputStream.close();
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("This is a test input stream".getBytes());
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap("This is a test input stream".getBytes());
        int maxSize = 10;
        ByteBuffer actualByteBuffer = dataUtil.readToByteBuffer(inputStream, maxSize);

        assertEquals(expectedByteBuffer, actualByteBuffer);
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithInvalidInputStream() throws IOException {
        ByteBuffer actualByteBuffer = dataUtil.readToByteBuffer(null, 0);
    }

    // Test for readToByteBuffer(InputStream) method

    @Test
    public void testReadToByteBufferWithValidArguments() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("This is a test input stream".getBytes());
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap("This is a test input stream".getBytes());
        ByteBuffer actualByteBuffer = dataUtil.readToByteBuffer(inputStream);

        assertEquals(expectedByteBuffer, actualByteBuffer);
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithInvalidInputStream() {
        ByteBuffer actualByteBuffer = dataUtil.readToByteBuffer(null);
    }

    // Test for getCharsetFromContentType(String) method

    @Test
    public void testGetCharsetFromContentTypeWithValidContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String actualCharset = dataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidContentType() {
        String contentType = "text/html; charset=InvalidCharset";
        String actualCharset = dataUtil.getCharsetFromContentType(contentType);

        assertEquals(null, actualCharset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithMissingCharset() {
        String contentType = "text/html";
        String actualCharset = dataUtil.getCharsetFromContentType(contentType);

        assertEquals(null, actualCharset);
    }
}