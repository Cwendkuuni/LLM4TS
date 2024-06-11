package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws Exception {
        File file = new File("testFile.html");
        String expectedContent = "Expected content of the file.";
        byte[] fileContent = expectedContent.getBytes();
        when(file.length()).thenReturn(fileContent.length);
        when(file.getBytes()).thenReturn(fileContent);

        Document document = DataUtil.load(file, null, "");
        assertEquals(expectedContent, document.body().text());
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws Exception {
        File file = new File("testFile.html");
        String expectedContent = "Expected content of the file.";
        byte[] fileContent = expectedContent.getBytes("UTF-8");
        when(file.length()).thenReturn(fileContent.length);
        when(file.getBytes()).thenReturn(fileContent);

        Document document = DataUtil.load(file, "UTF-8", "");
        assertEquals(expectedContent, document.body().text());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws Exception {
        String expectedContent = "Expected content of the input stream.";
        byte[] contentBytes = expectedContent.getBytes();
        InputStream inputStream = new ByteArrayInputStream(contentBytes);

        Document document = DataUtil.load(inputStream, null, "");
        assertEquals(expectedContent, document.body().text());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws Exception {
        String expectedContent = "Expected content of the input stream.";
        byte[] contentBytes = expectedContent.getBytes("UTF-8");

        InputStream inputStream = new ByteArrayInputStream(contentBytes);
        Document document = DataUtil.load(inputStream, "UTF-8", "");
        assertEquals(expectedContent, document.body().text());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws Exception {
        String expectedContent = "Expected content of the byte data.";
        byte[] byteData = expectedContent.getBytes();

        ByteBuffer byteBuffer = ByteBuffer.wrap(byteData);
        Document document = DataUtil.parseByteData(byteBuffer, null, "", Parser.htmlParser());
        assertEquals(expectedContent, document.body().text());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws Exception {
        String expectedContent = "Expected content of the byte data.";
        byte[] byteData = expectedContent.getBytes("UTF-8");

        ByteBuffer byteBuffer = ByteBuffer.wrap(byteData);
        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", "", Parser.htmlParser());
        assertEquals(expectedContent, document.body().text());
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
    public void testReadToByteBuffer() throws Exception {
        String input = "Test input.";
        byte[] expectedOutput = input.getBytes();
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        byte[] output = byteBuffer.array();
        assertEquals(expectedOutput.length, output.length);
        assertArrayEquals(expectedOutput, output);
    }
}