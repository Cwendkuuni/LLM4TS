package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Traversal;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.xml.namespace.QName;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Stack;

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        String html = "<html><head><title>Test</title></head>" +
                "<body><div id='container'><p id='p1'>Hello, world!</p></div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertNotNull(w3cDoc.getDocumentElement());
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());

        Iterator<Node> nodes = getChildNodes(w3cDoc.getDocumentElement());
        assertEquals(2, countNodes(nodes));

        Element head = (Element) nodes.next();
        Element title = (Element) nodes.next();
        assertEquals("HEAD", title.getTagName());
        assertEquals("title", title.getNodeName());

        Node p1 = findNode(w3cDoc, new QName("div", "container"), new QName("p", null), null);
        assertNotNull(p1);
        Element pElement = (Element) p1;
        assertEquals("p", pElement.getTagName());
        assertEquals("p1", pElement.getNodeName());
        assertEquals("Hello, world!", pElement.getTextContent());
    }

    @Test
    public void testConvert() {
        String html = "<html><head><title>Test</title></head>" +
                "<body><div id='container'><p id='p1'>Hello, world!</p></div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        Document w3cDoc2 = new W3CDom().convert(jsoupDoc, w3cDoc);
        assertSame(w3cDoc, w3cDoc2);
    }

    @Test
    public void testAsString() {
        String html = "<html><head><title>Test</title></head>" +
                "<body><div id='container'><p id='p1'>Hello, world!</p></div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        String w3cDocString = new W3CDom().asString(w3cDoc);
        String expected = "<html><head><title>Test</title></head><body><div id='container'><p id='p1'>Hello, world!</p></div></body></html>";
        assertEquals(expected, w3cDocString);
    }

    private static Node findNode(Document doc, QName elementName, QName attributeName, String attributeValue) {
        DocumentTraversal traversal = (DocumentTraversal) doc;
        NodeFilter filter = new NodeFilter() {
            @Override
            public boolean acceptNode(Node node) {
                if (node instanceof Element) {
                    Element element = (Element) node;
                    if (element.getTagName().equals(elementName.getLocalPart())) {
                        if (attributeName != null) {
                            Attribute attribute = element.getAttributeNode(attributeName.getLocalPart());
                            if (attribute != null && attribute.getValue().equals(attributeValue)) {
                                return true;
                            }
                        }
                        return true;
                    }
                }
                return false;
            }
        };
        Node node = traversal.getFirstChild(doc, filter);
        return node;
    }

    private static Iterator<Node> getChildNodes(Node node) {
        Traversal traversal = node.getOwnerDocument().createDocumentTraversal();
        return traversal.getChildNodes(node);
    }

    private static int countNodes(Iterator<Node> nodes) {
        int count = 0;
        while (nodes.hasNext()) {
            nodes.next();
            count++;
        }
        return count;
    }
}