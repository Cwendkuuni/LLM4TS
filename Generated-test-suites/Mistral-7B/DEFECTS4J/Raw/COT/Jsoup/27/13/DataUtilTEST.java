package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    private Parser parser;
    private Document document;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document expected = parser.parse("test.html");

        Document actual = DataUtil.load(file, charsetName, baseUri);

        assertEquals(expected.body(), actual.body());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String charsetName = null;
        String baseUri = "http://test.com";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("Test content".getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document actual = DataUtil.load(inputStream, charsetName, baseUri);

        assertEquals("Test content", actual.body().text());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String charsetName = "UTF-16";
        String baseUri = "http://test.com";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("Test content".getBytes("UTF-16"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document actual = DataUtil.load(inputStream, charsetName, baseUri);

        assertEquals("Test content", actual.body().text());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String charsetName = null;
        String baseUri = "http://test.com";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("Test content".getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document expected = parser.parse("Test content");

        Document actual = DataUtil.parseByteData(ByteBuffer.wrap(inputStream.readAllBytes()), charsetName, baseUri, parser);

        assertEquals(expected.body(), actual.body());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String charsetName = "UTF-16";
        String baseUri = "http://test.com";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("Test content".getBytes("UTF-16"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document expected = parser.parse("Test content");

        Document actual = DataUtil.parseByteData(ByteBuffer.wrap(inputStream.readAllBytes()), charsetName, baseUri, parser);

        assertEquals(expected.body(), actual.body());
    }

    @Test
    public void testLoadInputStreamWithEmptyContent() throws IOException {
        String charsetName = null;
        String baseUri = "http://test.com";

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        Document actual = DataUtil.load(inputStream, charsetName, baseUri);

        assertNull(actual);
    }

    @Test
    public void testParseByteDataWithEmptyContent() throws IOException {
        String charsetName = null;
        String baseUri = "http://test.com";

        ByteBuffer byteData = ByteBuffer.allocate(0);

        Document actual = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNull(actual);
    }
}