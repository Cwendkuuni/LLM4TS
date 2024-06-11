package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilderFactory;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.jsoup.parse("<html><head></head><body><p>Hello World</p></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() throws Exception {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.jsoup.parse("<html xmlns:html=\"http://www.w3.org/1999/xhtml\"><head></head><body xmlns:my=\"http://example.com/\"><p>Hello World</p></body></html>");
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocument();
        new W3CDom().convert(jsoupDoc, w3cDoc);

        Element htmlEl = (Element) w3cDoc.getDocumentElement();
        assertEquals("html", htmlEl.getTagName());
        assertEquals("http://www.w3.org/1999/xhtml", htmlEl.getNamespaceURI());

        Element bodyEl = (Element) htmlEl.getElementsByTagName("body").item(0);
        assertEquals("body", bodyEl.getTagName());
        assertEquals("http://www.w3.org/1999/xhtml", bodyEl.getNamespaceURI());

        Element pEl = (Element) bodyEl.getElementsByTagName("p").item(0);
        assertEquals("p", pEl.getTagName());
        assertEquals("", pEl.getNamespaceURI());

        Text textNode = (Text) pEl.getFirstChild();
        assertEquals("Hello World", textNode.getData());

        assertNull(w3cDoc.getDocumentURI());
    }

    @Test
    public void testAsString() throws Exception {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.jsoup.parse("<html><head></head><body><p>Hello World</p></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        String w3cDocAsString = new W3CDom().asString(w3cDoc);
        assertEquals("<html><head></head><body><p>Hello World</p></body></html>", w3cDocAsString);
    }
}