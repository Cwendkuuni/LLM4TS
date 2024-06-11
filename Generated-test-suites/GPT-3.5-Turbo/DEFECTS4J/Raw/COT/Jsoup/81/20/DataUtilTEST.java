
package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("path_to_file");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
  
        Assert.assertNotNull(document);
        // Add assertions to check the functionality of the method
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
  
        Assert.assertNotNull(document);
        // Add assertions to check the functionality of the method
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("example.xml");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
  
        Assert.assertNotNull(document);
        // Add assertions to check the functionality of the method
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("input.txt");
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
  
        // Add assertions to check the functionality of the method
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("input.txt");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
  
        Assert.assertNotNull(byteBuffer);
        // Add assertions to check the functionality of the method
    }

    @Test
    public void testReadInputStreamToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("input.txt");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
  
        Assert.assertNotNull(byteBuffer);
        // Add assertions to check the functionality of the method
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("path_to_file");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
  
        Assert.assertNotNull(byteBuffer);
        // Add assertions to check the functionality of the method
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
  
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
  
        Assert.assertNotNull(charset1);
        Assert.assertEquals("UTF-8", charset1);

        String contentType2 = "text/html";
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
  
        Assert.assertNull(charset2);
    }
}
