package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFromString() throws IOException {
        String data = "<html><body><div id='content'>Hello World</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(data.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFromStringWithParser() throws IOException {
        String data = "<html><body><div id='content'>Hello World</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(data.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteData() throws NoSuchFieldException, IllegalAccessException {
        String data = "<html><body><div id='content'>Hello World</div></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(data.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Field field = DataUtil.class.getDeclaredField("defaultCharset");
        field.setAccessible(true);
        String defaultCharset = (String) field.get(DataUtil.class);

        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);
        Assert.assertNotNull(document);

        field.set(DataUtil.class, defaultCharset);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        int maxSize = 1024;
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expected = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expected, charset);
    }
}