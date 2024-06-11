package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextDocument;
import org.junit.Test;
import org.w3c.dom.Document;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        Document jsoupDocument = new TextDocument();
        W3CDom w3CDom = new W3CDom();
        Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
        assertNotNull(w3cDocument);
    }

    @Test
    public void testConvert() {
        Document jsoupDocument = new TextDocument();
        W3CDom w3CDom = new W3CDom();
        Document w3cDocument = new Document();
        w3CDom.convert(jsoupDocument, w3cDocument);
        assertEquals(jsoupDocument.body().text(), w3cDocument.getDocumentElement().getTextContent());
    }

    @Test
    public void testAsString() {
        Document w3cDocument = w3CDom.fromJsoup(new TextDocument());
        String w3cString = w3CDom.asString(w3cDocument);
        assertEquals("", w3cString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupNullDocument() {
        W3CDom w3CDom = new W3CDom();
        w3CDom.fromJsoup(null);
    }

    @Test
    public void testConvertNullJsoupDocument() {
        W3CDom w3CDom = new W3CDom();
        Document w3cDocument = new Document();
        w3CDom.convert(null, w3cDocument);
    }

    @Test
    public void testConvertNullW3cDocument() {
        Document jsoupDocument = new TextDocument();
        W3CDom w3CDom = new W3CDom();
        w3CDom.convert(jsoupDocument, null);
    }

    @Test
    public void testAsStringNullDocument() {
        Document w3cDocument = new Document();
        W3CDom w3CDom = new W3CDom();
        String w3cString = w3CDom.asString(null);
    }

    @Test
    public void testConvertEmptyJsoupDocument() {
        Document jsoupDocument = new TextDocument();
        Document w3cDocument = new Document();
        W3CDom w3CDom = new W3CDom();
        w3CDom.convert(jsoupDocument, w3cDocument);
        assertEquals("", w3cDocument.getDocumentElement().getTextContent());
    }

    @Test(expected = TransformerException.class)
    public void testAsStringException() {
        Document w3cDocument = w3CDom.fromJsoup(new TextDocument());
        W3CDom w3CDom = new W3CDom();
        w3CDom.asString(w3cDocument);
    }

}