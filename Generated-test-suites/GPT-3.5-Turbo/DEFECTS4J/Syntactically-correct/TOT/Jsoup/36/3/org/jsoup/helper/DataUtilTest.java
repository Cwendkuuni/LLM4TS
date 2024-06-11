package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.*;

class DataUtilTest {
    private File testFile;
    private String baseUri;

    @BeforeEach
    void setUp() {
        testFile = new File("test.html");
        baseUri = "http://example.com";
    }

    @Test
    void load_shouldReturnDocument_whenValidFileAndCharsetAndBaseUriProvided() {
        try {
            Document doc = DataUtil.load(testFile, "UTF-8", baseUri);
            assertNotNull(doc);
            assertTrue(doc instanceof Document);
            assertEquals(baseUri, doc.baseUri());
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    void load_shouldThrowIOException_whenInvalidFileProvided() {
        assertThrows(IOException.class, () -> {
            File invalidFile = new File("invalid.html");
            DataUtil.load(invalidFile, "UTF-8", baseUri);
        });
    }

    @Test
    void load_shouldThrowIOException_whenNullCharsetProvided() {
        assertThrows(IOException.class, () -> {
            DataUtil.load(testFile, null, baseUri);
        });
    }

    @Test
    void load_shouldReturnDocument_whenValidInputStreamAndCharsetAndBaseUriProvided() {
        try {
            FileInputStream inStream = new FileInputStream(testFile);
            Document doc = DataUtil.load(inStream, "UTF-8", baseUri);
            assertNotNull(doc);
            assertTrue(doc instanceof Document);
            assertEquals(baseUri, doc.baseUri());
            inStream.close();
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    void load_shouldThrowIOException_whenInvalidInputStreamProvided() {
        assertThrows(IOException.class, () -> {
            InputStream invalidInputStream = new FileInputStream("invalid.html");
            DataUtil.load(invalidInputStream, "UTF-8", baseUri);
        });
    }

    @Test
    void load_shouldReturnDocument_whenValidInputStreamAndCharsetAndBaseUriAndParserProvided() {
        try {
            FileInputStream inStream = new FileInputStream(testFile);
            Document doc = DataUtil.load(inStream, "UTF-8", baseUri, Parser.htmlParser());
            assertNotNull(doc);
            assertTrue(doc instanceof Document);
            assertEquals(baseUri, doc.baseUri());
            inStream.close();
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    void load_shouldThrowIOException_whenInvalidInputStreamAndParserProvided() {
        assertThrows(IOException.class, () -> {
            InputStream invalidInputStream = new FileInputStream("invalid.html");
            DataUtil.load(invalidInputStream, "UTF-8", baseUri, Parser.htmlParser());
        });
    }

    @Test
    void parseByteData_shouldReturnDocument_whenValidByteDataAndCharsetAndBaseUriAndParserProvided() {
        ByteBuffer byteData = null;
        try {
            FileInputStream inStream = new FileInputStream(testFile);
            byteData = DataUtil.readToByteBuffer(inStream);
            inStream.close();
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }

        Document doc = DataUtil.parseByteData(byteData, "UTF-8", baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertTrue(doc instanceof Document);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    void readToByteBuffer_shouldReturnByteBuffer_whenValidInputStreamProvided() {
        try {
            FileInputStream inStream = new FileInputStream(testFile);
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream);
            assertNotNull(byteBuffer);
            assertTrue(byteBuffer instanceof ByteBuffer);
            inStream.close();
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    void readToByteBuffer_shouldThrowIOException_whenInvalidInputStreamProvided() {
        assertThrows(IOException.class, () -> {
            InputStream invalidInputStream = new FileInputStream("invalid.html");
            DataUtil.readToByteBuffer(invalidInputStream);
        });
    }

    @Test
    void getCharsetFromContentType_shouldReturnCharset_whenValidContentTypeProvided() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    void getCharsetFromContentType_shouldReturnNull_whenInvalidContentTypeProvided() {
        String contentType = "text/html; charset=invalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    void getCharsetFromContentType_shouldReturnNull_whenNullContentTypeProvided() {
        String charset = DataUtil.getCharsetFromContentType(null);
        assertNull(charset);
    }
}