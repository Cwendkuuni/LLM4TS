package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello world</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        assertNotNull(w3cDocument);
        assertEquals("<html><head><title>Test</title></head><body><p>Hello world</p></body></html>", asString(w3cDocument));
    }

    @Test
    public void testFromJsoupWithNamespace() {
        org.jsoup.nodes.Document nsJsoupDocument = org.jsoup.Jsoup.parse("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\"><head><title>Test</title></head><body xml:space=\"preserve\"><p xml:id=\"p1\">Hello world</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(nsJsoupDocument);
        assertNotNull(w3cDocument);
        assertEquals("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\"><head><title>Test</title></head><body xml:space=\"preserve\"><p xml:id=\"p1\">Hello world</p></body></html>", asString(w3cDocument));
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello world</p></body></html>");
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
        new W3CDom().convert(jsoupDocument, w3cDocument);
        assertNotNull(w3cDocument);
        assertEquals("<html><head><title>Test</title></head><body><p>Hello world</p></body></html>", asString(w3cDocument));
    }

    @Test
    public void testConvertWithNamespace() {
        org.jsoup.nodes.Document nsJsoupDocument = org.jsoup.Jsoup.parse("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\"><head><title>Test</title></head><body xml:space=\"preserve\"><p xml:id=\"p1\">Hello world</p></body></html>");
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
        new W3CDom().convert(nsJsoupDocument, w3cDocument);
        assertNotNull(w3cDocument);
        assertEquals("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\"><head><title>Test</title></head><body xml:space=\"preserve\"><p xml:id=\"p1\">Hello world</p></body></html>", asString(w3cDocument));
    }

    @Test
    public void testAsString() {
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
        Element root = w3cDocument.createElement("root");
        w3cDocument.appendChild(root);
        root.appendChild(w3cDocument.createTextNode("Hello world"));

        String string = new W3CDom().asString(w3cDocument);
        assertNotNull(string);
        assertEquals("Hello world", string.trim());
    }
}