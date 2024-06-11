package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test cases for DataUtil class.
 */
public class DataUtilTest {

    private static final String CHARSET_GB2312 = "GB2312";
    private static final String CHARSET_UTF8 = "UTF-8";

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("testFile.html");
        Document document = DataUtil.load(file, null, "");
        assertNotNull(document);
        assertEquals(CHARSET_UTF8, document.outputSettings().charset());
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("testFile.html");
        Document document = DataUtil.load(file, CHARSET_UTF8, "");
        assertNotNull(document);
        assertEquals(CHARSET_UTF8, document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("Test data".getBytes(CHARSET_UTF8));
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Document document = DataUtil.load(inputStream, null, "");
        assertNotNull(document);
        assertEquals(CHARSET_UTF8, document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("Test data".getBytes(CHARSET_UTF8));
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Document document = DataUtil.load(inputStream, CHARSET_UTF8, "");
        assertNotNull(document);
        assertEquals(CHARSET_UTF8, document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithDetectedCharset() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("Content-Type: text/html; charset=gb2312\r\n".getBytes(CHARSET_UTF8));
        byteArrayOutputStream.write("<html lang=\"zh\">Test data</html>".getBytes(CHARSET_GB2312));
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Parser parser = mock(Parser.class);
        Document document = DataUtil.load(inputStream, null, "");
        assertNotNull(document);
        assertEquals(CHARSET_GB2312, document.outputSettings().charset());
        verify(parser).parseInput("Test data", "");
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.allocate(128);
        byteData.put("Test data".getBytes(CHARSET_UTF8));
        byteData.flip();
        Document document = DataUtil.parseByteData(byteData, null, "", mock(Parser.class));
        assertNotNull(document);
        assertEquals(CHARSET_UTF8, document.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.allocate(128);
        byteData.put("Content-Type: text/html; charset=gb2312\r\n".getBytes(CHARSET_UTF8));
        byteData.put("<html lang=\"zh\">Test data</html>".getBytes(CHARSET_GB2312));
        byteData.flip();
        Parser parser = mock(Parser.class);
        Document document = DataUtil.parseByteData(byteData, null, "", parser);
        assertNotNull(document);
        assertEquals(CHARSET_GB2312, document.outputSettings().charset());
        verify(parser).parseInput("Test data", "");
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes(CHARSET_UTF8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(CHARSET_UTF8, Charset.forName(byteData.charset().name()).name());
        byte[] data = new byte[byteData.limit()];
        byteData.get(data);
        assertEquals("Test data", new String(data, CHARSET_UTF8));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=gb2312";
        assertEquals(CHARSET_GB2312, DataUtil.getCharsetFromContentType(contentType));

        String contentType2 = "text/html; charset=UTF-8";
        assertEquals(CHARSET_UTF8, DataUtil.getCharsetFromContentType(contentType2));

        String contentType3 = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType3));
    }
}