package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        File testFile = new File("test.html");
        Files.write(testFile.getAbsoluteFile().toPath(), fileContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(testFile, "UTF-8", null);
        assertNotNull(doc);
        assertEquals("<html>\n<head>\n<meta charset=\"UTF-8\"/>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>", doc.html());

        testFile.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
        assertEquals("<html>\n<head>\n<meta charset=\"UTF-8\"/>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>", doc.html());
    }

    @Test
    public void testLoadInputStreamWithCharsetDetection() throws IOException {
        String inputStreamContent = "<html>\n<head>\n<meta charset=\"ISO-8859-1\"/>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, null, null);
        assertNotNull(doc);
        assertEquals("<html>\n<head>\n<meta charset=\"ISO-8859-1\"/>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>", doc.html());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String inputStreamContent = "<html>\n<head>\n<meta charset=\"XYZ\"/>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, null, null));
    }

    @Test
    public void testLoadInvalidFile() {
        File testFile = new File("test.html");

        assertThrows(IOException.class, () -> DataUtil.load(testFile, null, null));
    }

    @Test
    public void testParseInputStream() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("<html>\n<head>\n<meta charset=\"UTF-8\"/>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>", doc.html());
    }

    @Test
    public void testParseInputStreamWithCharsetDetection() throws IOException {
        String inputStreamContent = "<html>\n<head>\n<meta charset=\"ISO-8859-1\"/>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("<html>\n<head>\n<meta charset=\"ISO-8859-1\"/>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>", doc.html());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String inputStreamContent = "<html>\n<head>\n<meta charset=\"XYZ\"/>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser()));
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        String inputStreamContent = "<html>\n<head>\n<meta charset=\"UTF-8\"/>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.xmlParser());
        assertNotNull(doc);
        assertEquals("<html>\n<head>\n<meta charset=\"UTF-8\"/>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>", doc.html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputStreamContent = "Hello World!";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        assertEquals("Hello World!", outputStream.toString(StandardCharsets.UTF_8));
    }
}