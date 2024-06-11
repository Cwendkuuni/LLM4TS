package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFileWithCharset() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputFile, charsetName, baseUri);
        assertNotNull(document);
        assertEquals(charsetName, document.outputSettings().charset());
    }

    @Test
    public void testLoadFileWithoutCharset() throws IOException {
        File inputFile = new File("test.html");
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputFile, null, baseUri);
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<document/>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
        assertEquals(charsetName, document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithoutCharset() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<document/>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, null, baseUri);
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n<document/>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
        String baseUri = "http://example.com";

        assertThrows(UnsupportedEncodingException.class, () -> DataUtil.load(inputStream, "UTF-16", baseUri));
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<document/>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals(charsetName, document.outputSettings().charset());
    }

    @Test
    public void testParseByteData() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<document/>";
        ByteBuffer byteData = ByteBuffer.wrap(content.getBytes());

        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String content = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n" +
                "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\"/>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        ByteBuffer byteData = ByteBuffer.wrap(content.getBytes());

        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("gb2312", document.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String content = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n" +
                "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-16\"/>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        ByteBuffer byteData = ByteBuffer.wrap(content.getBytes());

        assertThrows(UnsupportedEncodingException.class, () -> DataUtil.parseByteData(byteData, "UTF-16", null, Parser.htmlParser()));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=UTF-16";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<document/>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
        assertEquals(content.length(), byteData.limit());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<document/>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
        int maxSize = 10;

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
        assertNotNull(byteData);
        assertEquals(maxSize, byteData.limit());
    }
}