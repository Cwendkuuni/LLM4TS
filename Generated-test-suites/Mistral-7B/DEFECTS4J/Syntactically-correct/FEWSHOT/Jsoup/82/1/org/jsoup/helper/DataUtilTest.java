package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.jsoup.helper.DataUtil.getCharsetFromContentType;
import static org.jsoup.helper.DataUtil.load;
import static org.jsoup.helper.DataUtil.load; // redeclare for test in same package
import static org.jsoup.helper.DataUtil.mimeBoundary;
import static org.jsoup.helper.DataUtil.parseInputStream;
import static org.jsoup.helper.DataUtil.readToByteBuffer;
import static org.jsoup.helper.DataUtil.validateCharset;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        String filePath = "path/to/your/html/file.html";
        String charsetName = "UTF-8"; // or null for auto-detection
        String baseUri = "http://test.com";

        Document document = load(new File(filePath), charsetName, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("http://test.com", document.baseUri());
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>";
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = load(inputStream, charsetName, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("http://test.com", document.baseUri());
        Assert.assertEquals("Test", document.body().text());
    }

    @Test
    public void testLoadDocumentWithCustomParser() throws IOException {
        String htmlContent = "<xml version=\"1.0\" encoding=\"UTF-8\"><root><name>Test</name></root></xml>";
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = load(inputStream, charsetName, baseUri, Parser.xmlParser());

        Assert.assertNotNull(document);
        Assert.assertEquals("http://test.com", document.baseUri());
        Assert.assertEquals("Test", document.select("root > name").first().text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String sourceText = "Source text";
        String destinationText = "Destination text";

        String sourceFile = "source.txt";
        String destinationFile = "destination.txt";

        File source = new File(sourceFile);
        File destination = new File(destinationFile);

        try (
            InputStream input = new FileInputStream(source);
            OutputStream output = new FileOutputStream(destination);
        ) {
            DataUtil.crossStreams(input, output);
        }

        String sourceContent = new String(Files.readAllBytes(source.toPath()), StandardCharsets.UTF_8);
        String destinationContent = new String(Files.readAllBytes(destination.toPath()), StandardCharsets.UTF_8);

        Assert.assertEquals(sourceContent, destinationContent);
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>";

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = parseInputStream(inputStream, null, null, Parser.htmlParser());

        Assert.assertNotNull(document);
        Assert.assertEquals("Test", document.body().text());
    }

    @Test
    public void testParseInputStreamWithCustomCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"ISO-8859-1\"></head><body><p>Test</p></body></html>";

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = parseInputStream(inputStream, "ISO-8859-1", null, Parser.htmlParser());

        Assert.assertNotNull(document);
        Assert.assertEquals("Test", document.body().text());
    }

    @Test
    public void testParseInputStreamWithCustomParserAndCharset() throws IOException {
        String xmlContent = "<xml version=\"1.0\" encoding=\"UTF-8\"><root><name>Test</name></root></xml>";

        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = parseInputStream(inputStream, "UTF-8", null, Parser.xmlParser());

        Assert.assertNotNull(document);
        Assert.assertEquals("Test", document.select("root > name").first().text());
    }

    @Test
    public void testDetectCharsetFromBOM() {
        String bomUtf8 = "\uFEFF<html>\n";
        String bomUtf16BE = "\uFFFE\uFEFF<html>\n";
        String bomUtf16LE = "\uFEFF<html>\uFFFE\n";
        String bomUtf32BE = "\uFFFE\uFEFF\uFFFE\uFFFE<html>\n";
        String bomUtf32LE = "\uFFFE\uFFFE\uFFFE\uFFFE<html>\n";

        ByteBuffer bomUtf8ByteBuffer = ByteBuffer.wrap(bomUtf8.getBytes(StandardCharsets.UTF_8));
        ByteBuffer bomUtf16BEByteBuffer = ByteBuffer.wrap(bomUtf16BE.getBytes(StandardCharsets.UTF_8));
        ByteBuffer bomUtf16LEByteBuffer = ByteBuffer.wrap(bomUtf16LE.getBytes(StandardCharsets.UTF_8));
        ByteBuffer bomUtf32BEByteBuffer = ByteBuffer.wrap(bomUtf32BE.getBytes(StandardCharsets.UTF_8));
        ByteBuffer bomUtf32LEByteBuffer = ByteBuffer.wrap(bomUtf32LE.getBytes(StandardCharsets.UTF_8));

        BomCharset utf8Bom = DataUtil.detectCharsetFromBom(bomUtf8ByteBuffer);
        BomCharset utf16BEBom = DataUtil.detectCharsetFromBom(bomUtf16BEByteBuffer);
        BomCharset utf16LEBom = DataUtil.detectCharsetFromBom(bomUtf16LEByteBuffer);
        BomCharset utf32BEBom = DataUtil.detectCharsetFromBom(bomUtf32BEByteBuffer);
        BomCharset utf32LEBom = DataUtil.detectCharsetFromBom(bomUtf32LEByteBuffer);

        Assert.assertNotNull(utf8Bom);
        Assert.assertEquals("UTF-8", utf8Bom.charset);
        Assert.assertFalse(utf8Bom.offset);

        Assert.assertNotNull(utf16BEBom);
        Assert.assertEquals("UTF-16", utf16BEBom.charset);
        Assert.assertFalse(utf16BEBom.offset);

        Assert.assertNotNull(utf16LEBom);
        Assert.assertEquals("UTF-16", utf16LEBom.charset);
        Assert.assertTrue(utf16LEBom.offset);

        Assert.assertNotNull(utf32BEBom);
        Assert.assertEquals("UTF-32", utf32BEBom.charset);
        Assert.assertFalse(utf32BEBom.offset);

        Assert.assertNull(DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[0])));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String textPlain = "text/plain; charset=UTF-8";
        String textHtml = "text/html; charset=ISO-8859-1";
        String textXml = "text/xml; charset=UTF-16";

        String charsetFromTextPlain = getCharsetFromContentType(textPlain);
        String charsetFromTextHtml = getCharsetFromContentType(textHtml);
        String charsetFromTextXml = getCharsetFromContentType(textXml);

        Assert.assertEquals("UTF-8", charsetFromTextPlain);
        Assert.assertEquals("ISO-8859-1", charsetFromTextHtml);
        Assert.assertEquals("UTF-16", charsetFromTextXml);

        Assert.assertNull(getCharsetFromContentType(null));
        Assert.assertNull(getCharsetFromContentType(""));
    }

    @Test
    public void testValidateCharset() {
        String utf8 = "UTF-8";
        String iso8859 = "ISO-8859-1";
        String unsupportedCharset = "X-Pepsi";

        String validatedUtf8 = validateCharset(utf8);
        String validatedIso8859 = validateCharset(iso8859);
        String validatedUnsupportedCharset = validateCharset(unsupportedCharset);

        Assert.assertEquals("UTF-8", validatedUtf8);
        Assert.assertEquals("ISO-8859-1", validatedIso8859);
        Assert.assertNull(validatedUnsupportedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = mimeBoundary();

        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(mimeBoundary.length(), 32);
    }
}