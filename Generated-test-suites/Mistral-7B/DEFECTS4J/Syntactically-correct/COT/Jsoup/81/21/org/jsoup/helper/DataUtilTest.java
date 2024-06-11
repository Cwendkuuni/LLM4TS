package org.jsoup.helper;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.Assert;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class DataUtilTest {
    // Test constants
    private static final String TEST_CHARSET = "UTF-8";
    private static final String TEST_BASE_URI = "http://test.com";
    private static final String TEST_INPUT_CONTENT = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
            "<html>\n" +
            "<head>\n" +
            "<meta charset=\"gb2312\">\n" +
            "</head>\n" +
            "<body>\n" +
            "<h1>Hello, World!</h1>\n" +
            "</body>\n" +
            "</html>";

    @BeforeClass
    public static void setup() {
        // Set up the static variables, if any
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File testFile = new File("testFile.html");
        Document doc = DataUtil.load(testFile, null);
        Assert.assertThat(doc, is(notNullValue()));
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File testFile = new File("testFile.html");
        Document doc = DataUtil.load(testFile, TEST_CHARSET);
        Assert.assertThat(doc, is(notNullValue()));
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_CONTENT.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(testInputStream, null);
        Assert.assertThat(doc, is(notNullValue()));
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_CONTENT.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(testInputStream, TEST_CHARSET);
        Assert.assertThat(doc, is(notNullValue()));
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_CONTENT.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(testInputStream, TEST_CHARSET, TEST_BASE_URI, Parser.xmlParser());
        Assert.assertThat(doc, is(notNullValue()));
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream testInputStream = new ByteArrayInputStream("Test Input Stream".getBytes());
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(testInputStream, testOutputStream);

        String output = testOutputStream.toString();
        Assert.assertThat(output, is(notNullValue()));
        Assert.assertEquals("Test Input Stream", output);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_CONTENT.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(testInputStream, "InvalidCharset");
    }

    @Test
    public void testParseInputStreamWithBOM() throws IOException {
        String bomUTF16 = "\u00FE\u00FF";
        String utf16Content = "Test UTF-16 Content";

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(bomUTF16.getBytes(StandardCharsets.UTF_8));
        testInputStream.write(utf16Content.getBytes(StandardCharsets.UTF_16));
        testInputStream.mark(0);

        Document doc = DataUtil.parseInputStream(testInputStream, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
        Assert.assertThat(doc, is(notNullValue()));
    }

    @Test
    public void testParseInputStreamWithEncodingMetaTag() throws IOException {
        String utf8Content = "Test UTF-8 Content";

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_CONTENT.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(testInputStream, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
        XmlDeclaration xmlDeclaration = doc.xmlDeclaration();
        String charset = xmlDeclaration != null ? xmlDeclaration.attr("encoding") : null;

        Assert.assertThat(charset, is(TEST_CHARSET));
    }

    @Test
    public void testParseInputStreamWithXmlDeclaration() throws IOException {
        String utf8Content = "Test UTF-8 Content";

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_CONTENT.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(testInputStream, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
        XmlDeclaration xmlDeclaration = doc.xmlDeclaration();

        Assert.assertThat(xmlDeclaration, is(notNullValue()));
        Assert.assertEquals(xmlDeclaration.name(), "xml");
        Assert.assertEquals(xmlDeclaration.encodingScheme(), "encoding");
        Assert.assertEquals(xmlDeclaration.encoding(), TEST_CHARSET);
    }

    @Test
    public void testReadToByteBufferWithUnlimitedSize() throws IOException {
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_CONTENT.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream);

        Assert.assertThat(byteBuffer, is(notNullValue()));
        Assert.assertFalse(byteBuffer.hasRemaining());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_CONTENT.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream, 50);

        Assert.assertThat(byteBuffer, is(notNullValue()));
        Assert.assertFalse(byteBuffer.hasRemaining());
    }

    @Test
    public void testDetectCharsetFromBOM() throws IOException {
        String utf32Content = "\u0000\u0000\u00FE\u00FF";

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(utf32Content.getBytes(StandardCharsets.UTF_8));
        testInputStream.mark(0);

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(testInputStream, 4));

        Assert.assertThat(bomCharset, is(notNullValue()));
        Assert.assertEquals(bomCharset.charset, "UTF-32");
        Assert.assertEquals(bomCharset.offset, false);
    }

    @Test
    public void testDetectCharsetFromBOMWithOffset() throws IOException {
        String utf8Bom = "\u00FF\u00FB";

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(utf8Bom.getBytes(StandardCharsets.UTF_8));
        testInputStream.mark(0);

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(testInputStream, 2));

        Assert.assertThat(bomCharset, is(notNullValue()));
        Assert.assertEquals(bomCharset.charset, "UTF-8");
        Assert.assertEquals(bomCharset.offset, true);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String testContentType = "text/html; charset=gb2312";

        String charset = DataUtil.getCharsetFromContentType(testContentType);

        Assert.assertEquals(charset, "gb2312");
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String testContentType = null;

        String charset = DataUtil.getCharsetFromContentType(testContentType);

        Assert.assertEquals(charset, null);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidContentType() {
        String testContentType = "text/bogus; charset=InvalidCharset";

        String charset = DataUtil.getCharsetFromContentType(testContentType);

        Assert.assertEquals(charset, null);
    }
}