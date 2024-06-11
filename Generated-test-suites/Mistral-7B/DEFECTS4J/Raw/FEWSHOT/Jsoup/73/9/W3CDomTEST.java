package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1 id='test'>Hello World</h1></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
        assertEquals("Hello World", w3cDoc.getDocumentElement().getElementsByTagName("h1").item(0).getTextContent());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1 id='test'>Hello World</h1></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        Document newW3cDoc = new Document();
        new W3CDom().convert(jsoupDoc, newW3cDoc);

        assertEquals(w3cDoc.getDocumentElement(), newW3cDoc.getDocumentElement());
    }

    @Test
    public void testAsString() {
        Document w3cDoc = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse("<html><body><h1 id='test'>Hello World</h1></body></html>"));
        String output = new W3CDom().asString(w3cDoc);

        assertNotNull(output);
        assertEquals("<html><body><h1 id='test'>Hello World</h1></body></html>", output);
    }
}