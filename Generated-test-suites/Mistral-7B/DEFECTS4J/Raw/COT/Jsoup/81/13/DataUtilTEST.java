package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {
    

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#parseInputStream(InputStream, String, String, Parser)}.
     * @throws IOException on IO error
     */
    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#crossStreams(InputStream, OutputStream)}.
     * @throws IOException on IO error
     */
    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "Test input data.";
        String outputData = "Test output data.";
        byte[] inputBytes = inputData.getBytes(StandardCharsets.UTF_8);
        byte[] outputBytes = outputData.getBytes(StandardCharsets.UTF_8);

        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] readOutputBytes = outputStream.toByteArray();

        Assert.assertArrayEquals(outputBytes, readOutputBytes);
    }

    /**
     * Test method for edge case with invalid charset name.
     * @throws IOException on IO error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLoadWithInvalidCharsetName() throws IOException {
        File file = new File("test.html");
        String charsetName = "invalid_charset";
        String baseUri = "http://www.example.com";
        DataUtil.load(file, charsetName, baseUri);
    }

    /**
     * Test method for edge case with null baseUri.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadWithNullBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = null;
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNull(document);
    }

    
}