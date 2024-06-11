package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class DataUtilTest {
    private static final String VALID_HTML = "<html><body><h1>Hello, world!</h1></body></html>";
    private static final String INVALID_HTML = "<html><body><h1>Hello, world!</h2></body></html>";
    private static final String CHARSET_ISO_8859_1 = "ISO-8859-1";
    private static final String CHARSET_UTF_8 = "UTF-8";
    private static final String BASE_URI = "https://example.com/";

    @Test
    public void testLoadFromFile() throws Exception {
        File file = createTempFileWithContent(VALID_HTML);
        Document doc = DataUtil.load(file, CHARSET_UTF_8, BASE_URI);
        assertNotNull(doc);
        assertEquals("Hello, world!", doc.select("h1").text());
    }

    @Test
    public void testLoadFromInputStream() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(VALID_HTML.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, CHARSET_UTF_8, BASE_URI);
        assertNotNull(doc);
        assertEquals("Hello, world!", doc.select("h1").text());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(VALID_HTML.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, CHARSET_UTF_8, BASE_URI, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("Hello, world!", doc.select("h1").text());
    }

    @Test
    public void testCrossStreams() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(VALID_HTML.getBytes(StandardCharsets.UTF_8));
        FileOutputStream outputStream = new FileOutputStream(createTempFile());
        DataUtil.crossStreams(inputStream, outputStream);
        outputStream.close();

        Document doc = DataUtil.load(createTempFile(), CHARSET_UTF_8, BASE_URI);
        assertNotNull(doc);
        assertEquals("Hello, world!", doc.select("h1").text());
    }

    @Test
    public void testParseInputStreamWithCharset() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(VALID_HTML.getBytes(StandardCharsets.ISO_8859_1));
        Document doc = DataUtil.parseInputStream(inputStream, CHARSET_ISO_8859_1, BASE_URI, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("Hello, world!", doc.select("h1").text());
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(VALID_HTML.getBytes(StandardCharsets.UTF_8));
        byte[] bytes = DataUtil.readToByteBuffer(inputStream).array();
        assertEquals(VALID_HTML, new String(bytes, StandardCharsets.UTF_8));
    }

    @Test
    public void testReadFileToByteBuffer() throws Exception {
        File file = createTempFileWithContent(VALID_HTML);
        byte[] bytes = DataUtil.readFileToByteBuffer(file).array();
        assertEquals(VALID_HTML, new String(bytes, StandardCharsets.UTF_8));
    }

    @Test
    public void testEmptyByteBuffer() {
        assertEquals(0, DataUtil.emptyByteBuffer().capacity());
    }

    private File createTempFile() throws Exception {
        File file = File.createTempFile("jsoup", ".html");
        file.deleteOnExit();
        return file;
    }

    private File createTempFileWithContent(String content) throws Exception {
        File file = createTempFile();
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(content.getBytes(StandardCharsets.UTF_8));
        outputStream.close();
        return file;
    }
}