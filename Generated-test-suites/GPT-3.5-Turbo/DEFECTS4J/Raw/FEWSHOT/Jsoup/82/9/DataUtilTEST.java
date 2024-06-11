package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class DataUtilTest {
    private static final String BASE_URI = "https://www.example.com/";

    @Test
    public void testLoad() {
        File file = new File("test.html");
        try {
            Document document = DataUtil.load(file, "UTF-8", BASE_URI);
            Assert.assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStream() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        try {
            Document document = DataUtil.load(inputStream, "UTF-8", BASE_URI);
            Assert.assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        try {
            Document document = DataUtil.load(inputStream, "UTF-8", BASE_URI, Parser.htmlParser());
            Assert.assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrossStreams() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            DataUtil.crossStreams(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String expected = "<html>\n" +
                " <body>\n" +
                "  <p>Hello, world!</p>\n" +
                " </body>\n" +
                "</html>\n";

        Assert.assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testParseInputStream() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        try {
            Document document = DataUtil.parseInputStream(inputStream, "UTF-8", BASE_URI, Parser.htmlParser());
            Assert.assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadToByteBuffer() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
            Assert.assertNotNull(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
    }

    @Test
    public void testGetCharsetFromContentType() {
		String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }
}