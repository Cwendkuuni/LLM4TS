package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Test class for DataUtil class.
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}
     * @throws IOException
     */
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", null);
        Assert.assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}
     * @throws IOException
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.load(in, "UTF-8", null);
        Assert.assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}
     * @throws IOException
     */
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.load(in, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}
     */
    @Test
    public void testParseByteData() {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteBuffer, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream, int)} with unlimited size
     * @throws IOException
     */
    @Test
    public void testReadToByteBufferUnlimited() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in);
        Assert.assertNotNull(byteBuffer);
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream, int)} with limited size
     * @throws IOException
     */
    @Test
    public void testReadToByteBufferLimited() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, 10);
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(10, byteBuffer.remaining());
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)} with invalid content type
     */
    @Test(expected = IllegalStateException.class)
    public void testGetCharsetFromContentTypeInvalidContentType() {
        String contentType = "text/plain";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with null charset and baseUri
     */
    @Test
    public void testParseByteDataWithNullCharsetAndBaseUri() {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with empty charset and baseUri
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataWithEmptyCharsetAndBaseUri() {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteBuffer, "", null, Parser.htmlParser());
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with null parser
     */
    @Test(expected = NullPointerException.class)
    public void testParseByteDataWithNullParser() {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteBuffer, "UTF-8", null, null);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with invalid charset
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataWithInvalidCharset() {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteBuffer, "invalid", null, Parser.htmlParser());
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with null byteData
     */
    @Test(expected = NullPointerException.class)
    public void testParseByteDataWithNullByteData() {
        Document doc = DataUtil.parseByteData(null, "UTF-8", null, Parser.htmlParser());
    }
}