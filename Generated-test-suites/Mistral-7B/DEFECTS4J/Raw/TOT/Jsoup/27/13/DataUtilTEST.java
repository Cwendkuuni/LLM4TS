package org.jsoup.helper;

import org.junit.Test;
import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.parser.Parser.MalformedHtmlException;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Test class for {@link DataUtil} class
 */
public class DataUtilTest {

    /**
     * Test loading a file using default charset and base URI
     */
    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test.html");
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";

        Document actualDocument = DataUtil.load(testFile, null, baseUri);

        assertEquals("Loaded document should not be null", actualDocument, notNull(actualDocument));
        assertEquals("Document charset should match expected", expectedCharset, actualDocument.outputSettings().charset());

        // Add more assertions to check the actual content of the document
    }

    /**
     * Test loading a file with a specified charset and base URI
     */
    @Test
    public void testLoadFileWithCharset() throws IOException {
        File testFile = new File("test.html");
        String charset = "ISO-8859-1";
        String baseUri = "http://example.com";

        Document actualDocument = DataUtil.load(testFile, charset, baseUri);

        assertEquals("Loaded document should not be null", actualDocument, notNull(actualDocument));
        assertEquals("Document charset should match specified", charset, actualDocument.outputSettings().charset());

        // Add more assertions to check the actual content of the document
    }

    /**
     * Test loading a file with no specified charset
     */
    @Test
     public void testLoadFileNoCharset() throws IOException {
         File testFile = new File("test.html");
         String baseUri = "http://example.com";

         Document actualDocument = DataUtil.load(testFile, null, baseUri);

         assertEquals("Loaded document should not be null", actualDocument, notNull(actualDocument));
         // Add more assertions to check the actual content of the document and the detected charset
     }

    /**
     * Test loading an InputStream with default charset and base URI
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<meta charset=\"UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<p>Test content.</p>\n</body>\n</html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testData.getBytes(Charset.forName("UTF-8")));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String baseUri = "http://example.com";

        Document actualDocument = DataUtil.load(inputStream, null, baseUri);

        assertEquals("Loaded document should not be null", actualDocument, notNull(actualDocument));
        assertEquals("Document charset should match expected", "UTF-8", actualDocument.outputSettings().charset());

        // Add more assertions to check the actual content of the document
    }

    /**
     * Test loading an InputStream with a specified charset and base URI
     */
    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        String testData = "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<meta charset=\"ISO-8859-1\">\n<title>Test</title>\n</head>\n<body>\n<p>Test content.</p>\n</body>\n</html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testData.getBytes(Charset.forName("ISO-8859-1")));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String baseUri = "http://example.com";

        Document actualDocument = DataUtil.load(inputStream, "ISO-8859-1", baseUri);

        assertEquals("Loaded document should not be null", actualDocument, notNull(actualDocument));
        assertEquals("Document charset should match specified", "ISO-8859-1", actualDocument.outputSettings().charset());

        // Add more assertions to check the actual content of the document
    }

    /**
     * Test loading an InputStream with no specified charset
     */
    @Test
    public void testLoadInputStreamNoCharset() throws IOException {
        String testData = "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<meta charset=\"UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<p>Test content.</p>\n</body>\n</html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testData.getBytes(Charset.forName("UTF-8")));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String baseUri = "http://example.com";

        Document actualDocument = DataUtil.load(inputStream, null, baseUri);

        assertEquals("Loaded document should not be null", actualDocument, notNull(actualDocument));
        // Add more assertions to check the actual content of the document and the detected charset
    }

    /**
     * Test loading InputStream with a malformed HTML input
     */
    @Test(expected = MalformedHtmlException.class)
    public void testLoadInputStreamMalformedHtml() throws IOException {
        String testData = "<html lang=\"en\">\n<head>\n<title>Test</title>\n<body>\n<p>Test content.</p>\n<script>alert('Hello, world!');</script>\n</body>\n</html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testData.getBytes(Charset.forName("UTF-8")));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String baseUri = "http://example.com";

        DataUtil.load(inputStream, null, baseUri, Parser.htmlParser());
    }

    /**
     * Test loading InputStream with a malformed HTML input and a different parser
     */
    @Test(expected = org.jsoup.parser.Parser.MalformedHtmlException.class)
    public void testLoadInputStreamMalformedHtmlWithXmlParser() throws IOException {
        String testData = "<html lang=\"en\">\n<head>\n<title>Test</title>\n<body>\n<p>Test content.</p>\n<script>alert('Hello, world!');</script>\n</body>\n</html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testData.getBytes(Charset.forName("UTF-8")));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String baseUri = "http://example.com";

        DataUtil.load(inputStream, null, baseUri, Parser.xmlParser());
    }

    // Add more test cases if necessary

    /**
     * Helper method to assert that an object is not null
     * @param object The object to assert
     * @return The object itself for method chaining
     */
    private Object notNull(Object object) {
        Assert.assertNotNull(object);
        return object;
    }
}