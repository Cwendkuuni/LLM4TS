package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test</title>\n</head>\n<body>\n<p>Hello, World!</p>\n</body>\n</html>";
        File file = new File("test.html");
        FileUtils.writeStringToFile(file, fileContent, "UTF-8");

        Document doc = DataUtil.load(file, null);
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().text());

        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testLoadFileWithCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test</title>\n</head>\n<body>\n<p>Hello, World!</p>\n</body>\n</html>";
        File file = new File("test.html");
        FileUtils.writeStringToFile(file, fileContent, "UTF-8");

        Document doc = DataUtil.load(file, "UTF-8");
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().text());

        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test</title>\n</head>\n<body>\n<p>Hello, World!</p>\n</body>\n</html>";
        File file = new File("test.html");
        FileUtils.writeStringToFile(file, fileContent, "UTF-8");

        Document doc = DataUtil.load(file, "ISO-8859-1");
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().text());

        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test</title>\n</head>\n<body>\n<p>Hello, World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, null);
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test</title>\n</head>\n<body>\n<p>Hello, World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, "UTF-8");
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test</title>\n</head>\n<body>\n<p>Hello, World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, "ISO-8859-1");
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testParseByteData() throws IOException {
        String byteDataContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test</title>\n</head>\n<body>\n<p>Hello, World!</p>\n</body>\n</html>";
        ByteBuffer byteData = Charset.forName("UTF-8").encode(Charset.forName("UTF-8").decode(byteDataContent.getBytes()));

        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testParseByteDataWithCharset() throws IOException {
        String byteDataContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test</title>\n</head>\n<body>\n<p>Hello, World!</p>\n</body>\n</html>";
        ByteBuffer byteData = Charset.forName("UTF-8").encode(Charset.forName("UTF-8").decode(byteDataContent.getBytes()));

        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String byteDataContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test</title>\n</head>\n<body>\n<p>Hello, World!</p>\n</body>\n</html>";
        ByteBuffer byteData = Charset.forName("UTF-8").encode(Charset.forName("UTF-8").decode(byteDataContent.getBytes()));

        Document doc = DataUtil.parseByteData(byteData, "ISO-8859-1", null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String contentType2 = "text/html; charset=ISO-8859-1";
        String contentType3 = "text/html";

        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        String charset3 = DataUtil.getCharsetFromContentType(contentType3);

        assertEquals("UTF-8", charset1);
        assertEquals("ISO-8859-1", charset2);
        assertNull(charset3);
    }
}