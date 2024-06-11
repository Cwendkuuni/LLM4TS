package org.jsoup.helper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;
import static org.jsoup.helper.DataUtil.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    private static final String TEST_FILE = "test.html";
    private static final String TEST_FILE_UTF_16 = "test_utf16.html";
    private static final String TEST_FILE_UTF_32 = "test_utf32.html";
    private static byte[] testFileContent;
    private static byte[] testFileUtf16Content;
    private static byte[] testFileUtf32Content;

    @BeforeClass
    public static void setUp() throws IOException {
        String testFileContentStr = "Test file content.";
        testFileContent = testFileContentStr.getBytes("UTF-8");
        testFileUtf16Content = testFileContentStr.getBytes("UTF-16LE");
        testFileUtf32Content = testFileContentStr.getBytes("UTF-32BE");

        File testFile = new File(TEST_FILE);
        File testFileUtf16 = new File(TEST_FILE_UTF_16);
        File testFileUtf32 = new File(TEST_FILE_UTF_32);

        try (OutputStream output = new FileOutputStream(testFile)) {
            output.write(testFileContent);
        }

        try (OutputStream outputUtf16 = new FileOutputStream(testFileUtf16)) {
            outputUtf16.write(testFileUtf16Content, 0, testFileUtf16Content.length);
        }

        try (OutputStream outputUtf32 = new FileOutputStream(testFileUtf32)) {
            outputUtf32.write(testFileUtf32Content, 0, testFileUtf32Content.length);
        }
    }

    @Test
    public void testLoadFile() throws IOException {
        Document doc = load(new File("test.html"), null, null);
        assertEquals("Test file content.", doc.text());
    }

    @Test
    public void testLoadFileWithCharset() throws IOException {
        Document doc = load(new File("test.html"), "UTF-8", null);
        assertEquals("Test file content.", doc.text());
    }

    @Test
    public void testLoadFileWithBaseUri() throws IOException {
        Document doc = load(new File("test.html"), null, "http://example.com");
        String baseUri = "http://example.com/test";
        assertEquals(baseUri + "/test", doc.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        ByteArrayOutputStream testFileOutput = new ByteArrayOutputStream();
        testFileOutput.write(testFileContent);
        ByteArrayInputStream testFileInput = new ByteArrayInputStream(testFileOutput.toByteArray());

        Document doc = load(testFileInput, null, null);
        assertEquals("Test file content.", doc.text());
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        ByteArrayOutputStream testFileOutput = new ByteArrayOutputStream();
        testFileOutput.write(testFileContent);
        ByteArrayInputStream testFileInput = new ByteArrayInputStream(testFileOutput.toByteArray());

        Document doc = load(testFileInput, "UTF-8", null);
        assertEquals("Test file content.", doc.text());
    }

    @Test
    public void testLoadInputStreamWithBaseUri() throws IOException {
        ByteArrayOutputStream testFileOutput = new ByteArrayOutputStream();
        testFileOutput.write(testFileContent);
        ByteArrayInputStream testFileInput = new ByteArrayInputStream(testFileOutput.toByteArray());

        Document doc = load(testFileInput, null, "http://example.com");
        String baseUri = "http://example.com/test";
        assertEquals(baseUri + "/test", doc.baseUri());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        ByteArrayOutputStream testFileOutput = new ByteArrayOutputStream();
        testFileOutput.write(testFileContent);
        ByteArrayInputStream testFileInput = new ByteArrayInputStream(testFileOutput.toByteArray());

        Document doc = load(testFileInput, null, null, Parser.xmlParser());
        assertTrue(doc.xml() != null);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File testFile = new File("test.html");
        File testFileOutput = new File("test_output.html");

        try (InputStream input = new FileInputStream(testFile);
             OutputStream output = new FileOutputStream(testFileOutput)) {
            crossStreams(input, output);
        }

        byte[] testFileOutputContent = new byte[testFileOutput.length()];
        try (FileInputStream testFileInput = new FileInputStream(testFileOutput)) {
            testFileInput.read(testFileOutputContent);
        }

        Document doc = load(new ByteArrayInputStream(testFileOutputContent), null, null);
        assertEquals("Test file content.", doc.text());
    }

    @Test
    public void testParseByteData() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(testFileContent);
        Document doc = parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertEquals("Test file content.", doc.text());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(testFileUtf16Content);
        Document doc = parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("Test file content.", doc.text());
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithInvalidCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(testFileContent);
        Document doc = parseByteData(byteData, "invalid_charset", null, Parser.htmlParser());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteBuffer byteData = readToByteBuffer(new FileInputStream("test.html"));
        ByteBuffer expectedByteData = ByteBuffer.wrap(testFileContent);
        assertEquals(expectedByteData, byteData);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        ByteBuffer byteData = readToByteBuffer(new FileInputStream("test.html"), 1024);
        ByteBuffer expectedByteData = ByteBuffer.wrap(testFileContent.subArray(0, 1024));
        assertEquals(expectedByteData, byteData);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        ByteBuffer byteData = readFileToByteBuffer(new File("test.html"));
        ByteBuffer expectedByteData = ByteBuffer.wrap(testFileContent);
        assertEquals(expectedByteData, byteData);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteData = emptyByteBuffer();
        assertEquals(0, byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        assertEquals(expectedCharset, getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=invalid_charset";
        assertNull(getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals(charset, validateCharset(charset));

        String invalidCharset = "invalid_charset";
        assertNull(validateCharset(invalidCharset));
    }

    @Test
    public void testMimeBoundary() {
        String expectedMimeBoundary = "----_-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890";
        String mimeBoundary = mimeBoundary();
        assertEquals(expectedMimeBoundary, mimeBoundary);
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{0xFF, 0xFE, 0x00, 0x00});
        String charsetName = detectCharsetFromBom(byteData, null);
        assertEquals("UTF-16LE", charsetName);

        byteData = ByteBuffer.wrap(new byte[]{0x00, 0xFF, 0xFE, 0x00});
        charsetName = detectCharsetFromBom(byteData, null);
        assertEquals("UTF-16BE", charsetName);

        byteData = ByteBuffer.wrap(new byte[]{0xEF, 0xBB, 0xBF, 0x00});
        charsetName = detectCharsetFromBom(byteData, null);
        assertEquals("UTF-8", charsetName);

        byteData = ByteBuffer.wrap(new byte[]{0xFE, 0xFF, 0x00, 0x00});
        charsetName = detectCharsetFromBom(byteData, null);
        assertEquals("UTF-32BE", charsetName);

        byteData = ByteBuffer.wrap(new byte[]{0x00, 0xFE, 0xFF, 0x00});
        charsetName = detectCharsetFromBom(byteData, null);
        assertEquals("UTF-32LE", charsetName);
    }
}