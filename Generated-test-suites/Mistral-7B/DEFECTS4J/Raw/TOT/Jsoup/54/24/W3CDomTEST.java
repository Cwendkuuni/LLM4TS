package org.jsoup.helper.test;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.rpc.encoding.XmlUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class W3CDomTest {

    @Test
    public void testEmptyDocument() {
        W3CDom w3cdom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.parser.Parser.parseEmpty();
        Document w3cDoc = w3cdom.fromJsoup(jsoupDoc);
        assertEquals("1.0", w3cDoc.getDoctype().getNodeValue());
        assertNull(w3cDoc.getDocumentElement());
    }

    @Test
    public void testSimpleDocument() {
        W3CDom w3cdom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.parser.Parser.parse("<html><head></head><body><p>Hello World</p></body></html>");
        Document w3cDoc = w3cdom.fromJsoup(jsoupDoc);

        assertEquals("html", w3cDoc.getDocumentElement().getNodeName());
        assertEquals("Head", w3cDoc.getDocumentElement().getElementsByTagName("head").item(0).getNodeName());
        assertEquals("Body", w3cDoc.getDocumentElement().getElementsByTagName("body").item(0).getNodeName());
        assertEquals("p", w3cDoc.getDocumentElement().getElementsByTagName("p").item(0).getNodeName());
        assertEquals("Hello World", w3cDoc.getDocumentElement().getElementsByTagName("p").item(0).getTextContent());

        String w3cXml = w3cdom.asString(w3cDoc);
        String expectedXml = "<html xmlns:xmlns=\"http://www.w3.org/2000/xmlns/\"><head></head><body><p>Hello World</p></body></html>";
        assertEquals(XmlUtil.parseString(expectedXml), XmlUtil.parseString(w3cXml));
    }

    @Test
    public void testNamespaces() {
        W3CDom w3cdom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.parser.Parser.parse("<html xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><xhtml:body><xhtml:p xmlns:foo=\"http://example.com\">Hello World</xhtml:p></xhtml:body></html>");
        Document w3cDoc = w3cdom.fromJsoup(jsoupDoc);

        assertEquals("html", w3cDoc.getDocumentElement().getNodeName());
        assertEquals("body", w3cDoc.getDocumentElement().getElementsByTagNameNS("http://www.w3.org/1999/xhtml", "body").item(0).getNodeName());
        assertEquals("p", w3cDoc.getDocumentElement().getElementsByTagNameNS("http://www.w3.org/1999/xhtml", "p").item(0).getNodeName());
        assertEquals("Hello World", w3cDoc.getDocumentElement().getElementsByTagNameNS("http://www.w3.org/1999/xhtml", "p").item(0).getTextContent());
        assertEquals("http://example.com", w3cDoc.getDocumentElement().getElementsByTagNameNS("http://www.w3.org/1999/xhtml", "p").item(0).getNamespaceURI());

        String w3cXml = w3cdom.asString(w3cDoc);
        String expectedXml = "<html xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><xhtml:html xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><xhtml:head></xhtml:head><xhtml:body xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><xhtml:p xmlns:foo=\"http://example.com\">Hello World</xhtml:p></xhtml:body></xhtml:html>";
        assertEquals(XmlUtil.parseString(expectedXml), XmlUtil.parseString(w3cXml));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullJsoupDocument() {
        W3CDom w3cdom = new W3CDom();
        w3cdom.fromJsoup(null);
    }

}