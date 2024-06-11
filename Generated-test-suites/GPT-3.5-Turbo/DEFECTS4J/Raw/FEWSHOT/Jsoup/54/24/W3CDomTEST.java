package org.jsoup.helper;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {
    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.location("http://www.example.com");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document expectedW3CDoc = db.newDocument();

            org.jsoup.nodes.Element rootElement = jsoupDoc.createElement("root");
            jsoupDoc.appendChild(rootElement);

            Element expectedRootElement = expectedW3CDoc.createElement("root");
            expectedW3CDoc.appendChild(expectedRootElement);

            org.jsoup.nodes.Element childElement = jsoupDoc.createElement("child");
            rootElement.appendChild(childElement);

            Element expectedChildElement = expectedW3CDoc.createElement("child");
            expectedRootElement.appendChild(expectedChildElement);

            Element convertedW3CDoc = w3cDom.fromJsoup(jsoupDoc);

            Assert.assertEquals(expectedW3CDoc, convertedW3CDoc);
            Assert.assertEquals("http://www.example.com", convertedW3CDoc.getDocumentURI());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.location("http://www.example.com");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document expectedW3CDoc = db.newDocument();

            org.jsoup.nodes.Element rootElement = jsoupDoc.createElement("root");
            jsoupDoc.appendChild(rootElement);

            Element expectedRootElement = expectedW3CDoc.createElement("root");
            expectedW3CDoc.appendChild(expectedRootElement);

            org.jsoup.nodes.Element childElement = jsoupDoc.createElement("child");
            rootElement.appendChild(childElement);

            Element expectedChildElement = expectedW3CDoc.createElement("child");
            expectedRootElement.appendChild(expectedChildElement);

            w3cDom.convert(jsoupDoc, expectedW3CDoc);

            Element convertedW3CDoc = w3cDom.fromJsoup(jsoupDoc);

            Assert.assertEquals(expectedW3CDoc, convertedW3CDoc);
            Assert.assertEquals("http://www.example.com", convertedW3CDoc.getDocumentURI());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testW3CBuilderHead() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document w3cDoc = db.newDocument();
            Element rootElement = w3cDoc.createElement("root");
            w3cDoc.appendChild(rootElement);

            org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("child", "");
            org.jsoup.nodes.Attribute attr = new org.jsoup.nodes.Attribute("key", "value");
            jsoupElement.attributes().put(attr);
            org.jsoup.nodes.TextNode textNode = new org.jsoup.nodes.TextNode("text", "");
            org.jsoup.nodes.Comment comment = new org.jsoup.nodes.Comment("comment", "");

            Element elem = w3cDoc.createElement("child");
            elem.setAttribute("key", "value");
            Text text = w3cDoc.createTextNode("text");
            Comment comment = w3cDoc.createComment("comment");

            W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(w3cDoc);

            w3CBuilder.head(jsoupElement, 0);
            w3CBuilder.head(textNode, 0);
            w3CBuilder.head(comment, 0);

            Assert.assertEquals(elem, w3CBuilder.dest);
            Assert.assertEquals(text, w3CBuilder.dest.getChildNodes().item(0));
            Assert.assertEquals(comment, w3CBuilder.dest.getChildNodes().item(1));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testW3CBuilderTail() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document w3cDoc = db.newDocument();
            Element rootElement = w3cDoc.createElement("root");
            w3cDoc.appendChild(rootElement);

            org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("child", "");
            org.jsoup.nodes.Attribute attr = new org.jsoup.nodes.Attribute("key", "value");
            jsoupElement.attributes().put(attr);
            org.jsoup.nodes.TextNode textNode = new org.jsoup.nodes.TextNode("text", "");
            org.jsoup.nodes.Comment comment = new org.jsoup.nodes.Comment("comment", "");

            Element elem = w3cDoc.createElement("child");
            elem.setAttribute("key", "value");
            Text text = w3cDoc.createTextNode("text");
            Comment comment = w3cDoc.createComment("comment");

            W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(w3cDoc);

            w3CBuilder.head(jsoupElement, 0);
            w3CBuilder.head(textNode, 0);
            w3CBuilder.head(comment, 0);

            w3CBuilder.tail(jsoupElement, 0);
            w3CBuilder.tail(textNode, 0);
            w3CBuilder.tail(comment, 0);

            Assert.assertEquals(w3cDoc.getDocumentElement(), w3CBuilder.dest);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testW3CBuilderCopyAttributes() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document w3cDoc = db.newDocument();
            Element rootElement = w3cDoc.createElement("root");
            w3cDoc.appendChild(rootElement);

            org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("child", "");
            org.jsoup.nodes.Attribute attr = new org.jsoup.nodes.Attribute("key", "value");
            jsoupElement.attributes().put(attr);

            Element elem = w3cDoc.createElement("child");
            elem.setAttribute("key", "value");

            W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(w3cDoc);

            w3CBuilder.copyAttributes(jsoupElement, elem);

            Assert.assertEquals(elem, w3CBuilder.dest);
            Assert.assertEquals(w3cDoc.getDocumentElement().getAttributes().getNamedItem("key"), elem.getAttributes().getNamedItem("key"));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testW3CBuilderUpdateNamespaces() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document w3cDoc = db.newDocument();
            Element rootElement = w3cDoc.createElement("root");
            w3cDoc.appendChild(rootElement);

            org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("child", "");
            org.jsoup.nodes.Attribute attr = new org.jsoup.nodes.Attribute("key", "value");
            jsoupElement.attributes().put(attr);

            W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(w3cDoc);

            String prefix = w3CBuilder.updateNamespaces(jsoupElement);

            Assert.assertEquals("child", jsoupElement.tagName());
            Assert.assertEquals(0, w3CBuilder.namespaces.size());
            Assert.assertEquals("", prefix);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAsString() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document w3cDoc = db.newDocument();
            Element rootElement = w3cDoc.createElement("root");
            w3cDoc.appendChild(rootElement);

            String expectedOutput = "<root></root>";

            String actualOutput = w3cDom.asString(w3cDoc);

            Assert.assertEquals(expectedOutput, actualOutput);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}