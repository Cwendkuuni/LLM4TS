package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><div id=\"content\">Hello World</div></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String w3cDocString = w3cDom.asString(w3cDoc);

        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html><head><title>Test</title></head><body><div id=\"content\">Hello World</div></body></html>";
        assertEquals(expected, w3cDocString);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><div id=\"content\">Hello World</div></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = Jsoup.parse("");
        w3cDom.convert(jsoupDoc, w3cDoc);
        String w3cDocString = w3cDom.asString(w3cDoc);

        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html><head><title>Test</title></head><body><div id=\"content\">Hello World</div></body></html>";
        assertEquals(expected, w3cDocString);
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><div id=\"content\">Hello World</div></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String w3cDocString = w3cDom.asString(w3cDoc);

        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html><head><title>Test</title></head><body><div id=\"content\">Hello World</div></body></html>";
        assertEquals(expected, w3cDocString);
    }
}