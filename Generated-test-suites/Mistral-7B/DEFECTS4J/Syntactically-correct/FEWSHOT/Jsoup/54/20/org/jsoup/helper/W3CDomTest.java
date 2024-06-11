package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>", w3cDoc.getDocumentElement().getTagName());
    }

    @Test
    public void testConvert() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocument();
        new W3CDom().convert(jsoupDoc, w3cDoc);
        assertNotNull(w3cDoc);
        assertEquals("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>", w3cDoc.getDocumentElement().getTagName());
    }

    @Test
    public void testAsString() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocument();
        w3cDoc.setDocumentURI("test.html");
        Element htmlElement = w3cDoc.createElement("html");
        Element headElement = w3cDoc.createElement("head");
        Element titleElement = w3cDoc.createElement("title");
        titleElement.setTextContent("Test");
        headElement.appendChild(titleElement);
        Element bodyElement = w3cDoc.createElement("body");
        Element pElement = w3cDoc.createElement("p");
        pElement.setTextContent("Hello World!");
        bodyElement.appendChild(pElement);
        htmlElement.appendChild(headElement);
        htmlElement.appendChild(bodyElement);
        w3cDoc.appendChild(htmlElement);
        String expected = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        String result = new W3CDom().asString(w3cDoc);
        assertEquals(expected, result);
    }
}