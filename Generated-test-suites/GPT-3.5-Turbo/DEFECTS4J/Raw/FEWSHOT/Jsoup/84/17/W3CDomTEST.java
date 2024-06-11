package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class W3CDomTest {

    private W3CDom w3CDom;
    private org.jsoup.nodes.Document jsoupDoc;

    @Before
    public void setup() {
        w3CDom = new W3CDom();
        jsoupDoc = new org.jsoup.nodes.Document("");
    }

    @Test
    public void testFromJsoup() {
        // Initialize jsoupDoc
        org.jsoup.nodes.Element root = new org.jsoup.nodes.Element("root");
        jsoupDoc.appendChild(root);

        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        // Initialize jsoupDoc
        org.jsoup.nodes.Element root = new org.jsoup.nodes.Element("root");
        jsoupDoc.appendChild(root);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document w3cDoc = builder.newDocument();

        w3CDom.convert(jsoupDoc, w3cDoc);

        assertNotNull(w3cDoc);
    }

    @Test
    public void testW3CBuilderHeadElement() {
        Document w3cDoc = createW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Element source = new org.jsoup.nodes.Element("source");
        source.attr("xmlns", "http://www.w3.org/2000/svg");

        builder.head(source, 0);

        NodeList nodeList = w3cDoc.getElementsByTagName("source");

        assertEquals(1, nodeList.getLength());
        assertEquals("source", nodeList.item(0).getNodeName());
    }

    @Test
    public void testW3CBuilderHeadTextNode() {
        Document w3cDoc = createW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.TextNode source = new org.jsoup.nodes.TextNode("text");

        builder.head(source, 0);

        NodeList nodeList = w3cDoc.getElementsByTagName("#text");

        assertEquals(1, nodeList.getLength());
        assertEquals("text", nodeList.item(0).getNodeValue());
    }

    @Test
    public void testW3CBuilderHeadComment() {
        Document w3cDoc = createW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Comment source = new org.jsoup.nodes.Comment("comment");

        builder.head(source, 0);

        NodeList nodeList = w3cDoc.getElementsByTagName("#comment");

        assertEquals(1, nodeList.getLength());
        assertEquals("comment", nodeList.item(0).getNodeValue());
    }

    @Test
    public void testW3CBuilderHeadDataNode() {
        Document w3cDoc = createW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.DataNode source = new org.jsoup.nodes.DataNode("data");

        builder.head(source, 0);

        NodeList nodeList = w3cDoc.getElementsByTagName("#text");

        assertEquals(1, nodeList.getLength());
        assertEquals("data", nodeList.item(0).getNodeValue());
    }

    @Test
    public void testW3CBuilderTail() {
        Document w3cDoc = createW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Element source = new org.jsoup.nodes.Element("source");
        source.attr("xmlns", "http://www.w3.org/2000/svg");

        builder.head(source, 0);

        NodeList nodeList = w3cDoc.getElementsByTagName("source");

        assertEquals(1, nodeList.getLength());
        assertEquals("source", nodeList.item(0).getNodeName());

        builder.tail(source, 0);

        nodeList = w3cDoc.getElementsByTagName("source");

        assertEquals(0, nodeList.getLength());
    }

    @Test
    public void testCopyAttributes() {
        Document w3cDoc = createW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Element source = new org.jsoup.nodes.Element("source");
        source.attr("attr1", "value1");
        source.attr("attr2", "value2");
        source.attr("attr 3", "value 3");
        source.attr("attr_4", "value_4");

        Element dest = createW3CElement(w3cDoc);

        builder.copyAttributes(source, dest);

        Map<String, String> expectedAttributes = new HashMap<>();
        expectedAttributes.put("attr1", "value1");
        expectedAttributes.put("attr2", "value2");
        expectedAttributes.put("attr_4", "value_4");

        for (Attribute attribute : dest.attributes()) {
            String key = attribute.getKey();
            String value = attribute.getValue();

            String expectedValue = expectedAttributes.get(key);

            assertNotNull(expectedValue);
            assertEquals(expectedValue, value);

            expectedAttributes.remove(key);
        }

        assertEquals(0, expectedAttributes.size());
    }

    @Test
    public void testUpdateNamespaces() {
        Document w3cDoc = createW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("source");
        el.attr("xmlns", "http://www.w3.org/2000/svg");
        el.attr("xmlns:prefix", "http://www.w3.org/2000/prefix");

        String prefix = builder.updateNamespaces(el);

        assertEquals("prefix", prefix);

        Map<String, String> expectedNamespaces = new HashMap<>();
        expectedNamespaces.put("prefix", "http://www.w3.org/2000/prefix");

        Map<String, String> actualNamespaces = builder.namespacesStack.peek();

        for (Map.Entry<String, String> entry : expectedNamespaces.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            String actualValue = actualNamespaces.get(key);

            assertNotNull(actualValue);
            assertEquals(value, actualValue);
        }
    }

    @Test
    public void testAsString() {
        Document w3cDoc = createW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Element root = new org.jsoup.nodes.Element("root");

        org.w3c.dom.Element w3cElement = createW3CElement(w3cDoc);
        w3cDoc.appendChild(w3cElement);

        org.jsoup.nodes.Element child = new org.jsoup.nodes.Element("child");
        w3cElement.appendChild(builder.convertJsoupElement(child));

        String actualString = w3CDom.asString(w3cDoc);
        String expectedString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><root><element attr1=\"value1\">text</element></root>";

        assertEquals(expectedString, actualString);
    }

    private org.w3c.dom.Element createW3CElement(Document w3cDoc) {
        org.w3c.dom.Element w3cElement = w3cDoc.createElement("element");
        w3cElement.setAttribute("attr1", "value1");

        Text textNode = w3cDoc.createTextNode("text");
        w3cElement.appendChild(textNode);

        return w3cElement;
    }

    private Document createW3CDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.newDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}