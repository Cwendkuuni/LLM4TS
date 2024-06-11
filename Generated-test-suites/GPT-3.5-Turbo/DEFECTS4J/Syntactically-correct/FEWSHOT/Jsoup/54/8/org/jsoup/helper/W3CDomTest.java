package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.w3c.dom.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Text;
import org.jsoup.nodes.DataNode;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        W3CDom w3CDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.location("https://example.com");

        Element rootElement = new Element("root");
        rootElement.appendChild(new Comment("This is a comment"));
        rootElement.appendChild(new Text("This is a text node"));
        rootElement.appendChild(new DataNode("This is a data node"));
        Element childElement = new Element("child");
        childElement.attr("attr1", "value1");
        childElement.attr("attr2", "value2");
        rootElement.appendChild(childElement);
        jsoupDoc.appendChild(rootElement);

        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals("https://example.com", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvert() {
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);
        Document w3cDoc = w3CBuilder.doc;

        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Element rootElement = new Element("root");
        rootElement.appendChild(new Comment("This is a comment"));
        rootElement.appendChild(new Text("This is a text node"));
        rootElement.appendChild(new DataNode("This is a data node"));
        Element childElement = new Element("child");
        childElement.attr("attr1", "value1");
        childElement.attr("attr2", "value2");
        rootElement.appendChild(childElement);
        jsoupDoc.appendChild(rootElement);

        w3CBuilder.convert(jsoupDoc, w3cDoc);
        assertEquals(3, w3cDoc.getChildNodes().getLength());
        Element w3cRootElement = (Element) w3cDoc.getElementsByTagName("root").item(0);
        assertEquals(3, w3cRootElement.getChildNodes().getLength());
        assertEquals("This is a comment", w3cRootElement.getChildNodes().item(0).getNodeValue());
        assertEquals("This is a text node", w3cRootElement.getChildNodes().item(1).getNodeValue());
        assertEquals("This is a data node", w3cRootElement.getChildNodes().item(2).getNodeValue());
        Element w3cChildElement = (Element) w3cRootElement.getElementsByTagName("child").item(0);
        assertEquals(2, w3cChildElement.getAttributes().getLength());
        assertEquals("value1", w3cChildElement.getAttribute("attr1"));
        assertEquals("value2", w3cChildElement.getAttribute("attr2"));
    }

    @Test
    public void testCopyAttributes() {
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);
        Document w3cDoc = w3CBuilder.doc;

        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Element jsoupElement = new Element("element");
        jsoupElement.attr("attr1", "value1");
        jsoupElement.attr("attr2", "value2");
        jsoupDoc.appendChild(jsoupElement);

        Element w3cElement = w3cDoc.createElement("element");
        w3CBuilder.copyAttributes(jsoupElement, w3cElement);
        assertEquals(2, w3cElement.getAttributes().getLength());
        assertEquals("value1", w3cElement.getAttribute("attr1"));
        assertEquals("value2", w3cElement.getAttribute("attr2"));
    }

    @Test
    public void testUpdateNamespaces() {
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);

        org.jsoup.nodes.Element jsoupElement1 = new org.jsoup.nodes.Element("element1");
        jsoupElement1.attributes().put("xmlns", "urn1");
        w3CBuilder.updateNamespaces(jsoupElement1);
        HashMap<String, String> namespaces1 = w3CBuilder.namespaces;
        assertNotNull(namespaces1);
        assertEquals(1, namespaces1.size());
        assertEquals("urn1", namespaces1.get(""));

        org.jsoup.nodes.Element jsoupElement2 = new org.jsoup.nodes.Element("element2");
        jsoupElement2.attributes().put("xmlns:prefix1", "urn2");
        w3CBuilder.updateNamespaces(jsoupElement2);
        HashMap<String, String> namespaces2 = w3CBuilder.namespaces;
        assertNotNull(namespaces2);
        assertEquals(2, namespaces2.size());
        assertEquals("urn1", namespaces2.get(""));
        assertEquals("urn2", namespaces2.get("prefix1"));
    }

    @Test
    public void testAsString() throws IOException, SAXException {
        W3CDom w3CDom = new W3CDom();

        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Element rootElement = new Element("root");
        rootElement.appendChild(new Comment("This is a comment"));
        rootElement.appendChild(new Text("This is a text node"));
        rootElement.appendChild(new DataNode("This is a data node"));
        Element childElement = new Element("child");
        childElement.attr("attr1", "value1");
        childElement.attr("attr2", "value2");
        rootElement.appendChild(childElement);
        jsoupDoc.appendChild(rootElement);

        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        String xmlString = w3CDom.asString(w3cDoc);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document parsedDoc = builder.parse(new ByteArrayInputStream(xmlString.getBytes()));
            assertEquals("This is a comment", parsedDoc.getElementsByTagName("root").item(0).getChildNodes().item(0).getNodeValue());
            assertEquals("This is a text node", parsedDoc.getElementsByTagName("root").item(0).getChildNodes().item(1).getNodeValue());
            assertEquals("This is a data node", parsedDoc.getElementsByTagName("root").item(0).getChildNodes().item(2).getNodeValue());
            assertEquals("value1", parsedDoc.getElementsByTagName("child").item(0).getAttributes().getNamedItem("attr1").getNodeValue());
            assertEquals("value2", parsedDoc.getElementsByTagName("child").item(0).getAttributes().getNamedItem("attr2").getNodeValue());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}