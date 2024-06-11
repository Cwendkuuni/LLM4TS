import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Objects;

import static org.junit.Assert.*;

public class W3CDomTest {
    private W3CDom w3CDom;
    private DocumentBuilder documentBuilder;

    @Before
    public void setUp() throws Exception {
        w3CDom = new W3CDom();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        documentBuilder = factory.newDocumentBuilder();
    }

    @Test(expected = NullPointerException.class)
    public void testFromJsoup_withNullInput() {
        // Arrange
        org.jsoup.nodes.Document input = null;

        // Act
        Document output = w3CDom.fromJsoup(input);
    }

    @Test
    public void testFromJsoup_withValidInput() throws Exception {
        // Arrange
        org.jsoup.nodes.Document input = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");

        // Act
        Document output = w3CDom.fromJsoup(input);

        // Assert
        assertNotNull(output);
        assertEquals("Test", output.getElementsByTagName("title").item(0).getTextContent());
    }

    @Test(expected = NullPointerException.class)
    public void testConvert_withNullInput() {
        // Arrange
        org.jsoup.nodes.Document in = null;
        Document out = documentBuilder.newDocument();

        // Act
        w3CDom.convert(in, out);
    }

    @Test
    public void testConvert_withValidInput() throws Exception {
        // Arrange
        org.jsoup.nodes.Document in = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
        Document out = documentBuilder.newDocument();

        // Act
        w3CDom.convert(in, out);

        // Assert
        assertNotNull(out);
        assertEquals("Test", out.getElementsByTagName("title").item(0).getTextContent());
    }

    @Test(expected = NullPointerException.class)
    public void testAsString_withNullInput() {
        // Arrange
        Document doc = null;

        // Act
        String output = w3CDom.asString(doc);
    }

    @Test
    public void testAsString_withValidInput() throws Exception {
        // Arrange
        Document doc = documentBuilder.parse(new InputSource(new StringReader("<root><element>Test</element></root>")));

        // Act
        String output = w3CDom.asString(doc);

        // Assert
        assertNotNull(output);
        assertTrue(output.contains("Test"));
    }
}