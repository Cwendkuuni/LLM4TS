package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoad() throws IOException {
        // Test Case 1: Load file with valid charset
        File file1 = new File("testFile.html");
        String charsetName1 = "UTF-8";
        String baseUri1 = "http://www.example.com";
        Document doc1 = DataUtil.load(file1, charsetName1, baseUri1);
        assertNotNull(doc1);

        // Test Case 2: Load file with invalid charset
        File file2 = new File("testFile.html");
        String charsetName2 = "invalidCharset";
        String baseUri2 = "http://www.example.com";
        try {
            Document doc2 = DataUtil.load(file2, charsetName2, baseUri2);
            fail("IOException expected");
        } catch (IOException e) {
            assertEquals("Must set charset arg to character set of file to parse. Set to null to attempt to detect from HTML", e.getMessage());
        }
    }

    @Test
    public void testLoadWithInputStream() throws IOException {
        // Test Case 1: Load input stream with valid charset
        InputStream in1 = new FileInputStream("testFile.html");
        String charsetName1 = "UTF-8";
        String baseUri1 = "http://www.example.com";
        Document doc1 = DataUtil.load(in1, charsetName1, baseUri1);
        assertNotNull(doc1);

        // Test Case 2: Load input stream with invalid charset
        InputStream in2 = new FileInputStream("testFile.html");
        String charsetName2 = "invalidCharset";
        String baseUri2 = "http://www.example.com";
        try {
            Document doc2 = DataUtil.load(in2, charsetName2, baseUri2);
            fail("IOException expected");
        } catch (IOException e) {
            assertEquals("Must set charset arg to character set of file to parse. Set to null to attempt to detect from HTML", e.getMessage());
        }
    }

    @Test
    public void testLoadWithParser() throws IOException {
        // Test Case 1: Load input stream with valid charset and custom parser
        InputStream in1 = new FileInputStream("testFile.html");
        String charsetName1 = "UTF-8";
        String baseUri1 = "http://www.example.com";
        Parser parser1 = Parser.htmlParser();
        Document doc1 = DataUtil.load(in1, charsetName1, baseUri1, parser1);
        assertNotNull(doc1);

        // Test Case 2: Load input stream with invalid charset and custom parser
        InputStream in2 = new FileInputStream("testFile.html");
        String charsetName2 = "invalidCharset";
        String baseUri2 = "http://www.example.com";
        Parser parser2 = Parser.htmlParser();
        try {
            Document doc2 = DataUtil.load(in2, charsetName2, baseUri2, parser2);
            fail("IOException expected");
        } catch (IOException e) {
            assertEquals("Must set charset arg to character set of file to parse. Set to null to attempt to detect from HTML", e.getMessage());
        }
    }

    @Test
    public void testParseByteData() {
        // Test Case 1: Parse byte data with charset null
        ByteBuffer byteData1 = ByteBuffer.allocate(10);
        String charsetName1 = null;
        String baseUri1 = "http://www.example.com";
        Parser parser1 = Parser.htmlParser();
        Document doc1 = DataUtil.parseByteData(byteData1, charsetName1, baseUri1, parser1);
        assertNotNull(doc1);

        // Test Case 2: Parse byte data with specified charset
        ByteBuffer byteData2 = ByteBuffer.allocate(10);
        String charsetName2 = "UTF-8";
        String baseUri2 = "http://www.example.com";
        Parser parser2 = Parser.htmlParser();
        Document doc2 = DataUtil.parseByteData(byteData2, charsetName2, baseUri2, parser2);
        assertNotNull(doc2);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Test Case 1: Read input stream to byte buffer
        InputStream in1 = new FileInputStream("testFile.html");
        ByteBuffer byteData1 = DataUtil.readToByteBuffer(in1);
        assertNotNull(byteData1);

        // Test Case 2: Read input stream with no data to byte buffer
        InputStream in2 = new FileInputStream("emptyFile.html");
        ByteBuffer byteData2 = DataUtil.readToByteBuffer(in2);
        assertNotNull(byteData2);
        assertEquals(0, byteData2.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test Case 1: Get charset from content type header with valid input
        String contentType1 = "text/html; charset=UTF-8";
        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        assertEquals("UTF-8", charset1);

        // Test Case 2: Get charset from content type header with invalid input
        String contentType2 = "text/html";
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        assertNull(charset2);
    }
}