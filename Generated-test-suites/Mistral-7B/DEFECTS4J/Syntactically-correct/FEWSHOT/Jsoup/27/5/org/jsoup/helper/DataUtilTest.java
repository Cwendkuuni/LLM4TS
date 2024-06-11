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
    public void testLoadDocument() throws IOException {
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        String charsetName = "UTF-8";
        String baseUri = "/base";

        // Test loading from file
        ByteBuffer byteData = DataUtil.readToByteBuffer(mock(FileInputStream.class));
        Document document = DataUtil.load(file, charsetName, baseUri);
        verify(file).exists();
        verify(file, never()).close();
        assertEquals(byteData, document.body().bytes());

        // Test loading with invalid charset name
        charsetName = null;
        document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);

        // Test loading with meta charset tag
        String charsetFromMeta = "gb2312";
        when(file.exists()).thenReturn(true);
        when(file.getText()).thenReturn("" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + charsetFromMeta + "\">\n" +
                "</head>\n" +
                "<body>\n" +
                "</body>\n" +
                "</html>");
        document = DataUtil.load(file, charsetName, baseUri);
        assertEquals(charsetFromMeta, document.outputSettings().charset());
        verify(file, times(1)).exists();
        verify(file, times(1)).getText();
        verify(file, never()).close();
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.available()).thenReturn(bufferSize);

        String charsetName = "UTF-8";
        String baseUri = "/base";

        // Test loading with specified charset
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        verify(inputStream).available();
        assertEquals(byteData, document.body().bytes());
        verify(inputStream).close();

        // Test loading with null charset name
        charsetName = null;
        document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);

        // Test loading with meta charset tag
        String charsetFromMeta = "gb2312";
        when(inputStream.readAllBytes()).thenReturn("" +
                "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html; charset=" + charsetFromMeta + "\r\n" +
                "\r\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + charsetFromMeta + "\">\n" +
                "</head>\n" +
                "<body>\n" +
                "</body>\n" +
                "</html>").getBytes();
        document = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals(charsetFromMeta, document.outputSettings().charset());
        verify(inputStream).readAllBytes();
        verify(inputStream).close();
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'T', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', ' ', 't', 'e', 's', 't', ' ', 'f', 'i', 'l', 'e'});
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("This is a file", document.body().text());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'T', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', ' ', 't', 'e', 's', 't', ' ', 'f', 'i', 'l', 'e'});
        String charsetName = "UTF-8";
        Document document = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertEquals("This is a file", document.body().text());
        assertEquals(charsetName, document.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithMetaCharsetTag() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'T', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', ' ', 't', 'e', 's', 't', ' ', 'f', 'i', 'l', 'e'});
        String charsetName = "UTF-8";
        String metaCharset = "gb2312";
        Document document = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertEquals("This is a file", document.body().text());
        assertEquals(metaCharset, document.outputSettings().charset());

        // Test that the charsetName argument is ignored when metaCharset is present
        charsetName = "US-ASCII";
        document = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertEquals(metaCharset, document.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        byte[] buffer = new byte[bufferSize];
        when(inputStream.read(buffer)).thenReturn(bufferSize);

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(bufferSize, byteData.limit());
        assertEquals(buffer, byteData.array());
        verify(inputStream).read(buffer);
        verify(inputStream).close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=gb2312";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("gb2312", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}