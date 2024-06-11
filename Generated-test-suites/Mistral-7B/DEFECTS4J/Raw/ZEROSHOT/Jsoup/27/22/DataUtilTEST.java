package org.jsoup.helper.test;

import static org.junit.Assert.*;

import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.parser.Parser.HtmlParser;
import org.jsoup.helper.DataUtil;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException, HelperException {
        File file = new File("testFile.html");
        Document document = DataUtil.load(file, "UTF-8", null);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullCharset() throws IOException, HelperException {
        File file = new File("testFile.html");
        Document document = DataUtil.load(file, null, null);
    }

    @Test
    public void testLoadInputStream() throws IOException, HelperException {
        String html = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullCharset() throws IOException, HelperException {
        String html = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document document = DataUtil.load(inputStream, null, null);
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException, HelperException {
        String html = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document document = DataUtil.load(inputStream, "UTF-8", null, new HtmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteData() throws IOException, HelperException {
        String charset = "UTF-8";
        String html = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(charset));
        Document document = DataUtil.parseByteData(byteData, charset, null, new HtmlParser());
        assertNotNull(document);

        charset = "GB2312";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes("UTF-8"));
        ByteBuffer byteDataWithCharset = ByteBuffer.wrap(outputStream.toByteArray());
        Element meta = new Element("meta");
        meta.attr("http-equiv", "content-type");
        meta.attr("content", "text/html; charset=" + charset);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        document = DataUtil.parseByteData(byteDataWithCharset, charset, null, new HtmlParser());
        assertEquals(charset, document.outputSettings().charset());

        assertNotNull(document.select("meta[http-equiv=content-type], meta[charset]").first());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        byte[] expectedByteData = outputStream.toByteArray();
        byte[] actualByteData = byteData.array();
        assertArrayEquals(expectedByteData, actualByteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String expectedCharset = "EUC-JP";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }
}