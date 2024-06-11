package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws Exception {
        File file = new File("example.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        //Test assertions
    }

    @Test
    public void testLoadFileWithNullCharset() throws Exception {
        File file = new File("example.html");
        Document doc = DataUtil.load(file, null, "http://example.com");
        //Test assertions
    }

    @Test
    public void testLoadInputStream() throws Exception {
        InputStream in = new FileInputStream("example.html");
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
        //Test assertions
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws Exception {
        InputStream in = new FileInputStream("example.html");
        Document doc = DataUtil.load(in, null, "http://example.com");
        //Test assertions
    }

    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        InputStream in = new FileInputStream("example.html");
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com", Parser.htmlParser());
        //Test assertions
    }

    @Test
    public void testLoadInputStreamWithParserAndNullCharset() throws Exception {
        InputStream in = new FileInputStream("example.html");
        Document doc = DataUtil.load(in, null, "http://example.com", Parser.htmlParser());
        //Test assertions
    }

    @Test
    public void testParseByteDataWithCharset() throws Exception {
        ByteBuffer byteData = ByteBuffer.wrap("example data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        //Test assertions
    }

    @Test
    public void testParseByteDataWithoutCharset() throws Exception {
        ByteBuffer byteData = ByteBuffer.wrap("example data".getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        //Test assertions
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws Exception {
        InputStream inStream = new FileInputStream("example.html");
        int maxSize = 1000;
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream, maxSize);
        //Test assertions
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws Exception {
        InputStream inStream = new FileInputStream("example.html");
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        //Test assertions
    }

    @Test
    public void testGetCharsetFromContentType() throws Exception {
        String contentType = "text/html; charset=utf-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithUnsupportedCharset() throws Exception {
        String contentType = "text/html; charset=unsupported";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

}