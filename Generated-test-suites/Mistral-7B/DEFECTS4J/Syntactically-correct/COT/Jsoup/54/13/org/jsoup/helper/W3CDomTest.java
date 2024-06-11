package org.jsoup.helper;

import org.junit.Test;
import org.junit.Assert.*;
import org.w3c.dom.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.helper.W3CDom;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        Document jsoupDoc = new Document();
        Element root = jsoupDoc.createElement("root");
        root.setTextContent("Hello World");
        jsoupDoc.appendChild(root);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        String w3cDocString = new W3CDom().asString(w3cDoc);
        String expected = "<root>Hello World</root>";
        assertEquals(expected, w3cDocString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupNullInput() {
        new W3CDom().fromJsoup(null);
    }

    @Test
    public void testConvert() {
        Document jsoupDoc = new Document();
        Element root = jsoupDoc.createElement("root");
        root.setTextContent("Hello World");
        jsoupDoc.appendChild(root);
        Document w3cDoc = new Document();
        new W3CDom().convert(jsoupDoc, w3cDoc);
        Element rootW3C = w3cDoc.getDocumentElement();
        assertEquals("root", rootW3C.getTagName());
        assertEquals("Hello World", rootW3C.getTextContent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertNullInputs() {
        new W3CDom().convert(null, null);
    }

    @Test
    public void testW3CBuilderHeadTextNode() {
        Document w3cDoc = new Document();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.TextNode jsoupTextNode = new org.jsoup.nodes.TextNode("Test Text Node");
        builder.head(jsoupTextNode, 0);
        Element textElement = builder.dest;
        assertEquals("Test Text Node", textElement.getTextContent());
    }

    @Test
    public void testW3CBuilderHeadElement() {
        Document w3cDoc = new Document();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("TestElement");
        builder.head(jsoupElement, 0);
        Element element = builder.dest;
        assertEquals("TestElement", element.getTagName());
    }

    @Test
    public void testW3CBuilderHeadComment() {
        Document w3cDoc = new Document();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Comment jsoupComment = new org.jsoup.nodes.Comment("Test Comment");
        builder.head(jsoupComment, 0);
        Comment comment = builder.dest;
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testW3CBuilderHeadDataNode() {
        Document w3cDoc = new Document();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.DataNode jsoupDataNode = new org.jsoup.nodes.DataNode("Test Data Node");
        builder.head(jsoupDataNode, 0);
        Text text = builder.dest;
        assertEquals("Test Data Node", text.getTextContent());
    }

    @Test
    public void testW3CBuilderTailElement() {
        Document w3cDoc = new Document();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        Element parent = w3cDoc.createElement("TestParent");
        Element child = w3cDoc.createElement("TestChild");
        parent.appendChild(child);
        builder.head(child, 1);
        builder.tail(child, 1);
        assertEquals(0, parent.getChildNodes().getLength());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testW3CBuilderNullInput() {
        Document w3cDoc = new Document();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.head(null, 0);
    }

    @Test
    public void testCopyAttributes() {
        Document w3cDoc = new Document();
        Element parent = w3cDoc.createElement("TestParent");
        parent.setAttribute("testAttribute", "testValue");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("TestElement");
        jsoupElement.setAttribute("testAttribute", "updatedValue");
        builder.head(jsoupElement, 0);
        Element element = builder.dest;
        Attribute attribute = element.getAttributeNode("testAttribute");
        assertEquals("updatedValue", attribute.getValue());
    }

    @Test
    public void testUpdateNamespaces() {
        Document w3cDoc = new Document();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("root");
        jsoupElement.setAttribute("xmlns:test", "testNamespace");
        jsoupElement.setAttribute("test:testElement", "Test Value");
        builder.head(jsoupElement, 0);
        String prefix = builder.updateNamespaces(jsoupElement);
        assertEquals("test", prefix);
        String namespace = builder.namespaces.get(prefix);
        assertEquals("testNamespace", namespace);
    }

    @Test
    public void testAsString() {
        Document w3cDoc = new Document();
        Element root = w3cDoc.createElement("root");
        root.setTextContent("Hello World");
        w3cDoc.appendChild(root);
        String w3cDocString = new W3CDom().asString(w3cDoc);
        String expected = "<root>Hello World</root>";
        assertEquals(expected, w3cDocString);
    }

    @Test(expected = IllegalStateException.class)
    public void testAsStringException() {
        Document w3cDoc = new Document();
        new W3CDom().asString(w3cDoc);
    }

}