package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import static org.junit.Assert.assertEquals;

public class W3CDomTest {
    private W3CDom w3CDom;
    private Document jsoupDoc;

    @Before
    public void setup() {
        w3CDom = new W3CDom();
        jsoupDoc = Jsoup.parse("<html><head></head><body><div></div></body></html>");
    }

    @Test
    public void testFromJsoup() {
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        Element rootElement = w3cDoc.getDocumentElement();
        assertEquals("html", rootElement.getTagName());
    }

    @Test
    public void testConvert() {
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        org.w3c.dom.Document out = w3CDom.convert(jsoupDoc, w3cDoc);
        assertEquals(jsoupDoc.html(), w3CDom.asString(out));
    }
}