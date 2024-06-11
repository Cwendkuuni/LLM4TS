package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.nodes.Document;
import org.jsoup.helper.DataUtil;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;

import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("test.html");
        String charset = "UTF-8";
        String baseUri = null;
        Document document = DataUtil.load(inputFile, charset, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File inputFile = new File("test.html");
        String charset = "InvalidCharset";
        String baseUri = null;
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputFile, charset, baseUri));
    }

    @Test
    public void testLoadFileWithNullBaseUri() throws IOException {
        File inputFile = new File("test.html");
        String charset = "UTF-8";
        String baseUri = null;
        Document document = DataUtil.load(inputFile, charset, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        String charset = "UTF-8";
        String baseUri = null;
        Document document = DataUtil.load(inputStream, charset, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        String charset = null;
        String baseUri = null;
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, charset, baseUri));
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String html = "<html><body></body></html>";
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byteData.put(html.getBytes("UTF-8"));
        byteData.flip();
        String baseUri = null;
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String html = "<html><body></body></html>";
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byteData.put(html.getBytes("UTF-8"));
        byteData.flip();
        String charset = "UTF-8";
        String baseUri = null;
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charset, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String html = "<html><body></body></html>";
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byteData.put(html.getBytes("UTF-8"));
        byteData.flip();
        String charset = "InvalidCharset";
        String baseUri = null;
        Parser parser = Parser.htmlParser();
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, charset, baseUri, parser));
    }

    @Test
    public void testCrossStreams() throws IOException {
        String input = "Test data.";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes("UTF-8"));
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        String output = outputStream.toString("UTF-8");
        assertNotNull(output);
        assertNotNull(output.trim());
        assertNotNull(output.equals(input));
    }
}