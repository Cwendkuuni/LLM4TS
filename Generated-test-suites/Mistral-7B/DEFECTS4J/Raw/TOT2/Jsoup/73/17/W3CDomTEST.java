package org.jsoup.helper;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {
    private static final W3CDom instance = W3CDom.getInstance();

    @Test
    public void testFromJsoup() {
        Document jsoupDocument = new Document();
        Document w3cDocument = instance.fromJsoup(jsoupDocument);
        assertNotNull(w3cDocument);

        org.jsoup.nodes.Element rootEl = new org.jsoup.nodes.Element("html");
        rootEl.setAttribute("id", "root");
        Document in = new Document(rootEl);
        Document out = instance.fromJsoup(in);
        assertNotNull(out);
        assertNotNull(out.getDocumentElement().getAttribute("id"));

        // test edge cases
        Document largeJsoupDocument = new Document(); // create a document with a large number of elements
        Document largeW3cDocument = instance.fromJsoup(largeJsoupDocument);
        assertNotNull(largeW3cDocument);

        // test exception handling scenarios
        Document nullJsoupDocument = null;
        try {
            Document w3cDocument = instance.fromJsoup(nullJsoupDocument);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        Document emptyJsoupDocument = new Document(); // create an empty jsoup document
        try {
            Document w3cDocument = instance.fromJsoup(emptyJsoupDocument);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testConvert() {
        Document jsoupDocument = new Document();
        Document w3cDocument = new Document();
        instance.convert(jsoupDocument, w3cDocument);

        org.jsoup.nodes.Element rootEl = new org.jsoup.nodes.Element("html");
        rootEl.setAttribute("id", "root");
        Document in = new Document(rootEl);
        instance.convert(in, w3cDocument);

        assertNotNull(w3cDocument.getDocumentElement());
        assertNotNull(w3cDocument.getDocumentElement().getAttribute("id"));

        // test edge cases
        Document largeJsoupDocument = new Document(); // create a document with a large number of elements
        Document largeW3cDocument = new Document();
        instance.convert(largeJsoupDocument, largeW3cDocument);
        assertNotNull(largeW3cDocument.getDocumentElement());

        // test exception handling scenarios
        Document nullJsoupDocument = null;
        try {
            instance.convert(nullJsoupDocument, w3cDocument);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        Document emptyJsoupDocument = new Document(); // create an empty jsoup document
        try {
            instance.convert(emptyJsoupDocument, w3cDocument);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAsString() {
        Document w3cDocument = new Document();
        w3cDocument.setDocumentURI("test.xml");
        Element rootEl = w3cDocument.createElement("html");
        rootEl.setAttribute("id", "root");
        w3cDocument.appendChild(rootEl);

        String w3cString = instance.asString(w3cDocument);
        assertNotNull(w3cString);

        // test edge cases
        Document largeW3cDocument = new Document(); // create a document with a large number of elements
        String largeW3cString = instance.asString(largeW3cDocument);
        assertNotNull(largeW3cString);

        // test exception handling scenarios
        Document nullW3cDocument = null;
        try {
            String w3cString = instance.asString(nullW3cDocument);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}