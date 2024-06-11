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
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        File testFile = new File("testFile.xml");
        FileUtils.writeStringToFile(testFile, fileContent, "UTF-8");

        Document doc = DataUtil.load(testFile, "UTF-8", null);
        assertNotNull(doc);
        assertEquals("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>", doc.html());
        FileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String inputStreamContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputStreamContent.getBytes("UTF-8"));
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document doc = DataUtil.load(testInputStream, "UTF-8", null);
        assertNotNull(doc);
        assertEquals("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>", doc.html());
    }

    @Test
    public void testDetectCharsetFromBom() {
        byte[] bomUtf16LE = {0xFF, 0xFE, 0x00, 0x00};
        byte[] bomUtf16BE = {0x00, 0x00, 0xFF, 0xFE};
        byte[] bomUtf32LE = {0x00, 0x00, 0xFF, 0xFE, 0x00, 0x00};
        byte[] bomUtf32BE = {0xFF, 0xFE, 0x00, 0x00, 0x00, 0x00};
        byte[] bomUtf8 = {0xEF, 0xBB, 0xBF};

        String charsetName = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(bomUtf16LE), null);
        assertEquals("UTF-16", charsetName);

        charsetName = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(bomUtf16BE), null);
        assertEquals("UTF-16", charsetName);

        charsetName = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(bomUtf32LE), null);
        assertEquals("UTF-32", charsetName);

        charsetName = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(bomUtf32BE), null);
        assertEquals("UTF-32", charsetName);

        charsetName = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(bomUtf8), null);
        assertEquals("UTF-8", charsetName);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String contentType2 = "text/html; charset=ISO-8859-1";
        String contentType3 = "text/html";

        String charsetName1 = DataUtil.getCharsetFromContentType(contentType1);
        assertEquals("UTF-8", charsetName1);

        String charsetName2 = DataUtil.getCharsetFromContentType(contentType2);
        assertEquals("ISO-8859-1", charsetName2);

        String charsetName3 = DataUtil.getCharsetFromContentType(contentType3);
        assertNull(charsetName3);
    }

    @Test
    public void testParseByteData() throws IOException {
        String testContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testContent.getBytes("UTF-8"));

        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.xmlParser());
        assertNotNull(doc);
        assertEquals("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>", doc.html());

        byteData = ByteBuffer.wrap(testContent.getBytes("ISO-8859-1"));
        doc = DataUtil.parseByteData(byteData, "ISO-8859-1", null, Parser.xmlParser());
        assertNotNull(doc);
        assertEquals("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>", doc.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputStreamContent.getBytes("UTF-8"));
        InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());

        ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);
        assertEquals(inputStreamContent.length(), byteData.remaining());
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void testValidateCharset() {
        String charset1 = "UTF-8";
        String charset2 = "UTF-8\n";
        String charset3 = "UTF-8\t";
        String charset4 = "ISO-8859-1";
        String charset5 = "UTF-8\tISO-8859-1";
        String charset6 = "UTF-8\nISO-8859-1";
        String charset7 = "GBK";

        String validCharset1 = DataUtil.validateCharset(charset1);
        assertEquals(charset1, validCharset1);

        String validCharset2 = DataUtil.validateCharset(charset2);
        assertEquals(charset1, validCharset2);

        String validCharset3 = DataUtil.validateCharset(charset3);
        assertEquals(charset1, validCharset3);

        String validCharset4 = DataUtil.validateCharset(charset4);
        assertNull(validCharset4);

        String validCharset5 = DataUtil.validateCharset(charset5);
        assertNull(validCharset5);

        String validCharset6 = DataUtil.validateCharset(charset6);
        assertNull(validCharset6);

        String validCharset7 = DataUtil.validateCharset(charset7);
        assertNull(validCharset7);
    }
}