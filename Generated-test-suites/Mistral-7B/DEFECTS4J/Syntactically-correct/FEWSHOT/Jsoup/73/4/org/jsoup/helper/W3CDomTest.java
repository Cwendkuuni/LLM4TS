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
import org.w3c.dom.processing.XMLProcessingInstruction;

public class W3CDomTest {

    private static Document fromJsoup(org.jsoup.nodes.Document in) {
        W3CDom w3cdom = new W3CDom();
        return w3cdom.fromJsoup(in);
    }

    @Test
    public void testFromJsoup() {
        String html = "<html><head/><body><p>Hello World!</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals(html, w3cdom.asString(w3cDoc));
    }

    @Test
    public void testConvert() {
        String html = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\"><head/><body><p xml:id=\"id1\">Hello World!</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new Document();
        W3CDom w3cdom = new W3CDom();
        w3cdom.convert(jsoupDoc, w3cDoc);
        assertNotNull(w3cDoc);
        assertEquals(html, w3cdom.asString(w3cDoc));

        Element htmlEl = w3cDoc.getDocumentElement();
        assertSame(htmlEl.getTagName(), "html");
        assertTrue(htmlEl.hasAttribute("xmlns:html"));
        assertEquals("http://www.w3.org/1999/xhtml", htmlEl.getAttribute("xmlns:html"));

        Element bodyEl = (Element) w3cDoc.getElementsByTagName("body").item(0);
        Element pEl = (Element) w3cDoc.getElementsByTagName("p").item(0);
        assertEquals(bodyEl.getChildNodes().getLength(), 1);
        assertSame(bodyEl.getChildNodes().item(0), pEl);

        assertEquals(pEl.getTextContent(), "Hello World!");
        assertNull(pEl.getAttribute("xml:id"));
    }

    @Test
    public void testConvertWithNamespace() {
        String html = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\"><head/><body><p xml:id=\"id1\">Hello World!</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new Document();
        W3CDom w3cdom = new W3CDom();
        w3cdom.convert(jsoupDoc, w3cDoc);

        Element htmlEl = w3cDoc.getDocumentElement();
        assertSame(htmlEl.getTagName(), "html");
        assertTrue(htmlEl.hasAttribute("xmlns:html"));
        assertEquals("http://www.w3.org/1999/xhtml", htmlEl.getAttribute("xmlns:html"));

        Element pEl = (Element) w3cDoc.getElementsByTagNameNS("http://www.w3.org/1999/xhtml", "p").item(0);
        assertSame(pEl.getTagName(), "p");
        assertTrue(pEl.hasAttributeNS("http://www.w3.org/1999/xhtml", "xml:id"));
        assertEquals("id1", pEl.getAttributeNS("http://www.w3.org/1999/xhtml", "xml:id"));
        assertEquals("Hello World!", pEl.getTextContent());
    }

    @Test
    public void testAsString() {
        String html = "<html><head/><body><p>Hello World!</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = fromJsoup(jsoupDoc);
        String result = w3cdom.asString(w3cDoc);
        assertEquals(html, result);

        html = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\"><head/><body><p xml:id=\"id1\">Hello World!</p></body></html>";
        jsoupDoc = org.jsoup.Jsoup.parse(html);
        w3cDoc = new Document();
        w3cdom.convert(jsoupDoc, w3cDoc);
        result = w3cdom.asString(w3cDoc);
        assertEquals(html, result);
    }

    @Test
    public void testEmptyDocument() {
        Document w3cDoc = new Document();
        String result = w3cdom.asString(w3cDoc);
        assertEquals("<document></document>", result);
    }

    @Test
    public void testComment() {
        String html = "<!-- This is a comment --><html><head/><body><p>Hello World!</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = fromJsoup(jsoupDoc);
        String result = w3cdom.asString(w3cDoc);
        assertEquals(html, result);

        Comment comment = (Comment) jsoupDoc.body().child(0);
        Element commentEl = (Element) w3cDoc.getElementsByTagName("comment").item(0);
        assertEquals(comment.getData(), commentEl.getTextContent());
    }

    @Test
    public void testProcessingInstruction() {
        String html = "<html xml-version=\"1.0\"<?xml-declaration type=\"public\"?>><head/><body><p>Hello World!</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = fromJsoup(jsoupDoc);
        String result = w3cdom.asString(w3cDoc);
        assertEquals(html, result);

        XMLProcessingInstruction xmlDecl = (XMLProcessingInstruction) jsoupDoc.child(0);
        Element xmlDeclEl = (Element) w3cDoc.getElementsByTagName("processing-instruction").item(0);
        assertEquals(xmlDecl.getData(), xmlDeclEl.getTextContent());
        assertEquals(xmlDecl.getTarget(), xmlDeclEl.getAttribute("xml-name"));
    }

    @Test
    public void testTextNode() {
        String html = "<html><head/><body><p>Hello World!</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = fromJsoup(jsoupDoc);
        String result = w3cdom.asString(w3cDoc);
        assertEquals(html, result);

        Text textNode = (Text) jsoupDoc.body().child(0).child(0);
        Text w3cTextNode = (Text) w3cDoc.getElementsByTagName("textNode").item(0);
        assertEquals(textNode.text(), w3cTextNode.getData());
    }
}