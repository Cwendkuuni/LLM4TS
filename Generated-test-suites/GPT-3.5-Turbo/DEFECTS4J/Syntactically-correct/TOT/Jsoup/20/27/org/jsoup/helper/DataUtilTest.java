
package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class DataUtilTest {
    private static Document document;

    @BeforeClass
    public static void setUp() throws IOException {
        ClassLoader classLoader = DataUtilTest.class.getClassLoader();
        File file = new File(classLoader.getResource("test.html").getFile());
        document = DataUtil.load(file, null, "http://example.com");
    }

    @Test
    public void load_withValidFile_shouldReturnDocumentObject() throws IOException {
        File file = new File("path/to/validFile");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = DataUtil.load(file, charsetName, baseUri);
        
        assertNotNull(document);
    }

    @Test(expected = FileNotFoundException.class)
    public void load_withInvalidFile_shouldThrowFileNotFoundException() throws IOException {
        File file = new File("path/to/invalidFile");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void load_withInputStream_shouldReturnDocumentObject() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/validFile");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void load_withInvalidInputStream_shouldThrowIOException() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/invalidFile");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
    }
    
    @Test
    public void load_withCustomParser_shouldReturnDocumentObject() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/validFile");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertNotNull(document);
    }

    @Test
    public void parseByteData_withValidData_shouldReturnDocumentObject() {
        String data = "<html><head></head><body><p>Hello, World!</p></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(data.getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
    }

    @Test
    public void parseByteData_withInvalidData_shouldReturnNullDocumentObject() {
        String data = "<html><head></head><body><p>Hello, World!</p></body>";
        ByteBuffer byteData = ByteBuffer.wrap(data.getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNull(document);
    }

    @Test
    public void readToByteBuffer_withValidInputStream_shouldReturnByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/validFile");

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        assertNotNull(byteData);
    }

    @Test(expected = IOException.class)
    public void readToByteBuffer_withInvalidInputStream_shouldThrowIOException() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/invalidFile");

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
    }

    @Test
    public void getCharsetFromContentType_withValidContentType_shouldReturnCharset() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }

    @Test
    public void getCharsetFromContentType_withInvalidContentType_shouldReturnNull() {
        String contentType = "text/html";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }
}
