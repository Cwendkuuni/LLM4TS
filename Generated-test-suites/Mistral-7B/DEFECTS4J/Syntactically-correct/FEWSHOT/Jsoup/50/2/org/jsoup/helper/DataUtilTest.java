package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals("Test", doc.body().text());

        charsetName = "ISO-8859-1";
        byteData = ByteBuffer.wrap("<html><head><meta charset=\"ISO-8859-1\"></head><body>Test</body></html>".getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals("Test", doc.body().text());

        charsetName = null;
        byteData = ByteBuffer.wrap("<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>".getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals("Test", doc.body().text());

        String metaCharset = "UTF-16";
        byteData = ByteBuffer.wrap("<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=" + metaCharset + "\"><body>Test</body></html>".getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals("Test", doc.body().text());

        byteData = ByteBuffer.wrap("<html><head><meta charset=\"UTF-16\"></head><body>Test</body></html>".getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals("Test", doc.body().text());

        charsetName = "unknown_charset";
        byteData = ByteBuffer.wrap("<html><head><meta charset=\"unknown_charset\"></head><body>Test</body></html>".getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNull(doc);

        byteData = ByteBuffer.wrap("<html><head></head><body>Test</body></html>".getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNull(doc);
    }

    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        File file = new File("test.html");
        WriteFile("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>", file);
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertEquals("Test", doc.body().text());

        charsetName = null;
        doc = DataUtil.load(file, charsetName, baseUri);
        assertEquals("Test", doc.body().text());

        charsetName = "ISO-8859-1";
        doc = DataUtil.load(file, charsetName, baseUri);
        assertEquals("Test", doc.body().text());

        charsetName = "UTF-16";
        WriteFile("<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n<html><head><meta charset=\"UTF-16\"></head><body>Test</body></html>", file);
        doc = DataUtil.load(file, charsetName, baseUri);
        assertEquals("Test", doc.body().text());

        charsetName = "unknown_charset";
        WriteFile("<?xml version=\"1.0\" encoding=\"unknown_charset\"?>\n<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>", file);
        doc = DataUtil.load(file, charsetName, baseUri);
        assertNull(doc);

        WriteFile("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head></head><body>Test</body></html>", file);
        doc = DataUtil.load(file, charsetName, baseUri);
        assertNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals("Test", doc.body().text());

        charsetName = null;
        doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals("Test", doc.body().text());

        charsetName = "ISO-8859-1";
        doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals("Test", doc.body().text());

        charsetName = "UTF-16";
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_16);
        doc = DataUtil.load(reader, charsetName, baseUri);
        assertEquals("Test", doc.body().text());

        charsetName = "unknown_charset";
        reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        doc = DataUtil.load(reader, charsetName, baseUri);
        assertNull(doc);

        Reader readerWithBOM = new InputStreamReader(new ByteArrayInputStream(new byte[] { UNICODE_BOM }), StandardCharsets.UTF_8);
        doc = DataUtil.load(readerWithBOM, charsetName, baseUri);
        assertEquals("", doc.body().text());
        charsetName = "UTF-8";
        doc = DataUtil.load(readerWithBOM, charsetName, baseUri);
        assertEquals("Test", doc.body().text());

        reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        doc = DataUtil.load(reader, null, baseUri);
        assertEquals("Test", doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        String xmlContent = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertEquals("Test", doc.body().text());

        charsetName = null;
        doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertEquals("Test", doc.body().text());

        charsetName = "ISO-8859-1";
        doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertEquals("Test", doc.body().text());

        charsetName = "UTF-16";
        InputStream inputStreamWithBOM = new ByteArrayInputStream(new byte[] { UNICODE_BOM }
                , 0, 1);
        InputStream contentInputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_16));
        InputStream mergedInputStream = new SequenceInputStream(inputStreamWithBOM, contentInputStream);
        doc = DataUtil.load(mergedInputStream, charsetName, baseUri, parser);
        assertEquals("Test", doc.body().text());

        charsetName = "unknown_charset";
        doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "Test";
        String outputContent = "";
        InputStream in = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));
        OutputStream out = new FileOutputStream("output.txt");

        DataUtil.crossStreams(in, out);
        String readContent = new String(Files.readAllBytes(Paths.get("output.txt")), StandardCharsets.UTF_8);
        assertEquals(inputContent, readContent);

        in = new ByteArrayInputStream(new byte[0]);
        out = new FileOutputStream("output.txt");
        DataUtil.crossStreams(in, out);
        assertTrue(Files.notExists(Paths.get("output.txt")));

        in = new ByteArrayInputStream(new byte[bufferSize * 2]);
        out = new FileOutputStream("output.txt");
        DataUtil.crossStreams(in, out);
        String readContent2 = new String(Files.readAllBytes(Paths.get("output.txt")), StandardCharsets.UTF_8);
        assertEquals(inputContent, readContent2.substring(0, inputContent.length()));
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType("text/html; charset=ISO-8859-1"));
        assertEquals("", DataUtil.getCharsetFromContentType("text/html"));
        assertEquals("", DataUtil.getCharsetFromContentType("text/html; charset=unknown_charset"));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == 32);
        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(mimeBoundaryChars.contains(boundary.charAt(i)));
        }
    }

    public static void WriteFile(String content, File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.close();
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        assertFalse(byteData.hasRemaining());
        assertFalse(byteData.hasArray());
        assertFalse(byteData.isDirect());
        assertEquals(0, byteData.limit());
        assertEquals(0, byteData.position());
    }
}

class SequenceInputStream extends InputStream {
    private InputStream _inputStream;
    private InputStream _nextInputStream;
    private int _nextByte;

    public SequenceInputStream(InputStream inputStream, InputStream nextInputStream) {
        _inputStream = inputStream;
        _nextInputStream = nextInputStream;
    }

    public int read() throws IOException {
        int result = _inputStream.read();
        if (result != -1) {
            return result;
        }
        if (_nextByte != -1) {
            return _nextByte;
        }
        _nextByte = _nextInputStream.read();
        return _nextByte;
    }
}