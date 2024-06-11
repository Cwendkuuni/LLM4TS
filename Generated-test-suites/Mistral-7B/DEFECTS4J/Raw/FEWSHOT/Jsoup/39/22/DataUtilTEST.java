package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        // Test loading a file with default charset (UTF-8)
        File file = new File("testFile.html");
        Document doc = DataUtil.load(file, null);
        assertEquals("Test content", doc.body().text());

        // Test loading a file with specified charset
        String charsetName = "ISO-8859-1";
        Document docWithCharset = DataUtil.load(file, charsetName);
        assertEquals("Test content", docWithCharset.body().text());

        // Test loading a non-existent file
        File nonExistentFile = new File("nonExistentFile.html");
        assertThrows(FileNotFoundException.class, () -> DataUtil.load(nonExistentFile, null));

        // Test loading a file with an invalid charset
        String invalidCharset = "invalidCharset";
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(file, invalidCharset));
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testString = "Test content";
        InputStream testInputStream = new ByteArrayInputStream(testString.getBytes());

        // Test loading an input stream with default charset (UTF-8)
        Document docWithDefaultCharset = DataUtil.load(testInputStream, null);
        assertEquals("Test content", docWithDefaultCharset.body().text());

        // Test loading an input stream with specified charset
        String charsetName = "ISO-8859-1";
        Document docWithSpecifiedCharset = DataUtil.load(testInputStream, charsetName);
        assertEquals("Test content", docWithSpecifiedCharset.body().text());

        // Test loading an input stream with an invalid charset
        String invalidCharset = "invalidCharset";
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(testInputStream, invalidCharset));

        // Test closing the input stream
        DataUtil.load(testInputStream, null);
        assertFalse(testInputStream.isOpen());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testString = "Test content";
        InputStream testInputStream = new ByteArrayInputStream(testString.getBytes());

        // Test loading an input stream with default parser (htmlParser)
        Document docWithDefaultParser = DataUtil.load(testInputStream, null, null, Parser.htmlParser());
        assertEquals("Test content", docWithDefaultParser.body().text());

        // Test loading an input stream with a custom parser
        Parser customParser = Parser.xmlParser();
        Document docWithCustomParser = DataUtil.load(testInputStream, null, null, customParser);
        assertEquals("", docWithCustomParser.body().text());

        // Test closing the input stream
        DataUtil.load(testInputStream, null, null, Parser.htmlParser());
        assertFalse(testInputStream.isOpen());
    }

    @Test
    public void testParseByteData() {
        String testString = "Test content";
        ByteBuffer byteBuffer = ByteBuffer.wrap(testString.getBytes());

        String defaultCharset = DataUtil.defaultCharset;
        Document docWithDefaultCharset = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        assertEquals("Test content", docWithDefaultCharset.body().text());

        String charsetName = "ISO-8859-1";
        Document docWithSpecifiedCharset = DataUtil.parseByteData(byteBuffer, charsetName, null, Parser.htmlParser());
        assertEquals("Test content", docWithSpecifiedCharset.body().text());

        String invalidCharset = "invalidCharset";
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteBuffer, invalidCharset, null, Parser.htmlParser()));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        String contentTypeWithoutCharset = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentTypeWithoutCharset));

        String contentTypeWithInvalidCharset = "text/html; charset=invalidCharset";
        assertNull(DataUtil.getCharsetFromContentType(contentTypeWithInvalidCharset));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testString = "Test content";
        InputStream testInputStream = new ByteArrayInputStream(testString.getBytes());

        ByteBuffer byteBufferWithDefaultMaxSize = DataUtil.readToByteBuffer(testInputStream);
        assertEquals(testString.length(), byteBufferWithDefaultMaxSize.limit());

        ByteBuffer byteBufferWithSpecifiedMaxSize = DataUtil.readToByteBuffer(testInputStream, 10);
        assertEquals(10, byteBufferWithSpecifiedMaxSize.limit());

        ByteBuffer byteBufferWithUnlimitedMaxSize = DataUtil.readToByteBuffer(testInputStream, 0);
        assertEquals(testString.length(), byteBufferWithUnlimitedMaxSize.limit());

        byteBufferWithUnlimitedMaxSize.rewind();
        assertEquals(testString, Charset.defaultCharset().decode(byteBufferWithUnlimitedMaxSize).toString());

        testInputStream.close();
    }

    @Test
    public void testCharsetPattern() {
        String contentTypeWithUTF8Charset = "text/html; charset=UTF-8";
        String contentTypeWithGB2312Charset = "text/html; charset=gb2312";
        String contentTypeWithNoCharset = "text/html";

        Matcher m = DataUtil.charsetPattern.matcher(contentTypeWithUTF8Charset);
        assertTrue(m.find());
        assertEquals("UTF-8", m.group(1).trim());

        m = DataUtil.charsetPattern.matcher(contentTypeWithGB2312Charset);
        assertTrue(m.find());
        assertEquals("gb2312", m.group(1).trim());

        m = DataUtil.charsetPattern.matcher(contentTypeWithNoCharset);
        assertFalse(m.find());
    }
}