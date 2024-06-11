package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        final String testContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body>Test file content</body></html>";
        final File testFile = new File("testFile.xml");
        try (RandomAccessFile file = new RandomAccessFile(testFile, "rw");) {
            file.writeBytes(testContent.getBytes());
        }

        Document document = DataUtil.load(testFile, "UTF-8", null);
        assertNotNull(document);
        assertEquals("<html><body>Test file content</body></html>", document.html());

        testFile.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        final String testContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body>Test file content</body></html>";
        final ByteArrayOutputStream testStream = new ByteArrayOutputStream();
        testStream.write(testContent.getBytes());

        InputStream inputStream = new ByteArrayInputStream(testStream.toByteArray());
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
        assertEquals("<html><body>Test file content</body></html>", document.html());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        final String testContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body>Test file content</body></html>";
        final ByteArrayOutputStream testStream = new ByteArrayOutputStream();
        testStream.write(testContent.getBytes());

        InputStream inputStream = new ByteArrayInputStream(testStream.toByteArray());
        Document document = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(document);
        assertEquals("<html><body>Test file content</body></html>", document.xml().asXml());
    }

    @Test
    public void testParseByteData() throws IOException {
        final String testContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body>Test file content</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testContent.getBytes());

        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.xmlParser());
        assertNotNull(document);
        assertEquals("<html><body>Test file content</body></html>", document.xml().asXml());
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        final ByteBuffer utf8Bom = ByteBuffer.wrap(new byte[]{0xEF, 0xBB, 0xBF});
        final ByteBuffer utf16Bom = ByteBuffer.wrap(new byte[]{0xFF, 0xFE});
        final ByteBuffer utf32Bom = ByteBuffer.wrap(new byte[]{0x00, 0x00, (byte) 0xFE, (byte) 0xFF});

        String charset = DataUtil.detectCharsetFromBom(utf8Bom, null);
        assertEquals("UTF-8", charset);

        charset = DataUtil.detectCharsetFromBom(utf16Bom, null);
        assertEquals("UTF-16", charset);

        charset = DataUtil.detectCharsetFromBom(UTF32_BOM, null);
        assertEquals("UTF-32", charset);

        charset = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[0]), "UTF-8");
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html; charset=ISO-8859-1";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("ISO-8859-1", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-8\t";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "ISO-8859-1";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "ISO-8859-12";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "ISO-8859-1\t";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "";
        assertNull(DataUtil.validateCharset(charset));

        charset = "UTF-16\t";
        assertNull(DataUtil.validateCharset(charset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        String expected = "---------9d5b10d2a98f47d30a128a7d";
        assertEquals(expected, boundary);
    }
}