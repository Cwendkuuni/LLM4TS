package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    private static final String TEST_HTML = "<html lang=\"en\"><head><meta charset=\"UTF-8\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>Test</title></head><body><div id=\"content\">Hello World!</div></body></html>";
    private Document htmlDocument;

    @Before
    public void setup() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_HTML.getBytes());
        htmlDocument = DataUtil.load(inputStream, "UTF-8", "");
    }

    @Test
    public void testLoadFileWithUTF8Charset() throws IOException {
        File testFile = new File("test.html");
        FileInputStream fileInputStream = new FileInputStream(testFile);
        Document document = DataUtil.load(testFile, "UTF-8", "");
        fileInputStream.close();

        // Check that the loaded document is as expected
        assertEquals("Test", htmlDocument.title());
        assertEquals("Hello World!", htmlDocument.select("#content").text());

        // Test that the document's charset is correctly identified
        assertEquals("UTF-8", htmlDocument.outputSettings().charset());
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File testFile = new File("test.html");
        DataUtil.load(testFile, "invalidCharset", "");
    }

    @Test
    public void testLoadInputStreamWithUTF8Charset() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(TEST_HTML.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document document = DataUtil.load(inputStream, "UTF-8", "");
        inputStream.close();

        // Check that the loaded document is as expected
        assertEquals("Test", htmlDocument.title());
        assertEquals("Hello World!", htmlDocument.select("#content").text());

        // Test that the document's charset is correctly identified
        assertEquals("UTF-8", htmlDocument.outputSettings().charset());
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharsetAndNoHeaderOrMeta() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(TEST_HTML.getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document document = DataUtil.load(inputStream, null, "");
        inputStream.close();
    }

    @Test
    public void testParseByteData() throws IOException {
        String testData = "<html lang=\"en\"><head><meta charset=\"UTF-8\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>Test</title></head><body><div id=\"content\">Hello World!</div></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Document document = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        inputStream.close();

        // Check that the parsed document is as expected
        assertEquals("Test", htmlDocument.title());
        assertEquals("Hello World!", htmlDocument.select("#content").text());

        // Test that the document's charset is correctly identified
        assertEquals("UTF-8", htmlDocument.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithInvalidCharsetDetected() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(TEST_HTML.getBytes());
        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
        Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());

        // Check that the parsed document's charset is set to UTF-8
        assertEquals("UTF-8", document.outputSettings().charset());

        // Test that the document's meta charset is correctly identified and applied
        Element meta = document.select("meta[charset]").first();
        assertEquals("UTF-8", meta.attr("charset"));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_HTML.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        inputStream.close();

        // Test that the byte buffer contains the expected data
        assertEquals(TEST_HTML.getBytes().length, byteBuffer.limit());

        // Test that the buffer's data can be decoded to a string
        String decodedData = Charset.forName("UTF-8").decode(byteBuffer).toString();
        assertEquals(TEST_HTML, decodedData);
    }
}