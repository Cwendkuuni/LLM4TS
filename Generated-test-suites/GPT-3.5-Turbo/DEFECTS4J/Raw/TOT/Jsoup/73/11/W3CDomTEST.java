import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class W3CDomTest {
    private static DocumentBuilder documentBuilder;

    @BeforeClass
    public static void setUp() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            documentBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            Assert.fail("Failed to create DocumentBuilder");
        }
    }

    @Test
    public void testFromJsoup_validDocument_convertedSuccessfully() {
        // Create a sample jsoup document
        Document jsoupDocument = new Document("");

        // Call the method under test
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        // Assert that the conversion was successful
        Assert.assertNotNull(w3cDocument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_nullDocument_throwIllegalArgumentException() {
        // Call the method under test with null input
        new W3CDom().fromJsoup(null);
    }

    @Test
    public void testConvert_validDocuments_convertedSuccessfully() {
        // Create a sample jsoup document
        Document jsoupDocument = new Document("");

        // Create a sample W3C document
        org.w3c.dom.Document w3cDocument = documentBuilder.newDocument();

        // Call the method under test
        new W3CDom().convert(jsoupDocument, w3cDocument);

        // Assert that the conversion was successful
        Assert.assertEquals(jsoupDocument.location(), w3cDocument.getDocumentURI());
    }

    @Test
    public void testConvert_blankLocation_setDocumentURIAsBlank() {
        // Create a sample jsoup document with blank location
        Document jsoupDocument = new Document("");
        jsoupDocument.location("");

        // Create a sample W3C document
        org.w3c.dom.Document w3cDocument = documentBuilder.newDocument();

        // Call the method under test
        new W3CDom().convert(jsoupDocument, w3cDocument);

        // Assert that the documentURI is blank
        Assert.assertEquals("", w3cDocument.getDocumentURI());
    }

    @Test
    public void testConvert_elementWithNamespaceAdded_setCorrectNamespaceToW3CElement() {
        // Create a sample jsoup document with an element containing a namespace declaration
        Document jsoupDocument = new Document("");
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("prefix:tagName", "");
        element.attr("xmlns:prefix", "http://example.com");
        jsoupDocument.appendChild(element);

        // Create a sample W3C document
        org.w3c.dom.Document w3cDocument = documentBuilder.newDocument();

        // Call the method under test
        new W3CDom().convert(jsoupDocument, w3cDocument);

        // Assert that the W3C element has the correct namespace
        org.w3c.dom.Element w3cElement = w3cDocument.getDocumentElement();
        Assert.assertNotNull(w3cElement);
        Assert.assertEquals("http://example.com", w3cElement.getNamespaceURI());
    }

    @Test
    public void testConvert_elementWithoutNamespaceAdded_setNoNamespaceToW3CElement() {
        // Create a sample jsoup document with an element without any namespace declaration
        Document jsoupDocument = new Document("");
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("tagName", "");
        jsoupDocument.appendChild(element);

        // Create a sample W3C document
        org.w3c.dom.Document w3cDocument = documentBuilder.newDocument();

        // Call the method under test
        new W3CDom().convert(jsoupDocument, w3cDocument);

        // Assert that the W3C element has no namespace
        org.w3c.dom.Element w3cElement = w3cDocument.getDocumentElement();
        Assert.assertNotNull(w3cElement);
        Assert.assertNull(w3cElement.getNamespaceURI());
    }

    @Test
    public void testConvert_textNodeAddedToW3CDocument_textCopiedSuccessfully() {
        // Create a sample jsoup document with a text node
        Document jsoupDocument = new Document("");
        org.jsoup.nodes.TextNode textNode = new org.jsoup.nodes.TextNode("Sample Text", "");
        jsoupDocument.appendChild(textNode);

        // Create a sample W3C document
        org.w3c.dom.Document w3cDocument = documentBuilder.newDocument();

        // Call the method under test
        new W3CDom().convert(jsoupDocument, w3cDocument);

        // Assert that the text node was copied successfully
        String w3cText = w3cDocument.getTextContent();
        Assert.assertNotNull(w3cText);
        Assert.assertEquals("Sample Text", w3cText);
    }

    @Test
    public void testConvert_commentAddedToW3CDocument_commentCopiedSuccessfully() {
        // Create a sample jsoup document with a comment
        Document jsoupDocument = new Document("");
        org.jsoup.nodes.Comment comment = new org.jsoup.nodes.Comment("Sample Comment", "");
        jsoupDocument.appendChild(comment);

        // Create a sample W3C document
        org.w3c.dom.Document w3cDocument = documentBuilder.newDocument();

        // Call the method under test
        new W3CDom().convert(jsoupDocument, w3cDocument);

        // Assert that the comment was copied successfully
        String w3cComment = w3cDocument.getFirstChild().getNodeValue();
        Assert.assertNotNull(w3cComment);
        Assert.assertEquals("Sample Comment", w3cComment);
    }

    @Test
    public void testConvert_dataNodeAddedToW3CDocument_dataCopiedSuccessfully() {
        // Create a sample jsoup document with a data node
        Document jsoupDocument = new Document("");
        org.jsoup.nodes.DataNode dataNode = new org.jsoup.nodes.DataNode("Sample Data", "");
        jsoupDocument.appendChild(dataNode);

        // Create a sample W3C document
        org.w3c.dom.Document w3cDocument = documentBuilder.newDocument();

        // Call the method under test
        new W3CDom().convert(jsoupDocument, w3cDocument);

        // Assert that the data node was copied successfully
        String w3cData = w3cDocument.getFirstChild().getNodeValue();
        Assert.assertNotNull(w3cData);
        Assert.assertEquals("Sample Data", w3cData);
    }

    @Test
    public void testAsString_validDocument_returnConvertedString() {
        // Create a sample W3C document
        org.w3c.dom.Document w3cDocument = documentBuilder.newDocument();
        org.w3c.dom.Element rootElement = w3cDocument.createElement("RootElement");
        w3cDocument.appendChild(rootElement);

        // Call the method under test
        String result = new W3CDom().asString(w3cDocument);

        // Assert that the result is not null or empty
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testAsString_transformerException_throwIllegalStateException() {
        // Create a sample W3C document that cannot be transformed
        org.w3c.dom.Document w3cDocument = documentBuilder.newDocument();
        org.w3c.dom.Element rootElement = w3cDocument.createElement("RootElement");
        rootElement.appendChild(w3cDocument.createTextNode("\u0008")); // Invalid character for XML
        w3cDocument.appendChild(rootElement);

        // Call the method under test
        new W3CDom().asString(w3cDocument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsString_nullDocument_throwIllegalArgumentException() {
        // Call the method under test with null input
        new W3CDom().asString(null);
    }

    @Test
    public void testEndToEndConversion_validDocuments_convertedSuccessfully() throws IOException, TransformerException {
        // Create a sample jsoup document
        Document jsoupDocument = new Document("");
        org.jsoup.nodes.Element rootElement = new org.jsoup.nodes.Element("RootElement", "");
        jsoupDocument.appendChild(rootElement);

        // Convert jsoup document to W3C document
        org.w3c.dom.Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        // Convert W3C document to string
        String xmlString = new W3CDom().asString(w3cDocument);

        // Convert XML string back to W3C document
        org.w3c.dom.Document convertedW3cDocument = documentBuilder.parse(toInputStream(xmlString));

        // Convert W3C document back to jsoup document
        org.jsoup.nodes.Document convertedJsoupDocument = new W3CDom().fromJsoup(convertedW3cDocument);

        // Assert that the jsoup document and converted jsoup document are equal
        Assert.assertEquals(jsoupDocument, convertedJsoupDocument);
    }

    private InputStream toInputStream(String input) {
        return new ByteArrayInputStream(input.getBytes());
    }
}