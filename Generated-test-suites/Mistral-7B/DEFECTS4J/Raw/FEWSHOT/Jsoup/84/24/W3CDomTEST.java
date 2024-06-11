package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        String html = "<html><head/><body><div id=\"root\"><p>Hello World!</p></div></body></html>";
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse(html);

        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        assertNotNull(w3cDocument);

        assertEquals(html, new W3CDom().asString(w3cDocument));
    }

    @Test
    public void testConvert() {
        String html = "<html><head/><body><div id=\"root\"><p>Hello World!</p></div></body></html>";
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse(html);

        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        assertEquals(html, new W3CDom().asString(w3cDocument));
    }

    @Test
    public void testW3CBuilder() {
        String html = "<html xmlns:foo=\"http://example.com/\"><head><foo:bar xmlns:foo=\"http://example.com/\">Baz</foo:bar></head><body><div id=\"root\"><p>Hello World!</p></div></body></html>";
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse(html);

        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Element root = w3cDocument.getDocumentElement();
        assertEquals("html", root.getNodeName());

        NodeVisitor nodeVisitor = new W3CDom.W3CBuilder(w3cDocument);
        nodeVisitor.head(jsoupDocument.child(0), 0);

        Element htmlElement = (Element) root.getElementsByTagName("html").item(0);
        assertEquals("http://example.com/", htmlElement.getAttribute("xmlns:foo"));

        Element headElement = (Element) htmlElement.getElementsByTagName("head").item(0);
        Element barElement = (Element) headElement.getElementsByTagNameNS("http://example.com/", "bar").item(0);
        assertEquals("Baz", barElement.getTextContent());

        assertEquals(html, new W3CDom().asString(w3cDocument));
    }

    @Test
    public void testAttributes() {
        String html = "<html><head><meta charset=\"UTF-8\"><title>Test Title</title></head><body><div id=\"root\"><p>Hello World!</p></div></body></html>";
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse(html);

        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Element root = w3cDocument.getDocumentElement();
        Element htmlElement = (Element) root.getElementsByTagName("html").item(0);
        Element headElement = (Element) htmlElement.getElementsByTagName("head").item(0);
        Element metaElement = (Element) headElement.getElementsByTagName("meta").item(0);

        assertEquals("UTF-8", metaElement.getAttribute("charset"));
        assertEquals("Test Title", headElement.getElementsByTagName("title").item(0).getTextContent());

        assertEquals(html, new W3CDom().asString(w3cDocument));
    }

    @Test
    public void testCommentsAndTextNodes() {
        String html = "<html><head/><body><div id=\"root\"><p>Hello World!<!-- Comment --></p></div></body></html>";
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse(html);

        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Element root = w3cDocument.getDocumentElement();
        Element divElement = (Element) root.getElementsByTagName("div").item(0);
        Element pElement = (Element) divElement.getElementsByTagName("p").item(0);

        Text textNode = (Text) pElement.getFirstChild();
        assertEquals("Hello World!", textNode.getTextContent());

        Comment comment = (Comment) pElement.getLastChild();
        assertEquals(" Comment ", comment.getData());

        assertEquals(html, new W3CDom().asString(w3cDocument));
    }

    @Test
    public void testDataNodes() {
        String html = "<html><head/><body><div id=\"root\"><p data-foo=\"bar\">Hello World!</p></div></body></html>";
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse(html);

        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Element root = w3cDocument.getDocumentElement();
        Element divElement = (Element) root.getElementsByTagName("div").item(0);
        Element pElement = (Element) divElement.getElementsByTagName("p").item(0);
        Attr dataAttr = pElement.getAttributeNodes().item(0);

        assertEquals("bar", dataAttr.getNodeValue());

        assertEquals(html, new W3CDom().asString(w3cDocument));
    }

    @Test
    public void testSerializedDocument() {
        String html = "<html><head><title>Test Title</title></head><body><div id=\"root\"><p>Hello World!</p></div></body></html>";
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse(html);

        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        assertEquals(html, new W3CDom().asString(w3cDocument));

        String serializedDocument = new W3CDom().asString(w3cDocument);
        Document deserializedDocument = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse(serializedDocument));

        assertEquals(html, new W3CDom().asString(deserializedDocument));
    }

    @Test
    public void testEmptyDocument() {
        Document w3cDocument = new Document();
        String serializedDocument = new W3CDom().asString(w3cDocument);
        Document deserializedDocument = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse(serializedDocument));

        assertTrue(serializedDocument.isEmpty());
        assertTrue(deserializedDocument.getDocumentElement() == null);
    }
}