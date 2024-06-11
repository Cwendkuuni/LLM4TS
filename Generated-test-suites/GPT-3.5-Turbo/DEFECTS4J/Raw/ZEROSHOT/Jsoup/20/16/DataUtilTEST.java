package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    private Document document;
    private String baseUri;
    private InputStream in;
    private String charsetName;
    private Parser parser;

    @Before
    public void setUp() {
        document = null;
        baseUri = "http://example.com";
        in = null;
        charsetName = "UTF-8";
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoadFile() {
        try {
            File file = new File("testfile.txt");
            document = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            in = new FileInputStream("testfile.txt");
            document = DataUtil.load(in, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    fail("IOException should not be thrown");
                }
            }
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            in = new FileInputStream("testfile.txt");
            document = DataUtil.load(in, charsetName, baseUri, parser);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    fail("IOException should not be thrown");
                }
            }
        }
    }

    @Test
    public void testParseByteDataWithCharsetNull() {
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        document = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithCharsetNotNull() {
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            in = new FileInputStream("testfile.txt");
            ByteBuffer byteData = DataUtil.readToByteBuffer(in);
            assertNotNull(byteData);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    fail("IOException should not be thrown");
                }
            }
        }
    }

    @Test
    public void testGetCharsetFromContentTypeWithCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithoutCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithEmptyCharset() {
        String contentType = "text/html; charset=";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithTrimmedCharset() {
        String contentType = "text/html; charset=  UTF-8  ";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
}