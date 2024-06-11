package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Before;
import org.junit.Test;
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
    private org.jsoup.nodes.Document jsoupDocument;
    private org.jsoup.nodes.Element jsoupElement;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDocument = new org.jsoup.nodes.Document("");
        jsoupElement = new org.jsoup.nodes.Element("tag", null);
    }

    @Test
    public void testFromJsoup() {
        Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        assertEquals(jsoupDocument, w3cDocument);
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoupParserConfigurationException() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        w3cDom.factory = factory;
        w3cDom.fromJsoup(jsoupDocument);
    }

    @Test
    public void testConvert() {
        Document w3cDocument = new Document();
        w3cDom.convert(jsoupDocument, w3cDocument);
        assertEquals(jsoupDocument, w3cDocument);
    }

    @Test
    public void testConvertSkipLocation() {
        jsoupDocument.location("");
        Document w3cDocument = new Document();
        w3cDom.convert(jsoupDocument, w3cDocument);
        assertEquals("", w3cDocument.getDocumentURI());
    }

    @Test
    public void testConvertRoot() {
        jsoupDocument.appendChild(jsoupElement);
        Document w3cDocument = new Document();
        w3cDom.convert(jsoupDocument, w3cDocument);
        assertEquals(jsoupElement, w3cDocument.getElementsByTagName("tag").item(0));
    }

    @Test
    public void testW3CBuilderHeadElement() {
        jsoupElement.attributes().put("key", "value");
        Document w3cDocument = new Document();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);
        builder.head(jsoupElement, 0);
        Element w3cElement = w3cDocument.createElementNS("", "tag");
        w3cElement.setAttribute("key", "value");
        assertEquals(w3cElement, w3cDocument.getDocumentElement());
    }

    @Test
    public void testW3CBuilderHeadTextNode() {
        org.jsoup.nodes.TextNode textNode = new org.jsoup.nodes.TextNode("text", null);
        Document w3cDocument = new Document();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);
        builder.head(textNode, 0);
        Text w3cText = w3cDocument.createTextNode("text");
        assertEquals(w3cText, w3cDocument.getDocumentElement().getChildNodes().item(0));
    }

    @Test
    public void testW3CBuilderHeadComment() {
        org.jsoup.nodes.Comment comment = new org.jsoup.nodes.Comment("comment");
        Document w3cDocument = new Document();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);
        builder.head(comment, 0);
        Comment w3cComment = w3cDocument.createComment("comment");
        assertEquals(w3cComment, w3cDocument.getDocumentElement().getChildNodes().item(0));
    }

    @Test
    public void testW3CBuilderHeadDataNode() {
        org.jsoup.nodes.DataNode dataNode = new org.jsoup.nodes.DataNode("data", null);
        Document w3cDocument = new Document();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);
        builder.head(dataNode, 0);
        Text w3cText = w3cDocument.createTextNode("data");
        assertEquals(w3cText, w3cDocument.getDocumentElement().getChildNodes().item(0));
    }

    @Test
    public void testW3CBuilderHeadUnhandledNode() {
        org.jsoup.nodes.Node node = new org.jsoup.nodes.Node(0) {
            @Override
            public String nodeName() {
                return null;
            }
        };
        Document w3cDocument = new Document();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);
        builder.head(node, 0);
    }

    @Test
    public void testW3CBuilderTailElement() {
        jsoupElement.attributes().put("key", "value");
        Document w3cDocument = new Document();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);
        builder.head(jsoupElement, 0);
        builder.tail(jsoupElement, 0);
        assertEquals("", builder.dest.getTagName());
    }

    @Test
    public void testW3CBuilderTailUnhandledNode() {
        org.jsoup.nodes.Node node = new org.jsoup.nodes.Node(0) {
            @Override
            public String nodeName() {
                return null;
            }
        };
        Document w3cDocument = new Document();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);
        builder.head(node, 0);
        builder.tail(node, 0);
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Attributes jsoupAttributes = new org.jsoup.nodes.Attributes();
        jsoupAttributes.put("key", "value");
        Element w3cElement = new Element("", "tag");
        w3cDom.copyAttributes(jsoupElement, w3cElement);
        assertEquals("value", w3cElement.getAttribute("key"));
    }

    @Test
    public void testUpdateNamespaces() {
        jsoupElement.attributes().put("key", "value");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(new Document());
        builder.updateNamespaces(jsoupElement);
        assertEquals("tag", builder.namespaces.get(""));
    }

    @Test(expected = IllegalStateException.class)
    public void testAsStringTransformerException() throws TransformerException {
        DOMSource domSource = new DOMSource(new Document());
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(domSource, result);
        w3cDom.asString(new Document());
    }

    @Test
    public void testAsString() {
        Document w3cDocument = new Document();
        String expected = w3cDocument.toString();
        String actual = w3cDom.asString(w3cDocument);
        assertEquals(expected, actual);
    }
}