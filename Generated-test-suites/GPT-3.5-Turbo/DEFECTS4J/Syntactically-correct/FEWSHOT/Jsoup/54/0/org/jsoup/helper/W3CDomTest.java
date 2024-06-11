package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
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

public class W3CDomTest {
    private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private W3CDom w3cDom = new W3CDom();

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupNullInput() {
        w3cDom.fromJsoup(null);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
        jsoupDoc.appendChild(jsoupElement);

        Document w3cDoc = createEmptyW3cDoc();
        w3cDom.convert(jsoupDoc, w3cDoc);
        Element w3cElement = w3cDoc.getDocumentElement();
        assertNotNull(w3cElement);
        assertEquals("div", w3cElement.getTagName());
    }

    @Test
    public void testConvertWithLocation() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("http://example.com");
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
        jsoupDoc.appendChild(jsoupElement);

        Document w3cDoc = createEmptyW3cDoc();
        w3cDom.convert(jsoupDoc, w3cDoc);
        Element w3cElement = w3cDoc.getDocumentElement();
        assertNotNull(w3cElement);
        assertEquals("div", w3cElement.getTagName());
        assertEquals("http://example.com", w3cDoc.getDocumentURI());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertNullInput() {
        Document w3cDoc = createEmptyW3cDoc();
        w3cDom.convert(null, w3cDoc);
    }

    @Test
    public void testW3CBuilderHeadElement() {
        Document w3cDoc = createEmptyW3cDoc();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
        builder.head(jsoupElement, 0);
        Element w3cElement = w3cDoc.getDocumentElement();
        assertNotNull(w3cElement);
        assertEquals("div", w3cElement.getTagName());
    }

    @Test
    public void testW3CBuilderHeadTextNode() {
        Document w3cDoc = createEmptyW3cDoc();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.TextNode jsoupText = new org.jsoup.nodes.TextNode("text", "");
        builder.head(jsoupText, 0);
        Element w3cElement = w3cDoc.getDocumentElement();
        assertNotNull(w3cElement);
        assertEquals("text", w3cElement.getTextContent());
    }

    @Test
    public void testW3CBuilderHeadComment() {
        Document w3cDoc = createEmptyW3cDoc();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Comment jsoupComment = new org.jsoup.nodes.Comment("comment");
        builder.head(jsoupComment, 0);
        Element w3cElement = w3cDoc.getDocumentElement();
        assertNotNull(w3cElement);
        assertEquals("comment", w3cElement.getTextContent());
    }

    @Test
    public void testW3CBuilderHeadDataNode() {
        Document w3cDoc = createEmptyW3cDoc();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.DataNode jsoupData = new org.jsoup.nodes.DataNode("data", "");
        builder.head(jsoupData, 0);
        Element w3cElement = w3cDoc.getDocumentElement();
        assertNotNull(w3cElement);
        assertEquals("data", w3cElement.getTextContent());
    }

    @Test
    public void testW3CBuilderTailElement() {
        Document w3cDoc = createEmptyW3cDoc();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
        builder.head(jsoupElement, 0);
        builder.tail(jsoupElement, 0);
        Element w3cElement = w3cDoc.getDocumentElement();
        assertNull(w3cElement.getParentNode());
    }

    @Test
    public void testW3CBuilderTailNonElement() {
        Document w3cDoc = createEmptyW3cDoc();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.TextNode jsoupText = new org.jsoup.nodes.TextNode("text", "");
        builder.tail(jsoupText, 0);
        Element w3cElement = w3cDoc.getDocumentElement();
        assertNull(w3cElement.getParentNode());
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
        jsoupElement.attr("class", "myClass");
        jsoupElement.attr("data-id", "1234567890");

        Document w3cDoc = createEmptyW3cDoc();
        Element w3cElement = w3cDoc.createElement("div");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.copyAttributes(jsoupElement, w3cElement);

        assertEquals("myClass", w3cElement.getAttribute("class"));
        assertEquals("1234567890", w3cElement.getAttribute("data-id"));
    }

    @Test
    public void testCopyAttributesInvalidXml() {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
        jsoupElement.attr("invalidAttr", "<invalid>value</invalid>");

        Document w3cDoc = createEmptyW3cDoc();
        Element w3cElement = w3cDoc.createElement("div");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.copyAttributes(jsoupElement, w3cElement);

        assertEquals("", w3cElement.getAttribute("invalidAttr"));
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
        jsoupElement.attr("xmlns", "http://example.com");
        jsoupElement.attr("xmlns:prefix", "http://example.com/prefix");

        Document w3cDoc = createEmptyW3cDoc();
        Element w3cElement = w3cDoc.createElement("div");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        String prefix = builder.updateNamespaces(jsoupElement);

        assertEquals("http://example.com", w3cDoc.getDocumentElement().getAttribute("xmlns"));
        assertEquals("http://example.com/prefix", w3cDoc.getDocumentElement().getAttribute("xmlns:prefix"));
        assertEquals("", prefix);
    }

    @Test
    public void testAsString() {
        Document w3cDoc = createEmptyW3cDoc();
        Element root = w3cDoc.createElement("root");
        root.setTextContent("text");
        w3cDoc.appendChild(root);

        String xmlString = w3cDom.asString(w3cDoc);
        assertTrue(xmlString.contains("<root>text</root>"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsStringNullInput() {
        w3cDom.asString(null);
    }

    private Document createEmptyW3cDoc() {
        DocumentBuilder builder;
        try {
        	//set the factory to be namespace-aware
        	factory.setNamespaceAware(true);
            builder = factory.newDocumentBuilder();
            return builder.newDocument();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }
}