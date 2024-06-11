package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * Test class for DataUtil class.
 */
public class DataUtilTest {

    private static ByteArrayOutputStream outputStream;
    private static InputStream inputStream;
    private static Document document;

    @BeforeClass
    public static void setUp() {
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    }

    @Test
    public void testLoadFile() {
        File file = new File("testFile.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        try {
            document = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        String testData = "<html><body><h1>Test</h1></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);
        writer.print(testData);
        writer.close();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        try {
            document = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        String testData = "<html><body><h1>Test</h1></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);
        writer.print(testData);
        writer.close();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        try {
            document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testCrossStreams() {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        String expected = "Test";
        String actual = new String(outputStream.toByteArray());

        assertEquals(expected, actual);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String testData = "<html><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));

        try {
            document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String charsetName = "UTF-8";
        String testData = "<html><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));

        try {
            document = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        String expected = "Test";
        String actual = StandardCharsets.UTF_8.decode(byteBuffer).toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("testFile.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        String expected = "Test content of the file";
        String actual = StandardCharsets.UTF_8.decode(byteBuffer).toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertFalse(byteBuffer.hasRemaining());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expected = "UTF-8";
        String actual = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetCharsetFromContentTypeInvalidCharset() {
        String contentType = "text/html; charset=invalidContentType";
        String expected = null;
        String actual = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expected, actual);
    }

    @Test
    public void testMimeBoundary() {
        String expected = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_-1234567890";
        String actual = DataUtil.mimeBoundary();

        assertEquals(expected, actual);
    }
}