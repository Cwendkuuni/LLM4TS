package org.jsoup.helper.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document document = DataUtil.load(testFile, charsetName, baseUri);
        Assertions.assertNotNull(document);
        Assertions.assertFalse(document.outputSettings().isSimple());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"ISO-8859-1\"></meta></head><body>Test content</body></html>";
        InputStream testInputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://test.com";
        Document document = DataUtil.load(testInputStream, null, baseUri);
        Assertions.assertNotNull(document);
        Assertions.assertEquals("ISO-8859-1", document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"ISO-8859-1\"></meta></head><body>Test content</body></html>";
        InputStream testInputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8"; // user requests UTF-8 charset
        String baseUri = "http://test.com";
        Document document = DataUtil.load(testInputStream, charsetName, baseUri, Parser.htmlParser());
        Assertions.assertNotNull(document);
        Assertions.assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedParser() throws IOException {
        String htmlContent = "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\"><head><meta charset=\"UTF-8\"></meta></head><body><xsi:schema xmlns=\"xml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" version=\"1.0\"><element name=\"root\"/></xsi:schema></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document document = DataUtil.load(testInputStream, charsetName, baseUri, Parser.xmlParser());
        Assertions.assertNotNull(document);
        Assertions.assertEquals("UTF-8", document.outputSettings().charset());
        Assertions.assertFalse(document.outputSettings().isSimple());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "<html><body><h1>Test</h1></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));
        DataUtil.crossStreams(inputStream, outputStream);
        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assertions.assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String inputContent = "<html><head><meta charset=\"ISO-8859-1\"></meta></head><body>Test content</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(inputContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assertions.assertNotNull(document);
        Assertions.assertEquals("ISO-8859-1", document.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String inputContent = "<html><head><meta charset=\"ISO-8859-1\"></meta></head><body>Test content</body></html>";
        String charsetName = "UTF-8";
        ByteBuffer byteData = ByteBuffer.wrap(inputContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        Assertions.assertNotNull(document);
        Assertions.assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testParseByteDataInvalidCharset() {
        String inputContent = "<html><head><meta charset=\"InvalidCharset\"></meta></head><body>Test content</body></html>";
        String charsetName = "UTF-8";
        ByteBuffer byteData = ByteBuffer.wrap(inputContent.getBytes(StandardCharsets.UTF_8));
        Assertions.assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser()));
    }
}