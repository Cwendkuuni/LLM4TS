package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Before;
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
    private W3CDom w3cDom;
    private DocumentBuilderFactory factory;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
    }

    @Test
    public void testFromJsoupNotNull() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testFromJsoupConversion() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Element rootEl = new Element("root");
        jsoupDoc.appendChild(rootEl);

        Element childEl = new Element("child");
        rootEl.appendChild(childEl);

        TextNode textNode = new TextNode("text");
        childEl.appendChild(textNode);

        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Element w3cRootEl = w3cDoc.getDocumentElement();
        assertEquals("root", w3cRootEl.getNodeName());

        Element w3cChildEl = getFirstElementByTagName(w3cRootEl, "child");
        assertNotNull(w3cChildEl);

        Text w3cTextNode = (Text) w3cChildEl.getFirstChild();
        assertNotNull(w3cTextNode);
        assertEquals("text", w3cTextNode.getTextContent());
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoupException() throws ParserConfigurationException {
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(null);
        builder.setEntityResolver(null);
        builder.getDOMImplementation();
    }

    @Test
    public void testConvertNotBlankLocation() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("http://example.com");
        Attributes attributes = new Attributes();
        attributes.put("attr", "value");
        org.jsoup.nodes.Element rootEl = new org.jsoup.nodes.Element("root", "", attributes);
        jsoupDoc.appendChild(rootEl);

        Document w3cDoc = factory.newDocumentBuilder().newDocument();
        w3cDom.convert(jsoupDoc, w3cDoc);
        assertEquals("http://example.com", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvertWithRootNode() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Element rootEl = new Element("root");
        jsoupDoc.appendChild(rootEl);

        Document w3cDoc = factory.newDocumentBuilder().newDocument();
        w3cDom.convert(jsoupDoc, w3cDoc);

        Element w3cRootEl = w3cDoc.getDocumentElement();
        assertNotNull(w3cRootEl);
        assertEquals("root", w3cRootEl.getNodeName());
    }

    @Test
    public void testConvertElementNode() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Element rootEl = new Element("root");
        jsoupDoc.appendChild(rootEl);

        Element childEl = new Element("child");
        rootEl.appendChild(childEl);

        Document w3cDoc = factory.newDocumentBuilder().newDocument();
        w3cDom.convert(jsoupDoc, w3cDoc);

        Element w3cRootEl = w3cDoc.getDocumentElement();
        Element w3cChildEl = getFirstElementByTagName(w3cRootEl, "child");
        assertNotNull(w3cChildEl);
    }

    @Test
    public void testConvertTextNode() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Element rootEl = new Element("root");
        jsoupDoc.appendChild(rootEl);

        TextNode textNode = new TextNode("text");
        rootEl.appendChild(textNode);

        Document w3cDoc = factory.newDocumentBuilder().newDocument();
        w3cDom.convert(jsoupDoc, w3cDoc);

        Element w3cRootEl = w3cDoc.getDocumentElement();
        Text w3cTextNode = (Text) w3cRootEl.getFirstChild();
        assertNotNull(w3cTextNode);
        assertEquals("text", w3cTextNode.getTextContent());
    }

    @Test
    public void testConvertCommentNode() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Element rootEl = new Element("root");
        jsoupDoc.appendChild(rootEl);

        Comment comment = new Comment("comment");
        rootEl.appendChild(comment);

        Document w3cDoc = factory.newDocumentBuilder().newDocument();
        w3cDom.convert(jsoupDoc, w3cDoc);

        Element w3cRootEl = w3cDoc.getDocumentElement();
        Comment w3cComment = (Comment) w3cRootEl.getFirstChild();
        assertNotNull(w3cComment);
        assertEquals("comment", w3cComment.getData());
    }

    @Test
    public void testConvertDataNode() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Element rootEl = new Element("root");
        jsoupDoc.appendChild(rootEl);

        DataNode dataNode = new DataNode("data", "data value");
        rootEl.appendChild(dataNode);

        Document w3cDoc = factory.newDocumentBuilder().newDocument();
        w3cDom.convert(jsoupDoc, w3cDoc);

        Element w3cRootEl = w3cDoc.getDocumentElement();
        Text w3cDataNode = (Text) w3cRootEl.getFirstChild();
        assertNotNull(w3cDataNode);
        assertEquals("data value", w3cDataNode.getData());
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("tagName");
        jsoupElement.attr("attr1", "value1");
        jsoupElement.attr("attr2", "value2");

        Element destElement = factory.newDocumentBuilder().newDocument().createElement("tag");

        w3cDom.copyAttributes(jsoupElement, destElement);

        assertEquals(2, destElement.getAttributes().getLength());
        assertEquals("value1", destElement.getAttribute("attr1"));
        assertEquals("value2", destElement.getAttribute("attr2"));
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("tagName");
        jsoupElement.attr("xmlns", "http://example.com");

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(factory.newDocumentBuilder().newDocument());
        builder.updateNamespaces(jsoupElement);

        HashMap<String, String> namespaces = builder.namespaces;
        assertEquals(1, namespaces.size());
        assertTrue(namespaces.containsKey(""));
        assertEquals("http://example.com", namespaces.get(""));
    }

    @Test
    public void testAsString() {
        Document doc = factory.newDocumentBuilder().newDocument();
        Element rootEl = doc.createElement("root");
        doc.appendChild(rootEl);

        String result = w3cDom.asString(doc);
        assertTrue(result.contains("<root></root>"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAsStringException() throws TransformerException {
        Document doc = factory.newDocumentBuilder().newDocument();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(null, null);
    }

    private Element getFirstElementByTagName(Element element, String tagName) {
        Element result = null;
        org.w3c.dom.NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            result = (Element) nodeList.item(0);
        }
        return result;
    }
}