package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("src/test/resources/test.html");
        Document document = DataUtil.load(file, StandardCharsets.UTF_8.name(), null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteData() throws IOException {
        String html = "<html><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteBuffer, StandardCharsets.UTF_8.name(), null, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String html = "<html><head><meta charset='ISO-8859-1'></head><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("ISO-8859-1", document.outputSettings().charset());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String source = "Source text.";
        String target = "Target text.";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));
        OutputStream output = outputStream;

        DataUtil.crossStreams(inputStream, output);

        Assert.assertEquals(source, new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertEquals(boundary.length(), 32);
        for (int i = 0; i < boundary.length(); i++) {
            Assert.assertTrue(Character.isAlphabetic(boundary.charAt(i)) || Character.isDigit(boundary.charAt(i)) || boundary.charAt(i) == '-' || boundary.charAt(i) == '_');
        }
    }

    @Test
    public void testDetectCharsetFromBom() {
        byte[] bomUtf16Be = {0xFE, 0xFF, 0x00, 0x00};
        byte[] bomUtf16Le = {0xFF, 0xFE, 0x00, 0x00};
        byte[] bomUtf8 = {0xEF, 0xBB, 0xBF};
        byte[] bomUtf32Be = {0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00};
        byte[] bomUtf32Le = {0xFF, 0xFE, 0x00, 0x00, 0x00, 0x00};

        ByteBuffer byteBufferUtf16Be = ByteBuffer.wrap(bomUtf16Be);
        String charsetUtf16Be = DataUtil.detectCharsetFromBom(byteBufferUtf16Be, null);
        Assert.assertEquals("UTF-16", charsetUtf16Be);

        ByteBuffer byteBufferUtf16Le = ByteBuffer.wrap(bomUtf16Le);
        String charsetUtf16Le = DataUtil.detectCharsetFromBom(byteBufferUtf16Le, null);
        Assert.assertEquals("UTF-16", charsetUtf16Le);

        ByteBuffer byteBufferUtf8 = ByteBuffer.wrap(bomUtf8);
        String charsetUtf8 = DataUtil.detectCharsetFromBom(byteBufferUtf8, null);
        Assert.assertEquals("UTF-8", charsetUtf8);

        ByteBuffer byteBufferUtf32Be = ByteBuffer.wrap(bomUtf32Be);
        String charsetUtf32Be = DataUtil.detectCharsetFromBom(byteBufferUtf32Be, null);
        Assert.assertEquals("UTF-32", charsetUtf32Be);

        ByteBuffer byteBufferUtf32Le = ByteBuffer.wrap(bomUtf32Le);
        String charsetUtf32Le = DataUtil.detectCharsetFromBom(byteBufferUtf32Le, null);
        Assert.assertEquals("UTF-32", charsetUtf32Le);

        ByteBuffer byteBufferUnknown = ByteBuffer.wrap(new byte[]{0x00, 0x00, 0x00, 0x00});
        String charsetUnknown = DataUtil.detectCharsetFromBom(byteBufferUnknown, null);
        Assert.assertNull(charsetUnknown);
    }
}