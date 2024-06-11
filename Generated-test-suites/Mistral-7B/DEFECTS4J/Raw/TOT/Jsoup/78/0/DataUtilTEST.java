package org.jsoup.helper.test;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String expectedContent = new String(Files.readAllBytes(new File("test.html").toPath()), StandardCharsets.UTF_8);
        Document doc = DataUtil.load(new File("test.html"), null, null);
        Assert.assertEquals(expectedContent, doc.html());
    }

    @Test
    public void testLoadInputStreamWithUtf8Charset() throws IOException {
        String expectedContent = "This is a test.";
        byte[] contentBytes = expectedContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(contentBytes);
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertEquals(expectedContent, doc.html());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String expectedContent = "This is a test.";
        byte[] contentBytes = expectedContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(contentBytes);
        try {
            Document doc = DataUtil.load(inputStream, "ISO-8859-1", null);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String expectedContent = "This is a test.";
        byte[] contentBytes = expectedContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(contentBytes);
        Document doc = DataUtil.load(inputStream, null, null);
        Assert.assertEquals(expectedContent, doc.html());
    }

    @Test
    public void testParseInputStreamWithUtf8Charset() throws IOException {
        String expectedContent = "This is a test.";
        byte[] contentBytes = expectedContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(contentBytes);
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        Assert.assertEquals(expectedContent, doc.html());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String expectedContent = "This is a test.";
        byte[] contentBytes = expectedContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(contentBytes);
        try {
            Document doc = DataUtil.parseInputStream(inputStream, "ISO-8859-1", null, Parser.htmlParser());
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStreamWithNullCharset() throws IOException {
        String expectedContent = "This is a test.";
        byte[] contentBytes = expectedContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(contentBytes);
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        Assert.assertEquals(expectedContent, doc.html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String expectedContent = "This is a test.";
        String inputContent = "This is a test.";
        byte[] contentBytes = expectedContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(contentBytes);
        OutputStream outputStream = new FileOutputStream("output.txt");

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] outputBytes = Files.readAllBytes(new File("output.txt").toPath());
        Assert.assertEquals(expectedContent, new String(outputBytes, StandardCharsets.UTF_8));

        inputStream.close();
        outputStream.close();
    }
}