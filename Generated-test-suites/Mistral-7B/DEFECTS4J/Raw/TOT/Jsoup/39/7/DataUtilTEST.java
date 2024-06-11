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
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test File</title>\n</head>\n<body>\n<p>Test content</p>\n</body>\n</html>";
        File testFile = new File("testFile.html");
        Files.write(testFile.getAbsoluteFile().toPath(), fileContent.getBytes());

        Document doc = DataUtil.load(testFile, "UTF-8", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test File", doc.title());

        Files.delete(testFile.getAbsoluteFile().toPath());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test File</title>\n</head>\n<body>\n<p>Test content</p>\n</body>\n</html>";
        InputStream testInputStream = new ByteArrayInputStream(fileContent.getBytes());

        Document doc = DataUtil.load(testInputStream, null, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test File", doc.title());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<html>\n<head>\n<meta charset=\"ISO-8859-1\">\n<title>Test File</title>\n</head>\n<body>\n<p>Test content</p>\n</body>\n</html>";
        InputStream testInputStream = new ByteArrayInputStream(fileContent.getBytes());

        Document doc = DataUtil.load(testInputStream, "ISO-8859-1", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test File", doc.title());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String fileContent = "<xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test File</title>\n</head>\n<body>\n<p>Test content</p>\n</body>\n</xml>";
        InputStream testInputStream = new ByteArrayInputStream(fileContent.getBytes());

        Document doc = DataUtil.load(testInputStream, null, null, DataUtil.Parser.xmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test File", doc.title());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test File</title>\n</head>\n<body>\n<p>Test content</p>\n</body>\n</html>";
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        testOutputStream.write(fileContent.getBytes());
        InputStream testInputStream = new ByteArrayInputStream(testOutputStream.toByteArray());
        ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);

        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test File", doc.title());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<html>\n<head>\n<meta charset=\"ISO-8859-1\">\n<title>Test File</title>\n</head>\n<body>\n<p>Test content</p>\n</body>\n</html>";
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        testOutputStream.write(fileContent.getBytes());
        InputStream testInputStream = new ByteArrayInputStream(testOutputStream.toByteArray());
        ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);

        Document doc = DataUtil.parseByteData(byteData, "ISO-8859-1", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test File", doc.title());
    }

    @Test
    public void testDetectCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String detectedCharset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", detectedCharset);
    }

    @Test
    public void testDetectCharsetFromContentTypeWithUnsupportedCharset() {
        String contentType = "text/html; charset=ISO-8859-15";
        String detectedCharset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(detectedCharset);
    }

    @Test
    public void testDetectCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        String detectedCharset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(DataUtil.defaultCharset, detectedCharset);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithIOException() throws IOException {
        File testFile = new File("nonExistentFile.html");
        DataUtil.load(testFile, null, null);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithIOException() throws IOException {
        File testFile = new File("nonExistentFile.html");
        InputStream testInputStream = new FileInputStream(testFile);
        DataUtil.readToByteBuffer(testInputStream);
    }
}