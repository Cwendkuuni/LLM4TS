package org.jsoup.helper;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

/**
 * Test class for DataUtil methods.
 *
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)} method.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        File inputFile = new File("src/test/resources/DataUtilTestInput.html");
        Document document = DataUtil.load(inputFile, charsetName, baseUri);

        // Assertions
        assertNotNull(document);
        assertEquals("Test Page", document.title());
        // Add more assertions here as needed
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)} method.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        InputStream inputStream = getClass().getResourceAsStream("/DataUtilTestInput.html");
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Assertions
        assertNotNull(document);
        assertEquals("Test Page", document.title());
        // Add more assertions here as needed

        // Close input stream
        inputStream.close();
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)} method.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        InputStream inputStream = getClass().getResourceAsStream("/DataUtilTestInput.xml");
        Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());

        // Assertions
        assertNotNull(document);
        // Add more assertions here as needed

        // Close input stream
        inputStream.close();
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} method.
     */
    @Test
    public void testParseByteData() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        // Prepare test data
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "<element1>Test Element 1</element1>\n" +
                "<element2>Test Element 2</element2>\n" +
                "</root>";

        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.xmlParser());

        // Assertions
        assertNotNull(document);
        assertEquals("root", document.tagName());
        assertEquals(2, document.childNodeSize());

        Element root = document.child(0);
        assertEquals("element1", root.tagName());
        assertEquals("Test Element 1", root.text());

        Element element2 = document.child(1);
        assertEquals("element2", element2.tagName());
        assertEquals("Test Element 2", element2.text());
    }

    /**
     * Test method for {@link DataUtil#crossStreams(InputStream, OutputStream)} method.
     * @throws IOException on IO error
     */
    @Test
    public void testCrossStreams() throws IOException {
        String testData = "Test Data";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] outputData = outputStream.toByteArray();
        assertEquals(testData, new String(outputData, StandardCharsets.UTF_8));

        // Close input and output streams
        inputStream.close();
        outputStream.close();
    }
}