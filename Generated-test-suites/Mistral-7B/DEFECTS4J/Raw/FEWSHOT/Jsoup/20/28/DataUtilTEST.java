package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoad() throws IOException {
        File file = mock(File.class);
        FileInputStream fileInputStream = mock(FileInputStream.class);
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.getAbsolutePath()).thenReturn("path/to/file.html");
        when(fileInputStream.readAllBytes()).thenReturn(new byte[]{"\uFEFF", 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF});

        Document document = DataUtil.load(file, "UTF-8", "http://example.com");

        assertEquals("UTF-8", document.outputSettings().charset());
        verify(file).exists();
        verify(file).isFile();
        verify(file).getAbsolutePath();
        verify(fileInputStream).close();
        verifyNoMoreInteractions(fileInputStream);

        ByteBuffer byteData = DataUtil.readToByteBuffer(fileInputStream);
        assertEquals("UTF-8", byteData.charset().name());
        assertEquals(byteData.limit(), byteData.capacity());
        assertEquals(byteData.get(0), (byte) 0xFEFF);
        assertEquals(byteData.get(11), (byte) 0x8);
        assertEquals(byteData.get(12), (byte) 0x9);
    }

    @Test
    public void testLoadWithNullCharset() throws IOException {
        File file = mock(File.class);
        FileInputStream fileInputStream = mock(FileInputStream.class);
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.getAbsolutePath()).thenReturn("path/to/file.html");
        when(fileInputStream.readAllBytes()).thenReturn(new byte[]{"\uFEFF", 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF});

        Document document = DataUtil.load(file, null, "http://example.com");

        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testLoadWithEmptyCharset() throws IOException {
        File file = mock(File.class);
        FileInputStream fileInputStream = mock(FileInputStream.class);
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.getAbsolutePath()).thenReturn("path/to/file.html");
        when(fileInputStream.readAllBytes()).thenReturn(new byte[]{"\uFEFF", 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF});

        Document document = DataUtil.load(file, "", "http://example.com");

        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testLoadWithMetaCharset() throws IOException {
        File file = mock(File.class);
        FileInputStream fileInputStream = mock(FileInputStream.class);
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.getAbsolutePath()).thenReturn("path/to/file.html");
        when(fileInputStream.readAllBytes()).thenReturn(new byte[]{"<html lang=" + "'en-US'" + " <head><meta charset=" + "'gb2312'" + ">" + "</head><body>" + "</body>"}.concat(new byte[0]));

        Document document = DataUtil.load(file, null, "http://example.com");

        assertEquals("gb2312", document.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithUtf8Charset() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{"\uFEFF", 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF});
        Document document = DataUtil.parseByteData(byteData, "UTF-8", "http://example.com", Parser.htmlParser());

        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals(21, document.body().children().size());
        assertEquals("1", document.body().child(0).text().trim());
        assertEquals("2", document.body().child(1).text().trim());
        assertEquals("3", document.body().child(2).text().trim());
        assertEquals("4", document.body().child(3).text().trim());
        assertEquals("5", document.body().child(4).text().trim());
        assertEquals("6", document.body().child(5).text().trim());
        assertEquals("7", document.body().child(6).text().trim());
        assertEquals("8", document.body().child(7).text().trim());
        assertEquals("9", document.body().child(8).text().trim());
        assertEquals("A", document.body().child(9).text().trim());
        assertEquals("B", document.body().child(10).text().trim());
        assertEquals("C", document.body().child(11).text().trim());
        assertEquals("D", document.body().child(12).text().trim());
        assertEquals("E", document.body().child(13).text().trim());
        assertEquals("F", document.body().child(14).text().trim());
        assertEquals("", document.body().child(15).text().trim());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{"<html lang=" + "'en-US'" + " <head><meta charset=" + "'gb2312'" + ">" + "</head><body>" + "</body>"}.concat(new byte[0]));
        Document document = DataUtil.parseByteData(byteData, null, "http://example.com", Parser.htmlParser());

        assertEquals("gb2312", document.outputSettings().charset());
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithInvalidCharset() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{"<html lang=" + "'en-US'" + " <head><meta charset=" + "'x-custom'" + ">" + "</head><body>" + "</body>"}.concat(new byte[0]));
        DataUtil.parseByteData(byteData, "x-custom", "http://example.com", Parser.htmlParser());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.readAllBytes()).thenReturn(new byte[]{"\uFEFF", 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF});

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        assertEquals("UTF-8", byteData.charset().name());
        assertEquals(21, byteData.limit());
        assertEquals(21, byteData.capacity());
        assertEquals(byteData.get(0), (byte) 0xFEFF);
        assertEquals(byteData.get(11), (byte) 0x8);
        assertEquals(byteData.get(12), (byte) 0x9);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=gb2312";

        assertEquals("gb2312", DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String contentType = null;

        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }
}