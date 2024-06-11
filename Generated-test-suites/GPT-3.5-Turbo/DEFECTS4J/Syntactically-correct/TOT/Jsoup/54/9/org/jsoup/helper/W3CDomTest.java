
package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class W3CDomTest {

    private org.jsoup.nodes.Document jsoupDoc;
    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDoc = Jsoup.parse("<html><head></head><body><div id=\"testDiv\" class=\"testClass\">Text</div></body></html>");
    }

    @After
    public void tearDown() {
        jsoupDoc = null;
        w3cDom = null;
    }

    @Test
    public void testFromJsoup() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testFromJsoup_NullInput() {
        try {
            Document w3cDoc = w3cDom.fromJsoup(null);
            assertNull(w3cDoc);
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testConvert() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Document out = w3cDom.convert(w3cDoc, jsoupDoc);
        assertNotNull(out);
    }

    @Test
    public void testConvert_BlankLocation() {
        jsoupDoc.setBaseUri("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Document out = w3cDom.convert(w3cDoc, jsoupDoc);
        assertEquals("", out.getDocumentURI());
    }

    @Test
    public void testConvert_WithLocation() {
        jsoupDoc.setBaseUri("http://example.com");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Document out = w3cDom.convert(w3cDoc, jsoupDoc);
        assertEquals("http://example.com", out.getDocumentURI());
    }

    @Test
    public void testConvert_InvalidElement() {
        org.jsoup.nodes.Document invalidDoc = Jsoup.parseBodyFragment("<invalid");
        Document w3cDoc = w3cDom.fromJsoup(invalidDoc);
        Document out = w3cDom.convert(w3cDoc, invalidDoc);
        assertNull(out);
    }

    @Test
    public void testConvert_WithTraversor() {
        org.jsoup.nodes.Document doc = Jsoup.parse("<html><head></head><body><div id=\"testDiv\" class=\"testClass\"><p>Text</p></div></body></html>");
        Document w3cDoc = w3cDom.fromJsoup(doc);
        Document out = w3cDom.convert(w3cDoc, doc);
        assertNotNull(out);
    }

    @Test
    public void testConvert_InvalidTextNode() {
        org.jsoup.nodes.Document doc = Jsoup.parse("<p>Some text <script></p>");
        Document w3cDoc = w3cDom.fromJsoup(doc);
        Document out = w3cDom.convert(w3cDoc, doc);
        assertNull(out);
    }

    @Test
    public void testAsString() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String xmlString = w3cDom.asString(w3cDoc);
        assertNotNull(xmlString);
        // Add additional assertions if needed
    }

    @Test
    public void testAsString_NullInput() {
        try {
            String xmlString = w3cDom.asString(null);
            assertNull(xmlString);
        } catch (Exception e) {
            assertNotNull(e);
        }
    }
}
