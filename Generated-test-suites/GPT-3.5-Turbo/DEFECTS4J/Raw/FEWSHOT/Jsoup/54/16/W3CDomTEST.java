
package org.jsoup.helper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class W3CDomTest {
    private final org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");

    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertEquals("Test", w3cDoc.getElementsByTagName("title").item(0).getTextContent());
    }

    @Test
    public void testConvert() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.w3c.dom.Document w3cDoc = builder.doc;

        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(jsoupDoc, w3cDoc);

        assertEquals("Test", w3cDoc.getElementsByTagName("title").item(0).getTextContent());
    }

    @Test
    public void testCopyAttributes() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.w3c.dom.Element w3cEl = builder.doc.createElementNS("", "div");

        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        jsoupEl.attributes().put("class", "myClass");
        jsoupEl.attributes().put("data-attr", "value");
        builder.copyAttributes(jsoupEl, w3cEl);

        assertEquals("myClass", w3cEl.getAttribute("class"));
        assertEquals("value", w3cEl.getAttribute("data-attr"));
    }

    @Test
    public void testUpdateNamespaces() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        jsoupEl.attributes().put("xmlns:a", "http://www.example.com/a");
        jsoupEl.attributes().put("xmlns:b", "http://www.example.com/b");
        jsoupEl.attributes().put("xmlns", "http://www.example.com");

        String prefix = builder.updateNamespaces(jsoupEl);

        assertEquals("a", prefix);
        assertEquals("http://www.example.com/a", builder.namespaces.get("a"));
        assertEquals("http://www.example.com/b", builder.namespaces.get("b"));
        assertEquals("http://www.example.com", builder.namespaces.get(""));
    }

    @Test
    public void testW3CBuilderHeadElement() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.w3c.dom.Document w3cDoc = builder.doc;

        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        jsoupEl.attributes().put("xmlns:a", "http://www.example.com/a");
        jsoupEl.attributes().put("xmlns:b", "http://www.example.com/b");
        jsoupEl.attributes().put("xmlns", "http://www.example.com");

        builder.head(jsoupEl, 0);

        assertEquals("div", w3cDoc.getDocumentElement().getTagName());
        assertEquals("http://www.example.com", w3cDoc.getDocumentElement().getNamespaceURI());
        assertEquals("a", w3cDoc.getDocumentElement().getAttribute("xmlns:a"));
        assertEquals("http://www.example.com/a", w3cDoc.getDocumentElement().getAttribute("xmlns:a"));
        assertEquals("b", w3cDoc.getDocumentElement().getAttribute("xmlns:b"));
        assertEquals("http://www.example.com/b", w3cDoc.getDocumentElement().getAttribute("xmlns:b"));
        assertEquals("", w3cDoc.getDocumentElement().getAttribute("xmlns"));
        assertEquals("http://www.example.com", w3cDoc.getDocumentElement().getAttribute("xmlns"));
    }

    @Test
    public void testW3CBuilderHeadTextNode() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.w3c.dom.Element w3cEl = builder.doc.createElementNS("", "div");

        org.jsoup.nodes.TextNode jsoupText = new org.jsoup.nodes.TextNode("Hello", "");

        builder.dest = w3cEl;
        builder.head(jsoupText, 0);

        assertEquals("Hello", w3cEl.getFirstChild().getTextContent());
    }

    @Test
    public void testW3CBuilderHeadComment() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.w3c.dom.Element w3cEl = builder.doc.createElementNS("", "div");

        org.jsoup.nodes.Comment jsoupComment = new org.jsoup.nodes.Comment("Test comment");

        builder.dest = w3cEl;
        builder.head(jsoupComment, 0);

        assertEquals("Test comment", w3cEl.getFirstChild().getNodeValue());
    }

    @Test
    public void testW3CDomAsString() {
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String result = w3cDom.asString(w3cDoc);

        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html><head><title>Test</title></head><body/></html>", result);
    }
}
