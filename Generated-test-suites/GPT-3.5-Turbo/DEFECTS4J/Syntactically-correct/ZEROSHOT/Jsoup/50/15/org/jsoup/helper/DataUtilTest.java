package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html><body><h1>Hello!</h1></body></html>".getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", "");
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello!</h1>\n </body>\n</html>", document.toString());
    }

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "");
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello!</h1>\n </body>\n</html>", document.toString());
    }

    @Test
    public void testParseByteDataWithCharset() {
        byte[] bytes = "<html><body><h1>Hello!</h1></body></html>".getBytes(StandardCharsets.UTF_8);
        Document document = DataUtil.parseByteData(
            ByteBuffer.wrap(bytes), 
            "UTF-8", 
            "", 
            Parser.htmlParser()
        );
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello!</h1>\n </body>\n</html>", document.toString());
    }

    @Test
    public void testParseByteDataWithCharsetFromMeta() {
        byte[] bytes = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body><h1>Hello!</h1></body></html>".getBytes(StandardCharsets.UTF_8);
        Document document = DataUtil.parseByteData(
            ByteBuffer.wrap(bytes), 
            null, 
            "", 
            Parser.htmlParser()
        );
        Assert.assertEquals("<html>\n <head>\n  <meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">\n </head>\n <body>\n  <h1>Hello!</h1>\n </body>\n</html>", document.toString());
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        byte[] bytes = "<html><body><h1>Hello!</h1></body></html>".getBytes(StandardCharsets.UTF_8);
        Document document = DataUtil.parseByteData(
            ByteBuffer.wrap(bytes), 
            null, 
            "", 
            Parser.htmlParser()
        );
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello!</h1>\n </body>\n</html>", document.toString());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(mimeBoundary);
    }
}