package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import java.io.IOException;

public class W3CDomTest {

    @Test
    public void testFromJsoup() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        Document jsoupDoc = Parser.parseBodyFragment(html);
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        Assert.assertEquals("Test", w3cDoc.getElementsByTagName("title").item(0).getTextContent());
        Assert.assertEquals("Hello World!", w3cDoc.getElementsByTagName("h1").item(0).getTextContent());
    }

    @Test
    public void testConvert() throws IOException {
       //create jsoup doc
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        Document jsoupDoc = Parser.parseBodyFragment(html);
        
        //create w3c doc
        org.w3c.dom.Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);
        
        //convert jsoup doc to w3c doc
        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        Assert.assertEquals("Test", w3cDoc.getElementsByTagName("title").item(0).getTextContent());
        Assert.assertEquals("Hello World!", w3cDoc.getElementsByTagName("h1").item(0).getTextContent());
    }

    @Test
    public void testW3CBuilderHead() {
        Document doc = new Document("");
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("test"), "");

        org.jsoup.nodes.Element childElement = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("child"), "");
        childElement.text("child data");
        jsoupElement.appendChild(childElement);
        
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);
        builder.head(jsoupElement, 0);
        
        org.w3c.dom.Element w3cElement = doc.getElementById("test");
        Assert.assertEquals("test", w3cElement.getTagName());
        
        NodeList childNodes = w3cElement.getChildNodes();
        Assert.assertEquals(1, childNodes.getLength());
        
        Node childNode = childNodes.item(0);
        Assert.assertEquals(childNode.getNodeType(), Node.TEXT_NODE);
        Assert.assertEquals("child data", childNode.getNodeValue());
    }

    @Test
    public void testW3CBuilderTail() {
        Document doc = new Document("");
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("test"), "");
        org.jsoup.nodes.Element childElement = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("child"), "");
        childElement.text("child data");
        jsoupElement.appendChild(childElement);
        doc.appendChild(jsoupElement);
        
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);
        builder.tail(jsoupElement, 0);
        
        org.w3c.dom.Element w3cElement = doc.getElementById("test");
        Assert.assertEquals("test", w3cElement.getTagName());
        
        NodeList childNodes = w3cElement.getChildNodes();
        Assert.assertEquals(1, childNodes.getLength());
        
        Node childNode = childNodes.item(0);
        Assert.assertEquals(childNode.getNodeType(), Node.TEXT_NODE);
        Assert.assertEquals("child data", childNode.getNodeValue());
    }

    @Test
    public void testCopyAttributes() {
        Document doc = new Document("");
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("test"), "");

        jsoupElement.attr("attr1", "value1");
        jsoupElement.attr("attr2", "value2");
        jsoupElement.attr("attr3", "value3");
        
        org.w3c.dom.Element w3cElement = doc.createElement("test");
        
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);
        builder.copyAttributes(jsoupElement, w3cElement);
        
        NamedNodeMap attributes = w3cElement.getAttributes();
        Assert.assertEquals(3, attributes.getLength());
        
        Attr attr1 = (Attr) attributes.getNamedItem("attr1");
        Attr attr2 = (Attr) attributes.getNamedItem("attr2");
        Attr attr3 = (Attr) attributes.getNamedItem("attr3");
        
        Assert.assertEquals("value1", attr1.getValue());
        Assert.assertEquals("value2", attr2.getValue());
        Assert.assertEquals("value3", attr3.getValue());
    }

    @Test
    public void testUpdateNamespaces() {
        Document doc = new Document("");
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("test"), "");

        jsoupElement.attr("xmlns", "urn:test");
        jsoupElement.attr("xmlns:prefix", "urn:test:prefix");
        
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);
        String prefix = builder.updateNamespaces(jsoupElement);
        
        Assert.assertEquals("", prefix);
        
        HashMap<String, String> namespaces = builder.getNamespaces();
        Assert.assertEquals(2, namespaces.size());
        Assert.assertEquals("urn:test", namespaces.get(""));
        Assert.assertEquals("urn:test:prefix", namespaces.get("prefix"));
    }

    @Test
    public void testAsString() {
        Document doc = new Document("");
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("test"), "");

        jsoupElement.text("test data");
        doc.appendChild(jsoupElement);
        
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(doc);
        
        String xmlString = w3cDom.asString(w3cDoc);
        
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><test>test data</test>", xmlString);
    }
}