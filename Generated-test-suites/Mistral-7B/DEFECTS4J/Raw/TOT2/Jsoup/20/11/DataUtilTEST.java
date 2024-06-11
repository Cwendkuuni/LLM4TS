package org.junit.tests;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithInvalidPath() throws IOException {
        File file = new File("nonexistentfile.html");
        Document doc = DataUtil.load(file, "UTF-8", null);
        Assert.assertNull(doc);
    }

    @Test
    public void testLoadFileWithUnsupportedFormat() throws IOException {
        File file = new File("test.txt");
        Document doc = DataUtil.load(file, "UTF-8", null);
        Assert.assertNull(doc);
    }

    @Test
    public void testLoadFileWithIncorrectCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "ISO-8859-1", null);
        Assert.assertNotNull(doc); // might fail if the file actually uses ISO-8859-1
    }

    @Test
    public void testLoadFileWithIncorrectBaseUri() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "/wrong_base_uri");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithoutData() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNull(doc);
    }

    @Test
    public void testLoadInputStreamWithData() throws IOException {
        String html = "<html></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithIncorrectCharset() throws IOException {
        String html = "<html></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("ISO-8859-1"));
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNull(doc);
    }

    @Test
    public void testLoadInputStreamWithLargeData() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10 * 1024 * 1024]); // 10MB
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String xml = "<xml></xml>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", null, DataUtil.Parser.xmlParser());
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullInput() {
        Document doc = DataUtil.load(null, "UTF-8", null);
    }
}