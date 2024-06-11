package org.jsoup.helper;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import static org.jsoup.helper.DataUtil.defaultCharset;
import static org.jsoup.helper.DataUtil.detectCharsetFromBom;
import static org.jsoup.helper.DataUtil.emptyByteBuffer;
import static org.jsoup.helper.DataUtil.firstReadBufferSize;
import static org.jsoup.helper.DataUtil.getCharsetFromContentType;
import static org.jsoup.helper.DataUtil.load;
import static org.jsoup.helper.DataUtil.mimeBoundary;
import static org.jsoup.helper.DataUtil.parseInputStream;
import static org.jsoup.helper.DataUtil.validateCharset;

public class DataUtilTest {

    private static final String TEST_FILE_PATH = "test.html";
    private static final String TEST_FILE_CONTENT = "Test file content.";
    private static final String TEST_CHARSET = "UTF-16";

    @BeforeClass
    public static void setup() {
        // Ensure the test file exists and has the specified charset
        File testFile = new File(TEST_FILE_PATH);
        try {
            if (!testFile.exists()) {
                testFile.createNewFile();
            }
            writeTestFile(testFile, TEST_CHARSET, TEST_FILE_CONTENT);
            Assert.assertEquals(TEST_CHARSET, getCharsetFromContentType(TEST_FILE_CONTENT));
        } catch (IOException e) {
            throw new RuntimeException("Failed to prepare test file", e);
        }
    }

    @Test
    public void testLoadFile() throws IOException {
        Document doc = load(new File(TEST_FILE_PATH), defaultCharset, "");
        Assert.assertNotNull(doc);
        Assert.assertEquals(TEST_FILE_CONTENT, doc.body().text());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "Test input stream content.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(defaultCharset));
        Document doc = load(inputStream, defaultCharset, "");
        Assert.assertNotNull(doc);
        Assert.assertEquals(inputStreamContent, doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithCustomCharset() throws IOException {
        String inputStreamContent = "Test input stream content.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(TEST_CHARSET));
        Document doc = load(inputStream, TEST_CHARSET, "");
        Assert.assertNotNull(doc);
        Assert.assertEquals(inputStreamContent, doc.body().text());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String inputStreamContent = "Test input stream content.";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(defaultCharset));
        Document doc = parseInputStream(inputStream, null, "", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(inputStreamContent, doc.body().text());
    }

    @Test
    public void testParseInputStreamWithCharset() throws IOException {
        String inputStreamContent = "Test input stream content.";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(TEST_CHARSET));
        Document doc = parseInputStream(inputStream, TEST_CHARSET, "", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(inputStreamContent, doc.body().text());
    }

    @Test
    public void testParseInputStreamWithCharsetAndParser() throws IOException {
        String inputStreamContent = "Test input stream content.";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(defaultCharset));
        Document doc = parseInputStream(inputStream, defaultCharset, "", Parser.xmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals(inputStreamContent, doc.body().text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputStreamContent = "Test input stream content.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(defaultCharset));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        String outputStreamContent = outputStream.toString(defaultCharset);
        Assert.assertEquals(inputStreamContent, outputStreamContent);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "Test input stream content.";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(defaultCharset));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
        byte[] expectedBytes = inputStreamContent.getBytes(defaultCharset);
        byte[] actualBytes = byteBuffer.array();
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertTrue(byteBuffer.capacity() == 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadToByteBufferWithNegativeMaxSize() {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes(defaultCharset));
        DataUtil.readToByteBuffer(inputStream, -1);
    }

    @Test
    public void testParseInputStreamWithBOM() throws IOException {
        String bomContent = "\uFEFFTest input stream content.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bomContent.getBytes(defaultCharset));
        Document doc = parseInputStream(inputStream, null, "", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(bomContent.substring(1), doc.body().text());
        Assert.assertEquals(defaultCharset, detectCharsetFromBom(inputStream).charset);
    }

    @Test
    public void testParseInputStreamWithInvalidBOM() throws IOException {
        String bomContent = "Test input stream content.\u00EF\u00BB\u00BF"; // invalid UTF-16 BOM
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bomContent.getBytes(defaultCharset));
        Assert.assertNull(detectCharsetFromBom(inputStream.getBuffer()));
        Document doc = parseInputStream(inputStream, null, "", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(bomContent, doc.body().text());
    }

    @Test
    public void testParseInputStreamWithUtf8BOM() throws IOException {
        String bomContent = "\uEFFFETest input stream content."; // invalid UTF-8 BOM
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bomContent.getBytes(defaultCharset));
        Assert.assertNull(detectCharsetFromBom(inputStream.getBuffer()));
        Document doc = parseInputStream(inputStream, null, "", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(bomContent.substring(1), doc.body().text());
        Assert.assertEquals("UTF-8", validateCharset(detectCharsetFromBom(inputStream.getBuffer()).charset));
    }

    @Test
    public void testValidateCharset() {
        Assert.assertEquals("UTF-8", validateCharset(null));
        Assert.assertEquals("UTF-8", validateCharset("UTF-8"));
        Assert.assertEquals("UTF-8", validateCharset("UTF-8".toUpperCase(Locale.ENGLISH)));
        Assert.assertEquals("UTF-8", validateCharset("UTF-8".trim()));
        Assert.assertEquals(defaultCharset, validateCharset("UTF-8".toUpperCase(Locale.ENGLISH).replace("UTF-8", defaultCharset)));
        Assert.assertEquals(defaultCharset, validateCharset("UTF-8".trim().replace("UTF-8", defaultCharset)));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = mimeBoundary();
        Assert.assertEquals(boundary.length(), boundaryLength);
        for (int i = 0; i < boundaryLength; i++) {
            Assert.assertTrue(Character.isDefinedChar(boundary.charAt(i)));
        }
    }

    private void writeTestFile(File file, String charset, String content) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(file)) {
            byte[] contentBytes = content.getBytes(charset);
            outputStream.write(contentBytes);
            outputStream.flush();
        }
    }
}