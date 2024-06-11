package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        File inputFile = new File("testFile.html");
        FileUtils.writeStringToFile(inputFile, fileContent, "UTF-8");

        Document document = DataUtil.load(inputFile, null, "/");

        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>\n</title>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>", document.html());
        FileUtils.deleteQuietly(inputFile);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"ISO-8859-1\">\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        File inputFile = new File("testFile.html");
        FileUtils.writeStringToFile(inputFile, fileContent, "UTF-8");

        Document document = DataUtil.load(inputFile, "ISO-8859-1", "/");

        assertEquals("ISO-8859-1", document.outputSettings().charset());
        assertEquals("<html>\n<head>\n<meta charset=\"ISO-8859-1\">\n<title>\n</title>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>", document.html());
        FileUtils.deleteQuietly(inputFile);
    }

    @Test
    public void testLoadFileWithDetectedCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"gb2312\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        File inputFile = new File("testFile.html");
        FileUtils.writeStringToFile(inputFile, fileContent, "UTF-8");

        Document document = DataUtil.load(inputFile, null, "/");

        assertEquals("gb2312", document.outputSettings().charset());
        assertEquals("<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>\n</title>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>", document.html());
        FileUtils.deleteQuietly(inputFile);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));

        Document document = DataUtil.load(inputStream, null, "/");

        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>\n</title>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>", document.html());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"ISO-8859-1\">\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));

        Document document = DataUtil.load(inputStream, "ISO-8859-1", "/");

        assertEquals("ISO-8859-1", document.outputSettings().charset());
        assertEquals("<html>\n<head>\n<meta charset=\"ISO-8859-1\">\n<title>\n</title>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>", document.html());
    }

    @Test
    public void testLoadInputStreamWithDetectedCharset() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"gb2312\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));

        Document document = DataUtil.load(inputStream, null, "/");

        assertEquals("gb2312", document.outputSettings().charset());
        assertEquals("<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>\n</title>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>", document.html());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"ISO-8859-1\">\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));

        Document document = DataUtil.load(inputStream, null, "/", Parser.xmlParser());

        assertEquals("ISO-8859-1", document.outputSettings().charset());
        assertEquals("<html>\n<head>\n<meta charset=\"ISO-8859-1\">\n<title>\n</title>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>", document.xml().asXml());
    }
}