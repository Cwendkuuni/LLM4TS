package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
import java.util.Stack;

public class W3CDomTest {
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
  
    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        assertNull(w3cDocument);
    }
  
    @Test
    public void testConvert() throws ParserConfigurationException, TransformerException {
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        jsoupDocument.appendElement("html").appendElement("body").text("Hello, world!");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document w3cDocument = builder.newDocument();

        new W3CDom().convert(jsoupDocument, w3cDocument);
        Element rootElement = w3cDocument.getDocumentElement();
        assertEquals("html", rootElement.getTagName());
        Element bodyElement = (Element) rootElement.getElementsByTagName("body").item(0);
        Text textNode = (Text) bodyElement.getChildNodes().item(0);
        assertEquals("Hello, world!", textNode.getWholeText());
    }
  
    @Test
    public void testW3CBuilderHead() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(doc);

        org.jsoup.nodes.Element sourceElement = new org.jsoup.nodes.Element("div");
        sourceElement.appendElement("span").text("Hello");
        w3cBuilder.head(sourceElement, 1);

        Element rootElement = doc.getDocumentElement();
        assertEquals("div", rootElement.getTagName());
        Element spanElement = (Element) rootElement.getElementsByTagName("span").item(0);
        Text textNode = (Text) spanElement.getChildNodes().item(0);
        assertEquals("Hello", textNode.getWholeText());
    }
  
    @Test
    public void testW3CBuilderTail() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element rootElement = doc.createElement("div");
        doc.appendChild(rootElement);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(doc);
        w3cBuilder.dest = rootElement;

        org.jsoup.nodes.Element sourceElement = new org.jsoup.nodes.Element("span");
        w3cBuilder.tail(sourceElement, 1);

        assertNull(w3cBuilder.dest);
    }
  
    @Test
    public void testW3CBuilderHeadTextNode() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element rootElement = doc.createElement("div");
        doc.appendChild(rootElement);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(doc);
        w3cBuilder.dest = rootElement;

        org.jsoup.nodes.TextNode sourceTextNode = new org.jsoup.nodes.TextNode("Hello, world!", "");
        w3cBuilder.head(sourceTextNode, 1);

        Text textNode = (Text) rootElement.getChildNodes().item(0);
        assertEquals("Hello, world!", textNode.getWholeText());
    }
  
    @Test
    public void testW3CBuilderHeadCommentNode() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element rootElement = doc.createElement("div");
        doc.appendChild(rootElement);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(doc);
        w3cBuilder.dest = rootElement;

        org.jsoup.nodes.Comment sourceCommentNode = new org.jsoup.nodes.Comment(" Comment ");
        w3cBuilder.head(sourceCommentNode, 1);

        Comment commentNode = (Comment) rootElement.getChildNodes().item(0);
        assertEquals(" Comment ", commentNode.getNodeValue());
    }
  
    @Test
    public void testW3CBuilderHeadDataNode() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element rootElement = doc.createElement("div");
        doc.appendChild(rootElement);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(doc);
        w3cBuilder.dest = rootElement;

        org.jsoup.nodes.DataNode sourceDataNode = new org.jsoup.nodes.DataNode(" Data ", "");
        w3cBuilder.head(sourceDataNode, 1);

        Text dataNode = (Text) rootElement.getChildNodes().item(0);
        assertEquals(" Data ", dataNode.getWholeText());
    }
  
    @Test
    public void testCopyAttributes() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element element = doc.createElement("div");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(doc);

        org.jsoup.nodes.Element sourceElement = new org.jsoup.nodes.Element("span");
        sourceElement.attr("id","1");
        w3cBuilder.copyAttributes(sourceElement, element);

        Attr attribute = element.getAttributeNode("id");
        assertEquals("1", attribute.getValue());
    }
  
    @Test
    public void testUpdateNamespaces() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element rootElement = doc.createElement("div");
        doc.appendChild(rootElement);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(doc);
        w3cBuilder.dest = rootElement;

        org.jsoup.nodes.Element sourceElement = new org.jsoup.nodes.Element("span");
        sourceElement.attr("xmlns","http://www.w3.org/2000/xmlns/");
        w3cBuilder.updateNamespaces(sourceElement);

        HashMap<String, String> namespaces = w3cBuilder.namespacesStack.get(0);
        assertEquals("http://www.w3.org/2000/xmlns/", namespaces.get(""));
    }
  
    @Test
    public void testAsString() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        doc.appendChild(doc.createElement("div"));

        String expected = XML_HEADER + "<div></div>";
        String actual = new W3CDom().asString(doc);
        assertEquals(expected, actual);
    }
}