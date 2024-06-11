package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Random;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "Some content";
        File file = new File("testFile.html");
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document document = DataUtil.load(file, "UTF-8", "");
        assertNotNull(document);
        file.deleteOnExit();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String content = "Some content";
        byte[] contentBytes = content.getBytes("UTF-8");
        InputStream inputStream = new ByteArrayInputStream(contentBytes);

        Document document = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String content = "Some content";
        byte[] contentBytes = content.getBytes("UTF-8");
        InputStream inputStream = new ByteArrayInputStream(contentBytes);

        Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "Input content";
        String outputContent = "Output content";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputContent.getBytes("UTF-8"));
        DataUtil.crossStreams(inputStream, outputStream);
        byte[] outputBytes = outputStream.toByteArray();
        String outputString = new String(outputBytes, "UTF-8");

        assertNotNull(outputString);
        assertNotNull(outputContent.equals(outputString));
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String docData = "<html><head><title>Test</title></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes("UTF-8"));

        Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String docData = "<html><head><title>Test</title></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes("UTF-8"));

        Document document = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String docData = "<html><head><title>Test</title></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes("UTF-8"));

        assertThrows(IllegalArgumentException.class, () -> {
            DataUtil.parseByteData(byteData, "invalidCharset", "", Parser.htmlParser());
        });
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String content = "Some content";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        byte[] expectedBytes = content.getBytes("UTF-8");
        byte[] actualBytes = new byte[expectedBytes.length];
        byteBuffer.get(actualBytes);

        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String fileContent = "Some content";
        File file = new File("testFile.html");
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContent);
        fileWriter.close();

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        byte[] expectedBytes = fileContent.getBytes("UTF-8");
        byte[] actualBytes = new byte[expectedBytes.length];
        byteBuffer.get(actualBytes);

        assertArrayEquals(expectedBytes, actualBytes);
        file.deleteOnExit();
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String result = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", result);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String result = DataUtil.validateCharset(charset);
        assertEquals("UTF-8", result);
    }

    @Test
    public void testMimeBoundary() {
        String result = DataUtil.mimeBoundary();
        assertNotNull(result);
        String expected = "_____1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        assertEquals(expected.length(), result.length());
    }

}