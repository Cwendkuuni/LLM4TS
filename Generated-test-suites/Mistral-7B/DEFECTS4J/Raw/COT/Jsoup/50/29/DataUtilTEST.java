package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tests for DataUtil class
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("test.html"); // Replace with actual file path
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        String htmlData = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlData.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadInputStreamWithCustomParser() {
        String htmlData = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlData.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = org.jsoup.parser.Jsoup.xmlParser();
        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testCrossStreams() {
        File inputFile = new File("testInput.txt");
        File outputFile = new File("testOutput.txt");
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(inputFile);
            outputStream = new FileOutputStream(outputFile);
            DataUtil.crossStreams(inputStream, outputStream);
            assertEquals(inputFile.length(), outputFile.length());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String htmlData = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlData.getBytes(Charset.forName("UTF-8")));
        String charsetName = null;
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String htmlData = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlData.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testParseByteDataWithMimeBoundary() {
        String htmlData = "--MyBoundary\r\nContent-Type: text/html\r\nContent-Disposition: attachment; filename=\"test.html\"\r\n--MyBoundary--\r\n<html><body><h1>Hello World</h1></body></html>\r\n--MyBoundary--";
        ByteBuffer byteData = ByteBuffer.wrap(htmlData.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=MyCustomCharset";
        String expectedCharset = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        String expectedBoundary = new String(DataUtil.mimeBoundaryChars, 0, DataUtil.boundaryLength);
        assertEquals(expectedBoundary, boundary);
    }
}