package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String testFilePath = "path/to/your/test/file.html"; // Replace with the path to your test file
        File testFile = new File(testFilePath);
        // Test with default charset
        Document document = DataUtil.load(testFile, null);
        assertNotNull(document);
        
        // Test with specific charset
        String charsetName = "UTF-16"; // Replace with the charset you want to test
        Document documentWithCharset = DataUtil.load(testFile, charsetName);
        assertNotNull(documentWithCharset);
        assertEquals(charsetName, documentWithCharset.outputSettings().charset());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testContent = "Test content"; // Replace with the content you want to test
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testContent.getBytes());
        
        // Test with default charset
        Document document = DataUtil.load(testInputStream, null);
        assertNotNull(document);

        // Test with specific charset
        String charsetName = "UTF-16"; // Replace with the charset you want to test
        Document documentWithCharset = DataUtil.load(testInputStream, charsetName);
        assertNotNull(documentWithCharset);
        assertEquals(charsetName, documentWithCharset.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testContent = "Test content"; // Replace with the content you want to test
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testContent.getBytes());
        Parser parser = Parser.xmlParser(); // Use the parser you want to test

        // Test with default charset
        Document document = DataUtil.load(testInputStream, null, null, parser);
        assertNotNull(document);

        // Test with specific charset
        String charsetName = "UTF-16"; // Replace with the charset you want to test
        Document documentWithCharset = DataUtil.load(testInputStream, charsetName, null, parser);
        assertNotNull(documentWithCharset);
        assertEquals(charsetName, documentWithCharset.outputSettings().charset());
    }

    @Test
    public void testParseByteData() throws IOException {
        String testContent = "Test content"; // Replace with the content you want to test
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testContent.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);
        String charsetName = "UTF-16"; // Replace with the charset you want to test

        Document document = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(charsetName, document.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testContent = "Test content"; // Replace with the content you want to test
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testContent.getBytes());

        ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);
        assertEquals(testContent.getBytes().length, byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);

        contentType = "text/html"; // No charset specified
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}