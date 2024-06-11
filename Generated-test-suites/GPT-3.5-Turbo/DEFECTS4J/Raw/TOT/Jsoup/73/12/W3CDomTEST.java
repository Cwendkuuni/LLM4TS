import static org.junit.Assert.*;
import org.junit.Test;

public class W3CDomTest {

    @Test
    public void testFromJsoup_typical() {
        // Typical use case: converting a jsoup Document to a W3C Document
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoup_nullInput() {
        // Error scenario: passing a null input to fromJsoup()
        W3CDom w3cDom = new W3CDom();
        w3cDom.fromJsoup(null);
    }

    @Test
    public void testConvert_typical() {
        // Typical use case: converting a jsoup Document to a W3C Document by calling the convert() method
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.convert(jsoupDoc, w3cDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert_blankLocation() {
        // Edge case: converting a jsoup Document with a blank location to a W3C Document
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.location(""); // set the location to blank
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.convert(jsoupDoc, w3cDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert_textNode() {
        // Edge case: converting a jsoup Document with a text node to a W3C Document
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        org.jsoup.nodes.TextNode jsoupTextNode = new org.jsoup.nodes.TextNode("Hello World", "");
        jsoupDoc.appendChild(jsoupTextNode);
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.convert(jsoupDoc, w3cDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert_comment() {
        // Edge case: converting a jsoup Document with a comment to a W3C Document
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        org.jsoup.nodes.Comment jsoupComment = new org.jsoup.nodes.Comment("This is a comment", "");
        jsoupDoc.appendChild(jsoupComment);
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.convert(jsoupDoc, w3cDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert_dataNode() {
        // Edge case: converting a jsoup Document with a data node to a W3C Document
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        org.jsoup.nodes.DataNode jsoupDataNode = new org.jsoup.nodes.DataNode("Some data", "");
        jsoupDoc.appendChild(jsoupDataNode);
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.convert(jsoupDoc, w3cDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testCopyAttributes_validAttributes() {
        // Typical use case: copying valid attributes from a jsoup Node to a W3C Element
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div", "");
        jsoupElement.attr("test-attr", "test-value");
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element w3cElement = w3cDoc.createElement("div");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.copyAttributes(jsoupElement, w3cElement);
        assertEquals("test-value", w3cElement.getAttribute("test-attr"));
    }

    @Test
    public void testCopyAttributes_invalidAttributeName() {
        // Edge case: copying attributes from a jsoup Node to a W3C Element where the attribute name is invalid
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div", "");
        jsoupElement.attr("test-@attr", "test-value");
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element w3cElement = w3cDoc.createElement("div");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.copyAttributes(jsoupElement, w3cElement);
        assertTrue(w3cElement.getAttributes().getLength() == 0);
    }

    @Test
    public void testUpdateNamespaces() {
        // Typical use case: updating namespaces for a jsoup Element
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("ns:div", "");
        jsoupElement.attr("xmlns:ns", "http://www.example.com/ns");
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element w3cElement = w3cDoc.createElementNS("", jsoupElement.tagName());
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        String prefix = w3cBuilder.updateNamespaces(jsoupElement);
        assertEquals("ns", prefix);
        assertEquals("http://www.example.com/ns", w3cBuilder.namespaces.get(prefix));
    }

    @Test
    public void testAsString_typical() {
        // Typical use case: serializing a W3C document to a String
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        W3CDom w3cDom = new W3CDom();
        String xml = w3cDom.asString(w3cDoc);
        assertNotNull(xml);
    }

    @Test(expected = IllegalStateException.class)
    public void testAsString_error() {
        // Error scenario: serializing an invalid W3C document to a String
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        org.w3c.dom.Element w3cElement = w3cDoc.createElement("invalid");
        w3cDoc.appendChild(w3cElement);
        W3CDom w3cDom = new W3CDom();
        w3cDom.asString(w3cDoc);
    }

}