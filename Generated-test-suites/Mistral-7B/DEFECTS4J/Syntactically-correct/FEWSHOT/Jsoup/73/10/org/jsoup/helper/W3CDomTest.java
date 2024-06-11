package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.junit.Test;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document out = new W3CDom().fromJsoup(in);
        assertNotNull(out);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse("<html><body><h1 id='title'>Hello World</h1></body></html>");
        Document out = new Document();
        new W3CDom().convert(in, out);
        assertNotNull(out.getDocumentElement());
        assertEquals("html", out.getDocumentElement().getTagName());
        Element body = (Element) out.getElementsByTagName("body").item(0);
        assertEquals(1, body.getChildNodes().getLength());
        Element title = (Element) body.getElementsByTagName("h1").item(0);
        assertNotNull(title);
        assertEquals("h1", title.getTagName());
        assertEquals("Hello World", title.getTextContent());
        assertEquals("title", title.getAttribute("id"));
    }

    @Test
    public void testAsString() {
        Document doc = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(doc);
        String result = new W3CDom().asString(w3cDoc);
        assertEquals("<html xmlns='http://www.w3.org/1999/xhtml'><head><body xmlns='http://www.w3.org/1999/xhtml'><h1>Hello World</h1></body></html>", result);
    }

    @Test
    public void testConvertWithAttributes() {
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse("<html xmlns:foo='http://foo'><body><div class='bar' id='baz' foo:foo='bar'><h1>Hello World</h1></div></body></html>");
        Document out = new Document();
        new W3CDom().convert(in, out);
        Element html = (Element) out.getDocumentElement();
        assertEquals("html", html.getTagName());
        Element body = (Element) html.getElementsByTagName("body").item(0);
        Element div = (Element) body.getElementsByTagName("div").item(0);
        assertEquals("div", div.getTagName());
        assertEquals("class", div.getAttributeNode("class").getNodeValue());
        assertEquals("baz", div.getAttributeNode("id").getNodeValue());
        assertEquals("http://foo", div.getAttributeNode("foo:foo").getNodeValue());
        Element h1 = (Element) div.getElementsByTagName("h1").item(0);
        assertEquals("h1", h1.getTagName());
        assertEquals("Hello World", h1.getTextContent());
    }

    @Test
    public void testConvertWithNamespaces() {
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse("<html xmlns:xhtml='http://www.w3.org/1999/xhtml' xmlns:foo='http://foo'><body><div xhtml:class='bar' id='baz' foo:foo='bar'><h1>Hello World</h1></div></body></html>");
        Document out = new Document();
        new W3CDom().convert(in, out);
        Element html = (Element) out.getDocumentElement();
        assertEquals("html", html.getTagName());
        Element body = (Element) html.getElementsByTagName("body").item(0);
        Element div = (Element) body.getElementsByTagNameNS("http://www.w3.org/1999/xhtml", "div").item(0);
        assertEquals("div", div.getTagName());
        assertEquals("class", div.getAttributeNode("class").getNodeValue());
        assertEquals("baz", div.getAttributeNode("id").getNodeValue());
        Element h1 = (Element) div.getElementsByTagNameNS("http://www.w3.org/1999/xhtml", "h1").item(0);
        assertEquals("h1", h1.getTagName());
        assertEquals("Hello World", h1.getTextContent());
    }

    @Test
    public void testConvertWithComment() {
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse("<html><!--This is a comment--><body><h1>Hello World</h1></body></html>");
        Document out = new Document();
        new W3CDom().convert(in, out);
        Node commentNode = out.getElementsByTagName("!--").item(0);
        assertEquals("Comment", commentNode.getNodeType());
        assertEquals("This is a comment", commentNode.getTextContent());
    }

    @Test
    public void testConvertWithTextNode() {
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse("<html><body><h1>Hello World <script>alert('Hello')</script></body></html>");
        Document out = new Document();
        new W3CDom().convert(in, out);
        Node textNode = out.getElementsByTagName("h1").item(0).getFirstChild();
        assertEquals(Node.TEXT_NODE, textNode.getNodeType());
        assertEquals("Hello World ", textNode.getTextContent());
    }

    @Test
    public void testConvertWithTextNodeAndScriptTag() {
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse("<html><body><h1>Hello World <script>alert('Hello')</script></body></html>");
        Document out = new Document();
        new W3CDom().convert(in, out);
        Node textNode = out.getElementsByTagName("h1").item(0).getFirstChild();
        assertEquals(Node.TEXT_NODE, textNode.getNodeType());
        assertEquals("Hello World ", textNode.getTextContent());
        Element script = (Element) out.getElementsByTagName("script").item(0);
        assertEquals("script", script.getTagName());
        assertEquals(Node.ELEMENT_NODE, script.getNodeType());
        assertNull(script.getFirstChild());
    }

}