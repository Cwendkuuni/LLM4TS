package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    private String baseUri;
    private File file;

    @Before
    public void setup() {
        baseUri = "http://example.com";
        file = new File("test.html");
    }

    @Test
    public void testLoadWithValidFile() throws IOException {
        String charsetName = "UTF-8";
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadWithInvalidFile() throws IOException {
        String charsetName = "UTF-8";
        file = new File("invalid.html");
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadWithValidInputStream() throws IOException {
        String charsetName = "UTF-8";
        InputStream inputStream = new FileInputStream(file);
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadWithInvalidInputStream() throws IOException {
        String charsetName = "UTF-8";
        InputStream inputStream = new FileInputStream("invalid.html");
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadWithValidParser() throws IOException {
        String charsetName = "UTF-8";
        InputStream inputStream = new FileInputStream(file);
        Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadWithInvalidParser() throws IOException {
        String charsetName = "UTF-8";
        InputStream inputStream = new FileInputStream(file);
        Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.parser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new FileInputStream(file);
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        Assert.assertEquals(file.length(), outputStream.size());
    }

    @Test
    public void testReadToByteBufferWithValidMaxSize() throws IOException {
        int maxSize = 1024;
        InputStream inputStream = new FileInputStream(file);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testReadToByteBufferWithInvalidMaxSize() throws IOException {
        int maxSize = -1;
        InputStream inputStream = new FileInputStream(file);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testReadToByteBufferWithValidInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(file);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentTypeWithValidContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testValidateCharsetWithValidCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertEquals(charset, validatedCharset);
    }

    @Test
    public void testValidateCharsetWithUnsupportedCharset() {
        String charset = "InvalidCharset";
        String validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertNull(validatedCharset);
    }

    @Test
    public void testValidateCharsetWithEmptyCharset() {
        String charset = "";
        String validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBomWithValidBom() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.put((byte) 0xEF);
        byteBuffer.put((byte) 0xBB);
        byteBuffer.put((byte) 0xBF);
        byteBuffer.rewind();
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        Assert.assertNotNull(bomCharset);
        Assert.assertEquals("UTF-8", bomCharset.charset);
        Assert.assertTrue(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomWithInvalidBom() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.put((byte) 0xFF);
        byteBuffer.put((byte) 0xFE);
        byteBuffer.put((byte) 0x00);
        byteBuffer.put((byte) 0x00);
        byteBuffer.rewind();
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        Assert.assertNull(bomCharset);
    }

    @Test
    public void testDocumentLoad() throws IOException {
        String charsetName = "UTF-8";
        InputStream inputStream = new FileInputStream(file);
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testDocumentLoadWithInvalidInputStream() throws IOException {
        String charsetName = "UTF-8";
        InputStream inputStream = new FileInputStream("invalid.html");
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
    }
}