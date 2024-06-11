package org.jsoup.helper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jsoup.helper.DataUtil.charsetPattern;
import static org.jsoup.helper.DataUtil.defaultCharset;
import static org.jsoup.helper.DataUtil.firstReadBufferSize;
import static org.jsoup.helper.DataUtil.bufferSize;
import static org.jsoup.helper.DataUtil.mimeBoundaryChars;
import static org.jsoup.helper.DataUtil.load;
import static org.jsoup.helper.DataUtil.parseInputStream;
import static org.jsoup.helper.DataUtil.crossStreams;
import static org.jsoup.helper.DataUtil.parseInputStream;
import static org.jsoup.helper.DataUtil.readToByteBuffer;
import static org.jsoup.helper.DataUtil.readFileToByteBuffer;
import static org.jsoup.helper.DataUtil.emptyByteBuffer;
import static org.jsoup.helper.DataUtil.getCharsetFromContentType;
import static org.jsoup.helper.DataUtil.validateCharset;
import static org.jsoup.helper.DataUtil.mimeBoundary;
import static org.jsoup.helper.DataUtil.detectCharsetFromBom;

@RunWith(JUnit4.class)
public class DataUtilTest {

    private static final String TEST_FILE = "test.html";
    private static final String TEST_UTF8_FILE = "test_utf8.html";
    private static final String TEST_GB2312_FILE = "test_gb2312.html";
    private static final String TEST_UTF16LE_FILE = "test_utf16le.hex";
    private static final Charset UTF8 = StandardCharsets.UTF_8;
    private static final String TEST_UTF32_BOM = "\u0000\u0000\u00FE\u00FF";
    private static final String TEST_UTF16BE_BOM = "\u00FE\u00FF\u0000\u0000";

    @BeforeClass
    public static void setup() {
        // Set up files for testing
        new File(TEST_FILE).getParentFile().mkdirs();
        createTestFile(TEST_FILE, "Test file content.");
        new File(TEST_UTF8_FILE).getParentFile().mkdirs();
        createTestFile(TEST_UTF8_FILE, "Test UTF-8 file content.\u00A0\u00A1\uD800\uDC00\uD800\uDFFF");
        new File(TEST_GB2312_FILE).getParentFile().mkdirs();
        createTestFile(TEST_GB2312_FILE, "Test GB2312 file content.\u5B8C\u5F8B\u5B8C\u5F8B");
        new File(TEST_UTF16LE_FILE).getParentFile().mkdirs();
        createTestFile(TEST_UTF16LE_FILE, "Test UTF-16 LE file content.\uD840\uDC00\uD841\uDC01");
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        Document doc = load(new File(TEST_FILE), null, "");
        assertThat(doc.title(), is("Test file content."));
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        Document doc = load(new File(TEST_FILE), "UTF-8", "");
        assertThat(doc.title(), is("Test file content."));
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        File file = new File(TEST_FILE);
        InputStream input = new FileInputStream(file);
        Document doc = load(input, null, "");
        assertThat(doc.title(), is("Test file content."));
        input.close();
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        File file = new File(TEST_UTF8_FILE);
        InputStream input = new FileInputStream(file);
        Document doc = load(input, "UTF-8", "");
        assertThat(doc.title(), is("Test UTF-8 file content."));
        input.close();
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        File file = new File(TEST_UTF8_FILE);
        InputStream input = new FileInputStream(file);
        try {
            Document doc = load(input, "INVALID", "");
            // The test should fail here
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Invalid charset: INVALID"));
        } finally {
            input.close();
        }
    }

