package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * DataUtilTest
 */
public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException, HelperException {
        String charset = "UTF-8";
        File file = new File("testFile.html"); // Replace with the actual file path
        Document document = DataUtil.load(file, charset, null);
        assertNotNull(document);
        assertEquals(charset, document.outputSettings().charset());
    }

    @Test
    public void testLoadFileWithoutCharset() throws IOException, HelperException {
        File file = new File("testFile.html"); // Replace with the actual file path
        Document document = DataUtil.load(file, null, null);
        assertNotNull(document);
        assertNull(document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException, HelperException {
        String charset = "UTF-8";
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, charset, null);
        assertNotNull(document);
        assertEquals(charset, document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithoutCharset() throws IOException, HelperException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, null);
        assertNotNull(document);
        assertNull(document.outputSettings().charset());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException, HelperException {
        String charset = "InvalidCharset";
        byte[] inputData = "Test data".getBytes(); // Replace with actual test data
        InputStream inputStream = new ByteArrayInputStream(inputData);
        try {
            DataUtil.parseInputStream(inputStream, charset, null, Parser.htmlParser());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid charset name: InvalidCharset", e.getMessage());
        }
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException, HelperException {
        String charset = "UTF-8";
        byte[] inputData = "Test data".getBytes(StandardCharsets.UTF_8); // Replace with actual test data
        InputStream inputStream = new ByteArrayInputStream(inputData);
        Document document = DataUtil.parseInputStream(inputStream, charset, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(charset, document.outputSettings().charset());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testData = "Test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        byte[] expectedData = testData.getBytes();
        byte[] outputData = outputStream.toByteArray();
        assertArrayEquals(expectedData, outputData);
    }
}