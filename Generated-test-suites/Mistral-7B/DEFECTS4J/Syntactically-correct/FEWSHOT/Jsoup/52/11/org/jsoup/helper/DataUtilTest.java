package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;

public class DataUtilTest {
    @Test
    public void testLoad() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello</h1></body></html>";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.load(in, "UTF-8", null);
        assertEquals(html, doc.html());

        File file = new File("test.html");
        WritableByteChannel channel = file.getChannel();
        channel.write(ByteBuffer.wrap(html.getBytes("UTF-8")));
        channel.close();
        doc = DataUtil.load(file, null, null);
        assertEquals(html, doc.html());

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n<root></root>";
        ByteBuffer byteData = ByteBuffer.wrap(xml.getBytes("UTF-8"));
        Document docXml = DataUtil.parseByteData(byteData, null, null, Parser.xmlParser());
        assertEquals(xml, docXml.html());
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello</h1></body></html>";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(html.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);

        Document doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertEquals(html, doc.html());

        String invalidCharset = "InvalidCharset";
        Document docInvalidCharset = DataUtil.parseByteData(byteData, invalidCharset, null, Parser.htmlParser());
        assertNull(docInvalidCharset);

        Document docDetectedCharset = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals(html, docDetectedCharset.html());

        String charsetFromContentType = "GB2312";
        String htmlWithContentType = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + charsetFromContentType + "\"></head><body><h1>Hello</h1></body></html>";
        ByteArrayOutputStream outWithContentType = new ByteArrayOutputStream();
        ByteArrayInputStream inWithContentType = new ByteArrayInputStream(htmlWithContentType.getBytes("UTF-8"));
        ByteBuffer byteDataWithContentType = DataUtil.readToByteBuffer(inWithContentType);

        Document docDetectedCharsetFromContentType = DataUtil.parseByteData(byteDataWithContentType, null, null, Parser.htmlParser());
        assertEquals(htmlWithContentType, docDetectedCharsetFromContentType.html());

        Document docWithDetectedCharset = DataUtil.parseByteData(byteDataWithContentType, null, null, Parser.htmlParser());
        assertEquals(htmlWithContentType, docWithDetectedCharset.html());

        Document docWithProvidedCharset = DataUtil.parseByteData(byteDataWithContentType, charsetFromContentType, null, Parser.htmlParser());
        assertEquals(htmlWithContentType, docWithProvidedCharset.html());

        String xmlWithEncoding = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n<root></root>";
        ByteBuffer byteDataXml = ByteBuffer.wrap(xmlWithEncoding.getBytes("UTF-8"));
        Document docXmlDetected = DataUtil.parseByteData(byteDataXml, null, null, Parser.xmlParser());
        assertEquals(xmlWithEncoding, docXmlDetected.html());

        Document docXmlProvided = DataUtil.parseByteData(byteDataXml, "UTF-16", null, Parser.xmlParser());
        assertEquals(xmlWithEncoding, docXmlProvided.html());

        Document docXmlInvalid = DataUtil.parseByteData(byteDataXml, "InvalidCharset", null, Parser.xmlParser());
        assertNull(docXmlInvalid);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        String contentTypeWithNoCharset = "text/html";
        String charsetNoCharset = DataUtil.getCharsetFromContentType(contentTypeWithNoCharset);
        assertEquals(null, charsetNoCharset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validCharset);

        String invalidCharset1 = "InvalidCharset1";
        String invalidCharset2 = "InvalidCharset2";
        String validatedCharset1 = DataUtil.validateCharset(invalidCharset1);
        String validatedCharset2 = DataUtil.validateCharset(invalidCharset2);
        assertNull(validatedCharset1);
        assertNull(validatedCharset2);

        String charsetUppercase = "UTF-8".toUpperCase(Locale.ENGLISH);
        String validatedCharsetUppercase = DataUtil.validateCharset(charsetUppercase);
        assertEquals(charsetUppercase, validatedCharsetUppercase);

        String charsetWithQuotes = "UTF-8\"";
        String validatedCharsetWithQuotes = DataUtil.validateCharset(charsetWithQuotes);
        assertEquals(charset, validatedCharsetWithQuotes);

        String charsetWithComma = "UTF-8,";
        String validatedCharsetWithComma = DataUtil.validateCharset(charsetWithComma);
        assertEquals(charset, validatedCharsetWithComma);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertTrue(boundary.length() >= 32);
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        String utf8Bom = "\uFEFF" + "Hello World";
        ByteBuffer byteDataUTF8 = ByteBuffer.wrap(utf8Bom.getBytes("UTF-8"));
        String charsetUTF8 = DataUtil.detectCharsetFromBom(byteDataUTF8, null);
        assertEquals("UTF-8", charsetUTF8);

        String utf16BomBE = "\uFEFF\uFFFE" + "Hello World";
        ByteBuffer byteDataUTF16BE = ByteBuffer.wrap(utf16BomBE.getBytes("UTF-8"));
        String charsetUTF16BE = DataUtil.detectCharsetFromBom(byteDataUTF16BE, null);
        assertEquals("UTF-16", charsetUTF16BE);

        String utf16BomLE = "\uFFFE\uFEFF" + "Hello World";
        ByteBuffer byteDataUTF16LE = ByteBuffer.wrap(utf16BomLE.getBytes("UTF-8"));
        String charsetUTF16LE = DataUtil.detectCharsetFromBom(byteDataUTF16LE, null);
        assertEquals("UTF-16", charsetUTF16LE);

        ByteBuffer byteDataEmpty = DataUtil.emptyByteBuffer();
        String charsetEmpty = DataUtil.detectCharsetFromBom(byteDataEmpty, null);
        assertEquals(null, charsetEmpty);
    }
}