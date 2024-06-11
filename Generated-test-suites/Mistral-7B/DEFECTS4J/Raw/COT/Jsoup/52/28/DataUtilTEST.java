package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

import org.junit.Test;

/**
 * Test class for DataUtil class
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("test.html");
        Document doc = DataUtil.load(file, expectedCharset, baseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullCharset() throws IOException {
        String baseUri = "http://example.com";
        File file = new File("test.html");
        DataUtil.load(file, null, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithEmptyCharset() throws IOException {
        String baseUri = "http://example.com";
        File file = new File("test.html");
        DataUtil.load(file, "", baseUri);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";
        byte[] inputData = "<html><head></head><body></body></html>".getBytes();
        InputStream in = new ByteArrayInputStream(inputData);
        Document doc = DataUtil.load(in, expectedCharset, baseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String baseUri = "http://example.com";
        InputStream in = new ByteArrayInputStream("test data".getBytes());
        DataUtil.load(in, null, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithEmptyCharset() throws IOException {
        String baseUri = "http://example.com";
        InputStream in = new ByteArrayInputStream("test data".getBytes());
        DataUtil.load(in, "", baseUri);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byteData.put("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html></html>".getBytes());
        byteData.flip();
        String baseUri = "http://example.com";
        Parser parser = DataUtil.Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byteData.put("<?xml version=\"1.0\" encoding=\"gb2312\"?>\n<html></html>".getBytes());
        byteData.flip();
        String baseUri = "http://example.com";
        Parser parser = DataUtil.Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, "gb2312", baseUri, parser);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithNullInputStream() throws IOException {
        InputStream in = null;
        OutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithNullOutputStream() throws IOException {
        InputStream in = new ByteArrayInputStream("test data".getBytes());
        OutputStream out = null;
        DataUtil.crossStreams(in, out);
    }
}