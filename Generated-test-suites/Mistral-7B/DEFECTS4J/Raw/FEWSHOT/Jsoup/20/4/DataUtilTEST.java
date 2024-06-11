package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser.htmlParser;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFileDocument() throws Exception {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>";
        File tempFile = File.createTempFile("DataUtilTest", null);
        try (FileWriter fileWriter = new FileWriter(tempFile)) {
            fileWriter.write(fileContent);
        }

        Document doc = DataUtil.load(tempFile, "UTF-8", null);
        assertNotNull(doc);
        assertEquals("<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>", doc.html());

        tempFile.delete();
    }

    @Test
    public void testLoadInputStreamDocument() throws Exception {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
        assertEquals("<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>", doc.html());
    }

    @Test
    public void testLoadInputStreamDocumentWithCustomParser() throws Exception {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, "UTF-8", null, htmlParser.instance());
        assertNotNull(doc);
        assertEquals("<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>", doc.html());
    }

    @Test
    public void testDetermineCharset() throws Exception {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html; charset=GB2312";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("GB2312", charset);

        contentType = "text/html; charset=ISO-8859-1";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("ISO-8859-1", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws Exception {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>";
        ByteBuffer byteData = Charset.forName("UTF-8").decode(fileContent.getBytes("UTF-8"));

        Document doc = DataUtil.parseByteData(byteData, null, null, htmlParser.instance());
        assertNotNull(doc);
        assertEquals("<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>", doc.html());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws Exception {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>";
        ByteBuffer byteData = Charset.forName("UTF-8").decode(fileContent.getBytes("UTF-8"));

        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, htmlParser.instance());
        assertNotNull(doc);
        assertEquals("<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>", doc.html());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws Exception {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>";
        ByteBuffer byteData = Charset.forName("UTF-8").decode(fileContent.getBytes("UTF-8"));

        Document doc = DataUtil.parseByteData(byteData, null, null, htmlParser.instance());
        assertEquals("<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>", doc.html());

        byteData.rewind();
        Element meta = doc.select("meta[http-equiv=content-type], meta[charset]").first();
        String charset = meta != null ? meta.attr("charset") : null;
        assertEquals("UTF-8", charset);
    }
}