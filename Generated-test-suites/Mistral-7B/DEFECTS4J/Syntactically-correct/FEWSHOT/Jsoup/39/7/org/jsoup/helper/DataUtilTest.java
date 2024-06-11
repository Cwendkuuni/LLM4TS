package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoad() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Test loading from file
        File file = new File("testData.html");
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);

        // Test loading from InputStream
        InputStream inputStream = new FileInputStream(file);
        document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);

        // Test loading with null charsetName
        document = DataUtil.load(inputStream, null, baseUri);
        assertNotNull(document);

        // Test loading with null baseUri
        document = DataUtil.load(inputStream, charsetName, null);
        assertNotNull(document);

        // Test loading with null inputStream
        document = DataUtil.load(null, charsetName, baseUri);
        assertNull(document);

        // Test loading with null charsetName and inputStream
        document = DataUtil.load(null, null, baseUri);
        assertNull(document);

        // Test loading with null baseUri and inputStream
        document = DataUtil.load(inputStream, charsetName, null);
        assertNotNull(document);
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = new ByteArrayInputStream("".getBytes(charsetName));

        // Test with null document
        Document document = null;
        Document result = DataUtil.parseByteData(ByteBuffer.wrap(inputStream.readAllBytes()), null, baseUri, Parser.htmlParser());
        assertNotNull(result);
        document = result;

        // Test with null charsetName
        result = DataUtil.parseByteData(ByteBuffer.wrap(inputStream.readAllBytes()), charsetName, baseUri, Parser.htmlParser());
        assertSame(document, result);

        // Test with null baseUri
        result = DataUtil.parseByteData(ByteBuffer.wrap(inputStream.readAllBytes()), charsetName, null, Parser.htmlParser());
        assertSame(document, result);

        // Test with null parser
        result = DataUtil.parseByteData(ByteBuffer.wrap(inputStream.readAllBytes()), charsetName, baseUri, null);
        assertSame(document, result);

        // Test with invalid charsetName
        charsetName = "INVALID_CHARSET";
        result = DataUtil.parseByteData(ByteBuffer.wrap(inputStream.readAllBytes()), charsetName, baseUri, Parser.htmlParser());
        assertSame(document, result);

        // Test with charsetName found in content type
        inputStream = new ByteArrayInputStream("text/html; charset=UTF-8".getBytes(charsetName));
        result = DataUtil.parseByteData(ByteBuffer.wrap(inputStream.readAllBytes()), null, baseUri, Parser.htmlParser());
        assertSame(document, result);

        // Test with charsetName found in meta tag
        inputStream = new ByteArrayInputStream("<html><meta charset=\"UTF-16\"><body><h1>Test</h1></body></html>".getBytes(charsetName));
        result = DataUtil.parseByteData(ByteBuffer.wrap(inputStream.readAllBytes()), null, baseUri, Parser.htmlParser());
        assertSame(document, result);

        // Test with UTF-8 BOM
        inputStream = new ByteArrayInputStream(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF, "Test".getBytes(charsetName) });
        result = DataUtil.parseByteData(ByteBuffer.wrap(inputStream.readAllBytes()), null, baseUri, Parser.htmlParser());
        assertSame(document, result);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes("UTF-8"));

        // Test with maxSize > 0
        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream, 10);
        byte[] expectedData = "Test".getBytes("UTF-8");
        byte[] actualData = new byte[buffer.limit()];
        buffer.get(actualData);
        assertArrayEquals(expectedData, actualData);

        // Test with maxSize = 0
        buffer = DataUtil.readToByteBuffer(inputStream);
        expectedData = "Test".getBytes("UTF-8");
        actualData = new byte[buffer.limit()];
        buffer.get(actualData);
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String result = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", result);

        contentType = "text/html";
        result = DataUtil.getCharsetFromContentType(contentType);
        assertNull(result);
    }

    private void assertArrayEquals(byte[] expected, byte[] actual) {
        if (!java.util.Arrays.equals(expected, actual)) {
            throw new AssertionError("Expected: <" + new String(expected) + ">, but was: <" + new String(actual) + ">");
        }
    }
}