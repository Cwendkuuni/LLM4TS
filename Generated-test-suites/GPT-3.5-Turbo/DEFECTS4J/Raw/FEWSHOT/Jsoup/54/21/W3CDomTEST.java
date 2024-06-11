package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.helper.W3CDom;
import org.jsoup.Jsoup;

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><h1>Title</h1><p>Paragraph</p></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String actual = w3cDom.asString(w3cDoc);
        
        assertEquals(expected, actual);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<div><h1>Title</h1><p>Paragraph</p></div>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = Jsoup.parse("");
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><div><h1>Title</h1><p>Paragraph</p></div>";
        String actual = w3cDom.asString(w3cDoc);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAsString() {
        Document w3cDoc = Jsoup.parse("<p>Text</p>");
        W3CDom w3cDom = new W3CDom();
        String actual = w3cDom.asString(w3cDoc);
        
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><p>Text</p>";
        
        assertEquals(expected, actual);
    }
}