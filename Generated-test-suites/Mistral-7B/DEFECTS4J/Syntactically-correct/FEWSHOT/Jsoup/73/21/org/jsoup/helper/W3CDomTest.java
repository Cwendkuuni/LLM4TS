package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.XMLNamespaceManager;
import org.w3c.dom.XMLString;
import org.xml.names.QName;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head/><body><p id='test'>Hello World</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        assertNotNull(w3cDocument);
        assertEquals("<html><head/><body><p id='test'>Hello World</p></body></html>", asString(w3cDocument));
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head/><body><p id='test'>Hello World</p></body></html>");
        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);
        assertEquals("<html><head/><body><p id='test'>Hello World</p></body></html>", asString(w3cDocument));
    }

    @Test
    public void testW3CBuilder() {
        Document w3cDocument = new Document();
        Element root = w3cDocument.createElement("html");
        root.setNodeName("html");
        root.setNamespaceURI("http://www.w3.org/1999/xhtml");
        w3cDocument.appendChild(root);

        new W3CDom.W3CBuilder(w3cDocument).head(new org.jsoup.nodes.Element("head"), 0);
        new W3CDom.W3CBuilder(w3cDocument).head(new org.jsoup.nodes.TextNode(""), 0);
        new W3CDom.W3CBuilder(w3cDocument).head(new org.jsoup.nodes.Element("title"), 0);
        new W3CDom.W3CBuilder(w3cDocument).head(new org.jsoup.nodes.TextNode("Test Page"), 0);

        new W3CDom.W3CBuilder(root).head(new org.jsoup.nodes.Element("body"), 1);
        new W3CDom.W3CBuilder(root).head(new org.jsoup.nodes.Element("p"), 2);
        new W3CDom.W3CBuilder(root).head(new org.jsoup.nodes.Attribute("id", "test"), 2);
        new W3CDom.W3CBuilder(root).head(new org.jsoup.nodes.TextNode("Hello World"), 2);

        assertEquals("<html xmlns='http://www.w3.org/1999/xhtml'><head><title>Test Page</title></head><body><p id='test'>Hello World</p></body></html>", asString(w3cDocument));
    }

    @Test
    public void testConvertWithNamespaces() {
        Document w3cDocument = new Document();
        Element root = w3cDocument.createElement("html");
        root.setNodeName("html");
        root.setNamespaceURI("http://www.w3.org/1999/xhtml");
        w3cDocument.appendChild(root);

        Map<String, String> namespaces = new HashMap<>();
        namespaces.put("sv", "http://www.w3.org/2000/svg");
        new W3CDom.W3CBuilder(w3cDocument).setNamespaces(namespaces);

        new W3CDom.W3CBuilder(root).head(new org.jsoup.nodes.Element("svg"), 1);
        new W3CDom.W3CBuilder(root).head(new org.jsoup.nodes.Element("rect"), 2);
        new W3CDom.W3CBuilder(root).head(new org.jsoup.nodes.Attribute("x", "10"), 2);
        new W3CDom.W3CBuilder(root).head(new org.jsoup.nodes.Attribute("y", "10"), 2);
        new W3CDom.W3CBuilder(root).head(new org.jsoup.nodes.Attribute("width", "50"), 2);
        new W3CDom.W3CBuilder(root).head(new org.jsoup.nodes.Attribute("height", "50"), 2);
        new W3CDom.W3CBuilder(root).head(new org.jsoup.nodes.Attribute("style", "fill:red"), 2);

        XMLNamespaceManager namespaceManager = w3cDocument.getDocumentElement().getNamespaceManager();
        QName qname = new QName("{http://www.w3.org/2000/svg}", "rect");
        Element rect = (Element) namespaceManager.getElementByTagNameNS(qname.getNamespaceURI(), qname.getLocalPart(), root.getFirstChild());
        assertSame(rect, root.getFirstChild().getFirstChild());

        assertEquals("<html xmlns='http://www.w3.org/1999/xhtml' xmlns:sv='http://www.w3.org/2000/svg'><head><body><svg><rect x='10' y='10' width='50' height='50' style='fill:red'/></svg></body></html>", asString(w3cDocument));
    }

    @Test
    public void testAsString() {
        Document w3cDocument = new Document();
        Element root = w3cDocument.createElement("html");
        root.setNodeName("html");
        root.setNamespaceURI("http://www.w3.org/1999/xhtml");
        w3cDocument.appendChild(root);

        new W3CDom.W3CBuilder(w3cDocument).head(new org.jsoup.nodes.Element("head"), 0);
        new W3CDom.W3CBuilder(w3cDocument).head(new org.jsoup.nodes.Element("title"), 0);
        new W3CDom.W3CBuilder(w3cDocument).head(new org.jsoup.nodes.TextNode("Test Page"), 0);

        new W3CDom.W3CBuilder(root).head(new org.jsoup.nodes.Element("body"), 1);
        new W3CDom.W3CBuilder(root).head(new org.jsoup.nodes.Element("p"), 2);
        new W3CDom.W3CBuilder(root).head(new org.jsoup.nodes.TextNode("Hello World"), 2);

        assertEquals("<html xmlns='http://www.w3.org/1999/xhtml'><head><title>Test Page</title></head><body><p>Hello World</p></body></html>", asString(w3cDocument));
    }

    @Test
    public void testSerializedDocumentWithComment() {
        Document w3cDocument = new Document();
        Element root = w3cDocument.createElement("html");
        root.setNodeName("html");
        root.setNamespaceURI("http://www.w3.org/1999/xhtml");
        w3cDocument.appendChild(root);

        new W3CDom.W3CBuilder(w3cDocument).head(new org.jsoup.nodes.Element("head"), 0);
        new W3CDom.W3CBuilder(w3cDocument).head(new org.jsoup.nodes.Element("title"), 0);
        new W3CDom.W3CBuilder(w3cDocument).head(new org.jsoup.nodes.TextNode("Test Page"), 0);

        new W3CDom.W3CBuilder(root).head(new org.jsoup.nodes.Element("body"), 1);
        new W3CDom.W3CBuilder(root).head(new org.jsoup.nodes.Element("p"), 2);
        new W3CDom.W3CBuilder(root).head(new org.jsoup.nodes.TextNode("Hello World"), 2);

        new W3CDom.W3CBuilder(root).head(new org.jsoup.nodes.Comment("This is a comment"), 2);

        assertEquals("<html xmlns='http://www.w3.org/1999/xhtml'><head><title>Test Page</title></head><body><p>Hello World</p><!--This is a comment--></body></html>", asString(w3cDocument));
    }

    @Test
    public void testSerializedDocumentWithTextNodeWithScriptData() {
        Document w3cDocument = new Document();
        Element root = w3cDocument.createElement("html");
        root.setNodeName("html");
        root.setNamespaceURI("http://www.w3.org/1999/xhtml");
        w3cDocument.appendChild(root);

        new W3CDom.W3CBuilder(w3cDocument).head(new org.jsoup.nodes.Element("head"), 0);
        new W3CDom.W3CBuilder(w3cDocument).head(new org.jsoup.nodes.Element("title"), 0);
        new W3CDom.W3CBuilder(w3cDocument).head(new org.jsoup.nodes.TextNode("Test Page"), 0);

        new W3CDom.W3CBuilder(root).head(new org.jsoup.nodes.Element("body"), 1);
        new W3CDom.W3CBuilder(root).head(new org.jsoup.nodes.Element("p"), 2);
        new W3CDom.W3CBuilder(root).head(new org.jsoup.nodes.TextNode("Hello <script>alert(\"World\")</script> World"), 2);

        assertEquals("<html xmlns='http://www.w3.org/1999/xhtml'><head><title>Test Page</title></head><body><p>Hello <script>alert(&quot;World&quot;)</script> World</p></body></html>", asString(w3cDocument));
    }

    private String asString(Document doc) {
        return new W3CDom().asString(doc);
    }
}