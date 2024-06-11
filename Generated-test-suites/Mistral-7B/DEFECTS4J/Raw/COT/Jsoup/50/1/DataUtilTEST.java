package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    private static final String TEST_FILE_NAME = "testFile.html";
    private static final String TEST_CHARSET = "UTF-8";
    private static final String TEST_BASE_URI = "http://test.com";
    private static final String TEST_CONTENT_TYPE = "text/html; charset=UTF-8";
    private static final String TEST_DOC_DATA = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
    private static final String TEST_FILE_CONTENTS = TEST_DOC_DATA.getBytes(Charset.forName(TEST_CHARSET));
    private static final File TEST_FILE = new File(System.getProperty("user.dir"), TEST_FILE_NAME);
    private static final InputStream TEST_INPUT_STREAM = new ByteArrayInputStream(TEST_FILE_CONTENTS);
    private static final ByteBuffer TEST_BYTE_DATA = ByteBuffer.wrap(TEST_FILE_CONTENTS);
    private DataUtil dataUtil;
    private Parser parser;
    private Document doc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataUtil = new DataUtil();
        parser = org.jsoup.parser.Jsoup.htmlParser();
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File(TEST_FILE_NAME);
        ByteBuffer byteData = dataUtil.readFileToByteBuffer(file);
        Document doc = dataUtil.load(file, TEST_CHARSET, TEST_BASE_URI);
        assertEquals(doc.body().html(), TEST_DOC_DATA);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        Document doc = dataUtil.load(TEST_INPUT_STREAM, TEST_CHARSET, TEST_BASE_URI, parser);
        assertEquals(doc.body().html(), TEST_DOC_DATA);
    }

    @Test
    public void testParseByteData() throws IOException {
        Document doc = dataUtil.parseByteData(TEST_BYTE_DATA, TEST_CHARSET, TEST_BASE_URI, parser);
        assertEquals(doc.body().html(), TEST_DOC_DATA);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File(TEST_FILE_NAME);
        dataUtil.load(file, "invalidCharset", TEST_BASE_URI);
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals(DataUtil.getCharsetFromContentType(TEST_CONTENT_TYPE), TEST_CHARSET);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == 32);
        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(DataUtil.mimeBoundaryChars.contains(boundary.charAt(i)));
        }
    }
}