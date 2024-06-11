package org.jsoup.helper;

import static org.junit.Assert.*;

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
import java.util.Stack;

public class W3CDomTest {
    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = null;
        w3cDom.convert(jsoupDoc, w3cDoc);

        assertNotNull(w3cDoc);
    }

    @Test
    public void testW3CBuilderHead_Element() {
        Document doc = null; // initialize with W3C Document
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("tag", "");
        sourceEl.attr("key", "value");
        org.jsoup.nodes.Node source = sourceEl;
        int depth = 0;

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(doc);
        w3cBuilder.head(source, depth);

        Element dest = w3cBuilder.dest;
        assertNotNull(dest);
        assertEquals("tag", dest.getTagName());
        assertEquals("value", dest.getAttribute("key"));
    }

    @Test
    public void testW3CBuilderHead_TextNode() {
        Document doc = null; // initialize with W3C Document
        org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("text", "");
        org.jsoup.nodes.Node source = sourceText;
        int depth = 0;

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(doc);
        w3cBuilder.head(source, depth);

        Text dest = (Text) w3cBuilder.dest.getFirstChild();
        assertNotNull(dest);
        assertEquals("text", dest.getWholeText());
    }

    @Test
    public void testW3CBuilderHead_Comment() {
        Document doc = null; // initialize with W3C Document
        org.jsoup.nodes.Comment sourceComment = new org.jsoup.nodes.Comment("comment", "");
        org.jsoup.nodes.Node source = sourceComment;
        int depth = 0;

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(doc);
        w3cBuilder.head(source, depth);

        Comment dest = (Comment) w3cBuilder.dest.getFirstChild();
        assertNotNull(dest);
        assertEquals("comment", dest.getData());
    }

    @Test
    public void testW3CBuilderHead_DataNode() {
        Document doc = null; // initialize with W3C Document
        org.jsoup.nodes.DataNode sourceData = new org.jsoup.nodes.DataNode("data", "");
        org.jsoup.nodes.Node source = sourceData;
        int depth = 0;

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(doc);
        w3cBuilder.head(source, depth);

        Text dest = (Text) w3cBuilder.dest.getFirstChild();
        assertNotNull(dest);
        assertEquals("data", dest.getWholeData());
    }

    @Test
    public void testW3CBuilderTail() {
        Document doc = null; // initialize with W3C Document
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("tag", "");
        org.jsoup.nodes.Node source = sourceEl;
        int depth = 0;

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(doc);
        w3cBuilder.head(source, depth);
        w3cBuilder.tail(source, depth);

        Element dest = w3cBuilder.dest;
        assertNull(dest);
        assertTrue(w3cBuilder.namespacesStack.isEmpty());
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("tag", "");
        el.attr("xmlns", "namespace");

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        String prefix = w3cBuilder.updateNamespaces(el);

        assertEquals("", prefix);
        assertTrue(w3cBuilder.namespacesStack.peek().containsKey(""));
        assertEquals("namespace", w3cBuilder.namespacesStack.peek().get(""));
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element source = new org.jsoup.nodes.Element("tag", "");
        source.attr("key1", "value1");
        source.attr("key2", "value2");

        Document doc = null; // initialize with W3C Document
        Element el = doc.createElementNS(null, "tag");

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(doc);
        w3cBuilder.copyAttributes(source, el);

        assertEquals("value1", el.getAttribute("key1"));
        assertEquals("value2", el.getAttribute("key2"));
    }

    @Test
    public void testAsString() {
        Document doc = null; // initialize with W3C Document
        String output = w3cDom.asString(doc);

        assertNotNull(output);
    }

}