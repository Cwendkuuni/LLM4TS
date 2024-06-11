package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoad() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testLoadEmptyFile() throws IOException {
        File file = new File("empty.html");
        Document document = DataUtil.load(file, null, null);
        assertNull(document);
    }

    @Test
    public void testLoadInvalidFile() throws IOException {
        File file = new File("nonexistent.html");
        assertThrows(IOException.class, () -> DataUtil.load(file, null, null));
    }

    @Test
    public void testLoadWithInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"X-Invalid-Charset\"></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, "X-Invalid-Charset", null));
    }

    @Test
    public void testParseByteData() {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String html = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithUnknownCharset() {
        String html = "<html><head><meta charset=\"X-Unknown-Charset\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        Document document = DataUtil.parseByteData(byteData, "X-Unknown-Charset", null, Parser.htmlParser());
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithCustomParser() {
        String xml = "<xml><element></element></xml>";
        ByteBuffer byteData = ByteBuffer.wrap(xml.getBytes());
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.xmlParser());
        assertEquals(Parser.xmlParser(), document.parser());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] data = new byte[1024];
        InputStream inputStream = new ByteArrayInputStream(data);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 500);
        assertEquals(500, byteBuffer.limit());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        byte[] data = new byte[1024];
        InputStream inputStream = new ByteArrayInputStream(data);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 512);
        assertEquals(512, byteBuffer.limit());
    }

    @Test
    public void testReadToByteBufferWithUnlimitedSize() throws IOException {
        byte[] data = new byte[1024];
        InputStream inputStream = new ByteArrayInputStream(data);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        // limit and position are initially set to the size of the data
        assertEquals(data.length, byteBuffer.limit());
        assertEquals(0, byteBuffer.position());
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=utf-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertNull(DataUtil.getCharsetFromContentType("text/html"));
        assertNull(DataUtil.getCharsetFromContentType("text/html; charset=X-Unknown-Charset"));
    }

}