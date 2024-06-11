package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class W3CDomTest {

    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testFromJsoupWithNullDocument() {
        Document jsoupDoc = null;
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNull(w3cDoc);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = IllegalStateException.class)
    public void testFromJsoupWithInvalidParserConfigurationException() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(null);
        builder.setEntityResolver(null);
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("", "");
        org.w3c.dom.Document w3cDoc = builder.newDocument();
        w3cDom.convert(jsoupDoc, w3cDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Element rootEl = new Element(Tag.valueOf("root"), "");
        jsoupDoc.appendChild(rootEl);
        TextNode textNode = new TextNode("Hello World");
        rootEl.appendChild(textNode);
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvertWithBlankLocation() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.setBaseUri("");
        Element rootEl = new Element(Tag.valueOf("root"), "");
        jsoupDoc.appendChild(rootEl);
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals("", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvertWithNonBlankLocation() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("http://example.com");
        jsoupDoc.setBaseUri("http://example.com");
        Element rootEl = new Element(Tag.valueOf("root"), "");
        jsoupDoc.appendChild(rootEl);
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals("http://example.com", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvertWithElement() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Element rootEl = new Element(Tag.valueOf("root"), "");
        jsoupDoc.appendChild(rootEl);
        Element childEl = new Element(Tag.valueOf("child"), "");
        rootEl.appendChild(childEl);
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals(1, w3cDoc.getElementsByTagName("root").getLength());
        assertEquals(1, w3cDoc.getElementsByTagName("child").getLength());
    }

    @Test
    public void testConvertWithTextNode() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Element rootEl = new Element(Tag.valueOf("root"), "");
        jsoupDoc.appendChild(rootEl);
        TextNode textNode = new TextNode("Hello World");
        rootEl.appendChild(textNode);
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals(1, w3cDoc.getElementsByTagName("root").getLength());
        assertEquals(1, w3cDoc.getElementsByTagName("#text").getLength());
        Text w3cTextNode = (Text) w3cDoc.getElementsByTagName("#text").item(0);
        assertEquals("Hello World", w3cTextNode.getWholeText());
    }

    @Test
    public void testConvertWithCommentNode() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Element rootEl = new Element(Tag.valueOf("root"), "");
        jsoupDoc.appendChild(rootEl);
        org.jsoup.nodes.Comment commentNode = new org.jsoup.nodes.Comment("This is a comment");
        rootEl.appendChild(commentNode);
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals(1, w3cDoc.getElementsByTagName("root").getLength());
        assertEquals(1, w3cDoc.getElementsByTagName("#comment").getLength());
        Comment w3cCommentNode = (Comment) w3cDoc.getElementsByTagName("#comment").item(0);
        assertEquals("This is a comment", w3cCommentNode.getData());
    }

    @Test
    public void testConvertWithDataNode() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Element rootEl = new Element(Tag.valueOf("root"), "");
        jsoupDoc.appendChild(rootEl);
        org.jsoup.nodes.DataNode dataNode = new org.jsoup.nodes.DataNode("Some data");
        rootEl.appendChild(dataNode);
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals(1, w3cDoc.getElementsByTagName("root").getLength());
        assertEquals(1, w3cDoc.getElementsByTagName("#text").getLength());
        Text w3cDataNode = (Text) w3cDoc.getElementsByTagName("#text").item(0);
        assertEquals("Some data", w3cDataNode.getWholeText());
    }

    @Test
    public void testConvertWithInvalidNode() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Element rootEl = new Element(Tag.valueOf("root"), "");
        jsoupDoc.appendChild(rootEl);
        jsoupDoc.appendChild(new org.jsoup.nodes.Node("invalid") {});
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals(1, w3cDoc.getElementsByTagName("root").getLength());
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element(Tag.valueOf("el"), "");
        sourceEl.attr("attr1", "value1");
        sourceEl.attr("attr2", "value2");
        Element el = w3cDom.fromJsoup(new org.jsoup.nodes.Document("")).createElement("");
        w3cDom.copyAttributes(sourceEl, el);
        assertEquals("value1", el.getAttribute("attr1"));
        assertEquals("value2", el.getAttribute("attr2"));
    }

    @Test
    public void testCopyAttributesWithInvalidAttributeName() {
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element(Tag.valueOf("el"), "");
        sourceEl.attr("attr1$%", "value1");
        Element el = w3cDom.fromJsoup(new org.jsoup.nodes.Document("")).createElement("");
        w3cDom.copyAttributes(sourceEl, el);
        assertNull(el.getAttribute("attr1$%"));
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element(Tag.valueOf("el"), "");
        el.attr("xmlns", "urn:namespace");
        el.attr("xmlns:prefix", "urn:namespace");
        String prefix = w3cDom.updateNamespaces(el);
        assertEquals("", prefix);
        assertEquals("urn:namespace", w3cDom.namespaces.get(""));
        assertEquals("urn:namespace", w3cDom.namespaces.get("prefix"));
    }

    @Test
    public void testUpdateNamespacesWithInvalidKey() {
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element(Tag.valueOf("el"), "");
        el.attr("xmlns$", "urn:namespace");
        String prefix = w3cDom.updateNamespaces(el);
        assertEquals("", prefix);
        assertNull(w3cDom.namespaces.get("xmlns$"));
    }

    @Test
    public void testAsString() {
        DocumentBuilder builder = null;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        org.w3c.dom.Document w3cDoc = builder.newDocument();
        org.w3c.dom.Element rootEl = w3cDoc.createElement("root");
        w3cDoc.appendChild(rootEl);
        org.w3c.dom.Text textNode = w3cDoc.createTextNode("Hello World");
        rootEl.appendChild(textNode);
        String str = w3cDom.asString(w3cDoc);
        assertEquals("<?xml version=\"1.0\" standalone=\"no\"?><root>Hello World</root>", str);
    }

    @Test(expected = IllegalStateException.class)
    public void testAsStringWithTransformerException() {
        DocumentBuilder builder = null;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        org.w3c.dom.Document w3cDoc = builder.newDocument();
        org.w3c.dom.ProcessingInstruction processingInstruction = w3cDoc.createProcessingInstruction("xml-stylesheet", "type=\"text/css\" href=\"style.css\"");
        w3cDoc.appendChild(processingInstruction);
        w3cDom.asString(w3cDoc);
    }
}