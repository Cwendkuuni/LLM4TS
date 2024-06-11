package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

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

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("path/to/your/file.html");
        Document document = DataUtil.load(file, "UTF-8", "");
        assertNotNull(document);
    }

    @Test
    public void testLoadInvalidFile() throws IOException {
        File file = new File("path/to/non-existing/file.html");
        assertThrows(IOException.class, () -> DataUtil.load(file, "UTF-8", ""));
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document document = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(document);
    }

    @Test
    public void testLoadInvalidCharsetInputStream() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("ISO-8859-1"));
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, "UTF-8", ""));
    }

    @Test
    public void testLoadInputStreamWithBaseUri() throws IOException {
        String htmlContent = "<html><body><a href=\"about.html\">About</a></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com/");
        assertNotNull(document);
        Element link = document.select("a").first();
        String linkHref = link.attr("href");
        String expectedLink = "http://example.com/about.html";
        assertNotNull(linkHref);
        assertEquals(expectedLink, linkHref);
    }

    @Test
    public void testParseByteData() throws IOException {
        String htmlContent = "<html><body></body></html>";
        byte[] byteData = htmlContent.getBytes("UTF-8");
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteData);
        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testDetectCharsetFromBom() {
        String utf8Bom = "\u00ef\u00bb\u00bf";
        String utf16BomBe = "\u00fe\u00ff";
        String utf16BomLe = "\u00ff\u00fe";
        String utf32BomBe = "\u0000\u0000\u00fe\u00ff";
        String utf32BomLe = "\u00ff\u00fe\u0000\u0000";

        ByteBuffer byteBufferUtf8 = ByteBuffer.wrap(utf8Bom.getBytes("UTF-8"));
        ByteBuffer byteBufferUtf16Be = ByteBuffer.wrap(utf16BomBe.getBytes("UTF-16BE"));
        ByteBuffer byteBufferUtf16Le = ByteBuffer.wrap(utf16BomLe.getBytes("UTF-16LE"));
        ByteBuffer byteBufferUtf32Be = ByteBuffer.wrap(utf32BomBe.getBytes("UTF-32BE"));
        ByteBuffer byteBufferUtf32Le = ByteBuffer.wrap(utf32BomLe.getBytes("UTF-32LE"));

        String charsetUtf8 = DataUtil.detectCharsetFromBom(byteBufferUtf8, null);
        assertEquals("UTF-8", charsetUtf8);

        String charsetUtf16Be = DataUtil.detectCharsetFromBom(byteBufferUtf16Be, null);
        assertEquals("UTF-16BE", charsetUtf16Be);

        String charsetUtf16Le = DataUtil.detectCharsetFromBom(byteBufferUtf16Le, null);
        assertEquals("UTF-16LE", charsetUtf16Le);

        String charsetUtf32Be = DataUtil.detectCharsetFromBom(byteBufferUtf32Be, null);
        assertEquals("UTF-32BE", charsetUtf32Be);

        String charsetUtf32Le = DataUtil.detectCharsetFromBom(byteBufferUtf32Le, null);
        assertEquals("UTF-32LE", charsetUtf32Le);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertTrue(boundary.length() > 0);
    }
}