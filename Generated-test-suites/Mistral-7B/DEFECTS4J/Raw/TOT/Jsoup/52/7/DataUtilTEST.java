package org.jsoup.helper.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

public class DataUtilTest {

    private Document htmlDocument;
    private Document xmlDocument;

    @Before
    public void setUp() {
        String htmlSource = "<html><head></head><body></body></html>";
        htmlDocument = DataUtil.load(new ByteArrayInputStream(htmlSource.getBytes()), "UTF-8", null);

        String xmlSource = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root></root>";
        xmlDocument = DataUtil.load(new ByteArrayInputStream(xmlSource.getBytes()), "UTF-8", null, Parser.xmlParser());
    }

    @Test
    public void testLoadHtmlFileWithValidCharset() throws IOException {
        File htmlFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "/";
        Document document = DataUtil.load(htmlFile, charsetName, baseUri);
        assertEquals("Loaded HTML document", htmlDocument, document);
    }

    @Test
    public void testLoadHtmlFileWithInvalidCharset() throws IOException {
        File htmlFile = new File("test_invalid_charset.html");
        String charsetName = "invalid_charset";
        String baseUri = "/";
        Document document = null;
        try {
            document = DataUtil.load(htmlFile, charsetName, baseUri);
            fail("Expected IOException for invalid charset");
        } catch (IOException e) {
            // Expected
        }
        assertNull("Document should be null for invalid charset", document);
    }

    @Test
    public void testLoadXmlFileWithValidCharset() throws IOException {
        File xmlFile = new File("test.xml");
        String charsetName = "UTF-8";
        String baseUri = "/";
        Document document = DataUtil.load(xmlFile, charsetName, baseUri, Parser.xmlParser());
        assertEquals("Loaded XML document", xmlDocument, document);
    }

    @Test
    public void testLoadXmlFileWithInvalidCharset() throws IOException {
        File xmlFile = new File("test_invalid_charset.xml");
        String charsetName = "invalid_charset";
        String baseUri = "/";
        Document document = null;
        try {
            document = DataUtil.load(xmlFile, charsetName, baseUri, Parser.xmlParser());
            fail("Expected IOException for invalid charset");
        } catch (IOException e) {
            // Expected
        }
        assertNull("Document should be null for invalid charset", document);
    }

    @Test
    public void testParseByteDataWithValidCharset() throws IOException {
        String htmlSource = "<html><head></head><body></body></html>";
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(htmlSource.getBytes()));
        String charsetName = "UTF-8";
        String baseUri = null;
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals("Parsed HTML document from byte data", htmlDocument, document);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String htmlSource = "<html><head></head><body></body></html>";
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(htmlSource.getBytes()));
        String charsetName = "invalid_charset";
        String baseUri = null;
        Parser parser = Parser.htmlParser();
        Document document = null;
        try {
            document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
            fail("Expected IOException for invalid charset");
        } catch (IOException e) {
            // Expected
        }
        assertNull("Document should be null for invalid charset", document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        String result = outputStream.toString();
        assertEquals("Cross-streamed data", "Test", result);
    }
}