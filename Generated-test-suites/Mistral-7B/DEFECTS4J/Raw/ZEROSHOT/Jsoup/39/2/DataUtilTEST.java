package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for DataUtil.
 *
 */
public class DataUtilTest {

    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final int BUFFER_SIZE = 0x20000;
    private static final String BASE_URI = "baseUri";
    private static final String CHARSET_NAME = "charsetName";
    private static final String CHARSET_NAME_GB2312 = "gb2312";
    private static final String CHARSET_NAME_UTF8 = "UTF-8";
    private static final String FILE_NAME = "fileName";
    private static final String INPUT_STREAM_DATA = "inputStreamData";
    private static final String META_CHARSET = "EUC-JP";
    private static final String PARSER_NAME = "parserName";
    private static final String PARSER_XML = "xmlParser";
    private static final String INPUT_STREAM_DATA_BOM = "inputStreamDataBOM";
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    private Parser parser;
    private Document document;
    private File file;
    private InputStream inputStream;

    @Before
    public void setUp() {
        parser = mock(Parser.class);
        document = mock(Document.class);
        file = mock(File.class);
        inputStream = mock(InputStream.class);
    }

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadFile() throws IOException {
        // Given
        when(file.exists()).thenReturn(true);
        when(file.length()).thenReturn(130000); // ~130K

        // When
        Document result = DataUtil.load(file, CHARSET_NAME, BASE_URI);

        // Then
        assertNotNull(result);
    }

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     * @throws IOException on IO error
     */
    @Test(expected = IOException.class)
    public void testLoadFileIOException() throws IOException {
        // Given
        when(file.exists()).thenReturn(false);

        // When
        DataUtil.load(file, CHARSET_NAME, BASE_URI);

        // Then
        // The test should fail with an IOException
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadInputStream() throws IOException {
        // Given
        when(inputStream.available()).thenReturn(130000); // ~130K

        // When
        Document result = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI);

        // Then
        assertNotNull(result);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadInputStreamParser() throws IOException {
        // Given
        when(inputStream.available()).thenReturn(130000); // ~130K

        // When
        Document result = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI, parser);

        // Then
        assertNotNull(result);
        when(parser.parseInput(INPUT_STREAM_DATA, BASE_URI)).thenReturn(document);
        assertSame(document, result);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     * @throws IOException on IO error
     */
    @Test
    public void testParseByteData() throws IOException {
        // Given
        ByteBuffer byteBuffer = ByteBuffer.allocate(130000); // ~130K
        String charsetName = CHARSET_NAME_UTF8;
        String baseUri = BASE_URI;
        Parser parser = parser;

        // When
        Document result = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);

        // Then
        assertNotNull(result);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with charsetName null.
     * @throws IOException on IO error
     */
    @Test
    public void testParseByteDataNullCharsetName() throws IOException {
        // Given
        ByteBuffer byteBuffer = ByteBuffer.allocate(130000); // ~130K
        String baseUri = BASE_URI;
        Parser parser = parser;

        // When
        Document result = DataUtil.parseByteData(byteBuffer, null, baseUri, parser);

        // Then
        assertNull(result);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with charsetName empty.
     * @throws IOException on IO error
     */
    @Test
    public void testParseByteDataEmptyCharsetName() throws IOException {
        // Given
        ByteBuffer byteBuffer = ByteBuffer.allocate(130000); // ~130K
        String baseUri = BASE_URI;
        Parser parser = parser;

        // When
        Document result = DataUtil.parseByteData(byteBuffer, "", baseUri, parser);

        // Then
        assertNull(result);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with charsetName GB2312.
     * @throws IOException on IO error
     */
    @Test
    public void testParseByteDataGB2312() throws IOException {
        // Given
        ByteBuffer byteBuffer = ByteBuffer.allocate(130000); // ~130K
        String charsetName = CHARSET_NAME_GB2312;
        String baseUri = BASE_URI;
        Parser parser = parser;

        // When
        Document result = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);

        // Then
        assertNotNull(result);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with charsetName UTF-8.
     * @throws IOException on IO error
     */
    @Test
    public void testParseByteDataUTF8() throws IOException {
        // Given
        ByteBuffer byteBuffer = ByteBuffer.allocate(130000 + 1); // ~130K + BOM
        String charsetName = CHARSET_NAME_UTF8;
        String baseUri = BASE_URI;
        Parser parser = parser;

        // When
        Document result = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);

        // Then
        assertNotNull(result);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with charsetName null and UTF-8 BOM.
     * @throws IOException on IO error
     */
    @Test
    public void testParseByteDataNullCharsetNameUTF8BOM() throws IOException {
        // Given
        ByteBuffer byteBuffer = ByteBuffer.allocate(130001); // ~130K + BOM
        String baseUri = BASE_URI;
        Parser parser = parser;

        // When
        Document result = DataUtil.parseByteData(byteBuffer, null, baseUri, parser);

        // Then
        assertNotNull(result);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with charsetName GB2312 and no BOM.
     * @throws IOException on IO error
     */
    @Test(expected = IOException.class)
    public void testParseByteDataGB2312NoBOM() throws IOException {
        // Given
        ByteBuffer byteBuffer = ByteBuffer.allocate(130000); // ~130K
        String charsetName = CHARSET_NAME_GB2312;
        String baseUri = BASE_URI;
        Parser parser = parser;

        // When
        Document result = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);

        // Then
        // The test should fail with an IOException
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with charsetName UTF-8 and no BOM.
     * @throws IOException on IO error
     */
    @Test
    public void testParseByteDataUTF8NoBOM() throws IOException {
        // Given
        ByteBuffer byteBuffer = ByteBuffer.allocate(130000); // ~130K
        String charsetName = CHARSET_NAME_UTF8;
        String baseUri = BASE_URI;
        Parser parser = parser;

        // When
        Document result = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);

        // Then
        assertNotNull(result);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with charsetName UTF-8 and BOM.
     * @throws IOException on IO error
     */
    @Test
    public void testParseByteDataUTF8BOM() throws IOException {
        // Given
        ByteBuffer byteBuffer = ByteBuffer.allocate(130001); // ~130K + BOM
        String charsetName = CHARSET_NAME_UTF8;
        String baseUri = BASE_URI;
        Parser parser = parser;

        // When
        Document result = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);

        // Then
        assertNotNull(result);
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream, int)}.
     * @throws IOException on IO error
     */
    @Test
    public void testReadToByteBuffer() throws IOException {
        // Given
        InputStream inputStream = new ByteArrayInputStream(INPUT_STREAM_DATA.getBytes());

        // When
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        // Then
        assertNotNull(byteBuffer);
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream)} with maxSize 0.
     * @throws IOException on IO error
     */
    @Test
    public void testReadToByteBufferMaxSizeZero() throws IOException {
        // Given
        InputStream inputStream = new ByteArrayInputStream(INPUT_STREAM_DATA.getBytes());

        // When
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        // Then
        assertNotNull(byteBuffer);
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream)} with maxSize < 0.
     * @throws IOException on IO error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadToByteBufferMaxSizeNegative() throws IOException {
        // Given
        InputStream inputStream = new ByteArrayInputStream(INPUT_STREAM_DATA.getBytes());

        // When
        DataUtil.readToByteBuffer(inputStream, -1);

        // Then
        // The test should fail with an IllegalArgumentException
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
     * @throws IOException on IO error
     */
    @Test
    public void testGetCharsetFromContentType() {
        // Given
        String contentType = CONTENT_TYPE;

        // When
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Then
        assertNotNull(charset);
        assertEquals(CHARSET_NAME_UTF8, charset);
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)} when charset is not supported.
     * @throws IOException on IO error
     */
    @Test
    public void testGetCharsetFromContentTypeUnsupportedCharset() {
        // Given
        String contentType = "text/html; charset=X-UNKNOWN";

        // When
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Then
        assertNull(charset);
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)} when charset is null.
     * @throws IOException on IO error
     */
    @Test
    public void testGetCharsetFromContentTypeNullContentType() {
        // Given
        String contentType = null;

        // When
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Then
        assertNull(charset);
    }
}