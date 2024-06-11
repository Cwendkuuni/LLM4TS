package org.apache.commons.cli;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.helper.W3CDom.fromJsoup(org.jsoup.Jsoup.parse("<html><head></head><body></body></html>"));
        Document w3cDocument = W3CDom.fromJsoup(jsoupDocument);
        assertNotNull(w3cDocument);
        assertEquals("<html><head></head><body></body></html>", W3CDom.asString(w3cDocument));
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.helper.W3CDom.fromJsoup(org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p id='test'>Hello World</p></body></html>"));
        Document w3cDocument = W3CDom.fromJsoup(jsoupDocument);
        Element rootElement = w3cDocument.getDocumentElement();

        W3CDom.convert(jsoupDocument, w3cDocument);

        assertEquals("title", rootElement.getElementsByTagName("title").item(0).getTagName());
        Element bodyElement = rootElement.getElementsByTagName("body").item(0);
        assertEquals("p", bodyElement.getElementsByTagName("p").item(0).getTagName());
        assertEquals("test", bodyElement.getElementsByTagName("p").item(0).getAttribute("id"));
        assertEquals("Hello World", bodyElement.getElementsByTagName("p").item(0).getTextContent());
    }

    @Test
    public void testConvertWithNamespaces() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.helper.W3CDom.fromJsoup(org.jsoup.Jsoup.parse("<html xmlns:xhtml='http://www.w3.org/1999/xhtml'><head></head><body><p xmlns:my='http://example.com'>Hello World</p></body></html>"));
        Document w3cDocument = W3CDom.fromJsoup(jsoupDocument);
        Element rootElement = w3cDocument.getDocumentElement();

        W3CDom.convert(jsoupDocument, w3cDocument);

        Element pElement = (Element) rootElement.getElementsByTagNameNS("http://example.com", "my:p").item(0);
        assertEquals("p", pElement.getTagName());
        assertEquals("Hello World", pElement.getTextContent());
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.helper.W3CDom.fromJsoup(org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p id='test'>Hello World</p></body></html>"));
        Document w3cDocument = W3CDom.fromJsoup(jsoupDocument);
        String w3cString = W3CDom.asString(w3cDocument);
        assertEquals("<html><head><title>Test</title></head><body><p id='test'>Hello World</p></body></html>", w3cString);
    }
}