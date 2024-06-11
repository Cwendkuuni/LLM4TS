package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    private static final String TEST_FILE_NAME = "testFile.html";
    private static final String TEST_CHARSET = "UTF-8";
    private static final String TEST_BASE_URI = "http://test.com/";
    private static final String TEST_CONTENT_TYPE = "text/html; charset=UTF-8";

    @Mock
    private Parser parser;

    @Before
    public void setUp() throws Exception {
        when(parser.parseInput(anyString(), anyString())).thenReturn(new Document());
    }

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File(TEST_FILE_NAME);
        InputStream inStream = new FileInputStream(testFile);

        Document doc = DataUtil.load(testFile, TEST_CHARSET, TEST_BASE_URI);

        assertEquals("Loaded file with expected charset", TEST_CHARSET, doc.outputSettings().charset());

        inStream.close();
        testFile.deleteOnExit();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "Test Data";
        InputStream inStream = new ByteArrayInputStream(testData.getBytes(TEST_CHARSET));

        Document doc = DataUtil.load(inStream, TEST_CHARSET, TEST_BASE_URI);

        assertEquals("Loaded input stream with expected charset", TEST_CHARSET, doc.outputSettings().charset());

        inStream.close();
    }

    @Test
    public void testParseByteData() throws IOException {
        String testData = "Test Data";
        byte[] testBytes = testData.getBytes(TEST_CHARSET);
        ByteBuffer byteBuffer = ByteBuffer.wrap(testBytes);
        InputStream inStream = new ByteArrayInputStream(testBytes);

        Document doc = DataUtil.parseByteData(byteBuffer, TEST_CHARSET, TEST_BASE_URI, parser);

        assertEquals("Parsed byte data with expected charset", TEST_CHARSET, doc.outputSettings().charset());

        inStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = TEST_CONTENT_TYPE;

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("Expected charset returned from content type", TEST_CHARSET, charset);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testData = "Test Data";
        byte[] testBytes = testData.getBytes(TEST_CHARSET);

        InputStream inStream = new ByteArrayInputStream(testBytes);

        Document doc = DataUtil.load(inStream, TEST_CHARSET, TEST_BASE_URI, parser);

        assertEquals("Loaded input stream with expected charset", TEST_CHARSET, doc.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test Data";
        byte[] testBytes = testData.getBytes(TEST_CHARSET);
        InputStream inStream = new ByteArrayInputStream(testBytes);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream);

        byte[] expectedBytes = new byte[testBytes.length];
        System.arraycopy(testBytes, 0, expectedBytes, 0, testBytes.length);

        assertEquals("Expected read bytes to match original bytes", expectedBytes, byteBuffer.array());

        inStream.close();
    }
}