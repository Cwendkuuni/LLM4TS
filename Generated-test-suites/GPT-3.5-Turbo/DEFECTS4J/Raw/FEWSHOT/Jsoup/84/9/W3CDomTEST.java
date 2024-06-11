package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {
    W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        Document jsoupDoc = Jsoup.parse("<html><head></head><body><p>Hello, world!</p></body></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        Element rootElement = w3cDoc.getDocumentElement();
        assertNotNull(rootElement);
        assertEquals("html", rootElement.getTagName());
    }

    @Test
    public void testConvert() {
        Document jsoupDoc = Jsoup.parse("<html><head></head><body><p>Hello, world!</p></body></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        org.jsoup.nodes.Element jsoupElement = jsoupDoc.select("p").first();
        assertNotNull(jsoupElement);
        Element w3cElement = w3cDoc.createElement("p");
        w3cDom.convert(jsoupElement, w3cElement);
        assertNotNull(w3cElement);
        assertEquals("p", w3cElement.getTagName());
        assertEquals("Hello, world!", ((Text)w3cElement.getChildNodes().item(0)).getData());
    }

    @Test
    public void testAsString() {
        Document jsoupDoc = Jsoup.parse("<html><head></head><body><p>Hello, world!</p></body></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        String w3cDocString = w3cDom.asString(w3cDoc);
        assertNotNull(w3cDocString);
        assertEquals("<html><head></head><body><p>Hello, world!</p></body></html>", w3cDocString);
    }
}