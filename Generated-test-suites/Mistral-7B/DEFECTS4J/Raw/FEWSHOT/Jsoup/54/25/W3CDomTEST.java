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
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1 id='title'>Test</h1></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        assertNotNull(w3cDocument);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1 id='title'>Test</h1></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        Element rootElement = w3cDocument.getDocumentElement();
        new W3CDom().convert(jsoupDocument, rootElement);
        assertNotNull(rootElement.getElementsByTagName("h1").item(0));
    }

    @Test
    public void testAsString() {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element rootElement = document.createElement("html");
        rootElement.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
        document.appendChild(rootElement);
        Element body = document.createElement("body");
        Element h1 = document.createElement("h1");
        h1.setTextContent("Test");
        body.appendChild(h1);
        rootElement.appendChild(body);

        Document w3cDocument = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse(new W3CDom().asString(document)));
        assertEquals("<html xmlns=\"http://www.w3.org/1999/xhtml\"><body><h1>Test</h1></body></html>", W3CDom.asString(w3cDocument));
    }
}