
package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class W3CDomTest {

    private static DocumentBuilder documentBuilder;

    @BeforeClass
    public static void setUp() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        documentBuilder = factory.newDocumentBuilder();
    }

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        assertEquals(w3cDoc.getDocumentElement(), w3cDoc.createElement("doc"));
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = documentBuilder.newDocument();
        new W3CDom().convert(jsoupDoc, w3cDoc);

        Element rootElement = w3cDoc.getDocumentElement();
        assertEquals("doc", rootElement.getTagName());
        assertTrue(rootElement.hasChildNodes());
    }

    @Test
    public void testW3CBuilderHeadElement() {
        Document w3cDoc = documentBuilder.newDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        builder.head(jsoupElement, 0);

        Node rootElement = w3cDoc.getDocumentElement();
        assertTrue(rootElement instanceof Element);
        assertEquals("div", rootElement.getNodeName());
    }

    @Test
    public void testW3CBuilderHeadTextNode() {
        Document w3cDoc = documentBuilder.newDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.TextNode jsoupText = new org.jsoup.nodes.TextNode("text", "");
        builder.head(jsoupText, 0);

        NodeList nodeList = w3cDoc.getDocumentElement().getChildNodes();
        assertEquals(1, nodeList.getLength());
        Node textNode = nodeList.item(0);
        assertTrue(textNode instanceof Text);
        assertEquals("text", textNode.getNodeValue());
    }

    @Test
    public void testW3CBuilderHeadComment() {
        Document w3cDoc = documentBuilder.newDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Comment jsoupComment = new org.jsoup.nodes.Comment("comment");
        builder.head(jsoupComment, 0);

        NodeList nodeList = w3cDoc.getDocumentElement().getChildNodes();
        assertEquals(1, nodeList.getLength());
        Node commentNode = nodeList.item(0);
        assertTrue(commentNode instanceof Comment);
        assertEquals("comment", commentNode.getNodeValue());
    }

    @Test
    public void testW3CBuilderHeadDataNode() {
        Document w3cDoc = documentBuilder.newDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.DataNode jsoupData = new org.jsoup.nodes.DataNode("data", "");
        builder.head(jsoupData, 0);

        NodeList nodeList = w3cDoc.getDocumentElement().getChildNodes();
        assertEquals(1, nodeList.getLength());
        Node dataNode = nodeList.item(0);
        assertTrue(dataNode instanceof Text);
        assertEquals("data", dataNode.getNodeValue());
    }

    @Test
    public void testW3CBuilderTail() {
        Document w3cDoc = documentBuilder.newDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        Node rootElement = w3cDoc.createElement("div");
        w3cDoc.appendChild(rootElement);
        builder.dest = (Element) rootElement;

        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        builder.namespacesStack.push(new HashMap<>());
        builder.tail(jsoupElement, 0);

        Node parentElement = builder.dest.getParentNode();
        assertEquals(rootElement, parentElement);
    }

    @Test
    public void testCopyAttributes() {
        Document w3cDoc = documentBuilder.newDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        jsoupElement.attr("id", "test-id");
        jsoupElement.attr(":class", "test-class");

        Element w3cElement = w3cDoc.createElement("div");
        builder.copyAttributes(jsoupElement, w3cElement);

        NamedNodeMap attributes = w3cElement.getAttributes();
        assertEquals(2, attributes.getLength());
        Attr idAttribute = (Attr) attributes.getNamedItem("id");
        Attr classAttribute = (Attr) attributes.getNamedItem(":class");
        assertEquals("test-id", idAttribute.getValue());
        assertEquals("test-class", classAttribute.getValue());
    }

    @Test
    public void testUpdateNamespaces() {
        Document w3cDoc = documentBuilder.newDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        jsoupElement.attr("xmlns", "urn:test");

        String prefix = builder.updateNamespaces(jsoupElement);
        assertEquals("", prefix);

        Map<String, String> namespaces = builder.namespacesStack.peek();
        assertEquals("urn:test", namespaces.get(""));
    }

    @Test
    public void testAsString() {
        Document w3cDoc = documentBuilder.newDocument();
        Element rootElement = w3cDoc.createElement("div");
        w3cDoc.appendChild(rootElement);

        String xmlString = new W3CDom().asString(w3cDoc);
        assertTrue(xmlString.contains("<div"));
    }
}
