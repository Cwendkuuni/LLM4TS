package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for {@link DataUtil}.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        File file = new File("testFile.html");
        file.createNewFile();
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(fileContent);
        }

        Document document = DataUtil.load(file, "UTF-8", null);
        Assert.assertEquals(fileContent, document.body().text());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        byte[] contentAsBytes = inputStreamContent.getBytes("UTF-8");
        InputStream inputStream = new ByteArrayInputStream(contentAsBytes);

        Document document = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertEquals(inputStreamContent, document.body().text());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "<html><body><p>This is a test input stream.</p></body></html>";
        byte[] contentAsBytes = inputStreamContent.getBytes("UTF-8");
        InputStream inputStream = new ByteArrayInputStream(contentAsBytes);

        Document document = DataUtil.load(inputStream, "UTF-8", null, Parser.htmlParser());
        Assert.assertEquals(inputStreamContent, document.body().html());
    }

    @Test
    public void testParseByteData() throws IOException {
        String byteDataContent = "<html><body><p>This is a test byte data.</p></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(byteDataContent.getBytes("UTF-8"));

        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        Assert.assertEquals(byteDataContent, document.body().html());
    }

    @Test
    public void testCharsetDetection() throws IOException {
        String charsetDetectContent = "<html lang='ja' xmlns='http://www.w3.org/1999/xhtml' xml:lang='ja'>\n<head>\n<meta http-equiv='Content-Type' content='text/html; charset=EUC-JP'>\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        ByteBuffer byteData = ByteBuffer.wrap(charsetDetectContent.getBytes("UTF-8"));

        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assert.assertEquals("EUC-JP", document.outputSettings().charset());
        Assert.assertEquals(charsetDetectContent, document.body().html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String input = "Test data.";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(input.getBytes("UTF-8"));

        DataUtil.crossStreams(inputStream, outputStream);
        Assert.assertEquals(input, new String(outputStream.toByteArray(), "UTF-8"));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        contentType = "text/plain";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertEquals(mimeBoundary.length(), 32);
        Assert.assertTrue(mimeBoundary.matches("[-_a-zA-Z0-9]+"));
    }
}