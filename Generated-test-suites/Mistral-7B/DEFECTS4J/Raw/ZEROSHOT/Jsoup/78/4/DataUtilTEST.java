package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.powermockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

@PowerMockIgnore("javax.xml.XMLConstants") // to avoid warning from JUnit
@RunWith(PowerMockRunner.class)
public class DataUtilTest {

    private static final String TEST_FILE = "/path/to/testfile.html"; // update with your test file path
    private static final String TEST_CHARSET = "UTF-8";
    private static final String TEST_BASE_URI = "http://test.com";

    @Test
    public void testLoadFile() throws IOException {
        Document document = DataUtil.load(new File(TEST_FILE), TEST_CHARSET, TEST_BASE_URI);
        assertNotNull(document);
    }

    @Test
    public void testLoadInvalidFile() {
        try {
            DataUtil.load(null, TEST_CHARSET, TEST_BASE_URI);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStream() throws IOException {
        File testFile = new File(TEST_FILE);
        InputStream inputStream = new FileInputStream(testFile);

        Document document = DataUtil.load(inputStream, TEST_CHARSET, TEST_BASE_URI);
        assertNotNull(document);

        inputStream.close();
    }

    @Test
    public void testLoadEmptyInputStream() throws IOException {
        Document document = DataUtil.load(new ByteArrayInputStream(new byte[0]), TEST_CHARSET, TEST_BASE_URI);
        assertNotNull(document);
        assertEquals(0, document.childNodeSize());
    }

    @Test
    public void testParseInputStream() throws IOException {
        File testFile = new File(TEST_FILE);
        InputStream inputStream = new FileInputStream(testFile);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        inputStream.close();

        Document document = DataUtil.parseInputStream(new ByteArrayInputStream(outputStream.toByteArray()), TEST_CHARSET, TEST_BASE_URI, DataUtil.Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithCharsetFromContentType() throws IOException {
        File testFile = new File(TEST_FILE);
        InputStream inputStream = new FileInputStream(testFile);
        String contentType = "text/html; charset=gb2312";

        Document document = DataUtil.parseInputStream(inputStream, contentType, TEST_BASE_URI, DataUtil.Parser.htmlParser());
        assertNotNull(document);
        assertEquals("gb2312", document.outputSettings().charset());

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithInvalidCharsetFromContentType() throws IOException {
        File testFile = new File(TEST_FILE);
        InputStream inputStream = new FileInputStream(testFile);
        String contentType = "text/html; charset=invalidCharset";

        try {
            DataUtil.parseInputStream(inputStream, contentType, TEST_BASE_URI, DataUtil.Parser.htmlParser());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithMetaCharset() throws IOException {
        File testFile = new File(TEST_FILE);
        InputStream inputStream = new FileInputStream(testFile);
        String baseUri = "http://test.com/";
        String metaCharset = "gb2312";

        Document document = new Document(baseUri);
        document.outputSettings().charset(TEST_CHARSET);
        document.body().appendElement("meta").attr("http-equiv", "Content-Type").attr("content", "text/html; charset=" + metaCharset);

        DataUtil.parseInputStream(new InputStreamReader(new ByteArrayInputStream(document.html().getBytes(TEST_CHARSET))), TEST_CHARSET, baseUri, DataUtil.Parser.htmlParser());
        assertEquals(metaCharset, document.outputSettings().charset());

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithXmlDeclarationCharset() throws IOException {
        File testFile = new File(TEST_FILE);
        InputStream inputStream = new FileInputStream(testFile);
        String baseUri = "http://test.com/";

        Document document = new Document(baseUri);
        document.outputSettings().charset(TEST_CHARSET);
        document.body().appendElement("xml").attr("version", "1.0").attr("encoding", "gb2312");

        DataUtil.parseInputStream(new InputStreamReader(new ByteArrayInputStream(document.html().getBytes(TEST_CHARSET))), TEST_CHARSET, baseUri, DataUtil.Parser.htmlParser());
        assertEquals("gb2312", document.outputSettings().charset());

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithInvalidXmlDeclarationCharset() throws IOException {
        File testFile = new File(TEST_FILE);
        InputStream inputStream = new FileInputStream(testFile);
        String baseUri = "http://test.com/";

        Document document = new Document(baseUri);
        document.outputSettings().charset(TEST_CHARSET);
        document.body().appendElement("xml").attr("version", "1.0").attr("encoding", "invalidCharset");

        try {
            DataUtil.parseInputStream(new InputStreamReader(new ByteArrayInputStream(document.html().getBytes(TEST_CHARSET))), TEST_CHARSET, baseUri, DataUtil.Parser.htmlParser());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        inputStream.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File testFile = new File(TEST_FILE);
        InputStream inputStream = new FileInputStream(testFile);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
        assertSame(inputStream, byteBuffer.asInputBuffer().mark(), "Input streams should be the same.");

        inputStream.close();
    }

    @Test
    public void testReadToByteBufferWithEmptyInputStream() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[0]));
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        File testFile = new File(TEST_FILE);
        InputStream inputStream = new FileInputStream(testFile);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);
        assertNotNull(byteBuffer);
        assertEquals(10, byteBuffer.limit());

        inputStream.close();
    }

    @Test
    public void testReadToByteBufferWithMaxSizeGreaterThanAvailableData() throws IOException {
        File testFile = new File(TEST_FILE);
        InputStream inputStream = new FileInputStream(testFile);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 100);
        assertNotNull(byteBuffer);
        assertEquals(inputStream.available(), byteBuffer.limit());
        inputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=gb2312";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("gb2312", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String cs = "UTF-8";
        assertEquals(cs, DataUtil.validateCharset(cs));

        String csInvalid = "invalid_charset";
        assertNull(DataUtil.validateCharset(csInvalid));
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        File testFile = new File(TEST_FILE);
        InputStream inputStream = new FileInputStream(testFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 4);
        DataUtil.inputStreamMark(inputStream);

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(3, bomCharset.offset);

        inputStream.close();
    }

    @Test
    public void testDetectCharsetFromBomWithUtf16Be() throws IOException {
        File testFile = new File(TEST_FILE);
        InputStream inputStream = new FileInputStream(testFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 2);
        DataUtil.inputStreamMark(inputStream);

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-16", bomCharset.charset);
        assertEquals(0, bomCharset.offset);

        inputStream.close();
    }

    @Test
    public void testDetectCharsetFromBomWithUtf32Be() throws IOException {
        File testFile = new File(TEST_FILE);
        InputStream inputStream = new FileInputStream(testFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 8);
        DataUtil.inputStreamMark(inputStream);

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-32", bomCharset.charset);
        assertEquals(0, bomCharset.offset);

        inputStream.close();
    }

    @Test
    public void testDetectCharsetFromBomWithInvalidBom() throws IOException {
        File testFile = new File(TEST_FILE);
        InputStream inputStream = new FileInputStream(testFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 4);
        DataUtil.inputStreamMark(inputStream);

        // Change the first byte of the bom to a random character to make it invalid
        byteData.put(0, (byte) 0xAB);
        inputStream.reset();

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertNull(bomCharset);

        inputStream.close();
    }

    @Test
    public void testDetectCharsetFromBomWithEmptyFile() throws IOException {
        File testFile = new File(TEST_FILE);
        InputStream inputStream = new FileInputStream(testFile);

        DataUtil.inputStreamMark(inputStream);
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(DataUtil.emptyByteBuffer());
        assertNull(bomCharset);

        inputStream.close();
    }

    private static void inputStreamMark(InputStream inputStream) {
        PowerMockito.mockStatic(DataUtil.class);
        DataUtil mockDataUtil = PowerMockito.mock(DataUtil.class);
        PowerMockito.when(DataUtil.inputStreamMark(inputStream)).thenReturn(mockDataUtil);
    }
}