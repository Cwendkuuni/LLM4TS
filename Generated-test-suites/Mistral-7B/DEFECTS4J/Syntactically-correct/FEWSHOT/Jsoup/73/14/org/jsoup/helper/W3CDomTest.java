package org.jsoup.helper;

import static org.junit.Assert.*;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        String html = "<html><body><h1 id='title'>Test</h1></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        assertEquals(1, w3cDoc.getDocumentElement().getChildNodes().getLength());
        Element rootElement = w3cDoc.getDocumentElement();
        assertEquals("html", rootElement.getTagName());

        assertEquals(1, rootElement.getChildNodes().getLength());
        Element bodyElement = (Element) rootElement.getChildNodes().item(0);
        assertEquals("body", bodyElement.getTagName());

        assertEquals(1, bodyElement.getChildNodes().getLength());
        Element titleElement = (Element) bodyElement.getChildNodes().item(0);
        assertEquals("h1", titleElement.getTagName());
        assertEquals("Test", titleElement.getTextContent());
        assertEquals("title", titleElement.getAttribute("id"));
    }

    @Test
    public void testConvert() {
        String html = "<html><body><h1 id='title'>Test</h1></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new Document();
        new W3CDom().convert(jsoupDoc, w3cDoc);

        assertEquals(1, w3cDoc.getDocumentElement().getChildNodes().getLength());
        Element rootElement = w3cDoc.getDocumentElement();
        assertEquals("html", rootElement.getTagName());

        assertEquals(1, rootElement.getChildNodes().getLength());
        Element bodyElement = (Element) rootElement.getChildNodes().item(0);
        assertEquals("body", bodyElement.getTagName());

        assertEquals(1, bodyElement.getChildNodes().getLength());
        Element titleElement = (Element) bodyElement.getChildNodes().item(0);
        assertEquals("h1", titleElement.getTagName());
        assertEquals("Test", titleElement.getTextContent());
        assertEquals("title", titleElement.getAttribute("id"));
    }

    @Test
    public void testAsString() {
        String html = "<html><body><h1 id='title'>Test</h1></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        String w3cDocString = new W3CDom().asString(w3cDoc);

        String expectedHtml = "<html><body><h1 id='title'>Test</h1></body></html>";
        assertEquals(expectedHtml, w3cDocString);
    }
}