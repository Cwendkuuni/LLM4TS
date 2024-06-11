package org.jsoup.helper; // make sure the package is correct in your IDE or build system

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.helper.DataUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Test class for DataUtil
 */
public class DataUtilTest {

    @Test
    public void testLoadFileDocument() throws IOException {
        File testFile = new File("test.html"); // replace with an existing HTML file
        Document doc = DataUtil.load(testFile, "UTF-8", "");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadEmptyFileDocument() throws IOException {
        File emptyFile = new File("empty.html"); // replace with an empty HTML file or create an empty file
        Document doc = DataUtil.load(emptyFile, "UTF-8", "");
        Assert.assertNotNull(doc);
        Assert.assertEquals("", doc.body().text());
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File testFile = new File("test.html"); // replace with an existing HTML file
        Document doc = DataUtil.load(testFile, "invalidCharset", "");
    }

    @Test
    public void testLoadInputStreamDocument() throws IOException {
        String testHtml = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        Assert.assertNotNull(doc);
        Assert.assertEquals("<html><body><h1>Test</h1></body></html>", doc.toString());
    }

    @Test
    public void testParseInputStreamDocumentWithInvalidCharset() throws IOException {
        String testHtml = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "invalidCharset", "", DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("<html><body><h1>Test</h1></body></html>", doc.toString());
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamDocumentWithEmptyInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", DataUtil.Parser.htmlParser());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testHtml = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        Document doc = DataUtil.parseInputStream(new ByteArrayInputStream(outputStream.toByteArray()), "UTF-8", "", DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("<html><body><h1>Test</h1></body></html>", doc.toString());
    }
}