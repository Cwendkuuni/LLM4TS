package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Text;

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
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("http://example.com");
        Element root = new Element("html");
        jsoupDoc.appendChild(root);

        Element body = new Element("body");
        root.appendChild(body);

        Element p = new Element("p");
        p.appendText("This is a paragraph.");
        body.appendChild(p);

        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertEquals("http://example.com", w3cDoc.getDocumentURI());
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
        assertEquals("body", w3cDoc.getDocumentElement().getChildNodes().item(0).getNodeName());
        assertEquals("p", w3cDoc.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getNodeName());
        assertTrue(w3cDoc.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0) instanceof Text);
        assertEquals("This is a paragraph.", w3cDoc.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getNodeValue());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("http://example.com");
        Element root = new Element("html");
        jsoupDoc.appendChild(root);

        Element body = new Element("body");
        root.appendChild(body);

        Element p = new Element("p");
        p.appendText("This is a paragraph.");
        body.appendChild(p);

        Document w3cDoc = createEmptyW3CDocument();
        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(jsoupDoc, w3cDoc);

        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
        assertEquals("body", w3cDoc.getDocumentElement().getChildNodes().item(0).getNodeName());
        assertEquals("p", w3cDoc.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getNodeName());
        assertTrue(w3cDoc.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0) instanceof Text);
        assertEquals("This is a paragraph.", w3cDoc.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getNodeValue());
    }

    @Test
    public void testW3CBuilderHead() {
        Document w3cDoc = createEmptyW3CDocument();
        Element root = w3cDoc.createElement("root");
        w3cDoc.appendChild(root);
        Element parent = w3cDoc.createElement("parent");
        root.appendChild(parent);

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("element");
        sourceEl.attributes().put("attribute", "value");

        builder.head(sourceEl, 0);

        Element destEl = (Element) parent.getChildNodes().item(0);
        assertEquals("element", destEl.getTagName());
        assertEquals("value", destEl.getAttribute("attribute"));
    }

    @Test
    public void testW3CBuilderTail() {
        Document w3cDoc = createEmptyW3CDocument();
        Element root = w3cDoc.createElement("root");
        w3cDoc.appendChild(root);
        Element parent = w3cDoc.createElement("parent");
        root.appendChild(parent);

        Element child = w3cDoc.createElement("child");
        parent.appendChild(child);

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("element");
        sourceEl.attributes().put("attribute", "value");

        builder.head(sourceEl, 0);
        builder.tail(sourceEl, 0);

        assertEquals(parent, builder.dest);
    }

    @Test
    public void testW3CBuilderHeadTextNode() {
        Document w3cDoc = createEmptyW3CDocument();
        Element root = w3cDoc.createElement("root");
        w3cDoc.appendChild(root);
        Element parent = w3cDoc.createElement("parent");
        root.appendChild(parent);

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("text");

        builder.head(sourceText, 0);

        assertTrue(parent.getChildNodes().item(0) instanceof TextNode);
        assertEquals("text", parent.getChildNodes().item(0).getNodeValue());
    }

    @Test
    public void testW3CBuilderHeadComment() {
        Document w3cDoc = createEmptyW3CDocument();
        Element root = w3cDoc.createElement("root");
        w3cDoc.appendChild(root);
        Element parent = w3cDoc.createElement("parent");
        root.appendChild(parent);

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Comment sourceComment = new org.jsoup.nodes.Comment("comment");

        builder.head(sourceComment, 0);

        assertTrue(parent.getChildNodes().item(0) instanceof Comment);
        assertEquals("comment", ((Comment) parent.getChildNodes().item(0)).getData());
    }

    @Test
    public void testW3CBuilderHeadDataNode() {
        Document w3cDoc = createEmptyW3CDocument();
        Element root = w3cDoc.createElement("root");
        w3cDoc.appendChild(root);
        Element parent = w3cDoc.createElement("parent");
        root.appendChild(parent);

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.DataNode sourceData = new org.jsoup.nodes.DataNode("data");

        builder.head(sourceData, 0);

        assertTrue(parent.getChildNodes().item(0) instanceof Text);
        assertEquals("data", parent.getChildNodes().item(0).getNodeValue());
    }

    @Test
    public void testCopyAttributes() {
        Document w3cDoc = createEmptyW3CDocument();
        Element parent = w3cDoc.createElement("parent");
        w3cDoc.appendChild(parent);
        Element child = w3cDoc.createElement("child");
        parent.appendChild(child);

        org.jsoup.nodes.Element source = new org.jsoup.nodes.Element("element");
        source.attributes().put("attribute1", "value1");
        source.attributes().put("attribute2", "value2");

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.copyAttributes(source, child);

        assertEquals("value1", child.getAttribute("attribute1"));
        assertEquals("value2", child.getAttribute("attribute2"));
    }

    private Document createEmptyW3CDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            return builder.newDocument();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void testAsString() {
        Document w3cDoc = createEmptyW3CDocument();
        Element root = w3cDoc.createElement("root");
        w3cDoc.appendChild(root);
        Element parent = w3cDoc.createElement("parent");
        root.appendChild(parent);

        W3CDom w3cDom = new W3CDom();
        String xmlString = w3cDom.asString(w3cDoc);

        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><root><parent/></root>", xmlString);
    }
}