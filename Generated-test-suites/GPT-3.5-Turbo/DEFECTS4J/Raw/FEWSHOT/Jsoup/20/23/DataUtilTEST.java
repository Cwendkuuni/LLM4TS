
package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    private File testFile;
    private InputStream testInputStream;

    @Before
    public void setUp() throws IOException {
        testFile = new File("test.html");
        testFile.createNewFile();
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write("<html><head><title>Test</title></head><body></body></html>");
        fileWriter.close();

        testInputStream = new FileInputStream(testFile);
    }

    @Test
    public void testLoad_File() throws IOException {
        Document document = DataUtil.load(testFile, null, null);
        assertEquals("Test", document.title());
    }

    @Test
    public void testLoad_EmptyContent_File() throws IOException {
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write("");
        fileWriter.close();

        Document document = DataUtil.load(testFile, null, null);
        assertEquals("", document.title());
    }

    @Test
    public void testLoad_NullFile() {
        try {
            DataUtil.load(null, null, null);
        } catch (IOException e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    public void testLoad_InputStream() throws IOException {
        Document document = DataUtil.load(testInputStream, null, null);
        assertEquals("Test", document.title());
    }

    @Test
    public void testLoad_InputStream_CharsetName() throws IOException {
        Document document = DataUtil.load(testInputStream, "UTF-8", null);
        assertEquals("Test", document.title());
    }

    @Test
    public void testLoad_InputStream_NullInputStream() {
        try {
            DataUtil.load(null, null, null);
        } catch (IOException e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    public void testLoad_InputStream_CharsetName_NullInputStream() {
        try {
            DataUtil.load(null, "UTF-8", null);
        } catch (IOException e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    public void testLoad_InputStream_CharsetName_NullCharsetName() throws IOException {
        Document document = DataUtil.load(testInputStream, null, null);
        assertEquals("Test", document.title());
    }

    @Test
    public void testLoad_InputStream_CharsetName_NullBaseUri() throws IOException {
        Document document = DataUtil.load(testInputStream, "UTF-8", null);
        assertEquals("Test", document.title());
    }

    @Test
    public void testLoad_InputStream_CharsetName_NullParser() throws IOException {
        Document document = DataUtil.load(testInputStream, "UTF-8", null, Parser.htmlParser());
        assertEquals("Test", document.title());
    }

    @Test
    public void testLoad_InputStream_CharsetName_BaseUri_Parser() throws IOException {
        Document document = DataUtil.load(testInputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        assertEquals("Test", document.title());
    }

    @Test
    public void testLoad_InputStream_CharsetName_BaseUri_Parser_NullInputStream() {
        try {
            DataUtil.load(null, "UTF-8", "http://example.com", Parser.htmlParser());
        } catch (IOException e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    public void testParseByteData_NullCharsetName() {
        String docData = "<html><head><title>Test</title></head><body></body></html>";
        Document document = DataUtil.parseByteData(Charset.forName("UTF-8").encode(docData), null, null, Parser.htmlParser());
        assertEquals("Test", document.title());
    }

    @Test
    public void testParseByteData_NullBaseUri() {
        String docData = "<html><head><title>Test</title></head><body></body></html>";
        Document document = DataUtil.parseByteData(Charset.forName("UTF-8").encode(docData), "UTF-8", null, Parser.htmlParser());
        assertEquals("Test", document.title());
    }

    @Test
    public void testParseByteData_NullParser() {
        String docData = "<html><head><title>Test</title></head><body></body></html>";
        Document document = DataUtil.parseByteData(Charset.forName("UTF-8").encode(docData), "UTF-8", "http://example.com", null);
        assertEquals("Test", document.title());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_NullContentType() {
        String charset = DataUtil.getCharsetFromContentType(null);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_NoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_EmptyCharset() {
        String contentType = "text/html; charset=";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        FileInputStream inStream = new FileInputStream(testFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        String docData = Charset.forName("UTF-8").decode(byteData).toString();
        assertEquals("<html><head><title>Test</title></head><body></body></html>", docData);
        inStream.close();
    }

    @Test
    public void testReadToByteBuffer_NullInputStream() {
        try {
            DataUtil.readToByteBuffer(null);
        } catch (IOException e) {
            assertNull(e.getMessage());
        }
    }
}
