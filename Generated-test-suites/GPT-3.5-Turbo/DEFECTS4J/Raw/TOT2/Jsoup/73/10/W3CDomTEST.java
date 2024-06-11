package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.helper.W3CDom;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = new Document("<html><head></head><body></body></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertEquals("Document conversion from jsoup to w3c failed", w3cDoc.getElementsByTagName("html").getLength(), 1);
    }

    @Test
    public void testConvert() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = new Document("<html><head></head><body></body></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.convert(jsoupDoc, w3cDoc);
        assertEquals("Document conversion failed", w3cDoc.getElementsByTagName("body").getLength(), 1);
    }

    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = new Document("<html><head></head><body></body></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String w3cString = w3cDom.asString(w3cDoc);
        assertEquals("Document serialization failed", w3cString.contains("html"), true);
    }

    // Add additional test cases for edge cases and exceptions here
}