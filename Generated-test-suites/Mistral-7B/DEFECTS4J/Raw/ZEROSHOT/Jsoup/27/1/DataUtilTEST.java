package org.jsoup.helper.test;

import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert.*;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("src/test/resources/testData.html");
        String charsetName = "UTF-8";
        String baseUri = "http://localhost:8080";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidFile() throws IOException {
        File file = new File("src/test/resources/nonexistentfile.html");
        String charsetName = "UTF-8";
        String baseUri = "http://localhost:8080";
        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>test</body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://localhost:8080";
        Document doc = DataUtil.load(in, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>test</body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes(Charset.forName("ISO-8859-1")));
        String charsetName = null;
        String baseUri = "http://localhost:8080";
        Document doc = DataUtil.load(in, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithNonUTF8Charset() throws IOException {
        String html = "<html><head><meta charset=\"ISO-8859-1\"></head><body>test</body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes(Charset.forName("ISO-8859-1")));
        String charsetName = null;
        String baseUri = "http://localhost:8080";
        Document doc = DataUtil.load(in, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testParseByteData() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>test</body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8")));
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>test</body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8")));
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        String charsetName = "UTF-8";
        String baseUri = null;
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithInvalidCharsetInMetaTag() throws IOException {
        String html = "<html><head><meta charset=\"ISO-8859-1\"></head><body>test</body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8")));
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        String charsetName = null;
        String baseUri = null;
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithInvalidCharsetInContentType() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>test</body></html>";
        String contentType = "application/x-test; charset=ISO-8859-1";
        InputStream in = new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8")));
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        String charsetName = null;
        String baseUri = null;
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser(), contentType);
        assertNotNull(doc);
    }

}