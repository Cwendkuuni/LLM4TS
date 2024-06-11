package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.*;
import java.nio.ByteBuffer;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("testFile.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("testFile.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("testData".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("testData".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] testData = "testData".getBytes();
        InputStream inputStream = new ByteArrayInputStream(testData);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
        assertEquals(testData.length, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}