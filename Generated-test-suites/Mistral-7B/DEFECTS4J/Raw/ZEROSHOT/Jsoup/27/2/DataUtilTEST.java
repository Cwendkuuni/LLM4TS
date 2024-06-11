import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@RunWith(JUnit4.class)
public class DataUtilTest {

    private static Document expectedDocument;
    private static File inputFile;

    @BeforeClass
    public static void setUp() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        expectedDocument = DataUtil.parseString(html);
        inputFile = File.createTempFile("test", ".html");
        FileOutputStream outputStream = new FileOutputStream(inputFile);
        outputStream.write(html.getBytes());
        outputStream.close();
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        Document actualDocument = DataUtil.load(inputFile, null, null);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        Document actualDocument = DataUtil.load(inputFile, "UTF-8", null);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        Document actualDocument = DataUtil.load(inputFile, "INVALID_CHARSET", null);
        assertNull(actualDocument);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        InputStream inputStream = new FileInputStream(inputFile);
        Document actualDocument = DataUtil.load(inputStream, null, null);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        InputStream inputStream = new FileInputStream(inputFile);
        Document actualDocument = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = new FileInputStream(inputFile);
        Document actualDocument = DataUtil.load(inputStream, "INVALID_CHARSET", null);
        assertNull(actualDocument);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedParser() throws IOException {
        InputStream inputStream = new FileInputStream(inputFile);
        Document actualDocument = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertEquals(expectedDocument, actualDocument);
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithInvalidCharset() {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write("Test".getBytes());
        ByteBuffer byteData = ByteBuffer.wrap(byteStream.toByteArray());
        String charsetName = "INVALID_CHARSET";
        try {
            DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
            assertFalse("Expected IOException", true);
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write("Test".getBytes());
        ByteBuffer byteData = ByteBuffer.wrap(byteStream.toByteArray());
        Document actualDocument = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Document expectedDocument = DataUtil.parseString("Test");
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write("Test".getBytes("UTF-8"));
        ByteBuffer byteData = ByteBuffer.wrap(byteStream.toByteArray());
        Document actualDocument = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        Document expectedDocument = DataUtil.parseString("Test");
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String actualCharset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", actualCharset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        String actualCharset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(actualCharset);
    }
}