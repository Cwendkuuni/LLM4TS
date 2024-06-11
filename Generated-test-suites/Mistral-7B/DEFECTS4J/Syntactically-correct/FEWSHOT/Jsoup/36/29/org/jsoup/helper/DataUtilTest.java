package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.Element;
import org.jsoup.helper.DataUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class DataUtilTest {

    private static final String charsetTestData = "Content-Type: text/html; charset=UTF-8\r\n" +
            "--test\r\n" +
            "Meta-Charset: ISO-8859-1\r\n" +
            "<html><head><meta charset=\"Windows-1252\"></meta></head><body><h1>Test</h1></body></html>";

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        File inputFile = File.createTempFile("test", null);
        try (FileWriter writer = new FileWriter(inputFile)) {
            writer.write("This is a test.");
            writer.close();
        }
        String charsetName = null;
        String baseUri = null;
        Document document = DataUtil.load(inputFile, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadDocumentFromInputStreamWithDefaultCharset() throws IOException {
        String charsetName = null;
        String baseUri = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("This is a test.".getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadDocumentFromInputStreamWithSpecifiedCharset() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("Content-Type: text/html; charset=".getBytes());
        outputStream.write(charsetName.getBytes());
        outputStream.write("\r\n".getBytes());
        outputStream.write("--test\r\n".getBytes());
        outputStream.write("Meta-Charset: ISO-8859-1\r\n".getBytes());
        outputStream.write("<html><head><meta charset=\"".getBytes());
        outputStream.write(charsetName.getBytes());
        outputStream.write("\"</meta></head><body><h1>Test</h1></body></html>".getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String charsetName = null;
        String baseUri = null;
        ByteBuffer byteData = ByteBuffer.wrap("This is a test.".getBytes());
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String charsetName = "UTF-8";
        String baseUri = null;
        ByteBuffer byteData = ByteBuffer.wrap(charsetTestData.getBytes());
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(charsetTestData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}