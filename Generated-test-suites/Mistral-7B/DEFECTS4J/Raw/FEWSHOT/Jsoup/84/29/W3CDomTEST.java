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

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head></head><body><div id='test'>Hello World</div></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        assertNotNull(w3cDocument);
        assertEquals("html", w3cDocument.getDocumentElement().getTagName());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head></head><body><div id='test'>Hello World</div></body></html>");
        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);
        assertNotNull(w3cDocument.getDocumentElement());
        assertEquals("html", w3cDocument.getDocumentElement().getTagName());
    }

    @Test
    public void testW3CBuilder() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html xmlns:html='http://www.w3.org/1999/xhtml' xmlns:x='http://www.w3.org/2001/XMLSchema-instance' xmlns='http://www.w3.org/1999/xhtml'><head></head><body><div id='test' xmlns:html='http://www.w3.org/1999/xhtml'>Hello World</div></body></html>");
        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Element htmlElement = w3cDocument.getDocumentElement();
        assertEquals("html:html", htmlElement.getTagName());
        assertSame(w3cDocument, htmlElement.getOwnerDocument());

        Element headElement = (Element) htmlElement.getElementsByTagName("head").item(0);
        assertNotNull(headElement);

        Element bodyElement = (Element) htmlElement.getElementsByTagName("body").item(0);
        assertNotNull(bodyElement);

        Element divElement = (Element) bodyElement.getElementsByTagName("div").item(0);
        assertNotNull(divElement);
        assertEquals("html:div", divElement.getTagName());
        assertEquals("test", divElement.getAttribute("id"));

        Text textElement = (Text) divElement.getFirstChild();
        assertNotNull(textElement);
        assertEquals("Hello World", textElement.getData());

        Comment commentElement = (Comment) divElement.getElementsByTagName("comment").item(0);
        assertNull(commentElement);

        NodeVisitor visitor = new W3CDom.W3CBuilder(w3cDocument);
        org.jsoup.nodes.Element jsoupRoot = jsoupDocument.child(0);
        visitor.head(jsoupRoot, 0);
        assertEquals("html:html", w3cDocument.getDocumentElement().getTagName());
    }

    @Test
    public void testAsString() {
        Document document = new Document();
        document.appendChild(document.createTextNode("Hello World"));
        String w3cString = new W3CDom().asString(document);
        assertEquals("<html><head/><body><textNode>Hello World</textNode></body></html>", w3cString);
    }
}