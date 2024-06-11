package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {

    private static File testFile;

    @BeforeClass
    public static void setUp() {
        testFile = new File("test.html");
    }

    @Test
    public void testLoadFromFile() throws IOException {
        Document doc = DataUtil.load(testFile, "UTF-8", "");
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset().name());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset().name());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(inputStream, "UTF-8", "", parser);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset().name());
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);
        assertNull(doc);

        charsetName = null;
        doc = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        int maxSize = 1000;
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.capacity() <= maxSize);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html; charset=EUC-JP";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("EUC-JP", charset);

        contentType = "text/html; charset=ISO-8859-1";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("ISO-8859-1", charset);

        contentType = "text/html; charset=invalidcharset";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}