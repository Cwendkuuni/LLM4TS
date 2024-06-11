package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for DataUtil.
 *
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // replace with actual file path
        Document doc = DataUtil.load(file, "UTF-8", "http://test.com");
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullCharset() throws IOException {
        File file = new File("test.html"); // replace with actual file path
        Document doc = DataUtil.load(file, null, "http://test.com");
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "<html><body><h1>Test</h1></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testData.getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://test.com");
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String testData = "<html><body><h1>Test</h1></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testData.getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, null, "http://test.com");
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        String testData = "<html><body><h1>Test</h1></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testData.getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Parser parser = mock(Parser.class);
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://test.com", parser);
        assertNotNull(doc);
        when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(doc);
    }

    @Test
    public void testParseByteData() throws IOException {
        String testData = "<html><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "http://test.com", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String testData = "<html><head><meta charset=\"GBK\"><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, null, "http://test.com", Parser.htmlParser());
        assertNotNull(doc);
        Element meta = doc.select("meta[charset]").first();
        assertNotNull(meta);
        assertEquals("GBK", meta.attr("charset"));
    }

    @Test
    public void testParseByteDataWithNullCharsetAndDetectedCharset() throws IOException {
        String testData = "<html><head><meta charset=\"GBK\"><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, null, "http://test.com", Parser.htmlParser());
        assertNotNull(doc);
        Element meta = doc.select("meta[charset]").first();
        assertNotNull(meta);
        assertEquals("GBK", meta.attr("charset"));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(testData, Charset.forName("UTF-8").decode(byteData).toString());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        assertNull(DataUtil.getCharsetFromContentType(null));
    }
}