package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.XMLConstants;
import org.xml.namespace.QName;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

public class W3CDomTest {

    @Test
    public void testFromJsoup() throws ParserConfigurationException, JDOMException, TransformerException {
        String html = "<html><body><h1 id='foo'>Hello World</h1></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        assertEquals(XMLConstants.W3C_XML_NS_URI, w3cDoc.getDocumentElement().getNamespaceURI());
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());

        Element body = (Element) w3cDoc.getElementsByTagName("body").item(0);
        assertEquals("body", body.getTagName());

        Element h1 = (Element) w3cDoc.getElementsByTagName("h1").item(0);
        assertEquals("h1", h1.getTagName());
        assertEquals("Hello World", h1.getTextContent());
        assertEquals("foo", h1.getAttribute("id"));

        String xmlString = new XMLOutputter().outputString(w3cDoc);
        Document jdomDoc = org.jdom2.JDOM.load(new org.jdom2.input.StringBuilderBuilder(xmlString).getReader());
        Element jdomH1 = (Element) jdomDoc.getRootElement().getChild("body").getChild("h1");
        assertEquals("Hello World", jdomH1.getText());
        assertEquals("foo", jdomH1.getAttributeValue("id"));

        Document w3cDoc2 = new W3CDom().fromJsoup(jsoupDoc.clone());
        assertEquals(w3cDoc, w3cDoc2);
    }

    @Test
    public void testAsString() throws ParserConfigurationException, TransformerException {
        String html = "<html><body><h1 id='foo'>Hello World</h1></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        String w3cString = new W3CDom().asString(w3cDoc);

        String jsoupString = jsoupDoc.toString();
        assertEquals(jsoupString, w3cString);
    }

    @Test
    public void testEmptyDocument() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        String w3cString = new W3CDom().asString(w3cDoc);
        assertEquals("", w3cString);

        Document emptyW3CDoc = DocumentBuilderFactory.newInstance().newDocument();
        String emptyW3CString = new W3CDom().asString(emptyW3CDoc);
        assertEquals("", emptyW3CString);
    }

    @Test
    public void testNullDocument() {
        assertNull(new W3CDom().fromJsoup(null));
        assertNull(new W3CDom().asString(null));
    }

    @Test
    public void testConvert() throws ParserConfigurationException, TransformerException {
        String html = "<html><body><h1 id='foo'>Hello World</h1><script src='javascript:alert(\"Hello\")'></script></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        new W3CDom().convert(jsoupDoc, w3cDoc);

        assertSame(w3cDoc, new W3CDom().fromJsoup(jsoupDoc));

        Element script = (Element) w3cDoc.getElementsByTagName("script").item(0);
        assertNull(script);
    }
}