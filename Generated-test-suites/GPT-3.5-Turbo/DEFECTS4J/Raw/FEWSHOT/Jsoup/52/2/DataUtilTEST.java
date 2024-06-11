
package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataUtilTest {
    private static final String TEST_FILE_PATH = "test.txt";
    private static final String TEST_FILE_CONTENT = "Test content";

    @BeforeClass
    public static void setup() throws IOException {
        // Create a test file with content
        Path path = Path.of(TEST_FILE_PATH);
        Files.writeString(path, TEST_FILE_CONTENT, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
    }
    
    @Test
    public void testLoad_withFile() throws IOException {
        File file = new File(TEST_FILE_PATH);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(TEST_FILE_CONTENT, document.text());
    }
    
    @Test
    public void testLoad_withInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_FILE_CONTENT.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(TEST_FILE_CONTENT, document.text());
    }
    
    @Test
    public void testLoad_withParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_FILE_CONTENT.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(TEST_FILE_CONTENT, document.text());
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        String input = "Test input string";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        DataUtil.crossStreams(inputStream, outputStream);
        
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertEquals(input, output);
    }
    
    // TODO: Add more test cases for other methods in DataUtil class

    @Test
    public void testGetCharsetFromContentType_withValidContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentType_withInvalidContentType() {
        String contentType = "text/html; charset=InvalidCharset";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNull(charset);
    }
    
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }
}
