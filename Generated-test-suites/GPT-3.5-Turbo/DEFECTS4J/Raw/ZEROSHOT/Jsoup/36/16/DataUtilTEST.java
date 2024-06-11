package org.jsoup.helper;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;

public class DataUtilTest {

    private String filePath;
    private String charsetName;
    private String baseUri;
    private File file;
    private InputStream inputStream;
    private Parser parser;

    @Before
    public void setUp() {
        filePath = "dummyFilePath";
        charsetName = "UTF-8";
        baseUri = "dummyBaseUri";
        file = new File(filePath);
        inputStream = new FileInputStream(file);
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoadFile() {
        try {
            Document result = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(result);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            Document result = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(result);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            Document result = DataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(result);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String charsetName = "UTF-8";
        String baseUri = "dummyBaseUri";
        Parser parser = Parser.htmlParser();
        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(result);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() {
        int maxSize = 1024;
        try {
            ByteBuffer result = DataUtil.readToByteBuffer(inputStream, maxSize);
            assertNotNull(result);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            ByteBuffer result = DataUtil.readToByteBuffer(inputStream);
            assertNotNull(result);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        assertEquals("UTF-8", charset1);

        String contentType2 = "text/html; charset=EUC-JP";
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        assertEquals("EUC-JP", charset2);

        String contentType3 = "text/html";
        String charset3 = DataUtil.getCharsetFromContentType(contentType3);
        assertNull(charset3);
    }
}