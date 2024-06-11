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
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World!</body></html>";
        File testFile = new File("testFile.html");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(expectedHtml.getBytes(StandardCharsets.UTF_8));
        FileUtils.writeFile(testFile, os.toByteArray());

        Document doc = DataUtil.load(testFile, null, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World!", doc.body().text());

        // clean up
        testFile.delete();
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World!</body></html>";
        String charset = "ISO-8859-1";

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(expectedHtml.getBytes(StandardCharsets.UTF_8));

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        Document doc = DataUtil.load(is, charset, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World!</body></html>";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(expectedHtml.getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());

        Document doc = DataUtil.load(is, null, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World!</body></html>";
        String charset = "ISO-8859-1";

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(expectedHtml.getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());

        Document doc = DataUtil.load(is, charset, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedParser() throws IOException {
        String expectedHtml = "<xml><root><element>Hello World!</element></root></xml>";
        String charset = "UTF-8";

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(expectedHtml.getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());

        Document doc = DataUtil.load(is, charset, null, DataUtil.Parser.xmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World!", doc.rootElement().text());
    }

    @Test(expected = IOException.class)
    public void testLoadNullFile() throws IOException {
        Document doc = DataUtil.load(null, null, null);
        Assert.fail();
    }

    @Test(expected = IOException.class)
    public void testLoadEmptyFile() throws IOException {
        File testFile = new File("testFile.html");
        Document doc = DataUtil.load(testFile, null, null);
        Assert.fail();
    }

    @Test
    public void testDetectCharset() throws IOException {
        String expectedHtml = "<html><head><meta charset=\"gb2312\"></meta></head><body>你好世界!</body></html>";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(expectedHtml.getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());

        Document doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(is), null, null, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("你好世界!", doc.body().text());
    }
}