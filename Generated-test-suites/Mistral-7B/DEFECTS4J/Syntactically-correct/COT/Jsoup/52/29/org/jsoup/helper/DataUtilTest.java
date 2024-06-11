package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "Hello World";
        File file = new File("testFile.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes());
        FileUtils.writeByteArrayToFile(file, outputStream.toByteArray());
        Document document = DataUtil.load(file, "UTF-8", null);
        assertEquals("Hello World", document.body().text());
        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "Hello World";
        byte[] inputStreamData = inputStreamContent.getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputStreamData);
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        String inputStreamContent = "<xml><message>Hello World</message></xml>";
        byte[] inputStreamData = inputStreamContent.getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputStreamData);
        Document document = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        Element messageElement = document.select("message").first();
        assertEquals("Hello World", messageElement.text());
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        String fileContent = "Hello World";
        File file = new File("testFile.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes());
        FileUtils.writeByteArrayToFile(file, outputStream.toByteArray());
        Document document = null;
        try {
            document = DataUtil.load(file, "INVALID_CHARSET", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String documentData = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(documentData.getBytes());
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("Hello World", document.body().select("h1").text());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String documentData = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(documentData.getBytes());
        String charset = "UTF-8";
        Document document = DataUtil.parseByteData(byteData, charset, null, Parser.htmlParser());
        assertEquals("Hello World", document.body().select("h1").text());
    }

    @Test
    public void testParseByteDataWithMetaDataCharset() {
        String documentData = "<html><head><meta charset=\"UTF-16\"><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(documentData.getBytes());
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("Hello World", document.body().select("h1").text());
    }

    @Test
    public void testParseByteDataWithXmlDeclarationCharset() {
        String documentData = "<xml version=\"1.0\" encoding=\"UTF-32\"><body><h1>Hello World</h1></body></xml>";
        ByteBuffer byteData = ByteBuffer.wrap(documentData.getBytes());
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.xmlParser());
        assertEquals("Hello World", document.body().select("h1").text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String source = "Source text";
        String target = "Target text";
        ByteArrayOutputStream sourceOutputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream targetOutputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(new ByteArrayInputStream(source.getBytes()), new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                targetOutputStream.write(b);
            }
        });
        assertEquals(source, targetOutputStream.toString());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String invalidCharset = "INVALID_CHARSET";
        assertEquals(charset, DataUtil.validateCharset(charset));
        assertNull(DataUtil.validateCharset(invalidCharset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertFalse(boundary.isEmpty());
    }

    @Test
    public void testDetectCharsetFromBomWithUTF8Bom() {
        String bom = "\uFEFFHello World";
        ByteBuffer byteData = ByteBuffer.wrap(bom.getBytes());
        String charset = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testDetectCharsetFromBomWithUTF16Bom() {
        String bom = "\uFEFF\uFFFEHello World";
        ByteBuffer byteData = ByteBuffer.wrap(bom.getBytes());
        String charset = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-16", charset);
    }

    @Test
    public void testDetectCharsetFromBomWithUTF32Bom() {
        String bom = "\uFEFF\uFFFE\uFFFEHello World";
        ByteBuffer byteData = ByteBuffer.wrap(bom.getBytes());
        String charset = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-32", charset);
    }

    @Test
    public void testDetectCharsetFromBomWithNoBom() {
        ByteBuffer byteData = ByteBuffer.wrap("Hello World".getBytes());
        String charset = DataUtil.detectCharsetFromBom(byteData, null);
        assertNull(charset);
    }
}