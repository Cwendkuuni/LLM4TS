package org.jsoup.helper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    private Document doc;

    @Before
    public void setUp() throws Exception {
        // Set up any initialization here.
    }

    @After
    public void tearDown() throws Exception {
        // Release any allocated resources here.
        if (doc != null) {
            doc.close();
        }
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("path/to/your/test/file.html"); // Replace with the actual path to your test file.
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);

        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidPath() throws IOException {
        File file = new File("path/to/non/existent/file.html"); // Replace with the actual path to a non-existent file.
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "Your HTML content here";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(document);
        assertEquals(htmlContent, document.html());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "Your HTML content here";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(htmlContent, document.html());
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "Your HTML content here";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "undefined"; // Replace with an invalid charset name.
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("path/to/input/file.html"); // Replace with the actual path to the input file.
        File outputFile = new File("path/to/output/file.html"); // Replace with the actual path to the output file.

        DataUtil.crossStreams(new FileInputStream(inputFile), new FileOutputStream(outputFile));

        File inputFileVerification = new File("path/to/input/file.html"); // Verify input file is closed.
        File outputFileVerification = new File("path/to/output/file.html"); // Verify output file is created.

        Assert.assertFalse(inputFileVerification.exists());
        Assert.assertTrue(outputFileVerification.exists());
    }

    @Test
    public void testInvalidInputStream() {
        InputStream inputStream = null;
        OutputStream outputStream = new ByteArrayOutputStream();

        try {
            DataUtil.crossStreams(inputStream, outputStream);
            Assert.fail();
        } catch (IOException e) {
            // Expected outcome
        }
    }
}