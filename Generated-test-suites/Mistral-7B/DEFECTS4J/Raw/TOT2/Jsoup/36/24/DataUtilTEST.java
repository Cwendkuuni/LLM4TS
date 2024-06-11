package org.example;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class DataUtilTest {
    @Test
    public void testLoadFromFile() throws IOException {
        Document doc = DataUtil.load(new File("testFile.html"), "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("testData".getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("testData".getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("testData".getBytes());
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("testData".getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(byteData.limit(), "testData".length());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("testData".getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 10);
        assertEquals(byteData.limit(), 10);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals(DataUtil.getCharsetFromContentType(contentType), "UTF-8");
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadFromNonExistentFile() {
        DataUtil.load(new File("nonExistentFile.html"), "UTF-8", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadWithInvalidCharsetName() {
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(new File("testFile.html"), null, null));
    }

    @Test(expected = NullPointerException.class)
    public void testLoadWithNullBaseUri() {
        DataUtil.load(new File("testFile.html"), "UTF-8", null);
        assertThrows(NullPointerException.class, () -> DataUtil.load(new File("testFile.html"), "UTF-8", null, null));
    }

    @Test(expected = IOException.class)
    public void testLoadFromEmptyInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        assertThrows(IOException.class, () -> DataUtil.load(inputStream, "UTF-8", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataWithEmptyByteBuffer() {
        ByteBuffer byteData = ByteBuffer.allocate(0);
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataWithMalformedInput() {
        ByteBuffer byteData = ByteBuffer.wrap("invalidData".getBytes());
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, null, null, Parser.htmlParser()));
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetCharsetFromContentTypeWithUnsupportedCharset() {
        String contentType = "text/html; charset=X-UNSUPPORTED";
        assertThrows(UnsupportedEncodingException.class, () -> Charset.forName("X-UNSUPPORTED").decode(new byte[0]));
        assertThrows(UnsupportedEncodingException.class, () -> DataUtil.getCharsetFromContentType(contentType));
    }

    @Test(expected = NullPointerException.class)
    public void testLoadWithNullFile() {
        DataUtil.load(null, "UTF-8", null);
        assertThrows(NullPointerException.class, () -> DataUtil.load(new File("testFile.html"), null, null));
    }

    @Test(expected = NullPointerException.class)
    public void testLoadWithNullCharsetName() {
        DataUtil.load(new File("testFile.html"), null, null);
        assertThrows(NullPointerException.class, () -> DataUtil.load(new File("testFile.html"), null, null, null));
    }

    @Test(expected = NullPointerException.class)
    public void testLoadWithNullParser() {
        DataUtil.load(new File("testFile.html"), "UTF-8", null);
    }

    @Test(expected = NullPointerException.class)
    public void testParseByteDataWithNullBaseUri() {
        ByteBuffer byteData = ByteBuffer.wrap("testData".getBytes());
        assertThrows(NullPointerException.class, () -> DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser()));
    }

    @Test(expected = NullPointerException.class)
    public void testParseByteDataWithNullParser() {
        ByteBuffer byteData = ByteBuffer.wrap("testData".getBytes());
        assertThrows(NullPointerException.class, () -> DataUtil.parseByteData(byteData, "UTF-8", null, null));
    }
}