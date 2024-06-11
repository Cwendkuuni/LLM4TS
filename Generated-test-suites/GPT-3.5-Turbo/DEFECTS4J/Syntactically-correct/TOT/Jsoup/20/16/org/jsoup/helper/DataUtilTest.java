import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

public class DataUtilTest {

    private File htmlFile;
    private String htmlFilePath;
    private String charsetName;
    private String baseUri;

    @Before
    public void setUp() throws Exception {
        htmlFile = new File("test.html");
        htmlFilePath = htmlFile.getAbsolutePath();
        charsetName = "UTF-8";
        baseUri = "http://example.com";
    }

    @After
    public void tearDown() throws Exception {
        htmlFile.delete();
    }

    @Test
    public void testLoadFile() throws IOException {
        // Create a test HTML file
        FileWriter writer = new FileWriter(htmlFile);
        writer.write("<html><head><title>Test HTML</title></head><body><h1>Hello, world!</h1></body></html>");
        writer.close();

        // Test loading the file and verifying its content
        Document document = DataUtil.load(htmlFile, charsetName, baseUri);
        assertNotNull(document);
        assertEquals("<html><head><title>Test HTML</title></head><body><h1>Hello, world!</h1></body></html>", document.html());
    }

    @Test
    public void testLoadFileInvalidCharset() throws IOException {
        // Create a test HTML file with a specific charset
        FileWriter writer = new FileWriter(htmlFile);
        writer.write("<html><head><title>Test HTML</title></head><body><h1>Hello, world!</h1></body></html>");
        writer.close();

        // Test loading the file with an invalid charset and expecting the default charset to be used
        String invalidCharset = "InvalidCharset";
        Document document = DataUtil.load(htmlFile, invalidCharset, baseUri);
        assertNotNull(document);
        assertEquals("<html><head><title>Test HTML</title></head><body><h1>Hello, world!</h1></body></html>", document.html());
    }

    @Test
    public void testLoadFileIOException() {
        // Test loading a non-existing file and expecting an IOException
        File nonExistingFile = new File("non-existing.html");
        assertThrows(IOException.class, () -> DataUtil.load(nonExistingFile, charsetName, baseUri));
    }

    @Test
    public void testLoadInputStream() throws IOException {
        // Create a test HTML file
        FileWriter writer = new FileWriter(htmlFile);
        writer.write("<html><head><title>Test HTML</title></head><body><h1>Hello, world!</h1></body></html>");
        writer.close();

        // Read the test HTML file into an input stream
        FileInputStream inputStream = new FileInputStream(htmlFile);

        // Test loading the input stream and verifying its content
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
        assertEquals("<html><head><title>Test HTML</title></head><body><h1>Hello, world!</h1></body></html>", document.html());

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamInvalidCharset() throws IOException {
        // Create a test HTML file with a specific charset
        FileWriter writer = new FileWriter(htmlFile);
        writer.write("<html><head><title>Test HTML</title></head><body><h1>Hello, world!</h1></body></html>");
        writer.close();

        // Read the test HTML file into an input stream
        FileInputStream inputStream = new FileInputStream(htmlFile);

        // Test loading the input stream with an invalid charset and expecting the default charset to be used
        String invalidCharset = "InvalidCharset";
        Document document = DataUtil.load(inputStream, invalidCharset, baseUri);
        assertNotNull(document);
        assertEquals("<html><head><title>Test HTML</title></head><body><h1>Hello, world!</h1></body></html>", document.html());

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamIOException() {
        // Test loading a non-existing input stream and expecting an IOException
        InputStream nonExistingInputStream = new FileInputStream("non-existing.html");
        assertThrows(IOException.class, () -> DataUtil.load(nonExistingInputStream, charsetName, baseUri));
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        // Create a test HTML file
        FileWriter writer = new FileWriter(htmlFile);
        writer.write("<html><head><title>Test HTML</title></head><body><h1>Hello, world!</h1></body></html>");
        writer.close();

        // Read the test HTML file into an input stream
        FileInputStream inputStream = new FileInputStream(htmlFile);

        // Test loading the input stream with a custom parser and verifying its content
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals("<html><head><title>Test HTML</title></head><body><h1>Hello, world!</h1></body></html>", document.html());

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParserInvalidCharset() throws IOException {
        // Create a test HTML file with a specific charset
        FileWriter writer = new FileWriter(htmlFile);
        writer.write("<html><head><title>Test HTML</title></head><body><h1>Hello, world!</h1></body></html>");
        writer.close();

        // Read the test HTML file into an input stream
        FileInputStream inputStream = new FileInputStream(htmlFile);

        // Test loading the input stream with a custom parser and an invalid charset and expecting the default charset to be used
        Parser parser = Parser.htmlParser();
        String invalidCharset = "InvalidCharset";
        Document document = DataUtil.load(inputStream, invalidCharset, baseUri, parser);
        assertNotNull(document);
        assertEquals("<html><head><title>Test HTML</title></head><body><h1>Hello, world!</h1></body></html>", document.html());

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParserIOException() {
        // Test loading a non-existing input stream with a custom parser and expecting an IOException
        Parser parser = Parser.htmlParser();
        InputStream nonExistingInputStream = new FileInputStream("non-existing.html");
        assertThrows(IOException.class, () -> DataUtil.load(nonExistingInputStream, charsetName, baseUri, parser));
    }

    @Test
    public void testParseByteData() {
        // Test parsing byte data with a specific charset and expecting a valid document
        ByteBuffer byteData = ByteBuffer.wrap("<html><head><title>Test HTML</title></head><body><h1>Hello, world!</h1></body></html>".getBytes());
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals("<html><head><title>Test HTML</title></head><body><h1>Hello, world!</h1></body></html>", document.html());
    }

    @Test
    public void testParseByteDataNullCharset() {
        // Test parsing byte data with a null charset and expecting automatic charset detection
        ByteBuffer byteData = ByteBuffer.wrap(("<html><head><meta charset=\"" + charsetName + "\"></head><body><h1>Hello, world!</h1></body></html>").getBytes());
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertNotNull(document);
        assertEquals("<html><head><meta charset=\"" + charsetName + "\"></head><body><h1>Hello, world!</h1></body></html>", document.html());
    }

    @Test
    public void testParseByteDataChangedCharset() {
        // Test parsing byte data with a changed charset midstream and expecting correct re-parsing
        ByteBuffer byteData = ByteBuffer.wrap(("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=gb2312\"></head><body><h1>Hello, world!</h1></body></html>").getBytes());
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertNotNull(document);
        assertEquals("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=gb2312\"></head><body><h1>Hello, world!</h1></body></html>", document.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Test reading input stream to byte buffer and verifying its content
        FileInputStream inputStream = new FileInputStream(htmlFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        byte[] expectedData = "<html><head><title>Test HTML</title></head><body><h1>Hello, world!</h1></body></html>".getBytes();
        assertArrayEquals(expectedData, byteData.array());
        inputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test getting charset from a content type header and expecting correct charset extraction
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNullHeader() {
        // Test getting charset from a null content type header and expecting null result
        String charset = DataUtil.getCharsetFromContentType(null);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNoCharset() {
        // Test getting charset from a content type header that does not contain a charset and expecting null result
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeInvalidHeader() {
        // Test getting charset from an invalid content type header and expecting null result
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}