package org.jsoup.helper.test;

import org.junit.Assert;
import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.helper.DataUtil;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException, HelperException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"ISO-8859-1\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        File testFile = new File("testFile.html");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(fileContent);
        }

        Document doc = DataUtil.load(testFile, "ISO-8859-1", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("ISO-8859-1", doc.outputSettings().charset());

        Element pElement = doc.select("p").first();
        Assert.assertNotNull(pElement);
        Assert.assertEquals("Hello World!", pElement.text());

        testFile.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException, HelperException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"ISO-8859-1\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "ISO-8859-1", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("ISO-8859-1", doc.outputSettings().charset());

        Element pElement = doc.select("p").first();
        Assert.assertNotNull(pElement);
        Assert.assertEquals("Hello World!", pElement.text());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException, HelperException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"ISO-8859-1\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());

        Element pElement = doc.select("p").first();
        Assert.assertNotNull(pElement);
        Assert.assertEquals("Hello World!", pElement.text());
    }

    @Test
    public void testLoadInputStreamWithoutCharset() throws IOException, HelperException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"ISO-8859-1\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());

        Element pElement = doc.select("p").first();
        Assert.assertNotNull(pElement);
        Assert.assertEquals("Hello World!", pElement.text());
    }

    @Test(expected = IOException.class)
    public void testLoadNullInputStream() throws IOException {
        InputStream inputStream = null;
        DataUtil.load(inputStream, null, null);
    }

    @Test(expected = IOException.class)
    public void testLoadEmptyInputStream() throws IOException {
        String fileContent = "";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
        DataUtil.load(inputStream, null, null);
    }

    @Test
    public void testParseByteData() throws IOException, HelperException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"ISO-8859-1\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());

        Element pElement = doc.select("p").first();
        Assert.assertNotNull(pElement);
        Assert.assertEquals("Hello World!", pElement.text());

        outputStream.close();
    }

    @Test
    public void testParseByteDataWithCharset() throws IOException, HelperException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"ISO-8859-1\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), "ISO-8859-1", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("ISO-8859-1", doc.outputSettings().charset());

        Element pElement = doc.select("p").first();
        Assert.assertNotNull(pElement);
        Assert.assertEquals("Hello World!", pElement.text());

        outputStream.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"ISO-8859-1\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedByteArray = fileContent.getBytes(StandardCharsets.UTF_8);
        byte[] byteArray = new byte[byteData.limit()];
        byteData.get(byteArray);

        Assert.assertArrayEquals(expectedByteArray, byteArray);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"ISO-8859-1\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 1024);

        byte[] expectedByteArray = fileContent.getBytes(StandardCharsets.UTF_8);
        byte[] byteArray = new byte[byteData.limit()];
        byteData.get(byteArray);

        Assert.assertArrayEquals(expectedByteArray, byteArray);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=ISO-8859-1";
        String expectedCharset = "ISO-8859-1";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expectedCharset, charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

}