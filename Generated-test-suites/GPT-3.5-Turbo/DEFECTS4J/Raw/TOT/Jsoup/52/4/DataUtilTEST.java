import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class DataUtilTest {

    private static Document htmlDocument;
    private static Document xmlDocument;

    @BeforeClass
    public static void setUp() throws Exception {
        File htmlFile = new File("test.html");
        File xmlFile = new File("test.xml");

        String htmlContent = "<html><head><title>Test HTML</title></head><body><h1>Test</h1></body></html>";
        String xmlContent = "<root><element>Test XML</element></root>";

        htmlDocument = Parser.htmlParser().parseInput(htmlContent, "http://example.com");
        xmlDocument = Parser.xmlParser().parseInput(xmlContent, "http://example.com");

        DataUtil.writeFile(htmlFile, htmlContent);
        DataUtil.writeFile(xmlFile, xmlContent);
    }

    // Test load() method with File parameter
    @Test
    public void testLoadFile() {
        try {
            Document loadedDocument = DataUtil.load(new File("test.html"), "UTF-8", "http://example.com");

            assertEquals(htmlDocument.html(), loadedDocument.html());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    // Test load() method with InputStream parameter
    @Test
    public void testLoadInputStream() {
        try {
            InputStream inputStream = new ByteArrayInputStream("<html><body><h1>Test</h1></body></html>".getBytes(StandardCharsets.UTF_8));
            Document loadedDocument = DataUtil.load(inputStream, "UTF-8", "http://example.com");

            assertEquals(htmlDocument.html(), loadedDocument.html());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    // Test load() method with InputStream and Parser parameters
    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream inputStream = new ByteArrayInputStream("<root><element>Test XML</element></root>".getBytes(StandardCharsets.UTF_8));
            Document loadedDocument = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());

            assertEquals(xmlDocument.html(), loadedDocument.html());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    // Test crossStreams() method
    @Test
    public void testCrossStreams() {
        try {
            InputStream inputStream = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            DataUtil.crossStreams(inputStream, outputStream);

            String output = outputStream.toString(StandardCharsets.UTF_8.name());

            assertEquals("Test", output);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    // Test parseByteData() method
    @Test
    public void testParseByteData() {
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));

        Document parsedDocument = DataUtil.parseByteData(byteBuffer, "UTF-8", "http://example.com", Parser.htmlParser());

        assertEquals(htmlDocument.html(), parsedDocument.html());
    }

    // Additional test cases can be added for other methods in DataUtil class

}