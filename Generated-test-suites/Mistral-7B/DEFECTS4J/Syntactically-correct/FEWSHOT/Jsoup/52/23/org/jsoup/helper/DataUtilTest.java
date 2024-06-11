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
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("testFile.html");
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, expectedCharset, baseUri);
        // Add your assertions here
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputData = "<html><body><h1>Test</h1></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputData.getBytes("UTF-8"));
        InputStream in = new ByteArrayInputStream(outputStream.toByteArray());
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(in, expectedCharset, baseUri);
        // Add your assertions here
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        Document doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        // Add your assertions here

        String testFileData = "Test data for parsing byte data.";
        byteData = DataUtil.readFileToByteBuffer(new File("testFile.html"));
        charsetName = null;
        doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        // Add your assertions here

        byteData = DataUtil.readFileToByteBuffer(new File("testFile.html"));
        charsetName = "UTF-16";
        doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        // Add your assertions here
    }

    @Test
    public void testCrossStreams() throws IOException {
        File testFile = new File("testFile.html");
        InputStream in = new FileInputStream(testFile);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        // Add your assertions here
        in.close();
        out.close();
    }

    @Test
    public void testParseContentType() {
        String contentType = "text/html; charset=gb2312";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("gb2312", charset);

        contentType = "text/html; charset=UTF-8";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-32";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "EUC-JP";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "invalidCharset";
        assertNull(DataUtil.validateCharset(charset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(boundary.length(), 32);
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String charset = DataUtil.detectCharsetFromBom(byteData, null);
        assertNull(charset);

        byteData = DataUtil.readFileToByteBuffer(new File("testFileUTF32.html"));
        charset = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-32", charset);

        byteData = DataUtil.readFileToByteBuffer(new File("testFileUTF16.html"));
        charset = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-16", charset);

        byteData = DataUtil.readFileToByteBuffer(new File("testFileUTF8.html"));
        charset = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-8", charset);
    }
}