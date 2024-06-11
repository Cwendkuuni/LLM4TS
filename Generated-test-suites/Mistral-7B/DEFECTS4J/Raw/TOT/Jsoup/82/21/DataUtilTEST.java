package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoad() throws IOException {
        String htmlContent = "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello, world!</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", null);

        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset());

        Elements paragraphs = document.select("p");
        Assert.assertEquals(1, paragraphs.size());
        Assert.assertEquals("Hello, world!", paragraphs.get(0).text());

        inputStream.close();
    }

    @Test
    public void testLoadWithInvalidCharset() throws IOException {
        String htmlContent = "<html>\n" +
                "<head>\n" +
                "<meta charset=\"GBK\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello, world!</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = null;
        try {
            document = DataUtil.load(inputStream, "GBK", null);
        } catch (IOException e) {
            // Expected IOException
        } finally {
            inputStream.close();
        }

        Assert.assertNull(document);
    }

    @Test
    public void testParseInputStreamWithEmptyInput() throws IOException {
        Document document = DataUtil.parseInputStream(emptyByteBuffer(), null, null, Parser.htmlParser());

        Assert.assertNotNull(document);
        Assert.assertEquals("", document.body().html());
    }

    @Test
    public void testParseInputStreamWithUTF8Content() throws IOException {
        String htmlContent = "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello, world!</p>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteData = readToByteBuffer(new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8)), 0);

        Document document = DataUtil.parseInputStream(byteData.asInputStream(), null, null, Parser.htmlParser());

        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset());

        Elements paragraphs = document.select("p");
        Assert.assertEquals(1, paragraphs.size());
        Assert.assertEquals("Hello, world!", paragraphs.get(0).text());
    }

    @Test
    public void testParseInputStreamWithInvalidContent() throws IOException {
        String invalidContent = "<?xml encoding='ISO-8859-1'?>\n<html>\n<body>\n<p>Hello, world!</p>\n</body>\n</html>";

        ByteBuffer byteData = readToByteBuffer(new ByteArrayInputStream(invalidContent.getBytes(StandardCharsets.UTF_8)), 0);

        Document document = null;
        try {
            document = DataUtil.parseInputStream(byteData.asInputStream(), null, null, Parser.htmlParser());
        } catch (IOException e) {
            // Expected IOException
        } finally {
            byteData.clear();
        }

        Assert.assertNull(document);
    }

    @Test
    public void testParseInputStreamWithBOM() throws IOException {
        String utf16Content = "\ufeff<html>\n<head>\n<meta charset=\"UTF-16\">\n</head>\n<body>\n<p>Hello, world!</p>\n</body>\n</html>";

        ByteBuffer byteData = readToByteBuffer(new ByteArrayInputStream(utf16Content.getBytes(StandardCharsets.UTF_8)), 0);

        Document document = DataUtil.parseInputStream(byteData.asInputStream(), null, null, Parser.htmlParser());

        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-16", document.outputSettings().charset());

        Elements paragraphs = document.select("p");
        Assert.assertEquals(1, paragraphs.size());
        Assert.assertEquals("Hello, world!", paragraphs.get(0).text());
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        Assert.assertNotNull(boundary);
        Assert.assertEquals(boundary.length(), 32);
    }

    private InputStream newByteArrayInputStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

    private ByteBuffer readToByteBuffer(InputStream inputStream, int maxSize) throws IOException {
        return DataUtil.readToByteBuffer(inputStream, maxSize);
    }
}