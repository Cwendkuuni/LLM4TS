package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        Assert.assertNotNull(w3cDocument);
        Assert.assertEquals("html", w3cDocument.getDocumentElement().getNodeName());

        Element html = w3cDocument.getDocumentElement();
        Element head = (Element) html.getElementsByTagName("head").item(0);
        Element title = (Element) head.getElementsByTagName("title").item(0);
        Assert.assertEquals("Test", title.getTextContent());

        Element body = (Element) html.getElementsByTagName("body").item(0);
        Element p = (Element) body.getElementsByTagName("p").item(0);
        Assert.assertEquals("Hello World", p.getTextContent());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        Document outDocument = new Document();
        new W3CDom().convert(jsoupDocument, outDocument);

        Assert.assertEquals(w3cDocument.getDocumentElement(), outDocument.getDocumentElement());
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        String string = new W3CDom().asString(w3cDocument);

        Assert.assertNotNull(string);
        Assert.assertTrue(string.startsWith("<html>"));
        Assert.assertTrue(string.endsWith("</html>"));
    }

    @Test
    public void testW3CBuilder() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html xmlns:foo=\"http://example.com/foo\"><head><title>Test</title></head><body><p xmlns:bar=\"http://example.com/bar\" xmlns:baz=\"http://example.com/baz\">Hello World</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        Element html = w3cDocument.getDocumentElement();
        HashMap<String, String> namespaces = new HashMap<>();
        namespaces.put("foo", "http://example.com/foo");
        namespaces.put("bar", "http://example.com/bar");
        namespaces.put("baz", "http://example.com/baz");

        Element root = new W3CDom.W3CBuilder(w3cDocument.createDocument(null, "", null)).build(html);

        Assert.assertEquals("html", root.getNodeName());
        Assert.assertEquals("http://example.com/foo", root.getNamespaceURI());
        Assert.assertNull(root.getPrefix());

        Element head = (Element) root.getElementsByTagName("head").item(0);
        Assert.assertEquals("head", head.getNodeName());
        Assert.assertNull(head.getNamespaceURI());
        Assert.assertNull(head.getPrefix());

        Element title = (Element) head.getElementsByTagName("title").item(0);
        Assert.assertEquals("title", title.getNodeName());
        Assert.assertNull(title.getNamespaceURI());
        Assert.assertNull(title.getPrefix());
        Assert.assertEquals("Test", title.getTextContent());

        Element body = (Element) root.getElementsByTagName("body").item(0);
        Assert.assertEquals("body", body.getNodeName());
        Assert.assertEquals("http://example.com/baz", body.getNamespaceURI());
        Assert.assertEquals("baz", body.getPrefix());

        Element p = (Element) body.getElementsByTagName("p").item(0);
        Assert.assertEquals("p", p.getNodeName());
        Assert.assertEquals("http://example.com/bar", p.getNamespaceURI());
        Assert.assertEquals("bar", p.getPrefix());
        Assert.assertEquals("Hello World", p.getTextContent());
    }

    @Test
    public void testNodeVisitor() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        Element root = w3cDocument.getDocumentElement();
        W3CDom.W3CBuilder visitor = new W3CDom.W3CBuilder(w3cDocument.createDocument(null, "", null));
        visitor.build(root);

        Assert.assertEquals("html", root.getNodeName());
        Assert.assertNull(root.getNamespaceURI());
        Assert.assertNull(root.getPrefix());

        Element head = (Element) root.getElementsByTagName("head").item(0);
        Assert.assertEquals("head", head.getNodeName());
        Assert.assertNull(head.getNamespaceURI());
        Assert.assertNull(head.getPrefix());

        Element title = (Element) head.getElementsByTagName("title").item(0);
        Assert.assertEquals("title", title.getNodeName());
        Assert.assertNull(title.getNamespaceURI());
        Assert.assertNull(title.getPrefix());
        Assert.assertEquals("Test", title.getTextContent());

        Element body = (Element) root.getElementsByTagName("body").item(0);
        Assert.assertEquals("body", body.getNodeName());
        Assert.assertNull(body.getNamespaceURI());
        Assert.assertNull(body.getPrefix());

        Element p = (Element) body.getElementsByTagName("p").item(0);
        Assert.assertEquals("p", p.getNodeName());
        Assert.assertNull(p.getNamespaceURI());
        Assert.assertNull(p.getPrefix());
        Assert.assertEquals("Hello World", p.getTextContent());
    }

    @Test
    public void testSerialize() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        String w3cString = new W3CDom().asString(w3cDocument);

        Document newW3cDocument = (Document) newDocumentBuilder().parseString(w3cString);

        Assert.assertEquals(w3cDocument.getDocumentElement(), newW3cDocument.getDocumentElement());
    }
}