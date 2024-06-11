package org.jsoup.helper;

import org.junit.Test;
import org.w3c.dom.Document;

import static org.junit.Assert.*;

public class W3CDomTest {

    // Test fromJsoup() method
    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        assertNotNull(w3cDoc);
    }
    
    // Test convert() method
    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = new Document();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.convert(jsoupDoc, w3cDoc);
        
        assertNotNull(w3cDoc);
    }
    
    // Test asString() method
    @Test
    public void testAsString() {
        Document w3cDoc = new Document();
        W3CDom w3cDom = new W3CDom();
        String w3cString = w3cDom.asString(w3cDoc);
        
        assertNotNull(w3cString);
    }

}