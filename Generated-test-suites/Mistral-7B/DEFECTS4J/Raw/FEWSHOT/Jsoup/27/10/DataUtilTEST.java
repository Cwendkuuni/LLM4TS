package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String expectedHtml = "<html><body>Hello World!</body></html>";
        File file = new File("test.html");
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(expectedHtml.getBytes());
        outputStream.close();

        Document actualDocument = DataUtil.load(file, null, null);
        String actualHtml = actualDocument.html();
        assertEquals(expectedHtml, actualHtml);

        file.delete();
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String expectedHtml = "<html><body>Hello World!</body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(expectedHtml.getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document actualDocument = DataUtil.load(inputStream, null, null);
        String actualHtml = actualDocument.html();
        assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String expectedHtml = "<html><body>Hello World!</body></html>";
        String charsetName = "UTF-8";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(expectedHtml.getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document actualDocument = DataUtil.load(inputStream, charsetName, null);
        String actualHtml = actualDocument.html();
        assertEquals(expectedHtml, actualHtml);

        assertNotNull(actualDocument.outputSettings().charset());
        assertEquals(charsetName, actualDocument.outputSettings().charset());
    }

    @Test
    public void testParseByteData() throws IOException {
        String expectedHtml = "<html><body>Hello World!</body></html>";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(expectedHtml.getBytes());
        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());

        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        String actualHtml = actualDocument.html();
        assertEquals(expectedHtml, actualHtml);

        assertNotNull(actualDocument.outputSettings().charset());
        assertEquals(charsetName, actualDocument.outputSettings().charset());

        // Test decoding with a different charset
        ByteBuffer byteDataWithBom = ByteBuffer.wrap(new byte[] { 65279, (byte) 'H' }); // UTF-8 BOM
        String actualHtmlWithBom = DataUtil.parseByteData(byteDataWithBom, charsetName, baseUri, Parser.htmlParser()).html();
        assertEquals(expectedHtml, actualHtmlWithBom);
    }

    @Test
    public void testDetectCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String expectedData = "Hello World!";
        InputStream inputStream = new ByteArrayInputStream(expectedData.getBytes());

        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream);
        byte[] actualDataBytes = actualByteBuffer.array();
        assertEquals(expectedData, new String(actualDataBytes, "UTF-8"));
    }
}