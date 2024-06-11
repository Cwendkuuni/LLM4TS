package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><div>Hello World</div></body></html>";
        File testFile = new File(System.getProperty("java.io.tmpdir"), "testFile.html");
        FileOutputStream outputStream = new FileOutputStream(testFile);
        outputStream.write(fileContent.getBytes());
        outputStream.close();

        Document doc = DataUtil.load(testFile, null, null);
        assertEquals("Hello World", doc.select("div").text());
        doc.outputSettings().charset("UTF-8");
        assertEquals("UTF-8", doc.outputSettings().charset());
        testFile.delete();
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><div>Hello World</div></body></html>";
        File testFile = new File(System.getProperty("java.io.tmpdir"), "testFile.html");
        FileOutputStream outputStream = new FileOutputStream(testFile);
        outputStream.write(fileContent.getBytes());
        outputStream.close();

        Document doc = DataUtil.load(testFile, "ISO-8859-1", null);
        assertEquals("Hello World", doc.select("div").text());
        doc.outputSettings().charset("ISO-8859-1");
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
        testFile.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><div>Hello World</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());

        Document doc = DataUtil.load(inputStream, null, null);
        assertEquals("Hello World", doc.select("div").text());
        doc.outputSettings().charset("UTF-8");
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><div>Hello World</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());

        Document doc = DataUtil.load(inputStream, "ISO-8859-1", null);
        assertEquals("Hello World", doc.select("div").text());
        doc.outputSettings().charset("ISO-8859-1");
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><div>Hello World</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());

        Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertEquals("Hello World", doc.select("div").text());
        doc.outputSettings().charset("UTF-8");
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String docData = "<html><head><meta charset=\"UTF-8\"></head><body><div>Hello World</div></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());

        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("Hello World", doc.select("div").text());
        doc.outputSettings().charset("UTF-8");
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String docData = "<html><head><meta charset=\"UTF-8\"></head><body><div>Hello World</div></body></html>";
        String charsetName = "ISO-8859-1";

        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());

        Document doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertEquals("Hello World", doc.select("div").text());
        doc.outputSettings().charset(charsetName);
        assertEquals(charsetName, doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() {
        String docData = "<html><head><meta charset=\"ISO-8859-1\"><meta charset=\"UTF-8\"></head><body><div>Hello World</div></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());

        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("Hello World", doc.select("div").text());
        doc.outputSettings().charset("UTF-8");
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><div>Hello World</div></body></html>";
        File testFile = new File(System.getProperty("java.io.tmpdir"), "testFile.html");
        FileOutputStream outputStream = new FileOutputStream(testFile);
        outputStream.write(fileContent.getBytes());
        outputStream.close();

        ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(testFile), 0);
        assertEquals(fileContent.length(), byteData.limit());
        testFile.delete();
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><div>Hello World</div></body></html>";
        File testFile = new File(System.getProperty("java.io.tmpdir"), "testFile.html");
        FileOutputStream outputStream = new FileOutputStream(testFile);
        outputStream.write(fileContent.getBytes());
        outputStream.close();

        ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(testFile), 10);
        assertEquals(10, byteData.limit());
        testFile.delete();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html; charset=ISO-8859-1";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("ISO-8859-1", charset);

        contentType = "text/html; charset=GBK";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("GBK", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}