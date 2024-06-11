package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        String filePath = "test_file.html";
        File file = new File(filePath);
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamNull() {
        InputStream inputStream = null;
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";

        assertThrows(IOException.class, () -> DataUtil.load(inputStream, charsetName, baseUri));
    }

    @Test
    public void testLoadInputStreamEmpty() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamNoCharset() {
        String fileContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        String charsetName = null;
        String baseUri = "https://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamInvalidCharset() {
        String fileContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        String charsetName = "unsupported_charset";
        String baseUri = "https://example.com";

        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, charsetName, baseUri));
    }

    @Test
    public void testLoadInputStreamValidCharset() {
        String fileContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String fileContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String fileContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "unsupported_charset";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();

        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser));
    }

    @Test
    public void testParseInputStreamWithEmptyBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "This is a test.";
        String outputContent = "Copied test data.";

        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] outputBytes = outputStream.toByteArray();
        assertEquals(outputContent, new String(outputBytes, StandardCharsets.UTF_8));
    }
}