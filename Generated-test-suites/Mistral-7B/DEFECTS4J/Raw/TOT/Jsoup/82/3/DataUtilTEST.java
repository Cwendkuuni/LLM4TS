package org.jsoup.helper.test;

import org.junit.Assert;
import org.jsoup.DataUtil;
import org.jsoup.helper.DataUtilTest;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class DataUtilTest {

    @org.junit.Test
    public void testLoadDocumentFromFile() throws IOException {
        String inputFilePath = "testFiles/input.html";
        String expectedCharset = "UTF-8";
        String expectedBaseUri = "http://example.com";

        File inputFile = new File(inputFilePath);
        Document doc = DataUtil.load(inputFile, expectedCharset, expectedBaseUri);

        // Check document content and structure
        Assert.assertNotNull(doc);
        Assert.assertEquals(expectedBaseUri, doc.baseUri());
        Assert.assertEquals("HTML", doc.tagName());
        Assert.assertNotNull(doc.title());
        Assert.assertNotNull(doc.body());
        Assert.assertFalse(doc.body().isEmpty());

        // Test loading document with invalid charset
        String invalidCharset = "invalidCharset";
        Document docWithInvalidCharset = null;
        try {
            docWithInvalidCharset = DataUtil.load(inputFile, invalidCharset, expectedBaseUri);
        } catch (IOException e) {
            Assert.assertNotNull(e.getMessage());
        }
        Assert.assertNull(docWithInvalidCharset);

        // Test loading document with null charset
        Document docWithNullCharset = DataUtil.load(inputFile, null, expectedBaseUri);
        Assert.assertNotNull(docWithNullCharset);
        Assert.assertEquals(expectedCharset, docWithNullCharset.outputSettings().charset().name());
    }

    @org.junit.Test
    public void testParseInputStream() throws IOException {
        String inputHtml = "<html lang=\"en\"><head><meta charset=\"UTF-8\"/><title>Test</title></head><body><p>Hello World</p></body></html>";
        byte[] inputBytes = inputHtml.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(inputBytes);

        String expectedCharset = "UTF-8";
        String expectedBaseUri = null;
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseInputStream(inputStream, expectedCharset, expectedBaseUri, parser);

        // Check document content and structure
        Assert.assertNotNull(doc);
        Assert.assertEquals(expectedBaseUri, doc.baseUri());
        Assert.assertEquals("HTML", doc.tagName());
        Assert.assertNotNull(doc.title());
        Assert.assertNotNull(doc.body());
        Assert.assertFalse(doc.body().isEmpty());
        Assert.assertEquals("Hello World", doc.body().text());

        // Test parsing with invalid charset
        String invalidCharset = "invalidCharset";
        Document docWithInvalidCharset = null;
        try {
            docWithInvalidCharset = DataUtil.parseInputStream(inputStream, invalidCharset, expectedBaseUri, parser);
        } catch (IOException e) {
            Assert.assertNotNull(e.getMessage());
        }
        Assert.assertNull(docWithInvalidCharset);

        // Test parsing with null charset
        Document docWithNullCharset = DataUtil.parseInputStream(inputStream, null, expectedBaseUri, parser);
        Assert.assertNotNull(docWithNullCharset);
        Assert.assertEquals(expectedCharset, docWithNullCharset.outputSettings().charset().name());
    }

    @org.junit.Test
    public void testWriteToOutputStream() throws IOException {
        String inputHtml = "<html lang=\"en\"><head><meta charset=\"UTF-8\"/><title>Test</title></head><body><p>Hello World</p></body></html>";
        byte[] inputBytes = inputHtml.getBytes(StandardCharsets.UTF_8);

        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        OutputStream outputStream = new ByteArrayOutputStream();

        String charsetName = "UTF-8";

        DataUtil.crossStreams(inputStream, outputStream);

        // Check written bytes
        byte[] outputBytes = outputStream.toByteArray();
        Assert.assertArrayEquals(inputBytes, outputBytes);

        // Test writing with null charset
        DataUtil.crossStreams(inputStream, outputStream);
        outputBytes = outputStream.toByteArray();
        Assert.assertEquals(inputBytes.length, outputBytes.length);
    }

    @org.junit.Test
    public void testDetectCharsetFromBom() {
        String bomUtf8 = "\ufeff\u0000\u0001";
        String bomUtf16BE = "\u00fe\u00ff";
        String bomUtf16LE = "\u00ff\u00fe";
        String bomUtf32BE = "\u0000\u0000\u00fe\u00ff";
        String bomUtf32LE = "\u00ff\u00fe\u0000\u0000";

        ByteBuffer bomUtf8ByteBuffer = StandardCharsets.UTF_8.decode(bomUtf8.getBytes(StandardCharsets.UTF_8));
        ByteBuffer bomUtf16BEByteBuffer = StandardCharsets.UTF_16BE.decode(bomUtf16BE.getBytes(StandardCharsets.UTF_8));
        ByteBuffer bomUtf16LEByteBuffer = StandardCharsets.UTF_16LE.decode(bomUtf16LE.getBytes(StandardCharsets.UTF_8));
        ByteBuffer bomUtf32BEByteBuffer = StandardCharsets.UTF_32BE.decode(bomUtf32BE.getBytes(StandardCharsets.UTF_8));
        ByteBuffer bomUtf32LEByteBuffer = StandardCharsets.UTF_32LE.decode(bomUtf32LE.getBytes(StandardCharsets.UTF_8));

        BomCharset utf8Bom = DataUtil.detectCharsetFromBom(bomUtf8ByteBuffer);
        BomCharset utf16BEBom = DataUtil.detectCharsetFromBom(bomUtf16BEByteBuffer);
        BomCharset utf16LEBom = DataUtil.detectCharsetFromBom(bomUtf16LEByteBuffer);
        BomCharset utf32BEBom = DataUtil.detectCharsetFromBom(bomUtf32BEByteBuffer);
        BomCharset utf32LEBom = DataUtil.detectCharsetFromBom(bomUtf32LEByteBuffer);

        Assert.assertEquals("UTF-8", utf8Bom.charset);
        Assert.assertFalse(utf8Bom.offset);
        Assert.assertEquals("UTF-16 BE", utf16BEBom.charset);
        Assert.assertFalse(utf16BEBom.offset);
        Assert.assertEquals("UTF-16 LE", utf16LEBom.charset);
        Assert.assertFalse(utf16LEBom.offset);
        Assert.assertEquals("UTF-32 BE", utf32BEBom.charset);
        Assert.assertTrue(utf32BEBom.offset);
        Assert.assertEquals("UTF-32 LE", utf32LEBom.charset);
        Assert.assertTrue(utf32LEBom.offset);
    }

    @org.junit.Test
    public void testGetCharsetFromContentType() {
        String contentTypeUtf8 = "text/html; charset=UTF-8";
        String contentTypeUtf16 = "text/html; charset=utf-16";
        String contentTypeInvalid = "text/html; charset=Invalid";

        String charsetUtf8 = DataUtil.getCharsetFromContentType(contentTypeUtf8);
        String charsetUtf16 = DataUtil.getCharsetFromContentType(contentTypeUtf16);
        String charsetInvalid = DataUtil.getCharsetFromContentType(contentTypeInvalid);

        Assert.assertEquals("UTF-8", charsetUtf8);
        Assert.assertEquals("utf-16", charsetUtf16);
        Assert.assertNull(charsetInvalid);
    }

    @org.junit.Test
    public void testValidateCharset() {
        String validCharsetUtf8 = "UTF-8";
        String validCharsetUtf16 = "utf-16";
        String emptyCharset = "";
        String invalidCharset = "invalidCharset";

        String validatedCharsetUtf8 = DataUtil.validateCharset(validCharsetUtf8);
        String validatedCharsetUtf16 = DataUtil.validateCharset(validCharsetUtf16);
        String validatedEmptyCharset = DataUtil.validateCharset(emptyCharset);
        String validatedInvalidCharset = DataUtil.validateCharset(invalidCharset);

        Assert.assertEquals("UTF-8", validatedCharsetUtf8);
        Assert.assertEquals("utf-16", validatedCharsetUtf16);
        Assert.assertEquals("", validatedEmptyCharset);
        Assert.assertNull(validatedInvalidCharset);
    }

    @org.junit.Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        Assert.assertEquals(boundaryLength, mimeBoundary.length());
        for (int i = 0; i < boundaryLength; i++) {
            Assert.assertTrue(Character.isAlphabetic(mimeBoundary.charAt(i)));
        }
    }
}