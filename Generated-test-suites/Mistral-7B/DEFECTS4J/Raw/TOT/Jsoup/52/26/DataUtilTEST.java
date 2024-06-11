package org.jsoup.helper.test;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    /**
     * Test loading a file with a valid charset specified in the Content-Type header.
     */
    @Test
    public void testLoadFileWithValidCharset() throws Exception {
        File inputFile = new File("test_file_with_charset.html");
        String charsetName = "ISO-8859-1";
        String baseUri = "http://test.com";

        Document doc = DataUtil.load(inputFile, charsetName, baseUri);
        // Perform assertions on the Document object
        // ...
    }

    /**
     * Test loading a file with an invalid charset specified in the Content-Type header.
     */
    @Test
    public void testLoadFileWithInvalidCharset() throws Exception {
        File inputFile = new File("test_file_with_charset.html");
        String charsetName = "XXX-1234";
        String baseUri = "http://test.com";

        try {
            Document doc = DataUtil.load(inputFile, charsetName, baseUri);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test loading a file without any charset specified in the Content-Type header or in a meta tag.
     */
    @Test
    public void testLoadFileWithoutCharset() throws Exception {
        File inputFile = new File("test_file_without_charset.html");
        String charsetName = null;
        String baseUri = "http://test.com";

        Document doc = DataUtil.load(inputFile, charsetName, baseUri);
        // Perform assertions on the Document object
        // ...
    }

    /**
     * Test loading a file with a UTF-8 Byte Order Mark (BOM).
     */
    @Test
    public void testLoadFileWithUtf8Bom() throws Exception {
        String content = "\uFEFF<!DOCTYPE html>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        byteOutput.write(content.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(byteOutput.toByteArray());
        String charsetName = null;
        String baseUri = "http://test.com";

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        // Perform assertions on the Document object
        // ...
    }

    /**
     * Test parsing input stream with a valid charset specified.
     */
    @Test
    public void testParseInputStreamWithValidCharset() throws Exception {
        String content = "<html><body><p>Hello World!</p></body></html>";
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        byteOutput.write(content.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(byteOutput.toByteArray());
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document doc = DataUtil.load(inputStream, charsetName, baseUri, DataUtil.Parser.htmlParser());
        // Perform assertions on the Document object
        // ...
    }

    /**
     * Test parsing input stream with an invalid charset specified.
     */
    @Test
    public void testParseInputStreamWithInvalidCharset() throws Exception {
        String content = "<html><body><p>Hello World!</p></body></html>";
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        byteOutput.write(content.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(byteOutput.toByteArray());
        String charsetName = "XXX-1234";
        String baseUri = "http://test.com";

        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri, DataUtil.Parser.htmlParser());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test parsing input stream without any charset specified.
     */
    @Test
    public void testParseInputStreamWithoutCharset() throws Exception {
        String content = "<html><body><p>Hello World!</p></body></html>";
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        byteOutput.write(content.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(byteOutput.toByteArray());
        String charsetName = null;
        String baseUri = "http://test.com";

        Document doc = DataUtil.load(inputStream, charsetName, baseUri, DataUtil.Parser.htmlParser());
        // Perform assertions on the Document object
        // ...
    }

    /**
     * Test parsing input stream with a UTF-8 Byte Order Mark (BOM).
     */
    @Test
    public void testParseInputStreamWithUtf8Bom() throws Exception {
        String content = "\uFEFF<html><body><p>Hello World!</p></body></html>";
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        byteOutput.write(content.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(byteOutput.toByteArray());
        String charsetName = null;
        String baseUri = "http://test.com";

        Document doc = DataUtil.load(inputStream, charsetName, baseUri, DataUtil.Parser.htmlParser());
        // Perform assertions on the Document object
        // ...
    }

    /**
     * Test writing input stream to output stream.
     */
    @Test
    public void testWriteInputStreamToOutputStream() throws Exception {
        String inputContent = "Hello World!";
        byte[] inputBytes = inputContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] outputBytes = outputStream.toByteArray();
        String outputContent = new String(outputBytes, StandardCharsets.UTF_8);

        assertEquals(inputContent, outputContent);
    }
}