package org.jsoup.helper.test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class DataUtilTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private Document document;
    private InputStream inputStream;
    private File file;

    @Before
    public void setUp() throws IOException {
        inputStream = new ByteArrayInputStream("<html><body></body></html>".getBytes("UTF-8"));
        file = new File("test.html");
        FileUtils.writeStringToFile(file, "<html><body></body></html>", "UTF-8");
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        Document loadedDocument = DataUtil.load(file, null, "http://example.com");
        assertThat(loadedDocument, is(notNullValue()));
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        Document loadedDocument = DataUtil.load(file, "UTF-16", "http://example.com");
        assertThat(loadedDocument, is(notNullValue()));
    }

    @Test
    public void testLoadStreamWithDefaultCharset() throws IOException {
        Document loadedDocument = DataUtil.load(inputStream, null, "http://example.com");
        assertThat(loadedDocument, is(notNullValue()));
    }

    @Test
    public void testLoadStreamWithSpecifiedCharset() throws IOException {
        Document loadedDocument = DataUtil.load(inputStream, "UTF-16", "http://example.com");
        assertThat(loadedDocument, is(notNullValue()));
    }

    @Test
    public void testLoadStreamWithParser() throws IOException {
        Document loadedDocument = DataUtil.load(inputStream, null, "http://example.com", DataUtil.Parser.xmlParser());
        assertThat(loadedDocument, is(notNullValue()));
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byteData.put("UTF-8".getBytes()); // BOM
        byteData.put("<html><body></body></html>".getBytes("UTF-8")); // Data
        byteData.flip();

        Document loadedDocument = DataUtil.parseByteData(byteData, null, "http://example.com", DataUtil.Parser.htmlParser());
        assertThat(loadedDocument, is(notNullValue()));
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byteData.put("UTF-16".getBytes()); // BOM
        byteData.put("<html><body></body></html>".getBytes("UTF-16")); // Data
        byteData.flip();

        Document loadedDocument = DataUtil.parseByteData(byteData, "UTF-16", "http://example.com", DataUtil.Parser.htmlParser());
        assertThat(loadedDocument, is(notNullValue()));
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullBaseUri() throws IOException {
        Document loadedDocument = DataUtil.load(file, null, null);
        expectedEx.expect(IOException.class);
    }

    @Test(expected = IOException.class)
    public void testLoadStreamWithNullBaseUri() throws IOException {
        Document loadedDocument = DataUtil.load(inputStream, null, null);
        expectedEx.expect(IOException.class);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new ByteArrayInputStream("Hello World!".getBytes());
        OutputStream out = new ByteArrayOutputStream();

        DataUtil.crossStreams(in, out);

        String result = out.toString();
        assertThat(result, is("Hello World!"));
    }
}