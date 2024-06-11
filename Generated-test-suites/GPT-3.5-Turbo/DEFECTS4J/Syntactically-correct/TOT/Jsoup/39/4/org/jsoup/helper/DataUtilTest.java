package org.jsoup.helper;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Unit tests for DataUtil
 */
public class DataUtilTest {

    private File testFile;

    @Before
    public void setUp() throws Exception {
        // Create a test file
        String testHtml = "<html><head><title>Test HTML</title></head><body><h1>Hello World!</h1></body></html>";
        testFile = File.createTempFile("testFile", ".html");
        BufferedWriter writer = new BufferedWriter(new FileWriter(testFile));
        writer.write(testHtml);
        writer.close();
    }

    @After
    public void tearDown() {
        // Delete the test file
        if(testFile.exists()){
            testFile.delete();
        }
    }

    @Test
    public void testLoadFromFile() throws IOException {
        Document document = DataUtil.load(testFile, null, "");

        assertNotNull(document);
        assertEquals("Test HTML", document.title());
        assertEquals("<h1>Hello World!</h1>", document.body().html());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        FileInputStream inStream = new FileInputStream(testFile);
        Document document = DataUtil.load(inStream, null, "");

        assertNotNull(document);
        assertEquals("Test HTML", document.title());
        assertEquals("<h1>Hello World!</h1>", document.body().html());

        inStream.close();
    }

    @Test
    public void testLoadFromInputStreamWithCharset() throws IOException {
        FileInputStream inStream = new FileInputStream(testFile);
        Document document = DataUtil.load(inStream, "UTF-8", "");

        assertNotNull(document);
        assertEquals("Test HTML", document.title());
        assertEquals("<h1>Hello World!</h1>", document.body().html());

        inStream.close();
    }

    @Test
    public void testLoadFromInputStreamWithCharsetAndParser() throws IOException {
        FileInputStream inStream = new FileInputStream(testFile);
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.load(inStream, "UTF-8", "", parser);

        assertNotNull(document);
        assertEquals("Test HTML", document.title());
        assertEquals("<h1>Hello World!</h1>", document.body().html());

        inStream.close();
    }

    @Test
    public void testParseByteDataWithCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("<h1>Hello World!</h1>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals("Hello World!", document.body().text());
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("<h1>Hello World!</h1>".getBytes());
        String charsetName = null;
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals("Hello World!", document.body().text());
    }

    @Test
    public void testParseByteDataWithMetaCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("<meta charset=\"UTF-8\"><h1>Hello World!</h1>".getBytes());
        String charsetName = null;
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals("Hello World!", document.body().text());
    }

    @Test
    public void testParseByteDataWithHttpEquivCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"><h1>Hello World!</h1>".getBytes());
        String charsetName = null;
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals("Hello World!", document.body().text());
    }

    @Test
    public void testReadToByteBufferWithUnlimitedSize() throws IOException {
        FileInputStream inStream = new FileInputStream(testFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream, 0);

        assertEquals(testFile.length(), byteData.limit());

        inStream.close();
    }

    @Test
    public void testReadToByteBufferWithCappedSize() throws IOException {
        FileInputStream inStream = new FileInputStream(testFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream, 100);

        assertEquals(100, byteData.limit());

        inStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadToByteBufferWithNegativeMaxSize() throws IOException {
        FileInputStream inStream = new FileInputStream(testFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream, -1);

        inStream.close();
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String contentType = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidContentType() {
        String contentType = "text/html;charset=gb2312";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithValidContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithUppercaseCharset() {
        String contentType = "text/html; charset=ISO-8859-1";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("ISO-8859-1", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=invalid-charset";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }
}