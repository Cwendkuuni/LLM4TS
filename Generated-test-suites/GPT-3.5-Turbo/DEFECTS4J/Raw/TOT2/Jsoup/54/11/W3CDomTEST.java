package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.helper.W3CDom;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;

import static org.junit.Assert.*;

public class W3CDomTest {

    private W3CDom w3CDom = new W3CDom();

    @Test
    public void testFromJsoup() {
        Document jsoupDoc = new Document("");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        // Add more specific tests for edge cases or error scenarios if needed
    }

    @Test
    public void testConvert() {
        Document jsoupDoc = new Document("");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        w3CDom.convert(jsoupDoc, w3cDoc);
        // Add more specific tests for edge cases or error scenarios if needed
    }

    @Test
    public void testAsString() {
        Document jsoupDoc = new Document("");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        String w3cDocString = w3CDom.asString(w3cDoc);
        assertNotNull(w3cDocString);
        // Add more specific tests for edge cases or error scenarios if needed
    }

}