package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException{
        File testFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document doc = DataUtil.load(testFile, charsetName, baseUri);

        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException{
        String testData = "<div>Test</div>";
        InputStream testInputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document doc = DataUtil.load(testInputStream, charsetName, baseUri);

        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException{
        String testData = "<div>Test</div>";
        InputStream testInputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(testInputStream, charsetName, baseUri, parser);

        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
    }

    @Test
    public void testCrossStreams() throws IOException{
        byte[] testData = "Hello World".getBytes();
        InputStream in = new ByteArrayInputStream(testData);
        OutputStream out = new ByteArrayOutputStream();

        DataUtil.crossStreams(in, out);

        assertEquals("Hello World", out.toString());
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("<div>Test</div>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException{
        String testData = "Hello World";
        InputStream inStream = new ByteArrayInputStream(testData.getBytes());
        int maxSize = 5;
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream, maxSize);

        assertNotNull(byteBuffer);
        assertEquals(maxSize, byteBuffer.capacity());
        assertEquals("Hello", Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    @Test
    public void testReadToByteBuffer() throws IOException{
        String testData = "Hello World";
        InputStream inStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream);

        assertNotNull(byteBuffer);
        assertEquals(testData.length(), byteBuffer.capacity());
        assertEquals(testData, Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException{
        File testFile = new File("test.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);

        assertNotNull(byteBuffer);
        assertEquals(testFile.length(), byteBuffer.capacity());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);

        assertNotNull(validatedCharset);
        assertEquals(charset, validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap("Hello World".getBytes());
        String charsetName = "UTF-8";
        String detectedCharset = DataUtil.detectCharsetFromBom(byteData, charsetName);

        assertNotNull(detectedCharset);
        assertEquals(charsetName, detectedCharset);
    }
}