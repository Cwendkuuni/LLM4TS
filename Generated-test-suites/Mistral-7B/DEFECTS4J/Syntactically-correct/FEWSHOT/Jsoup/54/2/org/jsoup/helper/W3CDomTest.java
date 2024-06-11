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

import java.util.HashMap;
import java.util.Map;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><p>Hello</p></body></html>");

        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);

        assertEquals(jsoupDoc.tagName(), w3cDoc.getDocumentElement().getTagName());

        Element html = w3cDoc.getDocumentElement();
        assertEquals(1, html.getChildNodes().getLength());

        Element body = (Element) html.getFirstChild();
        assertEquals(1, body.getChildNodes().getLength());

        Element p = (Element) body.getFirstChild();
        assertEquals("p", p.getTagName());
        assertEquals("Hello", p.getTextContent());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><p>Hello</p></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        Element root = new W3CDom().convert(jsoupDoc, Document.newDocument());
        assertEquals(w3cDoc.getDocumentElement(), root);
    }

    @Test
    public void testW3CBuilder() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html xmlns:foo='http://example.com'><body><p xmlns:bar='http://example.org' xmlns:baz='http://example.net' foo:foo='bar' bar:baz='qux'><text>Hello</text></p></body></html>");

        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        NodeTraversor traversor = new NodeTraversor(new W3CDom.W3CBuilder(w3cDoc));
        traversor.traverse(jsoupDoc.child(0)); // the root node

        Element html = (Element) w3cDoc.getDocumentElement();
        assertEquals(1, html.getChildNodes().getLength());

        Element body = (Element) html.getFirstChild();
        assertEquals(1, body.getChildNodes().getLength());

        Element p = (Element) body.getFirstChild();
        assertEquals(2, p.getAttributes().getLength());

        assertEquals("html", html.getTagName());
        assertEquals("html", html.getNamespaceURI());

        assertEquals("body", body.getTagName());
        assertEquals("http://www.w3.org/1999/xhtml", body.getNamespaceURI());

        assertEquals("p", p.getTagName());
        assertEquals("http://www.w3.org/1999/xhtml", p.getNamespaceURI());

        assertEquals("foo", p.getNamespacePrefix("http://example.com"));
        assertEquals("http://example.com", p.getNamespaceURI("foo"));

        assertEquals("bar", p.getNamespacePrefix("http://example.org"));
        assertEquals("http://example.org", p.getNamespaceURI("bar"));

        assertEquals("baz", p.getNamespacePrefix("http://example.net"));
        assertEquals("http://example.net", p.getNamespaceURI("baz"));

        assertEquals("foo:foo", p.getAttributeNode("foo:foo").getNodeName());
        assertEquals("bar", p.getAttributeNode("bar:baz").getNodeValue());

        assertEquals("Hello", ((Text) p.getFirstChild()).getNodeValue());
    }

    @Test
    public void testAsString() {
        Document doc = DocumentBuilderFactory.newInstance().newDocument();
        Element root = doc.createElement("root");
        root.setTextContent("Hello World");
        doc.appendChild(root);

        String w3cString = new W3CDom().asString(doc);
        String jsoupString = org.jsoup.Jsoup.parse(w3cString).text();

        assertEquals("Hello World", jsoupString);
        assertEquals("Hello World", w3cString);
    }
}