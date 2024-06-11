
package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import static org.junit.Assert.*;

public class W3CDomTest {

    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;
    private DocumentBuilder documentBuilder;

    @Before
    public void setUp() throws Exception {
        w3cDom = new W3CDom();
        jsoupDoc = new org.jsoup.nodes.Document(jsoupDoc.location());
        documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }

    @Test
    public void fromJsoup() throws ParserConfigurationException {
        jsoupDoc.appendElement("body");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals(jsoupDoc.location(), w3cDoc.getDocumentURI());

        NodeList nodeList = w3cDoc.getElementsByTagName("html");
        assertEquals(1, nodeList.getLength());
        assertEquals("html", nodeList.item(0).getNodeName());

        nodeList = w3cDoc.getElementsByTagName("body");
        assertEquals(1, nodeList.getLength());
        assertEquals("body", nodeList.item(0).getNodeName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromJsoup_nullInput_throwsIllegalArgumentException() {
        w3cDom.fromJsoup(null);
    }

    @Test
    public void convert() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        jsoupDoc.appendElement("html")
                .appendChild(new Element("body"));

        Document w3cDoc = documentBuilder.newDocument();
        Method convertMethod = W3CDom.class.getDeclaredMethod("convert", org.jsoup.nodes.Document.class, Document.class);
        convertMethod.setAccessible(true);
        convertMethod.invoke(w3cDom, jsoupDoc, w3cDoc);

        assertNotNull(w3cDoc);
        assertEquals(jsoupDoc.location(), w3cDoc.getDocumentURI());

        NodeList nodeList = w3cDoc.getElementsByTagName("html");
        assertEquals(1, nodeList.getLength());
        assertEquals("html", nodeList.item(0).getNodeName());

        nodeList = w3cDoc.getElementsByTagName("body");
        assertEquals(1, nodeList.getLength());
        assertEquals("body", nodeList.item(0).getNodeName());
    }

    @Test
    public void copyAttributes() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("a");
        jsoupElement.attr("href", "http://example.com");
        jsoupElement.attr("id", "link");

        Element w3cElement = documentBuilder.newDocument().createElement("a");

        Method copyAttributesMethod = W3CDom.W3CBuilder.class.getDeclaredMethod("copyAttributes", org.jsoup.nodes.Node.class, Element.class);
        copyAttributesMethod.setAccessible(true);
        copyAttributesMethod.invoke(w3cDom.new W3CBuilder(null), jsoupElement, w3cElement);

        assertEquals("http://example.com", w3cElement.getAttribute("href"));
        assertEquals("link", w3cElement.getAttribute("id"));
        assertFalse(w3cElement.hasAttribute("undefined"));
    }

    @Test
    public void updateNamespaces() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("soap:Envelope");

        Method updateNamespacesMethod = W3CDom.W3CBuilder.class.getDeclaredMethod("updateNamespaces", org.jsoup.nodes.Element.class);
        updateNamespacesMethod.setAccessible(true);
        HashMap<String, String> namespaces = (HashMap<String, String>) updateNamespacesMethod.invoke(w3cDom.new W3CBuilder(null), jsoupElement);

        assertEquals(1, namespaces.size());
        assertEquals("soap", namespaces.keySet().toArray()[0]);
        assertEquals("Envelope", jsoupElement.tagName());
        assertEquals("", w3cDom.new W3CBuilder(null).dest.getTagName());
    }

    @Test
    public void asString() throws ParserConfigurationException {
        jsoupDoc.appendElement("html")
                .appendChild(new Element("body"));

        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);

        String xmlString = w3cDom.asString(w3cDoc);
        assertTrue(xmlString.contains("<html"));
        assertTrue(xmlString.contains("<body"));
    }
}
