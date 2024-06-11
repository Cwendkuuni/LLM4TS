package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Test class for DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "path/to/your/file.html";
        File file = new File(filePath);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        String htmlContent = "<html><head><title>Test</title></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        String xmlContent = "<xml><message>Hello World</message></xml>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("message").text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputFilePath = "path/to/inputFile.html";
        String outputFilePath = "path/to/outputFile.html";
        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document inputDoc = DataUtil.load(inputFile, charsetName, baseUri);
        Document outputDoc = DataUtil.load(outputFile, charsetName, baseUri);

        assertEquals(inputDoc.body().text(), outputDoc.body().text());

        inputFile.delete();
        outputFile.delete();
    }

    @Test(expected = IOException.class)
    public void testLoadFileInvalidCharset() throws IOException {
        String filePath = "path/to/your/file.html";
        File file = new File(filePath);
        String charsetName = "nonExistentCharset";
        String baseUri = "http://example.com";
        DataUtil.load(file, charsetName, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamInvalidCharset() throws IOException {
        String charsetName = "nonExistentCharset";
        String baseUri = "http://example.com";
        String htmlContent = "<html><head><title>Test</title></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String htmlContent = "<html><head><title>Test</title></head><body>Hello World</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String htmlContent = "<html><head><title>Test</title></head><body>Hello World</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        Document doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseByteDataWithMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        String content1 = "Content 1";
        String content2 = "Content 2";
        String boundaryContent = "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"content1\"\r\n\r\n"
                + content1 + "\r\n"
                + "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"content2\"\r\n\r\n"
                + content2 + "\r\n"
                + "--" + boundary + "--\r\n";
        ByteBuffer byteData = ByteBuffer.wrap(content1.getBytes(StandardCharsets.UTF_8)
                + "\r\n".getBytes(StandardCharsets.UTF_8)
                + boundary.getBytes(StandardCharsets.UTF_8)
                + "\r\n"
                + content2.getBytes(StandardCharsets.UTF_8)
                + "\r\n"
                + "--" + boundary + "--\r\n");
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(2, doc.select("form[enctype='multipart/form-data']").size());
        Element content1Element = doc.select("form[enctype='multipart/form-data']").get(0).select("input[name='content1']").first();
        assertEquals(content1, content1Element.text());
        Element content2Element = doc.select("form[enctype='multipart/form-data']").get(0).select("input[name='content2']").first();
        assertEquals(content2, content2Element.text());
    }
}