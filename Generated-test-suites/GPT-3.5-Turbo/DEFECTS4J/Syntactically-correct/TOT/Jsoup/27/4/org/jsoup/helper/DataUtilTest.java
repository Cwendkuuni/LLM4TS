package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DataUtilTest {

    private File testFile;
    private File nonexistentFile;
    private InputStream testInputStream;

    @BeforeEach
    public void setUp() {
        testFile = new File("test.html");
        nonexistentFile = new File("nonexistent.html");

        testInputStream = mock(InputStream.class);
        try {
            when(testInputStream.read(any())).thenReturn(-1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test case for load(File in, String charsetName, String baseUri) method.
     * Test when the input file exists and a valid charset is provided.
     */
    @Test
    public void testLoadFromFileValidCharset() {
        try {
            Document document = DataUtil.load(testFile, "UTF-8", "http://example.com");
            assertNotNull(document);
            assertEquals("http://example.com", document.baseUri());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    /**
     * Test case for load(File in, String charsetName, String baseUri) method.
     * Test when the input file does not exist.
     */
    @Test
    public void testLoadFromFileNonexistentFile() {
        assertThrows(IOException.class, () -> {
            DataUtil.load(nonexistentFile, "UTF-8", "http://example.com");
        });
    }

    /**
     * Test case for load(InputStream in, String charsetName, String baseUri) method.
     * Test when the input stream is empty.
     */
    @Test
    public void testLoadFromEmptyInputStream() {
        assertThrows(IOException.class, () -> {
            DataUtil.load(testInputStream, "UTF-8", "http://example.com");
        });
    }

    /**
     * Test case for load(InputStream in, String charsetName, String baseUri) method.
     * Test when the input stream is not empty.
     */
    @Test
    public void testLoadFromInputStream() {
        try {
            Document document = DataUtil.load(testInputStream, "UTF-8", "http://example.com");
            assertNotNull(document);
            assertEquals("http://example.com", document.baseUri());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    /**
     * Test case for load(InputStream in, String charsetName, String baseUri, Parser parser) method.
     * Test when the input stream is empty and a custom parser is used.
     */
    @Test
    public void testLoadFromEmptyInputStreamWithCustomParser() {
        Parser parser = mock(Parser.class);
        when(parser.parseInput(anyString(), anyString())).thenReturn(null);

        assertThrows(IOException.class, () -> {
            DataUtil.load(testInputStream, "UTF-8", "http://example.com", parser);
        });

        verify(parser, never()).parseInput(anyString(), anyString());
    }

    /**
     * Test case for load(InputStream in, String charsetName, String baseUri, Parser parser) method.
     * Test when the input stream is not empty and a custom parser is used.
     */
    @Test
    public void testLoadFromInputStreamWithCustomParser() {
        Parser parser = mock(Parser.class);
        when(parser.parseInput(anyString(), anyString())).thenReturn(new Document("http://example.com"));

        try {
            Document document = DataUtil.load(testInputStream, "UTF-8", "http://example.com", parser);
            assertNotNull(document);
            assertEquals("http://example.com", document.baseUri());
        } catch (IOException e) {
            fail("IOException occurred");
        }

        verify(parser, times(1)).parseInput(anyString(), anyString());
    }

    /**
     * Test case for parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser) method.
     * Test when charsetName is null and a valid meta charset is present in the document.
     */
    @Test
    public void testParseByteDataWithMetaCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("<meta charset=\"UTF-16\"></meta>".getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseByteData(byteData, null, "http://example.com", Parser.htmlParser());

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    /**
     * Test case for parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser) method.
     * Test when charsetName is null and a valid content-type http-equiv tag is present in the document.
     */
    @Test
    public void testParseByteDataWithHttpEquivContentType() {
        ByteBuffer byteData = ByteBuffer.wrap("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-16\">".getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseByteData(byteData, null, "http://example.com", Parser.htmlParser());

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    /**
     * Test case for parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser) method.
     * Test when charsetName is null and both meta charset and content-type http-equiv tags are present in the document.
     */
    @Test
    public void testParseByteDataWithBothCharsets() {
        ByteBuffer byteData = ByteBuffer.wrap("<meta charset=\"UTF-16\"><meta http-equiv=\"Content-Type\" content=\"text/html;charset=ISO-8859-1\">".getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseByteData(byteData, null, "http://example.com", Parser.htmlParser());

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    /**
     * Test case for parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser) method.
     * Test when charsetName is not null and a valid charset is provided.
     */
    @Test
    public void testParseByteDataWithValidCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("<html></html>".getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseByteData(byteData, "UTF-8", "http://example.com", Parser.htmlParser());

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    /**
     * Test case for parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser) method.
     * Test when charsetName is not null and an unsupported charset is provided.
     */
    @Test
    public void testParseByteDataWithUnsupportedCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("<html></html>".getBytes(StandardCharsets.UTF_8));

        assertThrows(IllegalArgumentException.class, () -> {
            DataUtil.parseByteData(byteData, "UNSUPPORTED", "http://example.com", Parser.htmlParser());
        });
    }

    /**
     * Test case for readToByteBuffer(InputStream inStream) method.
     * Test when the input stream is empty.
     */
    @Test
    public void testReadToByteBufferWithEmptyInputStream() {
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream);
            assertEquals(0, byteBuffer.remaining());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    /**
     * Test case for readToByteBuffer(InputStream inStream) method.
     * Test when the input stream is not empty.
     */
    @Test
    public void testReadToByteBufferWithNonEmptyInputStream() {
        try {
            FileInputStream fileInputStream = new FileInputStream(testFile);
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(fileInputStream);
            assertEquals(testFile.length(), byteBuffer.remaining());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    /**
     * Test case for getCharsetFromContentType(String contentType) method.
     * Test when a valid charset is present in the content type.
     */
    @Test
    public void testGetCharsetFromContentTypeWithValidCharset() {
        String contentType = "text/html; charset=UTF-16";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-16", charset);
    }

    /**
     * Test case for getCharsetFromContentType(String contentType) method.
     * Test when an invalid charset is present in the content type.
     */
    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=INVALID";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    /**
     * Test case for getCharsetFromContentType(String contentType) method.
     * Test when the content type is null.
     */
    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String contentType = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

}