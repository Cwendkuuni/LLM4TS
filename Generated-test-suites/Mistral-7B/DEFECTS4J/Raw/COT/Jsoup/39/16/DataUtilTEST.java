package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
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
 * Tests for the DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadFileDocument() throws IOException {
        String fileContent = "This is a test file.";
        File testFile = new File("testFile.html");
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document doc = DataUtil.load(testFile, "UTF-8", null);
        assertEquals(fileContent, doc.body().html());

        testFile.delete();
    }

    @Test(expected = IOException.class)
    public void testLoadFileDocumentWithNullCharset() throws IOException {
        File testFile = new File("testFile.html");
        Document doc = DataUtil.load(testFile, null, null);
    }

    @Test(expected = IOException.class)
    public void testLoadFileDocumentWithInvalidCharset() throws IOException {
        File testFile = new File("testFile.html");
        Document doc = DataUtil.load(testFile, "ABCDEF", null);
    }

    @Test
    public void testLoadInputStreamDocument() throws IOException {
        String testContent = "This is a test stream.";
        InputStream testStream = new ByteArrayInputStream(testContent.getBytes());

        Document doc = DataUtil.load(testStream, "UTF-8", null);
        assertEquals(testContent, doc.body().html());
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamDocumentWithNullCharset() throws IOException {
        String testContent = "This is a test stream.";
        InputStream testStream = new ByteArrayInputStream(testContent.getBytes());
        Document doc = DataUtil.load(testStream, null);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamDocumentWithInvalidCharset() throws IOException {
        String testContent = "This is a test stream.";
        InputStream testStream = new ByteArrayInputStream(testContent.getBytes());
        Document doc = DataUtil.load(testStream, "ABCDEF");
    }

    @Test
    public void testLoadInputStreamDocumentWithParser() throws IOException {
        String testContent = "This is a test stream.";
        InputStream testStream = new ByteArrayInputStream(testContent.getBytes());

        Document doc = DataUtil.load(testStream, "UTF-8", null, Parser.xmlParser());
        assertEquals(testContent, doc.body().html());
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithInvalidStream() throws IOException {
        InputStream invalidStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException();
            }
        };
        ByteBuffer buffer = DataUtil.readToByteBuffer(invalidStream);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testContent = "Test content.";
        InputStream testStream = new ByteArrayInputStream(testContent.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testStream);

        assertEquals(testContent, Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    @Test
    public void testParseByteDataWithUtf8() throws IOException {
        String testContent = "Test content.";
        ByteBuffer byteBuffer = ByteBuffer.wrap(testContent.getBytes());
        Document doc = DataUtil.parseByteData(byteBuffer, "UTF-8", null, Parser.htmlParser());

        assertEquals(testContent, doc.body().html());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String testContent = "Content-Type: text/html; charset=ISO-8859-1\r\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n<title>Test</title>\n</head>\n<body>\n<p>Test content.</p>\n</body>\n</html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(testContent.getBytes());
        Document doc = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());

        assertEquals("Test content.", doc.body().text());
    }

    @Test(expected = IllegalCharsetNameException.class)
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String testContent = "Test content.";
        ByteBuffer byteBuffer = ByteBuffer.wrap(testContent.getBytes());
        Document doc = DataUtil.parseByteData(byteBuffer, "XXX-8859-1", null, Parser.htmlParser());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String testContentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(testContentType);

        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithMissingCharset() {
        String testContentType = "text/html; charset=missing";
        String charset = DataUtil.getCharsetFromContentType(testContentType);

        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithMissingCharsetUppercase() {
        String testContentType = "text/html; charset=MISSING";
        String charset = DataUtil.getCharsetFromContentType(testContentType);

        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithEmptyContentType() {
        String testContentType = "";
        String charset = DataUtil.getCharsetFromContentType(testContentType);

        assertNull(charset);
    }
}