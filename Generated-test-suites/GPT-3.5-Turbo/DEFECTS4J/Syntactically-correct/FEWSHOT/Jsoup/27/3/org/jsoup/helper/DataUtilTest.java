package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;


public class DataUtilTest {
    private final String testFile = "test.html";
    private final String testCharset = "UTF-8";
    private final String testBaseUri = "https://www.example.com";

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File(testFile);
        Document doc = DataUtil.load(file, testCharset, testBaseUri);
        assertEquals(doc.baseUri(), testBaseUri);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        FileInputStream in = new FileInputStream(testFile);
        Document doc = DataUtil.load(in, testCharset, testBaseUri);
        assertEquals(doc.baseUri(), testBaseUri);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        FileInputStream in = new FileInputStream(testFile);
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(in, testCharset, testBaseUri, parser);
        assertEquals(doc.baseUri(), testBaseUri);
    }

    @Test
    public void testParseByteData() {
        String testData = "<html><head><title>Test Data</title></head><body>Test Body</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        Document doc = DataUtil.parseByteData(byteData, testCharset, testBaseUri, Parser.htmlParser());
        assertEquals(doc.baseUri(), testBaseUri);
        assertEquals(doc.title(), "Test Data");
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "<html><head><title>Test Data</title></head><body>Test Body</body></html>";
        InputStream inStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        assertEquals(byteData.capacity(), testData.getBytes().length);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(charset, "UTF-8");
    }
}