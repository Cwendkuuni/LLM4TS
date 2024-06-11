package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.w3c.dom.Document;

public class W3CDomTest {
    
    private W3CDom w3cDom = new W3CDom();
    
    @Test
    public void testFromJsoupNotNull(){
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testFromJsoupIllegalStateException(){
        w3cDom.factory = null;
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
    }
    
    @Test
    public void testConvert(){
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.convert(jsoupDoc, w3cDoc);
    }
    
    @Test
    public void testAsString(){
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String str = w3cDom.asString(w3cDoc);
        assertNotNull(str);
    }
}