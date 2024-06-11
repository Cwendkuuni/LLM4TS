import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class DataUtilTest {
    private File testFile;
    private String baseUri;
    private Parser parser;

    @Before
    public void setUp() {
        testFile = new File("test.html");
        baseUri = "https://example.com";
        parser = Parser.htmlParser();
    }

    @After
    public void tearDown() {
        testFile.delete();
    }

    @Test
    public void testLoadFromFile_NormalCharset() throws IOException {
        // Create a test HTML file with known content
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>");
        fileWriter.close();

        // Test loading the file with normal charset
        Document document = DataUtil.load(testFile, "UTF-8", baseUri);
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals("Hello, World!", document.select("h1").first().text());
    }

    @Test
    public void testLoadFromFile_NullCharset() throws IOException {
        // Create a test HTML file with known content
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>");
        fileWriter.close();

        // Test loading the file with null charset
        Document document = DataUtil.load(testFile, null, baseUri);
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals("Hello, World!", document.select("h1").first().text());
    }

    @Test
    public void testLoadFromFile_EmptyCharset() throws IOException {
        // Create a test HTML file with known content
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>");
        fileWriter.close();

        // Test loading the file with empty charset
        Document document = DataUtil.load(testFile, "", baseUri);
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals("Hello, World!", document.select("h1").first().text());
    }

    @Test
    public void testLoadFromInputStream_NormalCharset() throws IOException {
        // Create a test HTML file with known content
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>");
        fileWriter.close();

        // Read the test file into an input stream
        FileInputStream inputStream = new FileInputStream(testFile);

        // Test loading the input stream with normal charset
        Document document = DataUtil.load(inputStream, "UTF-8", baseUri);
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals("Hello, World!", document.select("h1").first().text());

        inputStream.close();
    }

    @Test
    public void testLoadFromInputStream_NullCharset() throws IOException {
        // Create a test HTML file with known content
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>");
        fileWriter.close();

        // Read the test file into an input stream
        FileInputStream inputStream = new FileInputStream(testFile);

        // Test loading the input stream with null charset
        Document document = DataUtil.load(inputStream, null, baseUri);
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals("Hello, World!", document.select("h1").first().text());

        inputStream.close();
    }

    @Test
    public void testLoadFromInputStream_EmptyCharset() throws IOException {
        // Create a test HTML file with known content
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>");
        fileWriter.close();

        // Read the test file into an input stream
        FileInputStream inputStream = new FileInputStream(testFile);

        // Test loading the input stream with empty charset
        Document document = DataUtil.load(inputStream, "", baseUri);
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals("Hello, World!", document.select("h1").first().text());

        inputStream.close();
    }

    @Test
    public void testLoadFromInputStream_CustomParser() throws IOException {
        // Create a test HTML file with known content
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>");
        fileWriter.close();

        // Read the test file into an input stream
        FileInputStream inputStream = new FileInputStream(testFile);

        // Create a custom parser
        Parser customParser = Parser.xmlParser();

        // Test loading the input stream with custom parser
        Document document = DataUtil.load(inputStream, "UTF-8", baseUri, customParser);
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals("Hello, World!", document.select("h1").first().text());

        inputStream.close();
    }

    @Test
    public void testParseByteData_NormalCharset() {
        // Prepare test data
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        ByteBuffer byteData = StandardCharsets.UTF_8.encode(html);
        String charsetName = "UTF-8";

        // Test parsing byte data with normal charset
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals("Hello, World!", document.select("h1").first().text());
    }

    @Test
    public void testParseByteData_NullCharset() {
        // Prepare test data
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        ByteBuffer byteData = StandardCharsets.UTF_8.encode(html);
        String charsetName = null;

        // Test parsing byte data with null charset
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals("Hello, World!", document.select("h1").first().text());
    }

    @Test
    public void testParseByteData_EmptyCharset() {
        // Prepare test data
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        ByteBuffer byteData = StandardCharsets.UTF_8.encode(html);
        String charsetName = "";

        // Test parsing byte data with empty charset
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals("Hello, World!", document.select("h1").first().text());
    }

    @Test
    public void testGetCharsetFromContentType_WithCharset() {
        // Prepare test data
        String contentType = "text/html; charset=EUC-JP";

        // Test getting charset from content type with specified charset
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("EUC-JP", charset);
    }

    @Test
    public void testGetCharsetFromContentType_NoCharset() {
        // Prepare test data
        String contentType = "text/html";

        // Test getting charset from content type without charset
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_InvalidCharset() {
        // Prepare test data
        String contentType = "text/html; charset=invalid";

        // Test getting charset from content type with invalid charset
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}