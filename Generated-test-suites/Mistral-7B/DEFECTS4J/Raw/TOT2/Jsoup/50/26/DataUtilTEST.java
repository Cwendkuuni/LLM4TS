package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    /**
     * Test for the {@code load(File, String, String)} method.
     */
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com/";

        Document document = DataUtil.load(file, charset, baseUri);
        Assert.assertNotNull(document);
    }

    /**
     * Test for the {@code load(InputStream, String, String)} method.
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body></body></html>";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(html.getBytes());
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        String charset = "UTF-8";
        String baseUri = "http://example.com/";

        Document document = DataUtil.load(inputStream, charset, baseUri);
        Assert.assertNotNull(document);
    }

    /**
     * Test for the {@code load(InputStream, String, String, Parser)} method.
     */
    @Test
    public void testLoadInputStreamParser() throws IOException {
        String html = "<html><body></body></html>";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(html.getBytes());
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        String charset = "UTF-8";
        String baseUri = "http://example.com/";
        Parser parser = Parser.xmlParser();

        Document document = DataUtil.load(inputStream, charset, baseUri, parser);
        Assert.assertNotNull(document);
    }

    /**
     * Test for the {@code crossStreams(InputStream, OutputStream)} method.
     */
    @Test
    public void testCrossStreams() throws IOException {
        File file = new File("test.html");
        InputStream inputStream = new FileInputStream(file);
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        Assert.assertNotSame(inputStream.available(), outputStream.size());

        inputStream.close();
        outputStream.close();
    }

    /**
     * Test for the edge case of the {@code load(InputStream, String, String)} method with an unsupported charset.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLoadInputStreamInvalidCharset() throws IOException {
        String html = "<html><body></body></html>";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(html.getBytes());
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        String charset = "x-Invalid-Charset";
        String baseUri = "http://example.com/";

        DataUtil.load(inputStream, charset, baseUri);
    }

    /**
     * Test for the edge case of the {@code parseByteData(ByteBuffer, String, String, Parser)} method with an empty byte buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        String charset = "UTF-8";
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();

        DataUtil.parseByteData(byteBuffer, charset, baseUri, parser);
    }

    /**
     * Test for the edge case of the {@code parseByteData(ByteBuffer, String, String, Parser)} method with a byte buffer containing an empty string.
     */
    @Test
    public void testParseByteDataEmptyString() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[0]);
        String charset = "UTF-8";
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteBuffer, charset, baseUri, parser);
        Assert.assertNull(document);
    }

    /**
     * Test for the edge case of the {@code parseByteData(ByteBuffer, String, String, Parser)} method with a byte buffer containing a BOM.
     */
    @Test
    public void testParseByteDataBOM() {
        String html = "\uFEFF<html><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes());
        String charset = "UTF-8";
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteBuffer, charset, baseUri, parser);
        Assert.assertNotNull(document);
        Assert.assertEquals(html, document.html());
    }

    /**
     * Test for the edge case of the {@code parseByteData(ByteBuffer, String,String, Parser)} method with a byte buffer containing an invalid charset.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataInvalidCharset() {
        String html = "<html><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes("UTF-8"));
        String charset = "x-Invalid-Charset";
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();

        DataUtil.parseByteData(byteBuffer, charset, baseUri, parser);
    }

    /**
     * Test for the edge case of the {@code parseByteData(ByteBuffer, String, String, Parser)} method with a byte buffer containing an HTML document with a defined charset.
     */
    @Test
    public void testParseByteDataDefinedCharset() {
        String html = "<meta http-equiv=\"content-type\" content=\"text/html; charset=GBK\">" +
                "<html><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes());
        String charset = null;
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteBuffer, charset, baseUri, parser);
        Assert.assertNotNull(document);
        Assert.assertEquals("GBK", document.outputSettings().charset());
    }

    /**
     * Test for the edge case of the {@code parseByteData(ByteBuffer, String, String, Parser)} method with a byte buffer containing an HTML document with a defined charset that is not supported.
     */
    @Test(expected = UnsupportedEncodingException.class)
    public void testParseByteDataUnsupportedCharset() {
        String html = "<meta http-equiv=\"content-type\" content=\"text/html; charset=X-Unsupported-Charset\">" +
                "<html><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes());
        String charset = null;
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();

        DataUtil.parseByteData(byteBuffer, charset, baseUri, parser);
    }

    /**
     * Test for the edge case of the {@code parseByteData(ByteBuffer, String, String, Parser)} method with a byte buffer containing an HTML document with no charset defined.
     */
    @Test
    public void testParseByteDataNoCharset() {
        String html = "<html><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes());
        String charset = "UTF-8"; // default charset
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteBuffer, charset, baseUri, parser);
        Assert.assertNotNull(document);
        Assert.assertEquals(charset, document.outputSettings().charset());
    }

    /**
     * Test for the edge case of the {@code parseByteData(ByteBuffer, String, String, Parser)} method with a byte buffer containing an HTML document with a malformed charset header.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataMalformedCharsetHeader() {
        String html = "<meta http-equiv=\"content-type\" content=\"text/html; charset=x-invalid;charset=UTF-8\">" +
                "<html><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes());
        String charset = null;
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();

        DataUtil.parseByteData(byteBuffer, charset, baseUri, parser);
    }

    /**
     * Test for the {@code getCharsetFromContentType(String)} method.
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        contentType = "text/html; charset=GBK";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("GBK", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(null, charset);

        contentType = "text/html; charset=x-Unsupported-Charset";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(null, charset);
    }
}