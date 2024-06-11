package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body>Hello world!</body></html>";
        File testFile = new File("testFile.html");
        FileOutputStream testFileStream = new FileOutputStream(testFile);
        testFileStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
        testFileStream.close();

        Document doc = DataUtil.load(testFile, "UTF-8", null);
        assertEquals("Hello world!", doc.body().text());

        testFile.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body>Hello world!</body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals("Hello world!", doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body>Hello world!</body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertEquals("Hello world!", doc.body().text());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testParseByteData() {
        String charsetName = "UTF-8";
        String baseUri = null;
        Parser parser = Parser.htmlParser();

        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body>Hello world!</body></html>";
        byte[] htmlBytes = htmlContent.getBytes(StandardCharsets.UTF_8);

        ByteBuffer byteData = ByteBuffer.wrap(htmlBytes);
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals("Hello world!", doc.body().text());

        charsetName = null;
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals("Hello world!", doc.body().text());

        String expectedCharset = "gb2312";
        byte[] gb2312Bytes = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + expectedCharset + "\"><body>Hello world!</body></html>".getBytes(StandardCharsets.UTF_8);
        byteData = ByteBuffer.wrap(gb2312Bytes);
        charsetName = expectedCharset;
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals("Hello world!", doc.body().text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testString = "Test String";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 0);
        byte[] expectedBytes = testString.getBytes(StandardCharsets.UTF_8);
        assertArrayEquals(expectedBytes, byteData.array());

        byteData = DataUtil.readToByteBuffer(inputStream);
        assertArrayEquals(expectedBytes, byteData.array());
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharsetName() {
        String contentType = "text/html; charset=invalidCharset";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testLoadFileWithInvalidCharsetName() {
        String charsetName = "invalidCharset";
        File testFile = new File("testFile.html");
        FileOutputStream testFileStream = new FileOutputStream(testFile);
        testFileStream.write("Test String".getBytes(StandardCharsets.UTF_8));
        testFileStream.close();

        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(testFile, charsetName, null));

        testFile.delete();
    }

    @Test
    public void testParseByteDataWithUnsupportedCharset() {
        String charsetName = "invalidCharset";
        ByteBuffer byteData = ByteBuffer.wrap("Test String".getBytes(StandardCharsets.UTF_8));

        assertNull(DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser()));
    }
}