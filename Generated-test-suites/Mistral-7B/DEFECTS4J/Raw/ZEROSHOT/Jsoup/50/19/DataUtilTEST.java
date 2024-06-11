package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataUtilTest {

    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final String BASE_URI = "http://example.com";
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final int BUFFER_SIZE = 0x20000;
    private static final int UNICODE_BOM = 0xFEFF;
    private static final String MIME_BOUNDARY = "---------------------------1234567890";

    private InputStream inputStream;
    private OutputStream outputStream;
    private File testFile;
    private Document document;

    @Before
    public void setUp() throws IOException {
        inputStream = mock(InputStream.class);
        outputStream = mock(OutputStream.class);
        testFile = new File("testFile.html");

        document = Mockito.mock(Document.class);
        when(document.outputSettings().charset()).thenReturn(UTF_8);
        when(document.select("meta[http-equiv]")).thenReturn(Mockito.mock(Element.class));
    }

    @After
    public void tearDown() throws Exception {
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testLoadFromFile() throws IOException {
        byte[] expectedData = {"<html><head></head><body></body></html>".getBytes(UTF_8)};
        when(document.select("meta[http-equiv]")).thenReturn(null);

        File inputFile = new File("testFile.html");
        FileUtils.writeByteArrayToFile(inputFile, expectedData);

        document = DataUtil.load(inputFile, DEFAULT_CHARSET, BASE_URI);

        byte[] actualData = document.body().html().getBytes(UTF_8);
        byte[] expectedHtmlData = {"<html><head></head><body></body></html>".getBytes(UTF_8)};
        byte[] expectedBodyData = {"<body></body>".getBytes(UTF_8)};

        assertThat(document.html(), is(new String(expectedHtmlData, UTF_8)));
        assertThat(document.body().html(), is(new String(expectedBodyData, UTF_8)));

        // Assert that the file was not modified
        byte[] actualFileData = FileUtils.readFileToByteArray(inputFile);
        assertThat(actualFileData, is(expectedData));
    }

    @Test
    public void testLoadFromInputStreamWithDefaultCharset() throws IOException {
        byte[] expectedData = {"<html><head></head><body></body></html>".getBytes(UTF_8)};

        InputStream inputStream = new ByteArrayInputStream(expectedData);

        document = DataUtil.load(inputStream, null, BASE_URI);

        byte[] actualData = document.body().html().getBytes(UTF_8);
        byte[] expectedHtmlData = {"<html><head></head><body></body></html>".getBytes(UTF_8)};
        byte[] expectedBodyData = {"<body></body>".getBytes(UTF_8)};

        assertThat(document.html(), is(new String(expectedHtmlData, UTF_8)));
        assertThat(document.body().html(), is(new String(expectedBodyData, UTF_8)));
    }

    @Test
    public void testLoadFromInputStreamWithSpecifiedCharset() throws IOException {
        byte[] expectedData = {"<html><head></head><body></body></html>".getBytes(UTF_8)};
        String charsetName = "UTF-16";

        InputStream inputStream = new ByteArrayInputStream(expectedData);

        document = DataUtil.load(inputStream, charsetName, BASE_URI);

        byte[] actualData = document.body().html().getBytes(Charset.forName(charsetName));
        byte[] expectedHtmlData = {"<html><head></head><body></body></html>".getBytes(UTF_8)};
        byte[] expectedBodyData = {"<body></body>".getBytes(UTF_8)};

        assertThat(document.html(), is(new String(expectedHtmlData, UTF_8)));
        assertThat(document.body().html(), is(new String(expectedBodyData, UTF_8)));

        // Assert that the input stream was not modified
        byte[] actualInputStreamData = new byte[inputStream.available()];
        inputStream.read(actualInputStreamData);
        assertThat(actualInputStreamData, is(expectedData));
    }

    @Test
    public void testLoadFromInputStreamWithSpecifiedParser() throws IOException {
        byte[] expectedData = {"<html><head></head><body></body></html>".getBytes(UTF_8)};
        String charsetName = "UTF-16";
        Parser parser = Parser.xmlParser();

        InputStream inputStream = new ByteArrayInputStream(expectedData);

        document = DataUtil.load(inputStream, charsetName, BASE_URI, parser);

        byte[] actualData = document.body().html().getBytes(Charset.forName(charsetName));
        byte[] expectedHtmlData = {"<html><head></head><body></body></html>".getBytes(UTF_8)};
        byte[] expectedBodyData = {"<body></body>".getBytes(UTF_8)};

        assertThat(document.html(), is(new String(expectedHtmlData, UTF_8)));
        assertThat(document.body().html(), is(new String(expectedBodyData, UTF_8)));

        // Assert that the input stream was not modified
        byte[] actualInputStreamData = new byte[inputStream.available()];
        inputStream.read(actualInputStreamData);
        assertThat(actualInputStreamData, is(expectedData));
    }

    @Test
    public void testCrossStreams() throws IOException {
        final byte[] inputData = {"<html><head></head><body></body></html>".getBytes(UTF_8)};
        final byte[] outputData = new byte[inputData.length];

        DataUtil.crossStreams(new ByteArrayInputStream(inputData), new ByteArrayOutputStream(outputData));

        byte[] actualData = new byte[outputData.length];
        System.arraycopy(outputData, 0, actualData, 0, outputData.length);

        assertThat(actualData, is(inputData));
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithInvalidCharsetName() throws IOException {
        byte[] expectedData = {"<html><head></head><body></body></html>".getBytes(UTF_8)};
        String charsetName = "INVALID_CHARSET";

        ByteBuffer byteData = ByteBuffer.wrap(expectedData);

        DataUtil.parseByteData(byteData, charsetName, BASE_URI, Parser.htmlParser());
    }

    @Test
    public void testParseByteDataWithValidCharsetName() throws IOException {
        byte[] expectedData = {"<html><head></head><body></body></html>".getBytes(UTF_8)};
        String charsetName = UTF_8.name();

        ByteBuffer byteData = ByteBuffer.wrap(expectedData);

        document = DataUtil.parseByteData(byteData, charsetName, BASE_URI, Parser.htmlParser());

        byte[] actualData = document.body().html().getBytes(UTF_8);
        byte[] expectedHtmlData = {"<html><head></head><body></body></html>".getBytes(UTF_8)};
        byte[] expectedBodyData = {"<body></body>".getBytes(UTF_8)};

        assertThat(document.html(), is(new String(expectedHtmlData, UTF_8)));
        assertThat(document.body().html(), is(new String(expectedBodyData, UTF_8)));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] expectedData = {"<html><head></head><body></body></html>".getBytes(UTF_8)};

        InputStream inputStream = new ByteArrayInputStream(expectedData);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        byte[] actualData = new byte[byteData.limit()];
        byteData.get(actualData);

        assertThat(actualData, is(expectedData));
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        byte[] expectedData = {"<html><head></head><body></body></html>".getBytes(UTF_8)};

        File inputFile = new File("testFile.html");
        FileUtils.writeByteArrayToFile(inputFile, expectedData);

        ByteBuffer byteData = DataUtil.readFileToByteBuffer(inputFile);

        byte[] actualData = new byte[byteData.limit()];
        byteData.get(actualData);

        assertThat(actualData, is(expectedData));

        // Assert that the file was not modified
        byte[] actualFileData = FileUtils.readFileToByteArray(inputFile);
        assertThat(actualFileData, is(expectedData));
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();

        byte[] actualData = new byte[byteData.limit()];
        byteData.get(actualData);

        assertThat(actualData, is(new byte[0]));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertThat(charset, is("UTF-8"));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        assertThat(boundary.length(), is(32));
        for (int i = 0; i < boundary.length(); i++) {
            char c = boundary.charAt(i);
            assertThat(mimeBoundaryChars, contains(c));
        }
    }
}