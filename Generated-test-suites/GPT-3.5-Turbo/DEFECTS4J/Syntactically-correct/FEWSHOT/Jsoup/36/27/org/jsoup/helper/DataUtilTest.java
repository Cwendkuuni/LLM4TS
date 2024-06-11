
package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        try {
            File file = new File("test.html");
            Document document = DataUtil.load(file, "UTF-8", "");
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException occurred while loading file");
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document document = DataUtil.load(inputStream, "UTF-8", "");
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException occurred while loading input stream");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Parser parser = Parser.htmlParser();
            Document document = DataUtil.load(inputStream, "UTF-8", "", parser);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException occurred while loading input stream with parser");
        }
    }

    // Private method tests

    @Test
    public void testParseByteDataCharsetNull() {
        ByteBuffer byteData = ByteBuffer.wrap("<html></html>".getBytes());
        Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataCharsetNotNull() {
        ByteBuffer byteData = ByteBuffer.wrap("<html></html>".getBytes());
        Document document = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
            assertNotNull(byteData);
        } catch (IOException e) {
            fail("IOException occurred while reading input stream to byte buffer");
        }
    }

    @Test
    public void testReadToByteBufferWithMaxSize() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 1024);
            assertNotNull(byteData);
        } catch (IOException e) {
            fail("IOException occurred while reading input stream to byte buffer with max size");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

}
