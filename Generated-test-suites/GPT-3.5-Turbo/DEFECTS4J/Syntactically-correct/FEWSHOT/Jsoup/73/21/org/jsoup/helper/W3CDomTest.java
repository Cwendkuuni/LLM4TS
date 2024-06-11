package org.jsoup.helper;

import org.jsoup.nodes.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Comment;
import org.junit.Test;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();

            org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
            Element root = builder.newDocument().createElement("root");
            jsoupDoc.appendChild(root);
            Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

            assertEquals(w3cDoc.getDocumentElement().getNodeName(), "root");

        } catch (ParserConfigurationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Element root = jsoupDoc.createElement("root");
        jsoupDoc.appendChild(root);

        Document w3cDoc = builder.newDocumentBuilder().newDocument();
        new W3CDom().convert(jsoupDoc, w3cDoc);
        assertEquals(w3cDoc.getDocumentElement().getTagName(), "root");
    }

    @Test
    public void testW3CBuilderHead() {
        Document w3cDoc = builder.newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        builder.head(sourceEl, 0);

        assertEquals(w3cDoc.getDocumentElement().getNodeName(), "div");
    }

    @Test
    public void testW3CBuilderTail() {
        Document w3cDoc = builder.newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        builder.head(sourceEl, 0);
        builder.tail(sourceEl, 0);

        assertEquals(w3cDoc.getDocumentElement().getNodeName(), "#document");
    }

    @Test
    public void testW3CBuilderHeadTextNode() {
        Document w3cDoc = builder.newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("Hello", "");
        builder.head(sourceText, 0);

        assertEquals(w3cDoc.getDocumentElement().getTextContent(), "Hello");
    }

    @Test
    public void testW3CBuilderHeadComment() {
        Document w3cDoc = builder.newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Comment sourceComment = new org.jsoup.nodes.Comment("Comment");
        builder.head(sourceComment, 0);

        assertEquals(w3cDoc.getDocumentElement().getNodeName(), "#comment");
    }

    @Test
    public void testW3CBuilderHeadDataNode() {
        Document w3cDoc = builder.newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.DataNode sourceData = new org.jsoup.nodes.DataNode("Data", "");
        builder.head(sourceData, 0);

        assertEquals(w3cDoc.getDocumentElement().getTextContent(), "Data");
    }

    @Test
    public void testCopyAttributesValid() {
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        sourceEl.attr("class", "testClass");

        Document w3cDoc = builder.newDocumentBuilder().newDocument();
        Element el = w3cDoc.createElementNS("", "element");

        new W3CDom().copyAttributes(sourceEl, el);

        assertEquals(el.getAttribute("class"), "testClass");
    }

    @Test
    public void testCopyAttributesInvalid() {
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        sourceEl.attr("class", "testClass");

        Document w3cDoc = builder.newDocumentBuilder().newDocument();
        Element el = w3cDoc.createElementNS("", "element");

        new W3CDom().copyAttributes(sourceEl, el);

        assertNull(el.getAttribute("class"));
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("div");
        el.attr("xmlns", "http://www.example.com");

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(builder.newDocumentBuilder().newDocument());
        String prefix = builder.updateNamespaces(el);

        assertEquals(builder.namespaces.get(prefix), "http://www.example.com");
    }

    @Test
    public void testAsString() {
        Document w3cDoc = builder.newDocumentBuilder().newDocument();
        Element root = w3cDoc.createElement("root");
        w3cDoc.appendChild(root);

        String w3cDocString = new W3CDom().asString(w3cDoc);

        assertEquals(w3cDocString, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><root></root>");
    }
}