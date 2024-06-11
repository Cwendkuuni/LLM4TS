
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {
    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;
    private org.w3c.dom.Document w3cDoc;
    
    @Before
    public void setUp() throws ParserConfigurationException {
        w3cDom = new W3CDom();
        jsoupDoc = new org.jsoup.nodes.Document();
        w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    }
    
    @Test
    public void testFromJsoup() {
        // Test with a valid jsoup document
        Document in = jsoupDoc.clone();
        org.w3c.dom.Document out = w3cDom.fromJsoup(in);
        
        // Verify that the conversion is successful
        assertNotNull(out);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_NullInput() {
        // Test with a null jsoup document
        org.w3c.dom.Document out = w3cDom.fromJsoup(null);
    }
    
    @Test
    public void testConvert() {
        // Test with a valid jsoup document and W3C document
        org.jsoup.nodes.Document in = jsoupDoc.clone();
        org.w3c.dom.Document out = w3cDoc.clone();
        
        w3cDom.convert(in, out);
        
        // Verify that the conversion is successful
        assertNotNull(out.getFirstChild());
    }
    
    @Test
    public void testConvert_Location() {
        // Test with a jsoup document with a location set
        jsoupDoc.location("http://example.com");
        org.w3c.dom.Document out = w3cDoc.clone();
        
        w3cDom.convert(jsoupDoc, out);
        
        // Verify that the URI of the output W3C document matches the input jsoup document
        assertEquals(jsoupDoc.location(), out.getDocumentURI());
    }
    
    @Test
    public void testConvert_EmptyDocument() {
        // Test with an empty jsoup document
        org.w3c.dom.Document out = w3cDoc.clone();
        w3cDom.convert(jsoupDoc, out);
        
        // Verify that the output W3C document is also empty
        assertNull(out.getFirstChild());
    }
    
    @Test
    public void testConvert_TextNode() {
        // Test with a jsoup TextNode
        org.jsoup.nodes.TextNode textNode = new org.jsoup.nodes.TextNode("This is a text node.", "");
        org.w3c.dom.Document out = w3cDoc.clone();
        w3cDom.convert(textNode, out);
        
        // Verify that the output W3C document contains a Text node with the same text content
        Node node = out.getFirstChild();
        assertEquals(org.w3c.dom.Text.class, node.getClass());
        assertEquals(textNode.getWholeText(), ((org.w3c.dom.Text) node).getWholeText());
    }
    
    @Test
    public void testConvert_Comment() {
        // Test with a jsoup Comment
        org.jsoup.nodes.Comment comment = new org.jsoup.nodes.Comment("This is a comment.");
        org.w3c.dom.Document out = w3cDoc.clone();
        w3cDom.convert(comment, out);
        
        // Verify that the output W3C document contains a Comment node with the same data
        Node node = out.getFirstChild();
        assertEquals(org.w3c.dom.Comment.class, node.getClass());
        assertEquals(comment.getData(), ((org.w3c.dom.Comment) node).getData());
    }
    
    @Test
    public void testConvert_DataNode() {
        // Test with a jsoup DataNode
        org.jsoup.nodes.DataNode dataNode = new org.jsoup.nodes.DataNode("This is a data node.", "");
        org.w3c.dom.Document out = w3cDoc.clone();
        w3cDom.convert(dataNode, out);
        
        // Verify that the output W3C document contains a Text node with the same data
        Node node = out.getFirstChild();
        assertEquals(org.w3c.dom.Text.class, node.getClass());
        assertEquals(dataNode.getWholeData(), ((org.w3c.dom.Text) node).getWholeData());
    }
    
    @Test
    public void testConvert_Element() {
        // Test with a jsoup Element
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div", "");
        jsoupElement.append("<p>This is a paragraph.</p>");
        org.w3c.dom.Document out = w3cDoc.clone();
        w3cDom.convert(jsoupElement, out);
        
        // Verify that the output W3C document contains an Element node with the same tag name and inner HTML
        Element element = out.createElement("div");
        element.setInnerHTML("<p>This is a paragraph.</p>");
        assertEquals(element, out.getFirstChild());
    }
    
    @Test
    public void testConvert_Element_Namespaces() {
        // Test with a jsoup Element with namespaces
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("x:div", "");
        jsoupElement.attr("xmlns:x", "http://example.com");
        org.w3c.dom.Document out = w3cDoc.clone();
        w3cDom.convert(jsoupElement, out);
        
        // Verify that the output W3C document contains an Element node with the same tag name and namespace
        Element element = out.createElementNS("http://example.com", "x:div");
        assertEquals(element, out.getFirstChild());
    }
    
    @Test
    public void testCopyAttributes() {
        // Test with a jsoup Element with attributes
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div", "");
        jsoupElement.attr("id", "myDiv");
        jsoupElement.attr("class", "myClass");
        jsoupElement.attr("custom-attr", "customValue");
        Element element = w3cDoc.createElement("div");
        
        w3cDom.copyAttributes(jsoupElement, element);
        
        // Verify that the copied attributes match the attributes of the jsoup Element
        assertEquals("myDiv", element.getAttribute("id"));
        assertEquals("myClass", element.getAttribute("class"));
        assertEquals("customValue", element.getAttribute("custom-attr"));
    }
    
    @Test
    public void testCopyAttributes_InvalidName() {
        // Test with a jsoup Element with an invalid attribute name
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div", "");
        jsoupElement.attr("1invalid", "value");
        Element element = w3cDoc.createElement("div");
        
        w3cDom.copyAttributes(jsoupElement, element);
        
        // Verify that the invalid attribute is not copied
        assertNull(element.getAttribute("1invalid"));
    }
    
    @Test
    public void testUpdateNamespaces() {
        // Test with a jsoup Element with namespaces
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("x:div", "");
        jsoupElement.attr("xmlns:x", "http://example.com");
        
        String prefix = w3cDom.updateNamespaces(jsoupElement);
        
        // Verify that the namespace is added to the namespaces map and the prefix is returned
        assertEquals("x", prefix);
        assertEquals("http://example.com", w3cDom.namespaces.get("x"));
    }
    
    @Test
    public void testUpdateNamespaces_NoNamespace() {
        // Test with a jsoup Element with no namespace
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div", "");
        
        String prefix = w3cDom.updateNamespaces(jsoupElement);
        
        // Verify that no namespace is added to the namespaces map and an empty prefix is returned
        assertEquals("", prefix);
        assertFalse(w3cDom.namespaces.containsKey(""));
    }
    
    @Test
    public void testAsString() {
        // Test with a valid W3C document
        org.w3c.dom.Document doc = w3cDoc.clone();
        doc.appendChild(doc.createElement("root"));
        
        String result = w3cDom.asString(doc);
        
        // Verify that the serialized document matches the expected string representation
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><root/>", result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAsString_NullInput() {
        // Test with a null W3C document
        String result = w3cDom.asString(null);
    }
}

