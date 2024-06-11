
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;

import static org.junit.Assert.*;

public class W3CDomTest {

    @Test
    public void testFromJsoupValidDocument() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<html><head></head><body></body></html>");

        // Act
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        assertNotNull(w3cDoc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupNullDocument() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = null;

        // Act
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
    }

    @Test
    public void testConvertValidDocuments() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<!-- comment --><html><head></head><body></body></html>");
        Document w3cDoc = createW3CDocument();

        // Act
        w3cDom.convert(jsoupDoc, w3cDoc);

        // Assert
        NodeList nodeList = w3cDoc.getElementsByTagName("html");
        int length = nodeList.getLength();
        assertEquals(1, length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertNullDocuments() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = null;
        Document w3cDoc = createW3CDocument();

        // Act
        w3cDom.convert(jsoupDoc, w3cDoc);
    }
    
    @Test
    public void testConvertBlankLocation() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<html><head></head><body></body></html>");
        Document w3cDoc = createW3CDocument();
        
        // Act
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        // Assert
        assertEquals("", w3cDoc.getDocumentURI());
    }
    
    @Test
    public void testConvertNonBlankLocation() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<html><head></head><body></body></html>", "http://example.com");
        Document w3cDoc = createW3CDocument();
        
        // Act
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        // Assert
        assertEquals("http://example.com", w3cDoc.getDocumentURI());
    }
    
    @Test
    public void testConvertTextNode() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<html><head></head><body>Hello World!</body></html>");
        Document w3cDoc = createW3CDocument();
        
        // Act
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        // Assert
        String textContent = w3cDoc.getElementsByTagName("body").item(0).getTextContent();
        assertEquals("Hello World!", textContent);
    }
    
    @Test
    public void testConvertCommentNode() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<!-- comment --><html><head></head><body></body></html>");
        Document w3cDoc = createW3CDocument();
        
        // Act
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        // Assert
        NodeList nodeList = w3cDoc.getElementsByTagName("#comment");
        int length = nodeList.getLength();
        assertEquals(1, length);
        assertEquals(" comment ", nodeList.item(0).getNodeValue());
    }
    
    @Test
    public void testConvertDataNode() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<html><head></head><body><![CDATA[Data]]></body></html>");
        Document w3cDoc = createW3CDocument();
        
        // Act
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        // Assert
        NodeList nodeList = w3cDoc.getElementsByTagName("#text");
        int length = nodeList.getLength();
        assertEquals(1, length);
        assertEquals("Data", nodeList.item(0).getNodeValue());
    }
    
    @Test
    public void testCopyAttributesValidAttributes() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<div id=\"testId\" class=\"testClass\"></div>");
        Element element = jsoupDoc.select("div").first();
        Document w3cDoc = createW3CDocument();
        Element w3cElement = w3cDoc.createElement("div");
        
        // Act
        w3cDom.copyAttributes(element, w3cElement);
        String id = w3cElement.getAttribute("id");
        String clazz = w3cElement.getAttribute("class");
        
        // Assert
        assertEquals("testId", id);
        assertEquals("testClass", clazz);
    }
    
    @Test
    public void testCopyAttributesInvalidAttributes() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<div id=\"test-Id\" class=\"_testClass\"></div>");
        Element element = jsoupDoc.select("div").first();
        Document w3cDoc = createW3CDocument();
        Element w3cElement = w3cDoc.createElement("div");
        
        // Act
        w3cDom.copyAttributes(element, w3cElement);
        String id = w3cElement.getAttribute("id");
        String clazz = w3cElement.getAttribute("class");
        
        // Assert
        assertEquals("", id);
        assertEquals("", clazz);
    }
    
    @Test
    public void testUpdateNamespacesWithNamespaceDeclaration() {
        // Arrange
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(createW3CDocument());
        org.jsoup.nodes.Element element = Jsoup.parse("<element xmlns:ns1=\"http://example.com\"></element>").select("element").first();
        
        // Act
        String prefix = w3cBuilder.updateNamespaces(element);
        
        // Assert
        assertEquals("ns1", prefix);
        assertEquals("http://example.com", w3cBuilder.namespaces.get(prefix));
    }
    
    @Test
    public void testUpdateNamespacesWithDefaultNamespaceDeclaration() {
        // Arrange
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(createW3CDocument());
        org.jsoup.nodes.Element element = Jsoup.parse("<element xmlns=\"http://example.com\"></element>").select("element").first();
        
        // Act
        String prefix = w3cBuilder.updateNamespaces(element);
        
        // Assert
        assertEquals("", prefix);
        assertEquals("http://example.com", w3cBuilder.namespaces.get(prefix));
    }
    
    @Test
    public void testUpdateNamespacesWithoutNamespaceDeclaration() {
        // Arrange
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(createW3CDocument());
        org.jsoup.nodes.Element element = Jsoup.parse("<element></element>").select("element").first();
        
        // Act
        String prefix = w3cBuilder.updateNamespaces(element);
        
        // Assert
        assertEquals("", prefix);
        assertFalse(w3cBuilder.namespaces.containsKey(prefix));
    }
    
    @Test
    public void testAsStringValidDocument() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = createW3CDocument();
        Element root = w3cDoc.createElement("root");
        w3cDoc.appendChild(root);
        String expectedOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><root/>";
        
        // Act
        String result = w3cDom.asString(w3cDoc);
        
        // Assert
        assertEquals(expectedOutput, result);
    }
    
    private Document createW3CDocument() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            return db.newDocument();
        } catch (Exception e) {
            throw new AssertionError("Error creating W3C Document");
        }
    }
}
