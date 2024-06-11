package org.jsoup.helper.test;

import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("testData/validHtmlFile.html"); // replace with valid html file path
        Document document = DataUtil.load(file, null);
        assertNotNull(document);
        assertEquals("html", document.tagName());
    }

    @Test
    public void testLoadFileWithSpecificCharset() throws IOException {
        File file = new File("testData/validUtf16File.html"); // replace with valid UTF-16 html file path
        Document document = DataUtil.load(file, "UTF-16");
        assertNotNull(document);
        assertEquals("html", document.tagName());
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("testData/validHtmlFile.html"); // replace with valid html file path
        Document document = DataUtil.load(file, "InvalidCharset");
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null);
        assertNotNull(document);
        assertEquals("html", document.tagName());
    }

    @Test
    public void testLoadInputStreamWithSpecificCharset() throws IOException {
        String htmlContent = "<html><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.ISO_8859_1));
        Document document = DataUtil.load(inputStream, "ISO-8859-1");
        assertNotNull(document);
        assertEquals("html", document.tagName());
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "InvalidCharset");
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        String xmlContent = "<root><data>Test</data></root>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertNotNull(document);
        assertEquals("root", document.tagName());
        Element dataElement = document.selectFirst("data");
        assertNotNull(dataElement);
        assertEquals("Test", dataElement.text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String sourceContent = "Source content";
        String targetContent = "Target content";
        String sourceFile = "source.txt";
        String targetFile = "target.txt";

        File sourceFileObj = new File(sourceFile);
        File targetFileObj = new File(targetFile);

        // Write data to source file
        try (OutputStream outputStream = new FileOutputStream(sourceFileObj)) {
            outputStream.write(sourceContent.getBytes(StandardCharsets.UTF_8));
        }

        // Cross stream test
        try (InputStream inputStream = new FileInputStream(sourceFileObj);
             OutputStream outputStream = new FileOutputStream(targetFileObj)) {
            DataUtil.crossStreams(inputStream, outputStream);
        }

        // Read data from target file
        try (BufferedReader reader = new BufferedReader(new FileReader(targetFileObj))) {
            assertEquals(sourceContent, reader.readLine());
        }

        // Delete created files
        sourceFileObj.delete();
        targetFileObj.delete();
    }
}