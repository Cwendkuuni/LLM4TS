package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.Elements;
import org.jsoup.nodes.Element;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "testFile.html";
        File file = new File(filePath);
        Document doc = DataUtil.load(file, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test Title</title>\n</head>\n<body>\n<h1>Test Heading</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test Title</title>\n</head>\n<body>\n<h1>Test Heading</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, "INVALID_CHARSET", null));
    }

    @Test
    public void testParseByteDataWithValidCharset() {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test Title</title>\n</head>\n<body>\n<h1>Test Heading</h1>\n</body>\n</html>";
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(testData.getBytes("UTF-8")));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithInvalidCharsetName() {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test Title</title>\n</head>\n<body>\n<h1>Test Heading</h1>\n</body>\n</html>";
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(testData.getBytes("UTF-8")));
        assertThrows(IllegalCharsetNameException.class, () -> DataUtil.parseByteData(byteData, "INVALID_CHARSET", null, Parser.htmlParser()));
    }

    @Test
    public void testParseByteDataWithUnsupportedCharset() {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test Title</title>\n</head>\n<body>\n<h1>Test Heading</h1>\n</body>\n</html>";
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(testData.getBytes("UTF-8")));
        assertThrows(UnsupportedCharsetException.class, () -> DataUtil.parseByteData(byteData, "UNSUPPORTED_CHARSET", null, Parser.htmlParser()));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertNotNull(Charset.forName(charset));
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=INVALID_CHARSET";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test Title</title>\n</head>\n<body>\n<h1>Test Heading</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test Title</title>\n</head>\n<body>\n<h1>Test Heading</h1>\n"; // Partial data
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 5);
        assertNotNull(byteData);
    }

}