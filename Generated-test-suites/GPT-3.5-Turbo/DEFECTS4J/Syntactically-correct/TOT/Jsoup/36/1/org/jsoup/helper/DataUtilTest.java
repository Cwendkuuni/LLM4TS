package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;

public class DataUtilTest {

    private final String baseUri = "http://www.example.com/";
    private final String utf8Charset = "UTF-8";
    private final String gbkCharset = "GBK";
    private final String invalidCharset = "InvalidCharset";

    private final String validFilePath = "src/test/resources/test.html";
    private final String invalidFilePath = "src/test/resources/nonexistent.html";

    private Document validDocument;

    @Test
    public void testLoad_ValidFile_ValidCharset_ValidBaseUri_ReturnsDocument() throws IOException {
        File validFile = new File(validFilePath);
        validDocument = DataUtil.load(validFile, utf8Charset, baseUri);
        Assert.assertNotNull(validDocument);
    }

    @Test(expected = IOException.class)
    public void testLoad_InvalidFile_ValidCharset_ValidBaseUri_ThrowsIOException() throws IOException {
        File invalidFile = new File(invalidFilePath);
        DataUtil.load(invalidFile, utf8Charset, baseUri);
    }

    @Test
    public void testLoad_ValidInputStream_ValidCharset_ValidBaseUri_ReturnsDocument() throws IOException {
        InputStream validInputStream = new FileInputStream(validFilePath);
        validDocument = DataUtil.load(validInputStream, utf8Charset, baseUri);
        Assert.assertNotNull(validDocument);
    }

    @Test(expected = IOException.class)
    public void testLoad_InvalidInputStream_ValidCharset_ValidBaseUri_ThrowsIOException() throws IOException {
        InputStream invalidInputStream = new FileInputStream(invalidFilePath);
        DataUtil.load(invalidInputStream, utf8Charset, baseUri);
    }

    @Test
    public void testLoad_ValidInputStream_ValidCharset_ValidBaseUri_ValidParser_ReturnsDocument() throws IOException {
        InputStream validInputStream = new FileInputStream(validFilePath);
        validDocument = DataUtil.load(validInputStream, utf8Charset, baseUri, Parser.htmlParser());
        Assert.assertNotNull(validDocument);
    }

    @Test
    public void testParseByteData_ValidByteData_ValidCharset_ValidBaseUri_ValidParser_ReturnsDocument() {
        ByteBuffer validByteData = ByteBuffer.wrap("<html><body><h1>Hello World!</h1></body></html>".getBytes(utf8Charset));
        Document validDoc = DataUtil.parseByteData(validByteData, utf8Charset, baseUri, Parser.htmlParser());
        Assert.assertNotNull(validDoc);
    }

    @Test
    public void testParseByteData_NullCharset_ValidByteData_ValidBaseUri_ValidParser_ReturnsDocument() {
        ByteBuffer validByteData = ByteBuffer.wrap("<html><body><h1>Hello World!</h1></body></html>".getBytes(utf8Charset));
        Document validDoc = DataUtil.parseByteData(validByteData, null, baseUri, Parser.htmlParser());
        Assert.assertNotNull(validDoc);
    }

    @Test
    public void testParseByteData_InvalidCharset_ValidByteData_ValidBaseUri_ValidParser_ReturnsDocument() {
        ByteBuffer validByteData = ByteBuffer.wrap("<html><body><h1>Hello World!</h1></body></html>".getBytes(utf8Charset));
        Document validDoc = DataUtil.parseByteData(validByteData, invalidCharset, baseUri, Parser.htmlParser());
        Assert.assertNotNull(validDoc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadToByteBuffer_NegativeMaxSize_ThrowsIllegalArgumentException() throws IOException {
        FileInputStream validFileInputStream = new FileInputStream(validFilePath);
        DataUtil.readToByteBuffer(validFileInputStream, -1);
    }

    @Test
    public void testReadToByteBuffer_ZeroMaxSize_ReadsFullInputStream() throws IOException {
        FileInputStream validFileInputStream = new FileInputStream(validFilePath);
        ByteBuffer byteData = DataUtil.readToByteBuffer(validFileInputStream, 0);
        Assert.assertTrue(byteData.limit() > 0);
    }

    @Test
    public void testReadToByteBuffer_PositiveMaxSize_ReadsLimitedBytes() throws IOException {
        FileInputStream validFileInputStream = new FileInputStream(validFilePath);
        ByteBuffer byteData = DataUtil.readToByteBuffer(validFileInputStream, 1024);
        Assert.assertTrue(byteData.limit() > 0 && byteData.limit() <= 1024);
    }

    @Test
    public void testReadToByteBuffer_ValidInputStream_ReadsAllBytes() throws IOException {
        FileInputStream validFileInputStream = new FileInputStream(validFilePath);
        ByteBuffer byteData = DataUtil.readToByteBuffer(validFileInputStream);
        Assert.assertTrue(byteData.limit() > 0);
    }

    @Test
    public void testGetCharsetFromContentType_ValidContentType_ReturnsCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(utf8Charset, charset);
    }

    @Test
    public void testGetCharsetFromContentType_ValidContentType_InvalidCharset_ReturnsCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(invalidCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentType_InvalidContentType_ReturnsNull() {
        String contentType = "application/json";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

}