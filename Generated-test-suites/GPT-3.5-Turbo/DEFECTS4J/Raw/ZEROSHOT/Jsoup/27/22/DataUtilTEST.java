
package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "";

    @Test
    public void testLoadFile() {
        try {
            File file = new File("test.html");
            Document document = DataUtil.load(file, CHARSET_NAME, BASE_URI);
            Assert.assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            String html = "<html><body><h1>Test</h1></body></html>";
            InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
            Document document = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI);
            Assert.assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            String html = "<html><body><h1>Test</h1></body></html>";
            InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
            Parser parser = Parser.htmlParser();
            Document document = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI, parser);
            Assert.assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseByteData() {
        try {
            String html = "<html><body><h1>Test</h1></body></html>";
            ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
            Document document = DataUtil.parseByteData(byteData, CHARSET_NAME, BASE_URI, Parser.htmlParser());
            Assert.assertNotNull(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            String html = "<html><body><h1>Test</h1></body></html>";
            InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
            Assert.assertNotNull(byteData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(CHARSET_NAME, charset);
    }
}