    @Test
    public void testLoadInputStreamWithBOM() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(TEST_UTF8_BOM.getBytes(UTF8));
        Document doc = load(input, null, "");
        assertThat(doc.title(), is("Test UTF-8 file content."));
        input.close();
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        InputStream input = new FileInputStream(new File(TEST_FILE));
        Document doc = parseInputStream(input, null, "", Parser.htmlParser());
        assertThat(doc.title(), is("Test file content."));
        input.close();
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        InputStream input = new FileInputStream(new File(TEST_UTF8_FILE));
        Document doc = parseInputStream(input, "UTF-8", "", Parser.htmlParser());
        assertThat(doc.title(), is("Test UTF-8 file content."));
        input.close();
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        InputStream input = new FileInputStream(new File(TEST_UTF8_FILE));
        try {
            Document doc = parseInputStream(input, "INVALID", "", Parser.htmlParser());
            // The test should fail here
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Invalid charset: INVALID"));
        } finally {
            input.close();
        }
    }

    @Test
    public void testParseInputStreamWithBOM() throws IOException {
        InputStream input = new ByteArrayInputStream(TEST_UTF8_BOM.getBytes(UTF8));
        Document doc = parseInputStream(input, null, "", Parser.htmlParser());
        assertThat(doc.title(), is("Test UTF-8 file content."));
        input.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream input = new FileInputStream(new File(TEST_FILE));
        OutputStream output = new ByteArrayOutputStream();
        crossStreams(input, output);
        input.close();
        String result = output.toString(UTF8.name());
        assertThat(result, is("Test file content."));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream input = new FileInputStream(new File(TEST_FILE));
        ByteBuffer byteBuffer = readToByteBuffer(input);
        input.close();
        byte[] expected = readFileToByteArray(TEST_FILE);
        byteBuffer.flip();
        byte[] actual = new byte[byteBuffer.remaining()];
        byteBuffer.get(actual);
        assertThat(expected, is(actual));
    }

    @Test
    public void testReadToByteBufferWithEmptyInput() throws IOException {
        ByteBuffer byteBuffer = readToByteBuffer(null);
        assertThat(byteBuffer, is(emptyByteBuffer()));
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream input = new FileInputStream(new File(TEST_FILE));
        ByteBuffer byteBuffer = readToByteBuffer(input, -1);
        input.close();
        byte[] expected = readFileToByteArray(TEST_FILE);
        byteBuffer.flip();
        byte[] actual = new byte[byteBuffer.remaining()];
        byteBuffer.get(actual);
        assertThat(expected, is(actual));
    }

    @Test
    public void testValidateCharset() {
        assertThat(validateCharset(null), is(nullValue()));
        assertThat(validateCharset("UTF-8"), is("UTF-8"));
        assertThat(validateCharset("UTF-8"), is(validateCharset("UTF-8").trim().replaceAll("[\"']", "")));
        assertThat(validateCharset("UTF-8"), is(validateCharset("UTF-8").toUpperCase(Locale.ENGLISH)));
        assertThat(validateCharset("UTF-8"), is(validateCharset("utf-8").trim().replaceAll("[\"']", "")));
        assertThat(validateCharset("UTF-8"), is(validateCharset("utf-8").toUpperCase(Locale.ENGLISH)));
        assertThat(validateCharset("UTF-16"), is("UTF-16"));
        assertThat(validateCharset("UTF-32"), is("UTF-32"));
        assertThat(validateCharset("INVALID"), is(nullValue()));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = mimeBoundary();
        assertThat(boundary, is(notNullValue()));
        assertThat(boundary.length(), is(boundaryLength));
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer utf32Bom = ByteBuffer.wrap(TEST_UTF32_BOM.getBytes(UTF8));
        BomCharset utf32Charset = detectCharsetFromBom(utf32Bom);
        assertThat(utf32Charset, is(notNullValue()));
        assertThat(utf32Charset.charset, is("UTF-32"));
        assertThat(utf32Charset.offset, is(false));

        ByteBuffer utf16LEBom = ByteBuffer.wrap(TEST_UTF16LE_BOM.getBytes(UTF8));
        BomCharset utf16LECharset = detectCharsetFromBom(utf16LEBom);
        assertThat(utf16LECharset, is(notNullValue()));
        assertThat(utf16LECharset.charset, is("UTF-16"));
        assertThat(utf16LECharset.offset, is(true));

        ByteBuffer nullBom = ByteBuffer.wrap(new byte[0]);
        BomCharset nullBomCharset = detectCharsetFromBom(nullBom);
        assertThat(nullBomCharset, is(nullValue()));
    }

    // Helper methods for creating test files
    private static void createTestFile(String fileName, String content) throws IOException {
        File file = new File(fileName);
        try (OutputStream output = new FileOutputStream(file)) {
            output.write(content.getBytes(UTF8));
        }
    }

    private static byte[] readFileToByteArray(String fileName) throws IOException {
        File file = new File(fileName);
        try (InputStream input = new FileInputStream(file)) {
            byte[] data = new byte[input.available()];
            input.read(data);
            return data;
        }
    }

    private static final String TEST_UTF8_BOM = "\u00EF\u00BB\u00BF";
}