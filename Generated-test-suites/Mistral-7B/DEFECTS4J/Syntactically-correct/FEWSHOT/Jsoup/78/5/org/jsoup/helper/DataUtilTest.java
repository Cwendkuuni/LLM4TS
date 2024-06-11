package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        File sampleFile = new File("sample.html");
        Document document = DataUtil.load(sampleFile, "UTF-8", null);
        assertNotNull(document);
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String sampleData = "<html><body><h1>Sample</h1></body></html>";
        InputStream sampleInputStream = new ByteArrayInputStream(sampleData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(sampleInputStream, "UTF-8", null);
        assertNotNull(document);
    }

    @Test
    public void testParseDocumentFromInputStream() throws IOException {
        String sampleData = "<html><body><h1>Sample</h1></body></html>";
        InputStream sampleInputStream = new ByteArrayInputStream(sampleData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(sampleInputStream, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseDocumentFromInputStreamWithXmlParser() throws IOException {
        String sampleData = "<html xmlns:xsi=\'http://www.w3.org/2001/XMLSchema-instance\' xmlns:xsi:schemalocation=\'http://www.w3.org/2001/XMLSchema.xsd'> <head/> <body></body> </html>";
        InputStream sampleInputStream = new ByteArrayInputStream(sampleData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(sampleInputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String sourceData = "Sourcedata";
        String targetData = "";
        InputStream sourceInputStream = new ByteArrayInputStream(sourceData.getBytes(StandardCharsets.UTF_8));
        OutputStream targetOutputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(sourceInputStream, targetOutputStream);

        assertEquals(sourceData, targetData = targetOutputStream.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void testDetectCharsetFromBOM() {
        ByteBuffer bomUtf8 = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        ByteBuffer bomUtf16 = ByteBuffer.wrap(new byte[]{(byte) 0xFF, (byte) 0xFE, 0x00, 0x00});
        ByteBuffer bomUtf32 = ByteBuffer.wrap(new byte[]{0x00, 0x00, (byte) 0xFE, (byte) 0xFF});

        BomCharset bomUtf8Result = DataUtil.detectCharsetFromBom(bomUtf8);
        BomCharset bomUtf16Result = DataUtil.detectCharsetFromBom(bomUtf16);
        BomCharset bomUtf32Result = DataUtil.detectCharsetFromBom(bomUtf32);

        assertEquals("UTF-8", bomUtf8Result.charset);
        assertEquals("UTF-16", bomUtf16Result.charset);
        assertNull(bomUtf32Result);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String contentType2 = "text/plain";
        String contentType3 = "application/xml; charset=UTF-16";

        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        String charset3 = DataUtil.getCharsetFromContentType(contentType3);

        assertEquals("UTF-8", charset1);
        assertEquals(null, charset2);
        assertEquals("UTF-16", charset3);
    }

    @Test
    public void testValidateCharset() {
        String charset1 = "UTF-8";
        String charset2 = "UTF-16";
        String charset3 = "us-ASCII";
        String charset4 = "invalidCharset";

        String validatedCharset1 = DataUtil.validateCharset(charset1);
        String validatedCharset2 = DataUtil.validateCharset(charset2);
        String validatedCharset3 = DataUtil.validateCharset(charset3);
        String validatedCharset4 = DataUtil.validateCharset(charset4);

        assertEquals("UTF-8", validatedCharset1);
        assertEquals("UTF-16", validatedCharset2);
        assertEquals("us-ASCII", validatedCharset3);
        assertEquals(null, validatedCharset4);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        String expectedMimeBoundary = "2d_34567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        assertEquals(expectedMimeBoundary, mimeBoundary);
    }
}