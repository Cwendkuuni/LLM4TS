package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    

    @Test
    public void testLoadFile_ValidCharset_Success() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document expectedDocument = new Document("http://example.com");
        Document actualDocument = DataUtil.load(file, charsetName, baseUri);
        Assert.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadFile_InvalidCharset_DefaultCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = null;
        String baseUri = "http://example.com";
        Document expectedDocument = new Document("http://example.com");
        Document actualDocument = DataUtil.load(file, charsetName, baseUri);
        Assert.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadInputStream_ValidCharset_Success() throws IOException {
        String inputData = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document expectedDocument = new Document("http://example.com");
        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadInputStream_InvalidCharset_DefaultCharset() throws IOException {
        String inputData = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.ISO_8859_1));
        String charsetName = null;
        String baseUri = "http://example.com";
        Document expectedDocument = new Document("http://example.com");
        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseInputStream_ValidCharset_Success() throws IOException {
        String inputData = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document expectedDocument = new Document("http://example.com");
        Document actualDocument = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        Assert.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseInputStream_InvalidCharset_DefaultCharset() throws IOException {
        String inputData = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.ISO_8859_1));
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document expectedDocument = new Document("http://example.com");
        Document actualDocument = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        Assert.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        byte[] actualData = outputStream.toByteArray();
        byte[] expectedData = "Hello World".getBytes(StandardCharsets.UTF_8);
        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test(expected = IOException.class)
    public void testParseInputStream_IOException() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
    }

    
}