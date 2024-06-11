package org.jsoup.helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {

    private Document jsoupDocument;
    private Document w3cDocument;

    @Before
    public void setUp() {
        jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello world!</p></body></html>");
        w3cDocument = W3CDom.fromJsoup(jsoupDocument);
    }

    @After
    public void tearDown() {
        jsoupDocument = null;
        w3cDocument = null;
    }

    @Test
    public void testFromJsoup() {
        assertNotNull(w3cDocument);
    }

    @Test
    public void testConvert() {
        assertEquals(jsoupDocument.title().text(), w3cDocument.getDocumentElement().getElementsByTagName("title").item(0).getTextContent());
        assertEquals(jsoupDocument.body().text(), w3cDocument.getDocumentElement().getElementsByTagName("body").item(0).getTextContent());
    }

    @Test
    public void testAsString() {
        String w3cString = W3CDom.asString(w3cDocument);
        String jsoupString = jsoupDocument.toString();
        assertEquals(jsoupString, w3cString);
    }

    @Test
    public void testConvertWithNamespaces() {
        org.jsoup.nodes.Document inWithNamespaces = org.jsoup.Jsoup.parse("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><xhtml:html xmlns:html=\"http://www.w3.org/1999/xhtml\"><head><title>Test</title></head><body><p xmlns:foo=\"http://example.com\">Hello world!</p></body></xhtml:html></html>");
        Document w3cDocumentWithNamespaces = W3CDom.fromJsoup(inWithNamespaces);

        Element body = w3cDocumentWithNamespaces.getDocumentElement().getElementsByTagName("body").item(0);
        Element p = body.getElementsByTagName("p").item(0);
        assertEquals("http://example.com", p.getNamespaceURI());
        assertEquals("foo", p.getPrefix());
        assertEquals("Hello world!", p.getTextContent());

        String w3cStringWithNamespaces = W3CDom.asString(w3cDocumentWithNamespaces);
        String jsoupStringWithNamespaces = inWithNamespaces.toString();
        assertEquals(jsoupStringWithNamespaces, w3cStringWithNamespaces);
    }
}