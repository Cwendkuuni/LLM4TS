package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Comment;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;

import javax.xml.parsers.ParserConfigurationException;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head/><body><p>Test</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        assertNotNull(w3cDocument);
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head/><body><p>Test</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        String w3cDocumentAsString = new W3CDom().asString(w3cDocument);
        assertEquals("<html><head/><body><p>Test</p></body></html>", w3cDocumentAsString);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html xmlns:foo=\"http://example.com/\"><head xmlns:bar=\"http://example2.com/\"><title>Test</title></head><body><p xmlns:baz=\"http://example3.com/\">Test</p></body></html>");
        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        assertNotNull(w3cDocument);
        Element html = w3cDocument.getDocumentElement();
        assertTrue(html.hasAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns"));
        assertTrue(html.hasAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:foo"));
        assertTrue(html.hasAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:bar"));
        Element head = (Element) html.getElementsByTagNameNS("http://www.w3.org/1999/xhtml", "head").item(0);
        assertTrue(head.hasAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:bar"));
        Element title = (Element) head.getElementsByTagNameNS("http://www.w3.org/1999/xhtml", "title").item(0);
        assertNull(title.getAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns"));

        Element body = html.getElementsByTagName("body").item(0);
        Element p = (Element) body.getElementsByTagNameNS("http://www.w3.org/1999/xhtml", "p").item(0);
        assertTrue(p.hasAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:baz"));
    }
}