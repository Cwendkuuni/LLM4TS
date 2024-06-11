package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head></head><body><p>Hello, World!</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String xml = "<xml><message>Hello, World!</message></xml>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inStream = new ByteArrayInputStream("Test".getBytes());
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inStream, outStream);
        assertEquals("Test", outStream.toString());
    }

    // Edge case: Testing parseByteData for Unicode BOM
    @Test
    public void testParseByteDataUnicodeBOM() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{(byte) 0xFE, (byte) 0xFF, 65, 66, 67});
        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(document);
    }

    // Error scenario: Testing readFileToByteBuffer for IOException
    @Test(expected = IOException.class)
    public void testReadFileToByteBufferIOException() throws IOException {
        File file = new File("non-existent-file.html");
        DataUtil.readFileToByteBuffer(file);
    }

    // Edge case: Testing mimeBoundary method for generating random string
    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(32, boundary.length());
    }
}