package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {

    private static final String TEST_FILE_PATH = "test_file.html";
    private static final String TEST_FILE_CONTENTS = "<html><head><title>Test File</title></head><body><h1>This is a test file</h1></body></html>";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "http://www.example.com";

    @Before
    public void setup() throws IOException {
        // Create a test file with some content
        Files.write(Paths.get(TEST_FILE_PATH), TEST_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File(TEST_FILE_PATH);
        Document document = DataUtil.load(testFile, CHARSET_NAME, BASE_URI);

        assertNotNull(document);
        assertEquals(TEST_FILE_CONTENTS, document.html());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(TEST_FILE_PATH);
        Document document = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI);

        assertNotNull(document);
        assertEquals(TEST_FILE_CONTENTS, document.html());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream(TEST_FILE_PATH);
        Document document = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());

        assertNotNull(document);
        assertEquals(TEST_FILE_CONTENTS, document.html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        String output = outputStream.toString();
        assertNotNull(output);
        assertEquals(TEST_FILE_CONTENTS, output);
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap(TEST_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, CHARSET_NAME, BASE_URI, Parser.htmlParser());

        assertNotNull(document);
        assertEquals(TEST_FILE_CONTENTS, document.html());
    }

    @Test
    public void testReadToByteBufferMaxSize() throws IOException {
        InputStream inputStream = new FileInputStream(TEST_FILE_PATH);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1000);

        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.capacity() <= 1000);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream(TEST_FILE_PATH);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.capacity() >= TEST_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8).length);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File testFile = new File(TEST_FILE_PATH);
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);

        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.capacity() >= TEST_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8).length);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html;charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test(expected = IOException.class)
    public void testLoadFileNonExistent() throws IOException {
        File nonExistentFile = new File("non_existent.html");
        DataUtil.load(nonExistentFile, CHARSET_NAME, BASE_URI);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamNonExistent() throws IOException {
        InputStream nonExistentInputStream = new FileInputStream("non_existent.html");
        DataUtil.load(nonExistentInputStream, CHARSET_NAME, BASE_URI);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithParserNonExistent() throws IOException {
        InputStream nonExistentInputStream = new FileInputStream("non_existent.html");
        DataUtil.load(nonExistentInputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsIOException() throws IOException {
        InputStream inputStream = new FileInputStream(TEST_FILE_PATH);
        OutputStream outputStream = new FileOutputStream(TEST_FILE_PATH); // Use the same file as input stream

        DataUtil.crossStreams(inputStream, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadToByteBufferInvalidMaxSize() throws IOException {
        InputStream inputStream = new FileInputStream(TEST_FILE_PATH);
        DataUtil.readToByteBuffer(inputStream, -1);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferIOException() throws IOException {
        InputStream invalidInputStream = new FileInputStream("invalid.html");
        DataUtil.readToByteBuffer(invalidInputStream);
    }

    @Test(expected = IOException.class)
    public void testReadFileToByteBufferNonExistent() throws IOException {
        File nonExistentFile = new File("non_existent.html");
        DataUtil.readFileToByteBuffer(nonExistentFile);
    }
}