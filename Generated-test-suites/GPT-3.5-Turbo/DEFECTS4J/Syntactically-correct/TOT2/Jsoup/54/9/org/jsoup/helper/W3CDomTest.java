package org.jsoup.helper;

import org.junit.Test;
import org.w3c.dom.Document;
import static org.junit.Assert.*;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        // Typical use case
        Document doc = new Document();
        W3CDom w3CDom = new W3CDom();
        Document w3cDoc = w3CDom.fromJsoup(doc);
        assertNotNull(w3cDoc);
        
        // Edge case: Null input
        try {
            w3CDom.fromJsoup(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testConvert() {
        // Typical use case
        W3CDom w3CDom = new W3CDom();
        Document jsoupDoc = new Document();
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        
        // Edge case: Empty location
        jsoupDoc.setBaseUri("");
        w3CDom.convert(jsoupDoc, w3cDoc);
        
        // Edge case: Non-empty location
        jsoupDoc.setBaseUri("www.example.com");
        w3CDom.convert(jsoupDoc, w3cDoc);
    }

    @Test
    public void testW3CBuilder() {
        // Typical use case
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(new Document());
        assertNotNull(w3cBuilder);

        // Edge case: Null input
        try {
            w3cBuilder.head(null, 0);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testAsString() {
        // Typical use case
        W3CDom w3CDom = new W3CDom();
        Document doc = new Document();
        String result = w3CDom.asString(doc);
        assertNotNull(result);
        
        // Edge case: Null input
        try {
            w3CDom.asString(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}