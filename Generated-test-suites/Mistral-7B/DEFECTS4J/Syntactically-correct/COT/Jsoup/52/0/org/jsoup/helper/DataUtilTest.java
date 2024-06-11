package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.regex.Pattern;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        File testFile = new File("testFile.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream writer = outputStream;
        byte[] fileContentInBytes = fileContent.getBytes(StandardCharsets.UTF_8);
        writer.write(fileContentInBytes, 0, fileContentInBytes.length);
        writer.close();

        Document document = DataUtil.load(testFile, "UTF-8", "/baseUri");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        byte[] contentInBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(contentInBytes);

        Document document = DataUtil.load(inputStream, "UTF-8", "/baseUri");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        byte[] contentInBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(contentInBytes);

        Document document = DataUtil.load(inputStream, "UTF-8", "/baseUri", Parser.xmlParser());
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        String fileContent = "This is a test file.";
        File testFile = new File("testFile.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream writer = outputStream;
        byte[] fileContentInBytes = fileContent.getBytes(StandardCharsets.UTF_8);
        writer.write(fileContentInBytes, 0, fileContentInBytes.length);
        writer.close();

        assertThrows(IOException.class, () -> DataUtil.load(testFile, "INVALID_CHARSET", "/baseUri"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadInputStreamWithNullCharset() {
        String inputStreamContent = "This is a test input stream.";
        byte[] contentInBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(contentInBytes);

        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, null, "/baseUri"));
    }

    @Test
    public void testParseByteData() {
        String charsetName = "UTF-8";
        String docData = "<html><head><meta charset='gb2312'></head><body>This is a test.</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseByteData(byteData, charsetName, "/baseUri", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        String charsetName = "INVALID_CHARSET";
        String docData = "<html><head><meta charset='gb2312'></head><body>This is a test.</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes(StandardCharsets.UTF_8));

        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, charsetName, "/baseUri", Parser.htmlParser()));
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputStreamContent = "Input stream content.";
        byte[] contentInBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(contentInBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] outputStreamContentInBytes = outputStream.toByteArray();
        String outputStreamContent = new String(outputStreamContentInBytes, StandardCharsets.UTF_8);
        assertNotNull(outputStreamContent);
        assertNotNull(inputStream);
        assertNotNull(outputStream);
    }
}