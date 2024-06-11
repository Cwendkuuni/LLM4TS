package org.jsoup.helper.test;

import org.junit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        File inputFile = new File("input/valid.html");
        Document doc = DataUtil.load(inputFile, "UTF-8", "http://example.com");

        // Assertions for the Document object
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());

        // Test parsing of HTML tags and attributes
        Element titleElement = doc.title();
        Assert.assertNotNull(titleElement);
        Assert.assertEquals("Test Page", titleElement.text());

        XmlDeclaration xmlDeclaration = doc.xml();
        Assert.assertNull(xmlDeclaration);

        Element htmlElement = doc.body();
        Assert.assertNotNull(htmlElement);

        Elements bodyElements = doc.select("p");
        Assert.assertFalse(bodyElements.isEmpty());

        String bodyText = doc.body().text();
        Assert.assertEquals("This is a test page.\n" +
                "It contains some paragraphs, which will be parsed correctly.\n" +
                "You can also test <b>HTML</b> tags and attributes, such as <a href=\"link.html\">links</a>.", bodyText);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File inputFile = new File("input/invalid-charset.html");
        try {
            Document doc = DataUtil.load(inputFile, "ISO-8859-1", "http://example.com");
            Assert.fail("Expected IOException for invalid charset");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String htmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>This is a test page.</p>\n" +
                "</body>\n" +
                "</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");

        // Assertions for the Document object
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());

        // Test parsing of HTML tags and attributes
        Element titleElement = doc.title();
        Assert.assertNull(titleElement);

        XmlDeclaration xmlDeclaration = doc.xml();
        Assert.assertNotNull(xmlDeclaration);
        Assert.assertEquals("UTF-8", xmlDeclaration.encoding());

        Element htmlElement = doc.body();
        Assert.assertNotNull(htmlElement);

        Elements bodyElements = doc.select("p");
        Assert.assertFalse(bodyElements.isEmpty());

        String bodyText = doc.body().text();
        Assert.assertEquals("This is a test page.", bodyText);
    }

    @Test
    public void testLoadInputStreamWithMissingCharset() throws IOException {
        String htmlContent = "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>This is a test page.</p>\n" +
                "</body>\n" +
                "</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = null;
        try {
            doc = DataUtil.load(inputStream, null, "http://example.com");
        } catch (IOException e) {
            // Expected
        }
        Assert.assertNull(doc);

        try {
            doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
            Assert.assertNotNull(doc);
        } catch (IOException e) {
            // Unexpected
            e.printStackTrace();
            Assert.fail("Failed to load document from input stream with correct charset.");
        }
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String invalidHtmlContent = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>This is a test page.</p>\n" +
                "</body>\n" +
                "</html>";
        InputStream inputStream = new ByteArrayInputStream(invalidHtmlContent.getBytes(StandardCharsets.UTF_8));
        try {
            Document doc = DataUtil.parseInputStream(inputStream, "ISO-8859-1", "http://example.com", Parser.htmlParser());
            Assert.fail("Expected IOException for invalid charset");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        String sourceContent = "Source content.";
        String targetContent = "Target content.";
        File inputFile = new File("input.txt");
        File outputFile = new File("output.txt");

        try (
            FileWriter inputWriter = new FileWriter(inputFile);
            FileWriter outputWriter = new FileWriter(outputFile)
        ) {
            inputWriter.write(sourceContent);
            inputWriter.close();
            outputWriter.close();
        }

        try (
            InputStream inputStream = new FileInputStream(inputFile);
            OutputStream outputStream = new FileOutputStream(outputFile)
        ) {
            DataUtil.crossStreams(inputStream, outputStream);
        }

        try (
            BufferedReader reader = new BufferedReader(new FileReader(outputFile))
        ) {
            String outputContent = reader.readLine();
            Assert.assertEquals(sourceContent, outputContent);
        }

        inputFile.delete();
        outputFile.delete();
    }

    @Test
    public void testParseInputStreamWithBOM() throws IOException {
        String bomHtmlContent = "\uFEFF<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>This is a test page.</p>\n" +
                "</body>\n" +
                "</html>";
        InputStream inputStream = new ByteArrayInputStream(bomHtmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.xmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testEmptyInputStream() throws IOException {
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        Document doc = DataUtil.parseInputStream(emptyInputStream, null, null, Parser.htmlParser());
        Assert.assertNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>This is a test page.</p>\n" +
                "</body>\n" +
                "</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024 * 1024);

        Assert.assertTrue(byteBuffer.hasArray());
        Assert.assertEquals(htmlContent.getBytes(StandardCharsets.UTF_8).length, byteBuffer.limit());
    }
}