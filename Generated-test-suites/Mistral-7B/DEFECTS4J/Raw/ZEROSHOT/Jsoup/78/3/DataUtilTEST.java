package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DataUtil.class)
public class DataUtilTest {

    private static Document document;
    private static File inputFile;

    @BeforeClass
    public static void setup() throws IOException {
        MockitoAnnotations.initMocks(DataUtilTest.class);
        inputFile = new File("test.html");
        FileOutputStream outputStream = new FileOutputStream(inputFile);
        outputStream.write("<?xml version='1.0' encoding='UTF-8'?>\n".getBytes());
        outputStream.write("<html><head><meta charset='UTF-16'/></head><body><h1>Test</h1></body></html>".getBytes());
        outputStream.close();
    }

    @Test
    public void testLoadFileWithUtf8Charset() throws IOException {
        Document doc = DataUtil.load(inputFile, "UTF-8", null);
        assertThat(doc.title(), is("Test"));
    }

    @Test
    public void testLoadFileWithUtf16Charset() throws IOException {
        Document doc = DataUtil.load(inputFile, "UTF-16", null);
        assertThat(doc.title(), is("Test"));
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        Document doc = null;
        try {
            doc = DataUtil.load(inputFile, "invalidCharset", null);
        } catch (IOException e) {
            // expected
        }
        assertNull(doc);
    }

    @Test
    public void testLoadInputStreamWithUtf8Charset() throws IOException {
        InputStream inputStream = new FileInputStream(inputFile);
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertThat(doc.title(), is("Test"));
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithUtf16Charset() throws IOException {
        InputStream inputStream = new FileInputStream(inputFile);
        Document doc = DataUtil.load(inputStream, "UTF-16", null);
        assertThat(doc.title(), is("Test"));
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = new FileInputStream(inputFile);
        Document doc = null;
        try {
            doc = DataUtil.load(inputStream, "invalidCharset", null);
        } catch (IOException e) {
            // expected
        }
        assertNull(doc);
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        InputStream inputStream = new FileInputStream(inputFile);
        Document doc = DataUtil.load(inputStream, null, null);
        assertThat(doc.title(), is("Test"));
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithUtf8Charset() throws IOException {
        InputStream inputStream = new FileInputStream(inputFile);
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertThat(doc.title(), is("Test"));
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithUtf16Charset() throws IOException {
        InputStream inputStream = new FileInputStream(inputFile);
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-16", null, Parser.htmlParser());
        assertThat(doc.title(), is("Test"));
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = new FileInputStream(inputFile);
        Document doc = null;
        try {
            doc = DataUtil.parseInputStream(inputStream, "invalidCharset", null, Parser.htmlParser());
        } catch (IOException e) {
            // expected
        }
        assertNull(doc);
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithNullCharset() throws IOException {
        InputStream inputStream = new FileInputStream(inputFile);
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertThat(doc.title(), is("Test"));
        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream("output.txt");
        DataUtil.crossStreams(inputStream, outputStream);
        inputStream.close();
        outputStream.close();

        File outputFile = new File("output.txt");
        String expectedOutput = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='UTF-16'/></head><body><h1>Test</h1></body></html>";
        FileInputStream outputInputStream = new FileInputStream(outputFile);
        String output = Charset.forName("UTF-8").decode(outputInputStream).toString();
        assertThat(output, is(expectedOutput));
        outputInputStream.close();
        outputFile.delete();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream(inputFile);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        inputStream.close();
        byte[] expectedBytes = new byte[inputFile.length()];
        FileInputStream expectedInputStream = new FileInputStream(inputFile);
        expectedInputStream.read(expectedBytes);
        expectedInputStream.close();

        byte[] actualBytes = new byte[byteBuffer.limit()];
        byteBuffer.get(actualBytes);

        assertThat(actualBytes, is(expectedBytes));
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = new FileInputStream(inputFile);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        inputStream.close();

        byte[] expectedBytes = new byte[1024];
        FileInputStream expectedInputStream = new FileInputStream(inputFile);
        expectedInputStream.read(expectedBytes);
        expectedInputStream.close();

        byte[] actualBytes = new byte[byteBuffer.limit()];
        byteBuffer.get(actualBytes);

        assertThat(actualBytes, is(expectedBytes));
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(inputFile);
        byte[] expectedBytes = new byte[inputFile.length()];
        FileInputStream expectedInputStream = new FileInputStream(inputFile);
        expectedInputStream.read(expectedBytes);
        expectedInputStream.close();

        byte[] actualBytes = new byte[byteBuffer.limit()];
        byteBuffer.get(actualBytes);

        assertThat(actualBytes, is(expectedBytes));
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        byte[] actualBytes = new byte[byteBuffer.limit()];
        byteBuffer.get(actualBytes);
        assertThat(actualBytes, is(new byte[0]));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertThat(charset, is(expectedCharset));
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        String expectedCharset = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertThat(charset, is(expectedCharset));
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String expectedCharset = charset;

        String validatedCharset = DataUtil.validateCharset(charset);
        assertThat(validatedCharset, is(expectedCharset));
    }

    @Test
    public void testValidateCharsetWithInvalidCharset() {
        String charset = "invalidCharset";
        String expectedCharset = null;

        String validatedCharset = DataUtil.validateCharset(charset);
        assertThat(validatedCharset, is(expectedCharset));
    }

    @Test
    public void testMimeBoundary() {
        String expectedBoundary = "J1ff_W9tMzbhRm0tU1pJUlVUa01tSGdVeFd1NlRiRkJyRlNlT1dJ_Tm_TnRiUlRlS0NhT2lGdFJGc2lVa1pRVkZKd2lSQ0tJQlZJR0tJQlJT1ZJd1dUQ1BOR09fR0VfR1dU";
        String boundary = DataUtil.mimeBoundary();

        assertThat(boundary, is(expectedBoundary));
    }

    @Test
    public void testDetectCharsetFromBomWithUtf32Bom() throws IOException {
        InputStream inputStream = new FileInputStream(new File("test-utf32.txt"));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream, 10));
        inputStream.close();

        String expectedCharset = "UTF-32";
        String actualCharset = bomCharset == null ? null : bomCharset.charset;

        assertThat(actualCharset, is(expectedCharset));
    }

    @Test
    public void testDetectCharsetFromBomWithUtf16Bom() throws IOException {
        InputStream inputStream = new FileInputStream(new File("test-utf16.txt"));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream, 10));
        inputStream.close();

        String expectedCharset = "UTF-16";
        String actualCharset = bomCharset == null ? null : bomCharset.charset;

        assertThat(actualCharset, is(expectedCharset));
    }

    @Test
    public void testDetectCharsetFromBomWithUtf8Bom() throws IOException {
        InputStream inputStream = new FileInputStream(new File("test-utf8.txt"));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream, 10));
        inputStream.close();

        String expectedCharset = "UTF-8";
        String actualCharset = bomCharset == null ? null : bomCharset.charset;

        assertThat(actualCharset, is(expectedCharset));
    }

    @Test
    public void testDetectCharsetFromBomWithInvalidBom() throws IOException {
        InputStream inputStream = new FileInputStream(new File("test-invalid.txt"));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream, 10));
        inputStream.close();

        String expectedCharset = null;
        String actualCharset = bomCharset == null ? null : bomCharset.charset;

        assertThat(actualCharset, is(expectedCharset));
    }
}

class BomCharset {
    private final String charset;
    private final int offset;

    public BomCharset(String charset, int offset) {
        this.charset = charset;
        this.offset = offset;
    }
}