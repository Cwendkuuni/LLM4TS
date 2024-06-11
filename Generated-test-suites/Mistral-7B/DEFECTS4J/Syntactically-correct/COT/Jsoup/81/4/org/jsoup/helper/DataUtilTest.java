package org.jsoup.helper; // adjust the package name if required

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

public class DataUtilTest {

    private static final String TEST_FILE = "testFile.html";
    private static final String TEST_CHARSET = "UTF-8";
    private static final String TEST_BASE_URI = "http://example.com/";

    private Document loadedFileDocument;
    private Document inputStreamDocument;
    private Document parserDocument;
    private Document emptyInputStreamDocument;
    private Document invalidCharsetInputStreamDocument;
    private Document noCharsetInputStreamDocument;
    private Document invalidParserDocument;
    private Document nullInputDocument;
    private Document nullParserDocument;

    @Before
    public void setUp() throws Exception {
        File testFile = new File(TEST_FILE);
        loadedFileDocument = DataUtil.load(testFile, TEST_CHARSET, TEST_BASE_URI);

        InputStream testFileInputStream = new FileInputStream(testFile);
        inputStreamDocument = DataUtil.load(testFileInputStream, TEST_CHARSET, TEST_BASE_URI);

        InputStream testFileInputStreamWithInvalidCharset = new FileInputStream(testFile);
        inputStreamDocument = DataUtil.load(testFileInputStreamWithInvalidCharset, "InvalidCharset", TEST_BASE_URI);

        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        emptyInputStreamDocument = DataUtil.load(emptyInputStream, TEST_CHARSET, TEST_BASE_URI);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(new FileInputStream(testFile), byteArrayOutputStream);
        InputStream invalidCharsetInputStream = new InputStreamReader(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), "InvalidCharset");
        noCharsetInputStreamDocument = DataUtil.load(invalidCharsetInputStream, null, TEST_BASE_URI);

        ByteArrayOutputStream parserByteArrayOutputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(new FileInputStream(testFile), parserByteArrayOutputStream);
        InputStream parserInputStream = new InputStreamReader(new ByteArrayInputStream(parserByteArrayOutputStream.toByteArray()), StandardCharsets.UTF_8);
        invalidParserDocument = DataUtil.load(parserInputStream, TEST_CHARSET, TEST_BASE_URI, null);

        nullInputDocument = DataUtil.load(null, TEST_CHARSET, TEST_BASE_URI);
        nullParserDocument = DataUtil.load(new FileInputStream(testFile), TEST_CHARSET, TEST_BASE_URI, null);
    }

    @Test
    public void testLoadFile() {
        // Basic functionality
        Assert.assertThat(loadedFileDocument, is(notNullValue()));

        // Edge cases and exceptions
        Assert.assertThat(emptyInputStreamDocument, is(notNullValue()));
        Assert.assertThat(noCharsetInputStreamDocument, is(notNullValue()));
        Assert.assertThat(invalidParserDocument, is(nullValue()));
        Assert.assertThat(nullInputDocument, is(nullValue()));
        Assert.assertThat(nullParserDocument, is(nullValue()));
    }

    @Test
    public void testLoadInputStream() {
        // Basic functionality
        Assert.assertThat(inputStreamDocument, is(notNullValue()));
        Assert.assertThat(inputStreamDocument, is(equalTo(loadedFileDocument)));

        // Edge cases and exceptions
        Assert.assertThat(invalidCharsetInputStreamDocument, is(notNullValue()));
        Assert.assertThat(invalidCharsetInputStreamDocument, is(not(equalTo(loadedFileDocument))));
        Assert.assertThat(emptyInputStreamDocument, is(notNullValue()));
        Assert.assertThat(emptyInputStreamDocument, is(not(equalTo(loadedFileDocument))));
        Assert.assertThat(noCharsetInputStreamDocument, is(notNullValue()));
        Assert.assertThat(noCharsetInputStreamDocument, is(not(equalTo(loadedFileDocument))));
        Assert.assertThat(nullInputDocument, is(nullValue()));
        Assert.assertThat(nullParserDocument, is(nullValue()));
    }

    @Test(expected = IOException.class)
    public void testIOExceptionWhenParsingInputStream() throws IOException {
        InputStream testFileInputStreamWithInvalidCharset = new FileInputStream(new File(TEST_FILE));
        DataUtil.load(testFileInputStreamWithInvalidCharset, "InvalidCharset", TEST_BASE_URI);
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerExceptionWhenLoadingNullParser() throws IOException {
        DataUtil.load(new FileInputStream(new File(TEST_FILE)), TEST_CHARSET, TEST_BASE_URI, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerExceptionWhenLoadingNullInput() throws IOException {
        DataUtil.load(null, TEST_CHARSET, TEST_BASE_URI);
    }
}