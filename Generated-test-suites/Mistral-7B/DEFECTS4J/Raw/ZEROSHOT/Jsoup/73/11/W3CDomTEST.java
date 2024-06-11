package org.jsoup.helper;

import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.junit.Assert.*;

import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {

    private W3CDom w3cdom = new W3CDom();

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        Document w3cDocument = w3cdom.fromJsoup(jsoupDocument);

        assertNotNull(w3cDocument);
        assertEquals("<html xmlns:xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\"><head><title>Test</title></head><body><p>Hello World</p></body></html>", asString(w3cDocument));
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        Document w3cDocument = w3cdom.fromJsoup(jsoupDocument);
        Document convertedDocument = new Document();
        w3cdom.convert(w3cDocument, convertedDocument);

        assertNotNull(convertedDocument);
        assertEquals("<html xmlns:xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\"><head><title>Test</title></head><body><p>Hello World</p></body></html>", asString(w3cDocument));
        assertEquals("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", asString(convertedDocument));
    }

    @Test
    public void testAsString() {
        Document w3cDocument = w3cdom.fromJsoup(org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>"));
        String w3cDocumentString = w3cdom.asString(w3cDocument);

        assertNotNull(w3cDocumentString);
        assertEquals("<html xmlns:xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\"><head><title>Test</title></head><body><p>Hello World</p></body></html>", w3cDocumentString);
    }

    private String asString(Document document) {
        return new String(w3cdom.asString(document).getBytes());
    }
}