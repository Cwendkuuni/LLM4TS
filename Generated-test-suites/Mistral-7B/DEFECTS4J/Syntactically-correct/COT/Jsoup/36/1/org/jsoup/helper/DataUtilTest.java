package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}
     * @throws IOException
     */
    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("test.html");
        Document document = DataUtil.load(inputFile, "UTF-8", "");
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}
     * @throws IOException
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document document = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}
     * @throws IOException
     */
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}
     */
    @Test
    public void testParseByteData() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body></body></html>".getBytes("UTF-8"));
        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", "", Parser.xmlParser());
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream, int)} with maxSize > 0
     * @throws IOException
     */
    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);
        assertEquals(10, byteBuffer.limit());
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream)} with maxSize = 0
     * @throws IOException
     */
    @Test
    public void testReadToByteBufferWithMaxSizeZero() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        // Since readToByteBuffer is designed to be unlimited, we cannot directly test the limit here.
        // Instead, we can check if the data has been read correctly.
        String readData = Charset.forName("UTF-8").decode(byteBuffer).toString();
        assertEquals(htmlContent, readData);
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("EUC-JP", charset);

        String contentTypeWithNoCharset = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentTypeWithNoCharset);
        assertNull(charset);
    }
}