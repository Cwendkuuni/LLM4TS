package org.jsoup.helper.test;

import org.apache.commons.io.FileUtils;
import org.jsoup.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-16", null); // change this to the actual charset you want to test
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-16", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = FileUtils.readFileToString(new File("test.html"), "UTF-8");
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = FileUtils.readFileToString(new File("test.html"), "UTF-8");
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-16")); // change this to the actual charset you want to test
        Document doc = DataUtil.load(inputStream, "UTF-16", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-16", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String htmlContent = FileUtils.readFileToString(new File("test.html"), "UTF-8");
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, null, null, DataUtil.Parser.xmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("input.html");
        File outputFile = new File("output.html");
        DataUtil.load(inputFile, null, null); // read file to be sure that its content is loaded properly
        FileUtils.writeStringToFile(outputFile, "Some content to write", "UTF-8");

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        Document doc = DataUtil.load(outputFile, null, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());

        FileUtils.deleteQuietly(outputFile);
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithException() throws IOException {
        File inputFile = new File("input.html");
        File outputFile = new File("output.html");

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        inputStream.close(); // this line should cause an exception
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        byte[] htmlBytes = "Some HTML content".getBytes("UTF-8");
        ByteBuffer byteBuffer = ByteBuffer.wrap(htmlBytes);
        Document doc = DataUtil.parseByteData(byteBuffer, null, null, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        byte[] htmlBytes = "Some HTML content".getBytes("UTF-16"); // change this to the actual charset you want to test
        ByteBuffer byteBuffer = ByteBuffer.wrap(htmlBytes);
        Document doc = DataUtil.parseByteData(byteBuffer, "UTF-16", null, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-16", doc.outputSettings().charset());
    }
}