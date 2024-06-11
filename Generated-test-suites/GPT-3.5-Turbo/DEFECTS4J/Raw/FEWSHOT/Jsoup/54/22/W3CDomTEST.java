package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.w3c.dom.Document;

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        // Test with empty jsoup document
        org.jsoup.nodes.Document emptyDoc = org.jsoup.Jsoup.parse("");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(emptyDoc);
        assertNotNull(w3cDoc);

        // Test with jsoup document containing an element
        org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse("<p>Hello World</p>");
        w3cDoc = w3cDom.fromJsoup(doc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        // Test with empty jsoup document
        org.jsoup.nodes.Document emptyDoc = org.jsoup.Jsoup.parse("");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(emptyDoc);
        assertNotNull(w3cDoc);

        // Test with jsoup document containing an element
        org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse("<p>Hello World</p>");
        w3cDoc = w3cDom.fromJsoup(doc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testAsString() {
        // Test with empty w3c document
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(org.jsoup.Jsoup.parse(""));
        String w3cString = w3cDom.asString(w3cDoc);
        assertEquals("", w3cString);

        // Test with w3c document containing an element
        org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse("<p>Hello World</p>");
        w3cDoc = w3cDom.fromJsoup(doc);
        w3cString = w3cDom.asString(w3cDoc);
        assertNotNull(w3cString);
    }
}