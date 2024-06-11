package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.w3c.dom.Document;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        String w3cDocumentString = new W3CDom().asString(w3cDocument);
        Assert.assertNotNull(w3cDocument);
        Assert.assertNotNull(w3cDocumentString);
    }

    @Test
    public void testConvert() {
        Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><p id='test'>Hello</p></body></html>");
        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);
        String w3cDocumentString = new W3CDom().asString(w3cDocument);
        Assert.assertNotNull(w3cDocument);
        Assert.assertNotNull(w3cDocumentString);
        Assert.assertEquals("<html><body><p id='test'>Hello</p></body></html>", w3cDocumentString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupNullInput() {
        new W3CDom().fromJsoup(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertNullInput() {
        new W3CDom().convert(null, new Document());
    }

    @Test
    public void testAsStringNullInput() {
        Document w3cDocument = new Document();
        String w3cDocumentString = new W3CDom().asString(w3cDocument);
        Assert.assertNull(w3cDocumentString);
    }

}