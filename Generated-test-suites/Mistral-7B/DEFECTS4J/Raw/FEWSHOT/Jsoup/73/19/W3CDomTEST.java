package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello</p></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello</p></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        new W3CDom().convert(jsoupDoc, w3cDoc);

        Element rootElement = (Element) w3cDoc.getDocumentElement();
        assertEquals("html", rootElement.getTagName());

        Element titleElement = (Element) rootElement.getElementsByTagName("title").item(0);
        assertEquals("Test", titleElement.getTextContent());

        Element bodyElement = (Element) rootElement.getElementsByTagName("body").item(0);
        Element pElement = (Element) bodyElement.getElementsByTagName("p").item(0);
        assertEquals("Hello", pElement.getTextContent());
    }

    @Test
    public void testSerilizeToString() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello</p></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        String w3cDocAsString = new W3CDom().asString(w3cDoc);

        org.jsoup.nodes.Document jsoupDocFromString = org.jsoup.Jsoup.parse(w3cDocAsString);

        assertEquals(jsoupDoc.html(), jsoupDocFromString.html());
    }

    @Test
    public void testHandleNamespaces() {
        String xmlNs = "xmlns:x";
        String xmlNsValue = "http://example.com/namespace";

        org.jsoup.nodes.Element element = org.jsoup.Jsoup.parse("<root xmlns:" + xmlNs + "=\"" + xmlNsValue + "\"><x:child>Hello</x:child></root>").child(0);
        Document w3cDoc = new W3CDom().fromJsoup(element.parent());

        Element root = (Element) w3cDoc.getDocumentElement();
        assertEquals("root", root.getTagName());
        assertEquals(xmlNsValue, root.getAttribute(xmlNs));

        Element child = (Element) root.getElementsByTagNameNS(xmlNsValue, "child").item(0);
        assertEquals("Hello", child.getTextContent());
    }

    @Test
    public void testHandleComments() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><!-- Test --><body></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        new W3CDom().convert(jsoupDoc, w3cDoc);

        Comment comment = (Comment) w3cDoc.getDocumentElement().getElementsByTagName("comment").item(0);
        assertEquals("Test", comment.getData());
    }

    @Test
    public void testHandleTextNodes() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><p>Hello World</p></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        new W3CDom().convert(jsoupDoc, w3cDoc);

        Text textNode = (Text) w3cDoc.getDocumentElement().getElementsByTagName("p").item(0).getFirstChild();
        assertEquals("Hello World", textNode.getData());
    }
}