package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String fileContent = "test<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body><p>Hello World</p>\n</body>\n</html>";
        File file = new File("test.html");
        FileWriter writer = new FileWriter(file);
        writer.write(fileContent);
        writer.close();

        Document doc = DataUtil.load(file, null, null);
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals("Hello World", doc.select("p").text());

        file.delete();
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        String fileContent = "test<html>\n<head>\n<meta charset=\"ISO-8859-1\">\n</head>\n<body><p>Hello World</p>\n</body>\n</html>";
        File file = new File("test.html");
        FileWriter writer = new FileWriter(file);
        writer.write(fileContent);
        writer.close();

        Document doc = DataUtil.load(file, "ISO-8859-1", null);
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
        assertEquals("Hello World", doc.select("p").text());

        file.delete();
    }

    @Test
    public void testLoadFileWithDetectedCharset() throws IOException {
        String fileContent = "test<html>\n<head>\n<meta charset=\"GBK\">\n</head>\n<body><p>Hello World</p>\n</body>\n</html>";
        File file = new File("test.html");
        FileWriter writer = new FileWriter(file);
        writer.write(fileContent);
        writer.close();

        Document doc = DataUtil.load(file, null, null);
        assertEquals("GBK", doc.outputSettings().charset());
        assertEquals("Hello World", doc.select("p").text());

        file.delete();
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String content = "test<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body><p>Hello World</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        Document doc = DataUtil.load(inputStream, null, null);
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals("Hello World", doc.select("p").text());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String content = "test<html>\n<head>\n<meta charset=\"ISO-8859-1\">\n</head>\n<body><p>Hello World</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        Document doc = DataUtil.load(inputStream, "ISO-8859-1", null);
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
        assertEquals("Hello World", doc.select("p").text());
    }

    @Test
    public void testLoadInputStreamWithDetectedCharset() throws IOException {
        String content = "test<html>\n<head>\n<meta charset=\"GBK\">\n</head>\n<body><p>Hello World</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        Document doc = DataUtil.load(inputStream, null, null);
        assertEquals("GBK", doc.outputSettings().charset());
        assertEquals("Hello World", doc.select("p").text());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String charset = "UTF-8";
        String content = "test<html>\n<head>\n<meta charset=\"" + charset + "\">\n</head>\n<body><p>Hello World</p>\n</body>\n</html>";
        ByteBuffer byteData = ByteBuffer.wrap(content.getBytes());

        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals(charset, doc.outputSettings().charset());
        assertEquals("Hello World", doc.select("p").text());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String content = "test<html>\n<head>\n<meta charset=\"GBK\">\n</head>\n<body><p>Hello World</p>\n</body>\n</html>";
        ByteBuffer byteData = ByteBuffer.wrap(content.getBytes());

        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("GBK", doc.outputSettings().charset());
        assertEquals("Hello World", doc.select("p").text());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String charset = "ISO-8859-1";
        String content = "test<html>\n<head>\n<meta charset=\"" + charset + "\">\n</head>\n<body><p>Hello World</p>\n</body>\n</html>";
        ByteBuffer byteData = ByteBuffer.wrap(content.getBytes());

        Document doc = DataUtil.parseByteData(byteData, charset, null, Parser.htmlParser());
        assertEquals(charset, doc.outputSettings().charset());
        assertEquals("Hello World", doc.select("p").text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String content = "test<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body><p>Hello World</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedBytes = content.getBytes();
        byte[] actualBytes = new byte[byteData.limit()];
        byteData.get(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String content = "test<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body><p>Hello World</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 10);

        byte[] expectedBytes = "test<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n".getBytes();
        byte[] actualBytes = new byte[byteData.limit()];
        byteData.get(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html; charset=ISO-8859-1";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("ISO-8859-1", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}