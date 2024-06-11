package org.jsoup.helper;

import org.junit.Test;
import org.w3c.dom.Document;

import static org.junit.Assert.*;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("<html><body><div>Test</div></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        assertNotNull(w3cDocument);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("<html><body><div>Test</div></body></html>");
        org.w3c.dom.Document w3cDocument = new org.w3c.dom.Document();
        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(jsoupDocument, w3cDocument);
        assertNotNull(w3cDocument);
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
        org.w3c.dom.Element w3cElement = new org.w3c.dom.Element();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cElement);
        w3cBuilder.copyAttributes(jsoupElement, w3cElement);
        assertFalse(w3cElement.getAttributes().isEmpty());
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(new org.w3c.dom.Element());
        String prefix = w3cBuilder.updateNamespaces(jsoupElement);
        assertEquals("", prefix);
    }

    @Test
    public void testAsString() {
        org.w3c.dom.Document w3cDocument = new org.w3c.dom.Document();
        W3CDom w3cDom = new W3CDom();
        String result = w3cDom.asString(w3cDocument);
        assertNotNull(result);
    }
}