package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DataUtil.class)
public class DataUtilTest {

    private static final String TEST_FILE_PATH = "testData/testFile.html";
    private static final String TEST_STREAM_INPUT = "testData/testStream.html";
    private static final String TEST_INPUT_STREAM_CHARSET = "UTF-8";
    private static final String TEST_BYTE_BUFFER_CHARSET = "UTF-8";
    private static final String TEST_BASE_URI = "http://test.com";
    private static final Parser TEST_HTML_PARSER = Parser.htmlParser();
    private static final String TEST_CHARSET = "ISO-8859-1";
    private static final int TEST_MAX_SIZE = 1024;
    private static final String TEST_CONTENT_TYPE = "text/html; charset=ISO-8859-1";

    private File testFile;
    private InputStream testInputStream;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        testFile = new File(TEST_FILE_PATH);
        testInputStream = new ByteArrayInputStream(TestUtil.readFile(TEST_STREAM_INPUT).getBytes());
    }

    @Test
    public void testLoadFile() throws Exception {
        Document document = DataUtil.load(testFile, TEST_BYTE_BUFFER_CHARSET, TEST_BASE_URI);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws Exception {
        Document document = DataUtil.load(testInputStream, TEST_INPUT_STREAM_CHARSET, TEST_BASE_URI);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        Document document = DataUtil.load(testInputStream, TEST_INPUT_STREAM_CHARSET, TEST_BASE_URI, TEST_HTML_PARSER);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithCharset() throws Exception {
        ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream, TEST_MAX_SIZE);
        Document document = DataUtil.parseByteData(byteData, TEST_BYTE_BUFFER_CHARSET, TEST_BASE_URI, TEST_HTML_PARSER);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithoutCharset() throws Exception {
        ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream, TEST_MAX_SIZE);
        Document document = DataUtil.parseByteData(byteData, null, TEST_BASE_URI, TEST_HTML_PARSER);
        Assert.assertNotNull(document);

        Element meta = document.selectFirst("meta[http-equiv=content-type], meta[charset]");
        if (meta != null) {
            String foundCharset = DataUtil.getCharsetFromContentType(meta.attr("content"));
            Assert.assertEquals(TEST_CHARSET, foundCharset);
            document = DataUtil.parseByteData(byteData, TEST_CHARSET, TEST_BASE_URI, TEST_HTML_PARSER);
        }

        Assert.assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream, TEST_MAX_SIZE);
        Assert.assertNotNull(byteData);
    }

    @Test
    public void testGetCharsetFromContentType() throws Exception {
        Assert.assertEquals(TEST_CHARSET, DataUtil.getCharsetFromContentType(TEST_CONTENT_TYPE));
    }
}