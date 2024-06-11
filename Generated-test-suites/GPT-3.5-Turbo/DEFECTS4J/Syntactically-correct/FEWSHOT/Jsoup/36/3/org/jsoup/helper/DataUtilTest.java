package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        // Test 1 - Load file with specified charset
        File file1 = new File("file1.txt");
        String charsetName1 = "UTF-8";
        String baseUri1 = "http://www.example.com";
        Document document1 = DataUtil.load(file1, charsetName1, baseUri1);
        assertEquals("Document loaded from file1.txt with specified charset", document1.title(), "File 1");

        // Test 2 - Load file with default charset
        File file2 = new File("file2.txt");
        String charsetName2 = null;
        String baseUri2 = "http://www.example.com";
        Document document2 = DataUtil.load(file2, charsetName2, baseUri2);
        assertEquals("Document loaded from file2.txt with default charset", document2.title(), "File 2");
    }

    @Test
    public void testLoadInputStream() throws IOException {
        // Test 1 - Load input stream with specified charset
        byte[] inputBytes1 = "Input 1".getBytes(Charset.forName("UTF-8"));
        InputStream inputStream1 = new ByteArrayInputStream(inputBytes1);
        String charsetName1 = "UTF-8";
        String baseUri1 = "http://www.example.com";
        Document document1 = DataUtil.load(inputStream1, charsetName1, baseUri1);
        assertEquals("Document loaded from input stream with specified charset", document1.title(), "Input 1");

        // Test 2 - Load input stream with default charset
        byte[] inputBytes2 = "Input 2".getBytes(Charset.forName("UTF-8"));
        InputStream inputStream2 = new ByteArrayInputStream(inputBytes2);
        String charsetName2 = null;
        String baseUri2 = "http://www.example.com";
        Document document2 = DataUtil.load(inputStream2, charsetName2, baseUri2);
        assertEquals("Document loaded from input stream with default charset", document2.title(), "Input 2");
    }

    @Test
    public void testLoadParser() throws IOException {
        // Test 1 - Load input stream with specified charset and custom parser
        byte[] inputBytes1 = "Input 1".getBytes(Charset.forName("UTF-8"));
        InputStream inputStream1 = new ByteArrayInputStream(inputBytes1);
        String charsetName1 = "UTF-8";
        String baseUri1 = "http://www.example.com";
        Parser parser1 = Parser.htmlParser();
        Document document1 = DataUtil.load(inputStream1, charsetName1, baseUri1, parser1);
        assertEquals("Document loaded from input stream with specified charset and custom parser", document1.title(), "Input 1");

        // Test 2 - Load input stream with default charset and custom parser
        byte[] inputBytes2 = "Input 2".getBytes(Charset.forName("UTF-8"));
        InputStream inputStream2 = new ByteArrayInputStream(inputBytes2);
        String charsetName2 = null;
        String baseUri2 = "http://www.example.com";
        Parser parser2 = Parser.htmlParser();
        Document document2 = DataUtil.load(inputStream2, charsetName2, baseUri2, parser2);
        assertEquals("Document loaded from input stream with default charset and custom parser", document2.title(), "Input 2");
    }

    @Test
    public void testParseByteData() {
        // Test 1 - Parse byte data with specified charset
        String docData1 = "<html><head><title>Byte Data 1</title></head><body></body></html>";
        String charsetName1 = "UTF-8";
        String baseUri1 = "http://www.example.com";
        Parser parser1 = Parser.htmlParser();
        ByteBuffer byteData1 = ByteBuffer.wrap(docData1.getBytes(Charset.forName(charsetName1)));
        Document document1 = DataUtil.parseByteData(byteData1, charsetName1, baseUri1, parser1);
        assertEquals("Document parsed from byte data with specified charset", document1.title(), "Byte Data 1");

        // Test 2 - Parse byte data with null charset
        String docData2 = "<html><head><title>Byte Data 2</title></head><body></body></html>";
        String charsetName2 = null;
        String baseUri2 = "http://www.example.com";
        Parser parser2 = Parser.htmlParser();
        ByteBuffer byteData2 = ByteBuffer.wrap(docData2.getBytes(Charset.forName("UTF-8")));
        Document document2 = DataUtil.parseByteData(byteData2, charsetName2, baseUri2, parser2);
        assertEquals("Document parsed from byte data with null charset", document2.title(), "Byte Data 2");
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Test 1 - Read input stream to byte buffer without size limit
        byte[] inputBytes1 = "Input 1".getBytes(Charset.forName("UTF-8"));
        InputStream inputStream1 = new ByteArrayInputStream(inputBytes1);
        ByteBuffer byteBuffer1 = DataUtil.readToByteBuffer(inputStream1);
        assertEquals("Byte buffer filled with input stream without size limit", byteBuffer1.array(), inputBytes1);

        // Test 2 - Read input stream to byte buffer with size limit
        byte[] inputBytes2 = "Input 2".getBytes(Charset.forName("UTF-8"));
        InputStream inputStream2 = new ByteArrayInputStream(inputBytes2);
        int maxSize2 = inputBytes2.length;
        ByteBuffer byteBuffer2 = DataUtil.readToByteBuffer(inputStream2, maxSize2);
        assertEquals("Byte buffer filled with input stream with size limit", byteBuffer2.array(), inputBytes2);
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test 1 - Content type with charset
        String contentType1 = "text/html; charset=UTF-8";
        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        assertEquals("Charset extracted from content type with charset", charset1, "UTF-8");

        // Test 2 - Content type without charset
        String contentType2 = "text/html";
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        assertNull("Charset not found in content type without charset", charset2);

        // Test 3 - Content type with unsupported charset
        String contentType3 = "text/html; charset=invalid";
        String charset3 = DataUtil.getCharsetFromContentType(contentType3);
        assertNull("Charset not found in content type with unsupported charset", charset3);
    }
}