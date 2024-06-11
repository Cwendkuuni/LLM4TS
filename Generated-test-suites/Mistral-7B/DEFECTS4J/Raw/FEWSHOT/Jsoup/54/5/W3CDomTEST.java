package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        String html = "<html><head></head><body><h1 id='test'>Test</h1></body></html>";
        Document jsoupDocument = Jsoup.parse(html);

        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        assertNotNull(w3cDocument);

        assertEquals("html", w3cDocument.getDocumentElement().getTagName());
        Element headElement = w3cDocument.getDocumentElement().getElementsByTagName("head").item(0);
        assertNull(headElement);

        Element bodyElement = w3cDocument.getDocumentElement().getElementsByTagName("body").item(0);
        assertNotNull(bodyElement);

        Element h1Element = bodyElement.getElementsByTagName("h1").item(0);
        assertNotNull(h1Element);
        assertEquals("h1", h1Element.getTagName());
        assertEquals("Test", h1Element.getTextContent());
        assertEquals("test", h1Element.getAttribute("id"));

        Text textNode = bodyElement.getTextNodes().item(0);
        assertNotNull(textNode);
        assertEquals("", textNode.getNodeValue());

        Comment comment = bodyElement.getChildNodes().item(1);
        assertNotNull(comment);
        assertEquals("", comment.getNodeValue());
    }

    @Test
    public void testConvert() {
        String html = "<html><head></head><body><h1 id='test'>Test</h1></body></html>";
        Document jsoupDocument = Jsoup.parse(html);
        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        assertEquals("html", w3cDocument.getDocumentElement().getTagName());

        Element headElement = w3cDocument.getDocumentElement().getElementsByTagName("head").item(0);
        assertNull(headElement);

        Element bodyElement = w3cDocument.getDocumentElement().getElementsByTagName("body").item(0);
        assertNotNull(bodyElement);

        Element h1Element = bodyElement.getElementsByTagName("h1").item(0);
        assertNotNull(h1Element);
        assertEquals("h1", h1Element.getTagName());
        assertEquals("Test", h1Element.getTextContent());
        assertEquals("test", h1Element.getAttribute("id"));

        Text textNode = bodyElement.getTextNodes().item(0);
        assertNotNull(textNode);
        assertEquals("", textNode.getNodeValue());

        Comment comment = bodyElement.getChildNodes().item(1);
        assertNull(comment);
    }

    @Test
    public void testAsString() {
        String html = "<html><head><title>Test</title></head><body><h1 id='test'>Test</h1></body></html>";
        Document w3cDocument = W3CDom.fromJsoup(Jsoup.parse(html));

        String w3cString = new W3CDom().asString(w3cDocument);
        String expected = "<html><head><title>Test</title></head><body><h1 id='test'>Test</h1></body></html>";

        assertEquals(expected, w3cString);
    }
}