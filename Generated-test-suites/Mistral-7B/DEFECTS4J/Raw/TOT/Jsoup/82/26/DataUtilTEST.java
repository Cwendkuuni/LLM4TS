package org.jsoup.helper.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class DataUtilTest {

    private DataUtil dataUtil;

    @Before
    public void setup() {
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File inputFile = new File("test.html");
        Document document = dataUtil.load(inputFile, null, null);
        assertEquals("expected HTML content", "<html><body><h1>Hello World</h1></body></html>", document.body().html());
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File inputFile = new File("test.html");
        Document document = dataUtil.load(inputFile, "UTF-8", null);
        assertEquals("expected HTML content with UTF-8 charset", "<html><body><h1>Hello World</h1></body></html>", document.body().html());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = dataUtil.load(inputStream, null, null, Parser.htmlParser());
        assertEquals("expected HTML content", "<html><body><h1>Hello World</h1></body></html>", document.body().html());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = dataUtil.load(inputStream, "UTF-8", null, Parser.htmlParser());
        assertEquals("expected HTML content with UTF-8 charset", "<html><body><h1>Hello World</h1></body></html>", document.body().html());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        try {
            Document document = dataUtil.load(inputStream, "ISO-8859-1", null, Parser.htmlParser());
            // test should fail here
        } catch (IOException e) {
            assertEquals("expected 'charset not supported: ISO-8859-1' exception", "charset not supported: ISO-8859-1", e.getMessage());
        }
    }

    @Test
    public void testParseInputStreamWithBOM() throws IOException {
        String htmlContentWithBom = "\uFEFF<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContentWithBom.getBytes(StandardCharsets.UTF_8));
        Document document = dataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertEquals("expected HTML content with BOM", "<html><body><h1>Hello World</h1></body></html>", document.body().html());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        try {
            Document document = dataUtil.parseInputStream(inputStream, "ISO-8859-1", null, Parser.htmlParser());
            // test should fail here
        } catch (IOException e) {
            assertEquals("expected 'charset not supported: ISO-8859-1' exception", "charset not supported: ISO-8859-1", e.getMessage());
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        String source = "Source content.";
        String target = "Target content.";
        File inputFile = new File("input.txt");
        File outputFile = new File("output.txt");
        try (
            InputStream inputStream = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));
            OutputStream outputStream = new FileOutputStream(outputFile)) {
            dataUtil.crossStreams(inputStream, outputStream);
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            assertEquals("expected 'Target content.'", target, reader.readLine());
        } catch (IOException e) {
            // test should not
            // reach here
        } finally {
            outputFile.delete();
        }
    }
}