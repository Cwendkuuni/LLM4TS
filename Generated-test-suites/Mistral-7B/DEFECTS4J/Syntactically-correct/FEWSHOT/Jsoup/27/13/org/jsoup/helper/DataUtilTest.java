package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test Class for DataUtil
 */
public class DataUtilTest {

    private static final String HTML = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
    private static final String CHARSET_HTML = "text/html; charset=UTF-8";
    private static final String CHARSET_GB2312 = "text/html; charset=gb2312";
    private static final String CHARSET_INVALID = "text/html; charset=invalid";
    private static final String BOM = "\ufeff";

    private InputStream inputStream;
    private File file;
    private Document document;

    @Before
    public void setUp() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(HTML.getBytes());
        this.inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        this.file = File.createTempFile("datautil", "tmp");
        Files.write(this.file.toPath(), HTML.getBytes());
        this.document = Parser.htmlParser().parseInput(HTML, null);
    }

    @Test
    public void testLoad() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = null;

        // test load from file
        Document document = DataUtil.load(this.file, charsetName, baseUri);
        assertEquals(this.document, document);

        // test load from InputStream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(HTML.getBytes());
        document = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals(this.document, document);
    }

    @Test
    public void testLoadWithCharset() throws IOException {
        String charsetName = "gb2312";
        String baseUri = null;

        // test load from file
        Document document = DataUtil.load(this.file, charsetName, baseUri);
        assertNull(document);

        // test load from InputStream
        InputStream gb2312InputStream = new ByteArrayInputStream(HTML.getBytes());
        document = DataUtil.load(gb2312InputStream, charsetName, baseUri);
        assertEquals(this.document, document);
    }

    @Test
    public void testLoadWithInvalidCharset() throws IOException {
        String charsetName = "invalid";
        String baseUri = null;

        // test load from file
        Document document = DataUtil.load(this.file, charsetName, baseUri);
        assertNull(document);

        // test load from InputStream
        InputStream invalidInputStream = new ByteArrayInputStream(HTML.getBytes());
        document = DataUtil.load(invalidInputStream, charsetName, baseUri);
        assertNull(document);
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = null;
        Parser parser = Parser.htmlParser();

        ByteBuffer byteData = ByteBuffer.wrap(HTML.getBytes());
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals(this.document, document);

        byteData.rewind();
        String foundCharset = "gb2312";
        String docData = "UTF-8" + HTML;
        document = null;
        document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNull(document);
        assertEquals(foundCharset, charsetName);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = null;
        Parser parser = Parser.htmlParser();

        ByteBuffer byteData = ByteBuffer.wrap(HTML.getBytes());
        Document document = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertEquals(this.document, document);

        byteData.rewind();
        document = null;
        document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = this.inputStream;
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedByteArray = HTML.getBytes();
        byte[] actualByteArray = byteData.array();
        assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = CHARSET_HTML;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = CHARSET_GB2312;
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("gb2312", charset);

        contentType = CHARSET_INVALID;
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testParseByteDataWithBOM() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = null;
        Parser parser = Parser.htmlParser();

        ByteBuffer byteData = ByteBuffer.wrap(BOM.getBytes());
        byteData.put(HTML.getBytes());

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals(this.document, document);
    }
}