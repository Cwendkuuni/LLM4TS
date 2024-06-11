package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Locale;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

public class DataUtilTest {
    
    private File testFile;
    private ByteBuffer testByteBuffer;
    private String testCharset;
    private String testBaseUri;
    private Parser testParser;

    @Before
    public void setup() {
        testFile = new File("test.txt");
        testByteBuffer = ByteBuffer.wrap("test".getBytes());
        testCharset = "UTF-8";
        testBaseUri = "http://example.com";
        testParser = Parser.htmlParser();
    }

    @Test
    public void testLoadFile() throws IOException {
        FileInputStream mockInStream = mock(FileInputStream.class);
        Document result = DataUtil.load(testFile, testCharset, testBaseUri);
        assertNotNull(result);
        verify(mockInStream).close();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        ByteArrayInputStream mockInStream = mock(ByteArrayInputStream.class);
        Document result = DataUtil.load(mockInStream, testCharset, testBaseUri);
        assertNotNull(result);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        ByteArrayInputStream mockInStream = mock(ByteArrayInputStream.class);
        Document result = DataUtil.load(mockInStream, testCharset, testBaseUri, testParser);
        assertNotNull(result);
    }

    @Test
    public void testParseByteData() {
        Document result = DataUtil.parseByteData(testByteBuffer, testCharset, testBaseUri, testParser);
        assertNotNull(result);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        ByteArrayInputStream mockInStream = mock(ByteArrayInputStream.class);
        ByteBuffer result = DataUtil.readToByteBuffer(mockInStream, 10);
        assertNotNull(result);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteArrayInputStream mockInStream = mock(ByteArrayInputStream.class);
        ByteBuffer result = DataUtil.readToByteBuffer(mockInStream);
        assertNotNull(result);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String result = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", result);
    }
}