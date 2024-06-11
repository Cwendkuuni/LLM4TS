package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

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

public class DataUtilTest {
    @Test
    public void testParseByteData() throws IOException {
        // Test parsing with default charset
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ByteArrayInputStream is = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.parseByteData(ByteBuffer.wrap(is.readAllBytes()), null, null, Parser.htmlParser());
        assertEquals("Test", doc.text());

        // Test parsing with specified charset
        Charset utf16 = Charset.forName("UTF-16");
        String htmlUtf16 = new String(html.getBytes("UTF-8"), utf16);
        ByteArrayOutputStream osUtf16 = new ByteArrayOutputStream();
        ByteArrayInputStream isUtf16 = new ByteArrayInputStream(htmlUtf16.getBytes());
        Document docUtf16 = DataUtil.parseByteData(ByteBuffer.wrap(isUtf16.readAllBytes()), "UTF-16", null, Parser.htmlParser());
        assertEquals("Test", docUtf16.text());

        // Test parsing with detected charset
        String htmlWithMetaTag = "<html><head><meta charset=\"UTF-16\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><body>Test</body></html>";
        ByteArrayOutputStream osWithMetaTag = new ByteArrayOutputStream();
        ByteArrayInputStream isWithMetaTag = new ByteArrayInputStream(htmlWithMetaTag.getBytes());
        Document docWithMetaTag = DataUtil.parseByteData(ByteBuffer.wrap(isWithMetaTag.readAllBytes()), null, null, Parser.htmlParser());
        assertEquals("Test", docWithMetaTag.text());
        assertEquals("UTF-16", docWithMetaTag.outputSettings().charset());

        // Test parsing with unsupported charset
        String htmlUtfEbcdic = new String(html.getBytes("UTF-8"), Charset.forName("UTF-7")); // Unsupported charset
        ByteArrayOutputStream osUtfEbcdic = new ByteArrayOutputStream();
        ByteArrayInputStream isUtfEbcdic = new ByteArrayInputStream(htmlUtfEbcdic.getBytes());
        try {
            Document docUtfEbcdic = DataUtil.parseByteData(ByteBuffer.wrap(isUtfEbcdic.readAllBytes()), null, null, Parser.htmlParser());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLoad() throws IOException {
        String testFilePath = "testFile.html";
        File testFile = new File(testFilePath);
        String testHtml = "<html><body>Test</body></html>";
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write(testHtml);
        fileWriter.close();

        // Test loading file with default charset
        Document docDefault = DataUtil.load(testFile, null, null);
        assertEquals("Test", docDefault.text());

        // Test loading file with specified charset
        Charset utf16 = Charset.forName("UTF-16");
        Document docUtf16 = DataUtil.load(testFile, "UTF-16", null);
        assertEquals("Test", docUtf16.text());

        // Test loading file with detected charset
        File tempFile = new File(testFilePath + ".temp");
        FileWriter fileWriterTemp = new FileWriter(tempFile);
        fileWriterTemp.write("<html><head><meta charset=\"UTF-16\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><body>Test</body></html>");
        fileWriterTemp.close();
        Document docWithMetaTag = DataUtil.load(new File(testFilePath + ".temp"), null, null);
        assertEquals("Test", docWithMetaTag.text());
        assertEquals("UTF-16", docWithMetaTag.outputSettings().charset());

        // Test loading file with unsupported charset in Content-Type header
        File tempFileUtfEbcdic = new File(testFilePath + ".temp");
        FileWriter fileWriterTempUtfEbcdic = new FileWriter(tempFileUtfEbcdic);
        fileWriterTempUtfEbcdic.write("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-7\"><body>Test</body></html>");
        fileWriterTempUtfEbcdic.close();
        try {
            Document docUtfEbcdic = DataUtil.load(new File(testFilePath + ".temp"), null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Clean up temporary files
        testFile.delete();
        tempFile.delete();
        tempFileUtfEbcdic.delete();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testString = "Test";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        FileInputStream is = new FileInputStream(new File("testFile.txt"));

        // Test reading byte buffer with maxSize
        ByteBuffer byteData = DataUtil.readToByteBuffer(is, 10);
        byte[] expectedBytes = testString.getBytes();
        byte[] actualBytes = new byte[expectedBytes.length];
        System.arraycopy(expectedBytes, 0, actualBytes, 0, expectedBytes.length);
        assertArrayEquals(actualBytes, byteData.array());

        // Test reading byte buffer without maxSize
        ByteBuffer byteDataNoMaxSize = DataUtil.readToByteBuffer(is);
        byte[] expectedBytesNoMaxSize = testString.getBytes();
        byte[] actualBytesNoMaxSize = new byte[expectedBytesNoMaxSize.length];
        System.arraycopy(expectedBytesNoMaxSize, 0, actualBytesNoMaxSize, 0, expectedBytesNoMaxSize.length);
        assertArrayEquals(actualBytesNoMaxSize, byteDataNoMaxSize.array());

        // Test reading byte buffer with maxSize smaller than input
        ByteBuffer byteDataSmall = DataUtil.readToByteBuffer(is, 5);
        byte[] expectedBytesSmall = new byte[5];
        System.arraycopy(testString.getBytes(), 0, expectedBytesSmall, 0, 5);
        assertArrayEquals(expectedBytesSmall, byteDataSmall.array());

        // Test reading byte buffer with maxSize equal to input
        ByteBuffer byteDataEqual = DataUtil.readToByteBuffer(is, testString.length());
        byte[] expectedBytesEqual = testString.getBytes();
        assertArrayEquals(expectedBytesEqual, byteDataEqual.array());

        // Test reading byte buffer with maxSize larger than input
        ByteBuffer byteDataLarge = DataUtil.readToByteBuffer(is, testString.length() + 1);
        byte[] expectedBytesLarge = testString.getBytes();
        assertArrayEquals(expectedBytesLarge, byteDataLarge.array());

        // Clean up input stream
        is.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test getting charset from content type with valid charset
        String contentTypeValid = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentTypeValid));

        // Test getting charset from content type with invalid charset
        String contentTypeInvalid = "text/html; charset=UTF-7"; // Unsupported charset
        assertNull(DataUtil.getCharsetFromContentType(contentTypeInvalid));

        // Test getting charset from content type with no charset specified
        String contentTypeNoCharset = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentTypeNoCharset));
    }
}