package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";
        File inputFile = new File("example.html");
        Files.write(inputFile.toPath(), fileContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputFile, "UTF-8", "/");
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset());

        Files.delete(inputFile.toPath());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "UTF-8", "/");
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset());

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithCharsetDetect() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, null, "/");
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset()); // Charset should be detected from meta tag

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"XYZ-123\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        try {
            Document document = DataUtil.load(inputStream, "XYZ-123", "/");
            Assert.fail("Expected IOException when providing invalid charset");
        } catch (IOException e) {
            // Expected
        }

        inputStream.close();
    }

    @Test
    public void testParseByteData() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("example.html"));
        String charsetName = "UTF-8";
        String baseUri = "/";
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());

        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset());

        Files.delete(new File("example.html").toPath());
    }

    @Test
    public void testParseByteDataWithCharsetDetect() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("example.html"));
        String charsetName = null;
        String baseUri = "/";
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());

        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset()); // Charset should be detected from meta tag

        Files.delete(new File("example.html").toPath());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"XYZ-123\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("example.html"));
        String charsetName = "XYZ-123";
        String baseUri = "/";

        try {
            Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
            Assert.fail("Expected IOException when providing invalid charset");
        } catch (IOException e) {
            // Expected
        }

        Files.delete(new File("example.html").toPath());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "Test input content.";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] outputBytes = outputStream.toByteArray();
        String outputContent = new String(outputBytes, StandardCharsets.UTF_8);

        Assert.assertEquals(inputContent, outputContent);

        inputStream.close();
        outputStream.close();
    }
}