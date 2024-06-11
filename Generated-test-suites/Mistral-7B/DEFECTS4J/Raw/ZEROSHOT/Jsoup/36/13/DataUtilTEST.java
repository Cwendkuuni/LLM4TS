package org.jsoup.helper;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputFile, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileInvalidFile() throws IOException {
        File inputFile = new File("nonexistentfile.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.load(inputFile, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "html content";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamNullInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.load(null, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStreamInvalidCharset() throws IOException {
        String htmlContent = "html content";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        String charsetName = "INVALID_CHARSET";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNull(document);
    }

    @Test
    public void testLoadInputStreamDetectCharset() throws IOException {
        String htmlContent = "<html lang=\"fr\">\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<h1>Test</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, null, baseUri);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testParseByteData() throws IOException {
        String htmlContent = "<html lang=\"fr\">\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<h1>Test</h1>\n</body>\n</html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertEquals("UTF-8", document.outputSettings().charset());

        byteData.rewind();
        htmlContent += "\uFEFF";
        document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = "<html lang=\"fr\">\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<h1>Test</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(htmlContent.length() + 1, byteData.limit()); // +1 for the byte buffer marker
    }

    @Test
    public void testReadToByteBufferNullInputStream() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(null);
        assertEquals(0, byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeInvalidCharset() {
        String contentType = "text/html; charset=INVALID_CHARSET";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}