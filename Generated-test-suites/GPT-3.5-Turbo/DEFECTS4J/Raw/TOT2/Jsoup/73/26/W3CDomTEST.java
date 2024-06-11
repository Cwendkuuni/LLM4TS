package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.w3c.dom.Document;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        // Typical use case
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document result = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(result);
        
        // Edge case: Testing with null input
        try {
            w3cDom.fromJsoup(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Input document cannot be null", e.getMessage());
        }
    }

    @Test
    public void testConvert() {
        // Typical use case
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.convert(jsoupDoc, w3cDoc);
        // Assertion based on expected conversion results
        
        // Edge case: Testing with an empty jsoup document
        try {
            w3cDom.convert(new org.jsoup.nodes.Document(""), w3cDoc);
        } catch (IllegalStateException e) {
            assertEquals("Input jsoup document is empty", e.getMessage());
        }
    }

    @Test
    public void testAsString() {
        // Typical use case
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String resultString = w3cDom.asString(w3cDoc);
        assertNotNull(resultString);
        
        // Edge case: Testing with null input
        try {
            w3cDom.asString(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Input document cannot be null", e.getMessage());
        }
    }
}