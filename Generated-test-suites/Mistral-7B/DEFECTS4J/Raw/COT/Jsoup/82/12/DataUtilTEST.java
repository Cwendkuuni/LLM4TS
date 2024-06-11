package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class DataUtilTest {
    private Document htmlDocument;
    private Document xmlDocument;
    private Parser parser;

    @Before
    public void setUp() {
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html"); // Replace with the actual test file path
        Document document = DataUtil.load(file, null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html"); // Replace with the actual test file path
        Document document = DataUtil.load(file, "UTF-8");
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document document = DataUtil.load(inputStream, null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8");
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document document = DataUtil.load(inputStream, null, null, parser);
        Assert.assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("test.input");
        File outputFile = new File("test.output");

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        Assert.assertTrue(outputFile.length() > 0);
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document document = DataUtil.parseInputStream(inputStream, null, null, parser);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, parser);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithSpecifiedParser() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, null, null, parser);
        Assert.assertNotNull(document);

        XmlDeclaration xmlDeclaration = new XmlDeclaration("1.0", "UTF-8", null);
        document.insertBefore(xmlDeclaration, document.head());
        document = DataUtil.parseInputStream(inputStream, null, null, parser.xmlParser());
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        DataUtil.parseInputStream(inputStream, "Invalid_Charset", null, parser);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=ISO-8859-1; invalid";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertEquals("UTF-8", validatedCharset);

        String invalidCharset = "Invalid_Charset";
        String validatedInvalidCharset = DataUtil.validateCharset(invalidCharset);
        Assert.assertNull(validatedInvalidCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertEquals(DataUtil.boundaryLength, boundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        StringBuilder bom = new StringBuilder();
        bom.append((char) 0xFF).append((char) 0xFE).append((char) 0); // LE BOM
        ByteBuffer byteBuffer = Charset.forName("UTF-16").decode(new InputStreamReader(new ByteArrayInputStream(bom.toString().getBytes())));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        Assert.assertNotNull(bomCharset);
        Assert.assertEquals("UTF-16", bomCharset.charset);
        Assert.assertFalse(bomCharset.offset);

        bom.setLength(0);
        bom.append((char) 0x00).append((char) 0x00).append((char) 0xFE).append((char) 0xFF); // BE BOM
        byteBuffer = Charset.forName("UTF-32").decode(new InputStreamReader(new ByteArrayInputStream(bom.toString().getBytes())));
        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        Assert.assertNotNull(bomCharset);
        Assert.assertEquals("UTF-32", bomCharset.charset);
        Assert.assertTrue(bomCharset.offset);

        bom.setLength(0);
        bom.append((char) 0xEF).append((char) 0xBB).append((char) 0xBF); // UTF-8 BOM
        byteBuffer = Charset.forName("UTF-8").decode(new InputStreamReader(new ByteArrayInputStream(bom.toString().getBytes())));
        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        Assert.assertNotNull(bomCharset);
        Assert.assertEquals("UTF-8", bomCharset.charset);
        Assert.assertTrue(bomCharset.offset);

        bom.setLength(0);
        bom.append("Invalid_BOM");
        byteBuffer = Charset.forName("UTF-8").decode(new InputStreamReader(new ByteArrayInputStream(bom.toString().getBytes())));
        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        Assert.assertNull(bomCharset);
    }
}