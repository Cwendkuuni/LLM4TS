package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {
    
    private DataUtil dataUtil;
    
    @Before
    public void setup() {
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = dataUtil.load(file, "UTF-8", "http://www.example.com");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        Document doc = dataUtil.load(in, "UTF-8", "http://www.example.com");
        assertNotNull(doc);
    }

    @Test
    public void testLoadWithParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        Parser parser = Parser.xmlParser();
        Document doc = dataUtil.load(in, "UTF-8", "http://www.example.com", parser);
        assertNotNull(doc);
    }

    @Test
    public void testParseByteData() throws IOException {
        ByteBuffer byteData = ByteBuffer.allocate(10);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = new FileInputStream("test.html");
        ByteBuffer byteData = dataUtil.readToByteBuffer(inStream);
        assertNotNull(byteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = dataUtil.getCharsetFromContentType(contentType);
        assertEquals(charset, "UTF-8");
    }
}