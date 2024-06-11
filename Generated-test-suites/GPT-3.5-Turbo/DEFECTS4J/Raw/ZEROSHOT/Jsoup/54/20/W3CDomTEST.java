import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Text;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

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

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document result = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(result);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            w3cDoc = builder.newDocument();
        } catch(ParserConfigurationException e) {
            fail("ParserConfigurationException occurred: " + e.getMessage());
        }
        w3cDom.convert(jsoupDoc, w3cDoc);
        assertFalse(w3cDom.asString(w3cDoc).isEmpty());
    }

    @Test
    public void testW3CBuilderHeadWithElement() {
        Document doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
        } catch(ParserConfigurationException e) {
            fail("ParserConfigurationException occurred: " + e.getMessage());
        }
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("tagName", "");
        builder.head(sourceEl, 1);
        assertEquals(1, doc.getChildNodes().getLength());
    }

    @Test
    public void testW3CBuilderHeadWithTextNode() {
        Document doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
        } catch(ParserConfigurationException e) {
            fail("ParserConfigurationException occurred: " + e.getMessage());
        }
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);
        org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("text", "");
        builder.head(sourceText, 1);
        assertEquals(1, doc.getChildNodes().getLength());
    }

    @Test
    public void testW3CBuilderHeadWithComment() {
        Document doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
        } catch(ParserConfigurationException e) {
            fail("ParserConfigurationException occurred: " + e.getMessage());
        }
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);
        org.jsoup.nodes.Comment sourceComment = new org.jsoup.nodes.Comment("comment", "");
        builder.head(sourceComment, 1);
        assertEquals(1, doc.getChildNodes().getLength());
    }

    @Test
    public void testW3CBuilderHeadWithDataNode() {
        Document doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
        } catch(ParserConfigurationException e) {
            fail("ParserConfigurationException occurred: " + e.getMessage());
        }
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);
        org.jsoup.nodes.DataNode sourceData = new org.jsoup.nodes.DataNode("data", "");
        builder.head(sourceData, 1);
        assertEquals(1, doc.getChildNodes().getLength());
    }

    @Test
    public void testW3CBuilderTailWithElement() {
        Document doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
        } catch(ParserConfigurationException e) {
            fail("ParserConfigurationException occurred: " + e.getMessage());
        }
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("tagName", "");
        builder.head(sourceEl, 1);
        builder.tail(sourceEl, 1);
        assertEquals(1, doc.getChildNodes().getLength());
        assertEquals(0, doc.getChildNodes().item(0).getChildNodes().getLength());
    }

    @Test
    public void testCopyAttributes() {
        Document doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
        } catch(ParserConfigurationException e) {
            fail("ParserConfigurationException occurred: " + e.getMessage());
        }
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("tagName", "");
        sourceEl.attr("attr1", "value1");
        sourceEl.attr("attr2", "value2");
        Element el = doc.createElementNS("", "tagName");
        builder.copyAttributes(sourceEl, el);
        assertEquals("value1", el.getAttribute("attr1"));
        assertEquals("value2", el.getAttribute("attr2"));
    }

    @Test
    public void testUpdateNamespaces() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("tagName", "");
        el.attr("xmlns", "namespace");
        builder.updateNamespaces(el);
        assertEquals("namespace", builder.namespaces.get(""));
    }

    @Test
    public void testAsString() {
        Document doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
            Element root = doc.createElement("root");
            Element el = doc.createElement("tagName");
            el.setTextContent("text");
            root.appendChild(el);
            doc.appendChild(root);
        } catch(ParserConfigurationException e) {
            fail("ParserConfigurationException occurred: " + e.getMessage());
        }
        String result = w3cDom.asString(doc);
        assertNotNull(result);
    }
}