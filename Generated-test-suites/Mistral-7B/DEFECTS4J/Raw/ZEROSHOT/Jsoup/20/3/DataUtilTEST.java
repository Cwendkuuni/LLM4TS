package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    private static Document doc;
    private static Parser parser;

    @BeforeAll
    public static void setup() {
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoadFromFile() throws IOException {
        File inputFile = new File("input.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        doc = DataUtil.load(inputFile, charsetName, baseUri);
        Assertions.assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStreamCharsetNull() throws IOException {
        String htmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.load(inputStream, null, "");
        Assertions.assertNotNull(doc);
        String encodedCharset = doc.head().selectFirst("meta[charset]") != null ? doc.head().selectFirst("meta[charset]").attr("charset") : null;
        Assertions.assertEquals("UTF-8", encodedCharset);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String htmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assertions.assertNotNull(doc);
        String encodedCharset = doc.head().selectFirst("meta[charset]") != null ? doc.head().selectFirst("meta[charset]").attr("charset") : null;
        Assertions.assertEquals("UTF-8", encodedCharset);
    }

    @Test
    public void testLoadFromInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.ISO_8859_1));
        doc = DataUtil.load(inputStream, null, "");
        Assertions.assertNotNull(doc);
        String encodedCharset = doc.head().selectFirst("meta[charset]") != null ? doc.head().selectFirst("meta[charset]").attr("charset") : null;
        Assertions.assertNull(encodedCharset);
        Assertions.assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteData() throws IOException {
        String htmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8)));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assertions.assertNotNull(doc);
        String encodedCharset = doc.head().selectFirst("meta[charset]") != null ? doc.head().selectFirst("meta[charset]").attr("charset") : null;
        Assertions.assertEquals("UTF-8", encodedCharset);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String htmlContent = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.ISO_8859_1)));
        doc = DataUtil.parseByteData(byteData, null, "");
        Assertions.assertNotNull(doc);
        String encodedCharset = doc.head().selectFirst("meta[charset]") != null ? doc.head().selectFirst("meta[charset]").attr("charset") : null;
        Assertions.assertNull(encodedCharset);
        Assertions.assertEquals("UTF-8", doc.outputSettings().charset());
    }
}