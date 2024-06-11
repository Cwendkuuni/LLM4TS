package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Replace with an actual test file
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes("UTF-8"));

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataDetectCharset() throws IOException {
        String charsetName = null;
        String baseUri = "http://example.com";

        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\"></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes("UTF-8"));

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("gb2312", document.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String input = "This is a test";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes("UTF-8"));
        int maxSize = 10;

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
        Assert.assertNotNull(byteData);

        byte[] data = new byte[byteData.limit()];
        byteData.get(data);
        String result = Charset.forName("UTF-8").decode(ByteBuffer.wrap(data)).toString();
        Assert.assertEquals(input, result);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }
}