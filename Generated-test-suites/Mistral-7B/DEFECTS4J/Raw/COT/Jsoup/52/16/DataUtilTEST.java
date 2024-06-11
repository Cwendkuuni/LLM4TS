package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.helper.DataUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Replace with the actual file path
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        // Add assertions for the loaded document here
    }

    @Test
    public void testLoadInputStreamUTF8() throws IOException {
        String documentData = "HTML content here";
        InputStream inputStream = new ByteArrayInputStream(documentData.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        // Add assertions for the loaded document here
    }

    @Test
    public void testLoadInputStreamNonUTF8() throws IOException {
        String documentData = "HTML content here";
        InputStream inputStream = new ByteArrayInputStream(documentData.getBytes("ISO-8859-1"));
        String charsetName = null; // Or any unsupported charset
        String baseUri = "http://example.com";
        Document document = null;
        try {
            document = DataUtil.load(inputStream, charsetName, baseUri);
        } catch (IllegalArgumentException e) {
            // Assert expected exception here
        }
        Assert.assertNull(document);
    }

    @Test
    public void testLoadInputStreamWithCharsetDetect() throws IOException {
        String documentData = "HTML content here";
        InputStream inputStream = new ByteArrayInputStream(documentData.getBytes(Charset.forName("ISO-8859-1")));
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, null, baseUri);
        // Add assertions for the loaded document here
    }

    @Test
    public void testParseByteData() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("test.html")); // Replace with the actual file path
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        // Add assertions for the loaded document here
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("test.html")); // Replace with the actual file path
        String charsetName = null;
        String baseUri = "http://example.com";
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        // Add assertions for the loaded document here
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataWithUnsupportedCharset() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("test.html")); // Replace with the actual file path
        String charsetName = "X-PASCAL";
        String baseUri = "http://example.com";
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        // This test should throw an IllegalArgumentException
    }

    @Test(expected = NullPointerException.class)
    public void testCrossStreamsNullInputStream() throws IOException {
        InputStream nullInputStream = null;
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(nullInputStream, outputStream);
        // This test should throw a NullPointerException
    }
}