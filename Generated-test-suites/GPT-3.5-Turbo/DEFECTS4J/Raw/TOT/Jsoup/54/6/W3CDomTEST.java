import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashMap;

public class W3CDomTest {

    @Test
    public void testFromJsoup_typical() {
        Document jsoupDoc = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Element rootElement = w3cDoc.getDocumentElement();
        Assert.assertEquals("html", rootElement.getTagName());
    }

    @Test
    public void testFromJsoup_nullInput() {
        W3CDom w3cDom = new W3CDom();
        try {
            org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(null);
            Assert.fail("NullPointerException should have been thrown.");
        } catch (NullPointerException e) {
            // exception should be thrown, test passed
        }
    }

    @Test
    public void testConvert_typical() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        W3CDom w3cDom = new W3CDom();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document w3cDoc = builder.newDocument();
            w3cDom.convert(jsoupDoc, w3cDoc);
            org.w3c.dom.Element rootElement = w3cDoc.getDocumentElement();
            Assert.assertEquals("html", rootElement.getTagName());
        } catch (Exception e) {
            Assert.fail("Exception thrown during conversion.");
        }
    }

    @Test
    public void testConvert_blankLocation() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("  ");
        W3CDom w3cDom = new W3CDom();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document w3cDoc = builder.newDocument();
            w3cDom.convert(jsoupDoc, w3cDoc);
            org.w3c.dom.Element rootElement = w3cDoc.getDocumentElement();
            Assert.assertEquals("", w3cDoc.getDocumentURI());
        } catch (Exception e) {
            Assert.fail("Exception thrown during conversion.");
        }
    }

    @Test
    public void testW3CBuilder_headElement() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        sourceEl.tagName("div");
        builder.head(sourceEl, 0);
        Assert.assertNotNull(builder.doc);
        Assert.assertNotNull(builder.dest);
        Assert.assertEquals("div", builder.dest.getTagName());
    }

    @Test
    public void testW3CBuilder_headTextNode() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("Hello World");
        builder.head(sourceText, 0);
        Assert.assertNotNull(builder.doc);
        Assert.assertNotNull(builder.dest);
        Assert.assertEquals("Hello World", builder.dest.getTextContent());
    }

    @Test
    public void testW3CBuilder_headComment() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Comment sourceComment = new org.jsoup.nodes.Comment("This is a comment");
        builder.head(sourceComment, 0);
        Assert.assertNotNull(builder.doc);
        Assert.assertNotNull(builder.dest);
        Assert.assertEquals("This is a comment", builder.dest.getNodeValue());
    }

    @Test
    public void testW3CBuilder_headDataNode() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.DataNode sourceData = new org.jsoup.nodes.DataNode("Data Node");
        builder.head(sourceData, 0);
        Assert.assertNotNull(builder.doc);
        Assert.assertNotNull(builder.dest);
        Assert.assertEquals("Data Node", builder.dest.getNodeValue());
    }

    @Test
    public void testW3CBuilder_tailElement() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        sourceEl.tagName("div");
        builder.head(sourceEl, 0);
        builder.tail(sourceEl, 0);
        Assert.assertNotNull(builder.doc);
        Assert.assertNotNull(builder.dest);
        Assert.assertNull(builder.dest.getParentNode());
    }

    @Test
    public void testCopyAttributes() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        sourceEl.attr("id", "123");
        sourceEl.attr("class", "example");
        org.w3c.dom.Document doc = null;
        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = docBuilder.newDocument();
        } catch (Exception e) {
            Assert.fail("Exception thrown during document creation.");
        }
        Element dest = doc.createElement("div");
        builder.copyAttributes(sourceEl, dest);
        NamedNodeMap attributes = dest.getAttributes();
        Assert.assertEquals(2, attributes.getLength());
        Node idAttr = attributes.getNamedItem("id");
        Node classAttr = attributes.getNamedItem("class");
        Assert.assertNotNull(idAttr);
        Assert.assertNotNull(classAttr);
        Assert.assertEquals("123", ((Attr) idAttr).getValue());
        Assert.assertEquals("example", ((Attr) classAttr).getValue());
    }

    @Test
    public void testUpdateNamespaces() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("div");
        el.attr("xmlns", "http://www.example.com");
        el.attr("xmlns:prefix", "http://www.example2.com");
        el.tag("div:tag");
        String prefix = builder.updateNamespaces(el);
        Assert.assertEquals(2, builder.namespaces.size());
        Assert.assertEquals("http://www.example.com", builder.namespaces.get(""));
        Assert.assertEquals("http://www.example2.com", builder.namespaces.get("prefix"));
        Assert.assertEquals("div", el.tagName());
        Assert.assertEquals("prefix", prefix);
    }

    @Test
    public void testAsString_typical() {
        // Create a sample W3C Document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        org.w3c.dom.Document w3cDoc = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            w3cDoc = builder.newDocument();
            org.w3c.dom.Element root = w3cDoc.createElement("root");
            w3cDoc.appendChild(root);
            org.w3c.dom.Element child = w3cDoc.createElement("child");
            root.appendChild(child);
            Attr attribute = w3cDoc.createAttribute("attribute");
            attribute.setValue("value");
            child.setAttributeNode(attribute);
        } catch (Exception e) {
            Assert.fail("Exception thrown during document creation.");
        }

        W3CDom w3cDom = new W3CDom();
        String xmlString = w3cDom.asString(w3cDoc);
        Assert.assertNotNull(xmlString);
        Assert.assertTrue(xmlString.contains("<root><child attribute=\"value\"></child></root>"));
    }

    @Test
    public void testAsString_exceptionThrown() {
        W3CDom w3cDom = new W3CDom();
        try {
            w3cDom.asString(null);
            Assert.fail("NullPointerException should have been thrown.");
        } catch (NullPointerException e) {
            // exception should be thrown, test passed
        }
    }

}