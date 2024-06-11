package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Traversal;
import org.w3c.dom.traversal.DocumentTraversal;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Title</title></head><body><h1>Heading</h1></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        assertNotNull(w3cDocument);
        assertEquals("Title", w3cDocument.getDocumentElement().getTagName());
        assertEquals("html", w3cDocument.getDocumentElement().getTagName());

        Traversal traversal = ((DocumentTraversal) w3cDocument);
        Element h1Element = (Element) traversal.querySelector(w3cDocument, "h1");
        assertNotNull(h1Element);
        assertEquals("Heading", h1Element.getTextContent());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Title</title></head><body><h1>Heading</h1></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        Document w3cDocumentConverted = new W3CDom().convert(jsoupDocument, w3cDocument);

        assertNotNull(w3cDocumentConverted);
        assertEquals(w3cDocument, w3cDocumentConverted);
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Title</title></head><body><h1>Heading</h1></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        String w3cDocumentAsString = new W3CDom().asString(w3cDocument);

        assertNotNull(w3cDocumentAsString);
        assertEquals("<html><head><title>Title</title></head><body><h1>Heading</h1></body></html>", w3cDocumentAsString);
    }

    @Test
    public void testStripLeadingAndTrailingComments() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><!--comment--><head><title>Title</title></head><body><h1>Heading</h1></body><!--comment--></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        Document w3cDocumentConverted = new W3CDom().convert(jsoupDocument, w3cDocument);

        Traversal traversal = ((DocumentTraversal) w3cDocumentConverted);
        for (Node child : traversal.getChildNodes(w3cDocumentConverted.getDocumentElement())) {
            if (child instanceof Comment) {
                traversal.removeChild(w3cDocumentConverted.getDocumentElement(), (Comment) child);
            }
        }

        String w3cDocumentAsString = new W3CDom().asString(w3cDocumentConverted);
        assertEquals("<html><head><title>Title</title></head><body><h1>Heading</h1></body></html>", w3cDocumentAsString);
    }
}