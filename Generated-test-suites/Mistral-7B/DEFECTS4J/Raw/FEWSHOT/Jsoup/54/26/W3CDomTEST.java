package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.namespace.QName;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        String html = "<html><body><h1 id='title'>Test</h1><p>Hello, world!</p></body></html>";
        Document jsoupDoc = new TextDocument(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
        assertEquals(1, w3cDoc.getDocumentElement().getChildNodes().getLength());

        Element htmlNode = w3cDoc.getDocumentElement();
        assertEquals("html", htmlNode.getTagName());
        assertEquals("http://www.w3.org/1999/xhtml", htmlNode.getNamespaceURI());
        assertEquals("http://www.w3.org/1999/xhtml", htmlNode.getPrefix());

        assertEquals(1, htmlNode.getChildNodes().getLength());
        Element bodyNode = (Element) htmlNode.getFirstChild();
        assertEquals("body", bodyNode.getTagName());

        assertEquals(2, bodyNode.getChildNodes().getLength());
        Element titleNode = (Element) bodyNode.getChildNodes().item(0);
        assertEquals("h1", titleNode.getTagName());
        assertEquals("http://www.w3.org/1999/xhtml", titleNode.getNamespaceURI());
        assertEquals("", titleNode.getPrefix());
        assertEquals("title", titleNode.getAttribute("id"));

        Element pNode = (Element) bodyNode.getChildNodes().item(1);
        assertEquals("p", pNode.getTagName());
        assertEquals("http://www.w3.org/1999/xhtml", pNode.getNamespaceURI());
        assertEquals("", pNode.getPrefix());
        assertEquals("Hello, world!", pNode.getTextContent());
    }

    @Test
    public void testConvert() {
        String html = "<html xmlns:html='http://www.w3.org/1999/xhtml'><body><h1 id='title'>Test</h1><p xml:id='p'>Hello, world!</p></body></html>";
        Document jsoupDoc = new TextDocument(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        Element rootEl = (Element) w3cDoc.getDocumentElement();
        W3CDom.convert(jsoupDoc, w3cDoc);

        assertEquals("html", rootEl.getTagName());
        assertEquals("http://www.w3.org/1999/xhtml", rootEl.getNamespaceURI());
        assertEquals("html", rootEl.getPrefix());

        assertEquals(1, rootEl.getChildNodes().getLength());
        Element bodyNode = (Element) rootEl.getFirstChild();
        assertEquals("body", bodyNode.getTagName());

        assertEquals(2, bodyNode.getChildNodes().getLength());
        Element titleNode = (Element) bodyNode.getChildNodes().item(0);
        assertEquals("h1", titleNode.getTagName());
        assertEquals("http://www.w3.org/1999/xhtml", titleNode.getNamespaceURI());
        assertEquals("html", titleNode.getPrefix());
        assertEquals("title", titleNode.getAttribute("id"));

        Element pNode = (Element) bodyNode.getChildNodes().item(1);
        assertEquals("p", pNode.getTagName());
        assertEquals("http://www.w3.org/1999/xhtml", pNode.getNamespaceURI());
        assertEquals("xml", pNode.getPrefix());
        assertEquals("p", pNode.getAttribute("xml:id"));
        assertEquals("Hello, world!", pNode.getTextContent());
    }

    @Test
    public void testAsString() {
        String html = "<html xmlns:html='http://www.w3.org/1999/xhtml'><body><h1 id='title'>Test</h1><p xml:id='p'>Hello, world!</p></body></html>";
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocument();
        new W3CDom().convert(new TextDocument(html), w3cDoc);

        Document w3cDocWithNamespaces = new W3CDom().fromJsoup(new TextDocument(html));

        assertEquals(w3cDocWithNamespaces.getDocumentElement().getDocumentURI(), w3cDoc.getDocumentURI());
        assertEquals(new W3CDom().asString(w3cDoc), new W3CDom().asString(w3cDocWithNamespaces));
    }

    @Test
    public void testNamespaces() {
        String html = "<html xmlns:html='http://www.w3.org/1999/xhtml' xmlns:xlink='http://www.w3.org/1999/xlink'><body><a xmlns:xlink:href='foo.html' href='foo.html'>Link</a></body></html>";
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocument();
        new W3CDom().convert(new TextDocument(html), w3cDoc);

        Document w3cDocWithNamespaces = new W3CDom().fromJsoup(new TextDocument(html));

        Element linkNode = (Element) w3cDocWithNamespaces.getDocumentElement().getElementsByTagName("a").item(0);
        assertEquals("http://www.w3.org/1999/xlink", linkNode.getNamespaceURI());
        assertEquals("xlink", linkNode.getPrefix());
        assertEquals("http://www.w3.org/1999/xlink", linkNode.getElementsByAttribute(new QName("href")).item(0).getNamespaceURI());
        assertEquals("", linkNode.getElementsByAttribute(new QName("href")).item(0).getPrefix());
        assertEquals("href", linkNode.getElementsByAttribute(new QName("href")).item(0).getLocalName());
        assertEquals("foo.html", linkNode.getElementsByAttribute(new QName("href")).item(0).getTextContent());
    }

    @Test
    public void testEmptyDocument() {
        Document w3cDoc = new W3CDom().fromJsoup(new TextDocument(""));
        assertEquals("", new W3CDom().asString(w3cDoc));
    }
}