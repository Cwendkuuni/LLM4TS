package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser.Tag;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testGetCharsetFromContentType() {
        // Test cases with null or empty contentType
        assertNull(DataUtil.getCharsetFromContentType(null));
        assertNull(DataUtil.getCharsetFromContentType(""));
        assertNull(DataUtil.getCharsetFromContentType(" text/html"));
        assertNull(DataUtil.getCharsetFromContentType(" text/html; charset="));

        // Test cases with valid charsets
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(" text/html; charset=UTF-8"));
        assertEquals("EUC-JP", DataUtil.getCharsetFromContentType(" text/html; charset=EUC-JP"));
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType(" text/html; charset=ISO-8859-1"));
        assertEquals("gb2312", DataUtil.getCharsetFromContentType(" text/html; charset=gb2312"));
        assertEquals("windows-1251", DataUtil.getCharsetFromContentType(" text/html; charset=windows-1251"));

        // Test cases with invalid charsets
        assertNull(DataUtil.getCharsetFromContentType(" text/html; charset=invalidCharset"));
        assertNull(DataUtil.getCharsetFromContentType(" text/html; charset= "));
    }

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        String baseUri = "https://www.example.com";
        File file = new File("test.html");
        String charsetName = "UTF-8";

        // Test case with empty file
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNull(document);

        // Test case with a valid file
        String expectedHtml = "<!doctype html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<meta name=\"description\" content=\"Test page\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World!</h1>\n" +
                "</body>\n" +
                "</html>";

        File testFile = new File("testData/test.html");
        document = DataUtil.load(testFile, charsetName, baseUri);
        assertEquals(expectedHtml, document.outerHtml());

        // Test case with a file with an unsupported charset
        File unsupportedCharsetFile = new File("testData/unsupportedCharset.html");
        document = DataUtil.load(unsupportedCharsetFile, null, baseUri);
        assertEquals("<!doctype html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>The document's character set is not supported.</h1>\n" +
                "</body>\n" +
                "</html>", document.outerHtml());
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String baseUri = "https://www.example.com";
        InputStream inputStream = new ByteArrayInputStream("test input".getBytes());
        String charsetName = "UTF-8";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals("<html><body><p>test input</p></body></html>", document.outerHtml());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithParser() throws IOException {
        String baseUri = "https://www.example.com";
        InputStream inputStream = new ByteArrayInputStream("<root><element>test</element></root>".getBytes());
        String charsetName = "UTF-8";
        Parser parser = mock(Parser.class);
        when(parser.parseInput((String) any(), (String) any())).thenReturn(new Document());

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertEquals("<root><element>test</element></root>", document.outerHtml());
    }

    @Test
    public void testParseByteData() {
        String baseUri = "https://www.example.com";
        String charsetName = "UTF-8";
        String docData = "<!doctype html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World!</h1>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertEquals(docData, document.outerHtml());

        byteData.rewind();
        charsetName = "gb2312";
        docData = "<!doctype html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"gb2312\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>你好世界!</h1>\n" +
                "</body>\n" +
                "</html>";
        document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertEquals(docData, document.outerHtml());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        byte[] expectedBytes = testData.getBytes();
        assertArrayEquals(expectedBytes, byteData.array());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String testData = "test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        int maxSize = 10;

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
        byte[] expectedBytes = testData.substring(0, maxSize).getBytes();
        assertArrayEquals(expectedBytes, byteData.array());
    }

    @Test
    public void testReadToByteBufferWithNullInputStream() {
        assertThrows(NullPointerException.class, () -> DataUtil.readToByteBuffer(null));
    }

    @Test
    public void testReadToByteBufferWithNullMaxSize() {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());

        assertThrows(NullPointerException.class, () -> DataUtil.readToByteBuffer(inputStream, null));
    }
}