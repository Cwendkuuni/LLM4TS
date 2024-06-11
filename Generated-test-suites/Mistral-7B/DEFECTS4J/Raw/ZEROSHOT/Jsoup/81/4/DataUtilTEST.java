package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    private Document doc;

    @Before
    public void setUp() {
        try {
            doc = DataUtil.load(new File("path/to/test/file.html"), "UTF-8", "http://example.com");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        if (doc != null) {
            doc.close();
        }
    }

    /**
     * Test method for {@link DataUtil#load(File, String, String)}
     * @throws IOException
     */
    @Test
    public void testLoadFile() throws IOException {
        Document expected = parseFile("path/to/test/expected_file.html");
        Document actual = DataUtil.load(new File("path/to/test/file.html"), "UTF-8", "http://example.com");
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}
     * @throws IOException
     */
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(new File("path/to/test/file.html"));
        Document actual = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        Document expected = parseFile("path/to/test/expected_file.html");
        assertEquals(expected, actual);
        inputStream.close();
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}
     * @throws IOException
     */
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream(new File("path/to/test/file.html"));
        Document actual = DataUtil.load(inputStream, "UTF-8", "http://example.com", DataUtil.Parser.xmlParser());
        Document expected = parseFile("path/to/test/expected_xml_file.xml");
        assertEquals(expected, actual);
        inputStream.close();
    }

    /**
     * Test method for {@link DataUtil#parseInputStream(InputStream, String, String, Parser)}
     * @throws IOException
     */
    @Test
    public void testParseInputStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes("UTF-8"));
        DataUtil.crossStreams(inputStream, outputStream);
        inputStream.close();
        Document actual = DataUtil.parseInputStream(outputStream.toByteArray(), "UTF-8", "http://example.com", DataUtil.Parser.htmlParser());
        Document expected = parseFile("path/to/test/expected_html_file.html");
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link DataUtil#detectCharsetFromBom(ByteBuffer)}
     */
    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{0x00, 0x00, (byte) 0xFE, (byte) 0xFF}); // BE UTF-32
        BomCharset actual = DataUtil.detectCharsetFromBom(byteData);
        assertEquals(new BomCharset("UTF-32", false), actual);

        byteData = ByteBuffer.wrap(new byte[]{0xFF, 0xFE, 0x00, 0x00}); // LE UTF-32
        actual = DataUtil.detectCharsetFromBom(byteData);
        assertEquals(new BomCharset("UTF-32", false), actual);

        byteData = ByteBuffer.wrap(new byte[]{0xEF, 0xBB, 0xBF}); // UTF-8
        actual = DataUtil.detectCharsetFromBom(byteData);
        assertEquals(new BomCharset("UTF-8", true), actual);

        byteData = ByteBuffer.wrap(new byte[]{0xFE, 0xFF}); // UTF-16 BE
        actual = DataUtil.detectCharsetFromBom(byteData);
        assertEquals(new BomCharset("UTF-16", false), actual);

        byteData = ByteBuffer.wrap(new byte[]{0xFF, 0xFE}); // UTF-16 LE
        actual = DataUtil.detectCharsetFromBom(byteData);
        assertEquals(new BomCharset("UTF-16", false), actual);

        byteData = ByteBuffer.allocate(0);
        actual = DataUtil.detectCharsetFromBom(byteData);
        assertNull(actual);
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=gb2312";
        String actual = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("gb2312", actual);

        contentType = "text/html; charset=UTF-8";
        actual = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", actual);

        contentType = "text/html";
        actual = DataUtil.getCharsetFromContentType(contentType);
        assertNull(actual);
    }

    /**
     * Test method for {@link DataUtil#validateCharset(String)}
     */
    @Test
    public void testValidateCharset() {
        String cs = "gb2312";
        String actual = DataUtil.validateCharset(cs);
        assertEquals("gb2312", actual);

        cs = "UTF-8";
        actual = DataUtil.validateCharset(cs);
        assertEquals("UTF-8", actual);

        cs = "EUC-JP";
        actual = DataUtil.validateCharset(cs);
        assertEquals("EUC-JP", actual);

        cs = "";
        actual = DataUtil.validateCharset(cs);
        assertNull(actual);

        cs = "invalidCharset";
        actual = DataUtil.validateCharset(cs);
        assertNull(actual);
    }

    /**
     * Test method for {@link DataUtil#mimeBoundary()}.
     */
    @Test
    public void testMimeBoundary() {
        String actual = DataUtil.mimeBoundary();
        String expected = new String(new char[DataUtil.boundaryLength])
                .replaceAll("\\p{JavaUpperCase}+", mimeBoundaryChars[new Random().nextInt(mimeBoundaryChars.length)] + "");
        assertEquals(expected, actual);
    }

    private Document parseFile(String filePath) throws IOException {
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        Document document = DataUtil.load(inputStream, "UTF-8", "", DataUtil.Parser.htmlParser());
        inputStream.close();
        return document;
    }
}