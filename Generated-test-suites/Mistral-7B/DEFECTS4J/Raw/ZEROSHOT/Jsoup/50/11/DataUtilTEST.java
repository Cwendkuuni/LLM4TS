package org.jsoup.helper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import org.junit.Test;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     */
    @Test
    public void testLoadFile() throws IOException {
        String baseUri = "http://example.com";
        File inputFile = new File("src/test/resources/test.html");
        Document expectedDoc = new Parser().parseInput(new File("src/test/resources/test.html"), baseUri);

        Document actualDoc = DataUtil.load(inputFile, "UTF-8", baseUri);
        assertNotNull(actualDoc);
        assertTrue(actualDoc.titleText().equals(expectedDoc.titleText()));
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String baseUri = "http://example.com";
        String inputData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<title>Expected Title</title>\n</head>\n<body>\n<p>Expected Content</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));

        Document expectedDoc = new Parser().parseInput(new InputStreamReader(inputStream, StandardCharsets.UTF_8), baseUri);

        Document actualDoc = DataUtil.load(inputStream, "UTF-8", baseUri);
        assertNotNull(actualDoc);
        assertTrue(actualDoc.titleText().equals(expectedDoc.titleText()));
        assertTrue(actualDoc.body().text().equals(expectedDoc.body().text()));

        inputStream.close();
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     */
    @Test
    public void testParseByteData() throws IOException {
        String baseUri = "http://example.com";
        String charsetName = "UTF-8";
        String inputData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<title>Expected Title</title>\n</head>\n<body>\n<p>Expected Content</p>\n</body>\n</html>";
        ByteBuffer byteData = ByteBuffer.wrap(inputData.getBytes(StandardCharsets.UTF_8));

        Document expectedDoc = new Parser().parseInput(new InputStreamReader(new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8), baseUri);

        Document actualDoc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(actualDoc);
        assertTrue(actualDoc.titleText().equals(expectedDoc.titleText()));
        assertTrue(actualDoc.body().text().equals(expectedDoc.body().text()));
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     * With invalid charset.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataInvalidCharset() throws IOException {
        String baseUri = "http://example.com";
        String charsetName = "INVALID_CHARSET";
        String inputData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<title>Expected Title</title>\n</head>\n<body>\n<p>Expected Content</p>\n</body>\n</html>";
        ByteBuffer byteData = ByteBuffer.wrap(inputData.getBytes(StandardCharsets.UTF_8));

        DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, actualCharset);
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
     * With invalid charset.
     */
    @Test
    public void testGetCharsetFromContentTypeInvalidCharset() {
        String contentType = "text/html; charset=INVALID_CHARSET";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    /**
     * Test method for {@link DataUtil#mimeBoundary()}.
     */
    @Test
    public void testMimeBoundary() {
        String expectedBoundary = "---01d401d401d401d401d401d401d401d4-";
        String actualBoundary = DataUtil.mimeBoundary();
        assertEquals(expectedBoundary, actualBoundary);
    }
}