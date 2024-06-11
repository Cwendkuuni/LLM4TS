package org.jsoup.helper;

import org.junit.Test;
import org.w3c.dom.*;

import static org.junit.Assert.*;


public class W3CDomTest {
    
    // Test for fromJsoup() method
    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        
        assertNotNull(w3cDoc);
    }
    
    // Test for convert() method
    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element(Tag.valueOf("div"), "");
        jsoupDoc.appendChild(jsoupEl);
        
        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        // Check if the conversion was successful by checking if the elements are the same
        Element w3cEl = w3cDoc.getDocumentElement();
        assertEquals(jsoupEl.tagName(), w3cEl.getTagName());
    }
    
    // Test for asString() method
    @Test
    public void testAsString() {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        String docString = new W3CDom().asString(doc);
        
        assertNotNull(docString);
    }
}