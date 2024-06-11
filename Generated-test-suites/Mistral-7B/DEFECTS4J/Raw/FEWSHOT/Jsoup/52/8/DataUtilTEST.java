package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String fileContent = "This is a test.";
        File testFile = new File("testFile.html");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(fileContent.getBytes());
        FileUtils.writeFile(testFile, os.toByteArray());

        Document doc = DataUtil.load(testFile, null, null);
        assertEquals(fileContent, doc.body().html());
        assertNotNull(doc.childNode(0));
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        String fileContent = "This is a test.";
        File testFile = new File("testFile.html");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(fileContent.getBytes("UTF-8"));
        FileUtils.writeFile(testFile, os.toByteArray());

        Document doc = DataUtil.load(testFile, "UTF-8", null);
        assertEquals(fileContent, doc.body().html());
        assertNotNull(doc.childNode(0));
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputContent = "This is a test.";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes());

        Document doc = DataUtil.load(inputStream, null, null);
        assertEquals(inputContent, doc.body().html());
        assertNotNull(doc.childNode(0));
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String inputContent = "This is a test.";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals(inputContent, doc.body().html());
        assertNotNull(doc.childNode(0));
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        byte[] data = "This is a test.".getBytes();
        ByteBuffer byteData = ByteBuffer.wrap(data);

        String expected = "This is a test.";
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals(expected, doc.body().html());
        assertNotNull(doc.childNode(0));
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String charsetName = "UTF-8";
        byte[] data = "This is a test.".getBytes();
        ByteBuffer byteData = ByteBuffer.wrap(data);

        Document doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertEquals("This is a test.", doc.body().html());
        assertNotNull(doc.childNode(0));
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testString = "This is a test.";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        InputStream is = new ByteArrayInputStream(testString.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        DataUtil.crossStreams(is, output);
        String result = output.toString();
        assertEquals(testString, result);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String fileContent = "This is a test.";
        File testFile = new File("testFile.html");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(fileContent.getBytes());
        FileUtils.writeFile(testFile, os.toByteArray(), " invalidCharset");

        try {
            Document doc = DataUtil.load(testFile, "invalidCharset", null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Unknown charset: invalidCharset", e.getMessage());
        }

        FileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testGetCharsetFromContentTypeWithSupportedCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithUnsupportedCharset() {
        String contentType = "text/html; charset=x-pqr";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(DataUtil.defaultCharset, charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals(charset, DataUtil.validateCharset(charset));

        String invalidCharset = "x-pqr";
        assertNull(DataUtil.validateCharset(invalidCharset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertFalse(boundary.isEmpty());
    }
}