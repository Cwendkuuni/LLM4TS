import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    @Test
    public void testLoadWithFile() {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testLoadWithInputStream() {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.load(in, charsetName, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testLoadWithInputStreamAndParser() {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        try {
            Document doc = DataUtil.load(in, charsetName, baseUri, parser);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testParseByteDataWithCharset() {
        ByteBuffer byteData = ByteBuffer.allocate(10);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        ByteBuffer byteData = ByteBuffer.allocate(10);
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() {
        InputStream inStream = new FileInputStream("test.html");
        int maxSize = 100;
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inStream, maxSize);
            assertNotNull(byteData);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() {
        InputStream inStream = new FileInputStream("test.html");
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
            assertNotNull(byteData);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testGetCharsetFromContentTypeWithCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithoutCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}