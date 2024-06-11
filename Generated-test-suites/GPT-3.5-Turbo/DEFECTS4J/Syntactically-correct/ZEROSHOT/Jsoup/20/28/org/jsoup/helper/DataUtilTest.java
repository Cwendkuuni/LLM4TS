package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "");
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "");
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("<html><title>Test</title></html>".getBytes());
        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", "", Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("<html>\n <head>\n  <title>Test</title>\n </head>\n <body></body>\n</html>", document.html());
        Assert.assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(0x20000, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNotNull(charset);
        Assert.assertEquals("UTF-8", charset);
    }
}