package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {

    private File file;
    private String charsetName;
    private String baseUri;

    @Before
    public void setUp() {
        file = new File("test.html");
        charsetName = "UTF-8";
        baseUri = "http://example.com";
    }

    @Test
    public void testLoadFile() {
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            FileInputStream inStream = new FileInputStream(file);
            Document document = DataUtil.load(inStream, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            FileInputStream inStream = new FileInputStream(file);
            Parser parser = Parser.htmlParser();
            Document document = DataUtil.load(inStream, charsetName, baseUri, parser);
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals(charsetName, document.outputSettings().charset().name());
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            FileInputStream inStream = new FileInputStream(file);
            ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
            assertNotNull(byteData);
            assertTrue(byteData.limit() > 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

}