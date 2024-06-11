package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    private static ByteBuffer byteData;
    private static final String TEST_FILE_PATH = "test.html";
    private static final String TEST_INPUT_STREAM_CONTENT = "This is a test file.";
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String TEST_BASE_URI = "http://test.com";
    private static final Parser parser = Parser.htmlParser();

    @BeforeClass
    public static void setup() throws IOException {
        byteData = ByteBuffer.allocate(TEST_INPUT_STREAM_CONTENT.getBytes().length);
        File file = new File(TEST_FILE_PATH);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(TEST_INPUT_STREAM_CONTENT.getBytes());
        }
    }

    @Test
    public void testLoadFromFile() throws IOException {
        Document doc = DataUtil.load(new File(TEST_FILE_PATH), DEFAULT_CHARSET, TEST_BASE_URI);
        assertEquals("Test file content", TEST_INPUT_STREAM_CONTENT, doc.body().text());
    }

    @Test
    public void testLoadFromInputStreamWithDefaultCharset() throws IOException {
        InputStream in = new ByteArrayInputStream(TEST_INPUT_STREAM_CONTENT.getBytes());
        Document doc = DataUtil.load(in, null, TEST_BASE_URI);
        assertEquals("Test file content", TEST_INPUT_STREAM_CONTENT, doc.body().text());
    }

    @Test
    public void testLoadFromInputStreamWithSpecifiedCharset() throws IOException {
        InputStream in = new ByteArrayInputStream(TEST_INPUT_STREAM_CONTENT.getBytes());
        Document doc = DataUtil.load(in, "UTF-8", TEST_BASE_URI);
        assertEquals("Test file content", TEST_INPUT_STREAM_CONTENT, doc.body().text());
    }

    @Test
    public void testParseByteData() throws IOException {
        byteData.asByteBuffer().put(TEST_INPUT_STREAM_CONTENT.getBytes());
        byteData.position(0);
        Document doc = DataUtil.parseByteData(byteData, DEFAULT_CHARSET, TEST_BASE_URI, parser);
        assertEquals("Test file content", TEST_INPUT_STREAM_CONTENT, doc.body().text());
    }

    @Test
    public void testDetectCharset() throws IOException {
        byteData.asByteBuffer().put(TEST_INPUT_STREAM_CONTENT.getBytes());
        byteData.position(0);
        String detectedCharset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("Detected charset", DEFAULT_CHARSET, detectedCharset);

        byteData.position(0);
        byteData.asByteBuffer().put("<html lang='en' charset='ISO-8859-1'>".getBytes());
        detectedCharset = DataUtil.getCharsetFromContentType(new String(byteData.array(), 0, 70));
        assertEquals("Detected charset", "ISO-8859-1", detectedCharset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDetectCharsetWithNullContentType() {
        String contentType = null;
        DataUtil.getCharsetFromContentType(contentType);
    }
}