
package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

public class W3CDomTest {
    private org.jsoup.nodes.Document jsoupDoc;
    private W3CDom w3cDom;

    @Before
    public void setUp() throws Exception {
        jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>");
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        // add assertion for other properties of w3cDoc if necessary
    }

    @Test
    public void testConvert() {
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(false);
        jsoupDoc.outputSettings(outputSettings);
        w3cDom.convert(jsoupDoc, w3cDoc);
        Element rootElement = w3cDoc.getDocumentElement();
        assertNotNull(rootElement);
        // add assertion for other elements in the root element if necessary
    }

    @Test
    public void testAsString() {
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String w3cString = w3cDom.asString(w3cDoc);
        String expectedString = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        assertEquals(expectedString, w3cString);
    }

}
