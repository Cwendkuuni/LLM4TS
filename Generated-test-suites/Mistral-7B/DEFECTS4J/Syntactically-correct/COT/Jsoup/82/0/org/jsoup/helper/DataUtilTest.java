package org.jsoup.helper;

import org.junit. Assert;
import org.junit.Test;
import org.jsoup.Document;
import org.jsoup.Helper;
import org.jsoup.IOException;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("sample.html");
        Document doc = Helper.load(file, "UTF-8", "");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamNull() throws IOException {
        InputStream input = null;
        try {
            Document doc = Helper.load(input, "UTF-8", "");
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamEmpty() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Document doc = Helper.load(input, "UTF-8", "");
        Assert.assertNotNull(doc);
        Assert.assertEquals("", doc.body().text());
    }

    @Test
    public void testLoadInputStreamInvalidCharset() throws IOException {
        String invalidCharset = "xyz";
        InputStream input = new ByteArrayInputStream("Sample text".getBytes("UTF-8"));
        try {
            Document doc = Helper.load(input, invalidCharset, "");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamInvalidInputStream() throws IOException {
        InputStream input = new ByteArrayInputStream("Sample text".getBytes("UTF-8"));
        try {
            Document doc = Helper.load(null, "UTF-8", "");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            Document doc = Helper.load(input, "UTF-8", "");
            Assert.fail();
        } catch (IOException e) {
            // expected, input stream should not be closed in this test
        }
    }

    @Test
    public void testLoadInputStreamWithBaseUri() throws IOException {
        String baseUri = "https://example.com/";
        InputStream input = new ByteArrayInputStream("Sample text".getBytes("UTF-8"));
        Document doc = Helper.load(input, "UTF-8", baseUri);
        Assert.assertNotNull(doc);
        Element link = doc.selectFirst("a[href]");
        Assert.assertNotNull(link);
        Assert.assertEquals("https://example.com/another", link.absUrl("another.html"));
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String xmlContent = "<root><element>Sample text</element></root>";
        InputStream input = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"));
        Document doc = Helper.load(input, "UTF-8", "", Parser.xmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Sample text", doc.selectFirst("//element").text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String source = "Source text";
        String destination = "Destination text";
        InputStream input = new ByteArrayInputStream(source.getBytes("UTF-8"));
        OutputStream output = new ByteArrayOutputStream();

        Helper.crossStreams(input, output);
        String outputText = output.toString();
        Assert.assertEquals(source, outputText);
    }

    @Test
    public void testParseInputStreamNull() throws IOException {
        InputStream input = null;
        try {
            Document doc = Helper.parseInputStream(input, "UTF-8", "", Parser.htmlParser());
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseInputStreamEmpty() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Document doc = Helper.parseInputStream(input, "UTF-8", "", Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("", doc.body().text());
    }

    @Test
    public void testParseInputStreamInvalidCharset() throws IOException {
        String invalidCharset = "xyz";
        InputStream input = new ByteArrayInputStream("Sample text".getBytes("UTF-8"));
        try {
            Document doc = Helper.parseInputStream(input, invalidCharset, "", Parser.htmlParser());
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseInputStreamInvalidInputStream() throws IOException {
        InputStream input = new ByteArrayInputStream("Sample text".getBytes("UTF-8"));
        try {
            Document doc = Helper.parseInputStream(null, "UTF-8", "", Parser.htmlParser());
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            Document doc = Helper.parseInputStream(input, "UTF-8", "", Parser.htmlParser());
            Assert.fail();
        } catch (IOException e) {
            // expected, input stream should not be closed in this test
        }
    }

    @Test
    public void testParseInputStreamWithBaseUri() throws IOException {
        String baseUri = "https://example.com/";
        InputStream input = new ByteArrayInputStream("Sample text".getBytes("UTF-8"));
        Document doc = Helper.parseInputStream(input, "UTF-8", baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Element link = doc.selectFirst("a[href]");
        Assert.assertNotNull(link);
        Assert.assertEquals("https://example.com/another", link.absUrl("another.html"));
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        String xmlContent = "<root><element>Sample text</element></root>";
        InputStream input = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"));
        Document doc = Helper.parseInputStream(input, "UTF-8", "", Parser.xmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Sample text", doc.selectFirst("//element").text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream input = new ByteArrayInputStream("Sample text".getBytes("UTF-8"));
        ByteBuffer buffer = Helper.readToByteBuffer(input, 1024 * 32);
        Assert.assertNotNull(buffer);
        Assert.assertEquals(1024 * 32, buffer.limit());
        String output = Charset.forName("UTF-8").decode(buffer).toString();
        Assert.assertEquals("Sample text", output);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Sample text".getBytes("UTF-8"));
        ByteBuffer buffer = Helper.readToByteBuffer(input, 1024 * 64);
        Assert.assertNotNull(buffer);
        Assert.assertEquals(1024 * 64, buffer.limit());
        String output = Charset.forName("UTF-8").decode(buffer).toString();
        Assert.assertEquals("Sample text", output);
    }

    @Test
    public void testReadToByteBufferWithUnlimitedSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Sample text".getBytes("UTF-8"));
        ByteBuffer buffer = Helper.readToByteBuffer(input, 0);
        Assert.assertNotNull(buffer);
        Assert.assertEquals(0, buffer.limit());
        String output = Charset.forName("UTF-8").decode(buffer).toString();
        Assert.assertEquals("Sample text", output);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = Helper.emptyByteBuffer();
        Assert.assertNotNull(buffer);
        Assert.assertEquals(0, buffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = Helper.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = Helper.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);

        contentType = "text/html; charset=GB2312";
        charset = Helper.getCharsetFromContentType(contentType);
        Assert.assertEquals("GB2312", charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        Assert.assertEquals("UTF-8", Helper.validateCharset(charset));

        charset = "UTF-8\t";
        Assert.assertEquals("UTF-8", Helper.validateCharset(charset));

        charset = "UTF-8\"";
        Assert.assertEquals("UTF-8", Helper.validateCharset(charset));

        charset = null;
        Assert.assertNull(Helper.validateCharset(charset));

        charset = "xyz";
        Assert.assertNull(Helper.validateCharset(charset));

        charset = "euc-jp";
        Assert.assertEquals("euc-jp", Helper.validateCharset(charset));

        charset = "EUC-JP";
        Assert.assertEquals("euc-jp", Helper.validateCharset(charset));

        charset = "EUC-JP\t";
        Assert.assertEquals("euc-jp", Helper.validateCharset(charset));

        charset = "EUC-JP\"";
        Assert.assertEquals("euc-jp", Helper.validateCharset(charset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = Helper.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertTrue(boundary.length() >= 32);
    }

    @Test
    public void testDetectCharsetFromBomNull() {
        ByteBuffer byteData = Helper.emptyByteBuffer();
        BomCharset bomCharset = Helper.detectCharsetFromBom(byteData);
        Assert.assertNull(bomCharset);
    }

    @Test
    public void testDetectCharsetFromBomUTF8() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{0xFF, 0xED, 0xFE, 0xFC, 0x00, 0x1F, 0xBF, 0xDB});
        BomCharset bomCharset = Helper.detectCharsetFromBom(byteData);
        Assert.assertNotNull(bomCharset);
        Assert.assertEquals("UTF-8", bomCharset.charset);
        Assert.assertFalse(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomUTF16BE() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{0xFF, 0xFE, 0x00, 0x00, (byte) 0xFE, (byte) 0xED, 0x00, 0x1F, (byte) 0xBF, 0xDB});
        BomCharset bomCharset = Helper.detectCharsetFromBom(byteData);
        Assert.assertNotNull(bomCharset);
        Assert.assertEquals("UTF-16", bomCharset.charset);
        Assert.assertFalse(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomUTF16LE() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{0xFF, 0xED, 0xFE, 0x00, (byte) 0xDB, (byte) 0xBF, 0x00, 0x1F, (byte) 0xFE, 0x00});
        BomCharset bomCharset = Helper.detectCharsetFromBom(byteData);
        Assert.assertNotNull(bomCharset);
        Assert.assertEquals("UTF-16", bomCharset.charset);
        Assert.assertTrue(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomUTF32BE() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{0xFF, 0xFE, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF});
        BomCharset bomCharset = Helper.detectCharsetFromBom(byteData);
        Assert.assertNotNull(bomCharset);
        Assert.assertEquals("UTF-32", bomCharset.charset);
        Assert.assertFalse(bomCharset.offset);
    }

    @Test(expected = IllegalStateException.class)
    public void testDetectCharsetFromBomInvalidUTF8() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{0xED, 0xFA, 0xFE, 0x00, 0x1F, 0xBF, 0xDB});
        Helper.detectCharsetFromBom(byteData);
    }
}

class BomCharset {
    String charset;
    boolean offset;

    public BomCharset(String charset, boolean offset) {
        this.charset = charset;
        this.offset = offset;
    }
}