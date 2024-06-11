package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        File file = new File("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charset, baseUri);

        // Test normal case
        Assert.assertNotNull(document);
        Assert.assertEquals(DocumentType.HTML, document.documentType());
        Assert.assertTrue(document.body().text().length() > 0);

        // Test empty file
        file = new File("empty.html");
        document = DataUtil.load(file, charset, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(DocumentType.HTML, document.documentType());
        Assert.assertEquals("", document.body().text());

        // Test invalid file
        file = new File("invalid.txt");
        try {
            document = DataUtil.load(file, charset, baseUri);
            Assert.fail();
        } catch (IOException e) {
            Assert.assertNotNull(e.getMessage());
        }

        // Test invalid charset
        charset = "invalid_charset";
        try {
            document = DataUtil.load(file, charset, baseUri);
            Assert.fail();
        } catch (IOException e) {
            Assert.assertNotNull(e.getMessage());
        }

        // Test null input
        InputStream input = null;
        try {
            document = DataUtil.load(input, charset, baseUri);
            Assert.fail();
        } catch (IOException e) {
            Assert.assertNotNull(e.getMessage());
        }

        // Test valid input stream
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        document = DataUtil.load(inputStream, charset, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(DocumentType.HTML, document.documentType());
        Assert.assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testParseDocumentFromInputStream() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        // Test normal case
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, charset, baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals(DocumentType.HTML, document.documentType());
        Assert.assertEquals("Hello World", document.body().text());

        // Test empty input stream
        inputStream = new ByteArrayInputStream(new byte[0]);
        document = DataUtil.parseInputStream(inputStream, charset, baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals(DocumentType.HTML, document.documentType());
        Assert.assertEquals("", document.body().text());

        // Test invalid charset
        charset = "invalid_charset";
        document = DataUtil.parseInputStream(inputStream, charset, baseUri, Parser.htmlParser());
        Assert.assertNull(document);

        // Test null input
        inputStream = null;
        try {
            document = DataUtil.parseInputStream(inputStream, charset, baseUri, Parser.htmlParser());
            Assert.fail();
        } catch (IOException e) {
            Assert.assertNotNull(e.getMessage());
        }

        // Test valid input stream with specified parser
        String xml = "<xml><data>Hello World</data></xml>";
        Parser xmlParser = Parser.xmlParser();
        document = DataUtil.parseInputStream(inputStream, charset, baseUri, xmlParser);
        Assert.assertNotNull(document);
        Assert.assertEquals(DocumentType.XML, document.documentType());
        Assert.assertEquals("Hello World", document.selectFirst("data").text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputCharset = "UTF-8";
        String outputCharset = "UTF-16";
        InputStream input = new FileInputStream(new File("input.html"));
        OutputStream output = new RandomAccessFile("output.txt", "rw");

        try {
            DataUtil.crossStreams(input, output);
        } finally {
            input.close();
            output.close();
        }

        // Test normal case
        Document document = DataUtil.load(new File("output.txt"), outputCharset, null);
        Assert.assertNotNull(document);
        Assert.assertEquals(DocumentType.HTML, document.documentType());
        Assert.assertTrue(document.body().text().length() > 0);

        // Test empty input
        input = new FileInputStream(new File("empty.html"));
        try {
            DataUtil.crossStreams(input, output);
        } finally {
            input.close();
            output.close();
        }
        document = DataUtil.load(new File("output.txt"), outputCharset, null);
        Assert.assertNotNull(document);
        Assert.assertEquals(DocumentType.HTML, document.documentType());
        Assert.assertEquals("", document.body().text());

        // Test error on input
        input = new FileInputStream(new File("error.txt"));
        try {
            DataUtil.crossStreams(input, output);
        } finally {
            input.close();
            output.close();
        }
        document = DataUtil.load(new File("output.txt"), outputCharset, null);
        Assert.assertNull(document);

        // Test error on output
        output = new RandomAccessFile("output.txt", "rw");
        try {
            DataUtil.crossStreams(new FileInputStream(new File("input.html")), output);
        } finally {
            output.close();
        }
        document = DataUtil.load(new File("output.txt"), outputCharset, null);
        Assert.assertNull(document);
    }
}