package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {
    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;

    @Before
    public void setUp() throws IOException {
        w3cDom = new W3CDom();
        jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Heading</h1></body></html>");
    }

    @Test
    public void testFromJsoup() {
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
        assertEquals("head", w3cDoc.getElementsByTagName("head").item(0).getNodeName());
        assertEquals("title", w3cDoc.getElementsByTagName("title").item(0).getNodeName());
    }
    
    @Test
    public void testConvert() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();

        w3cDom.convert(jsoupDoc, w3cDoc);
        
        Element rootEl = w3cDoc.getDocumentElement();
        
        assertEquals("html", rootEl.getTagName());
        assertEquals(1, rootEl.getElementsByTagName("head").getLength());
        assertEquals("title", rootEl.getElementsByTagName("title").item(0).getNodeName());
        assertEquals("body", rootEl.getElementsByTagName("body").item(0).getNodeName());
        assertEquals(1, rootEl.getElementsByTagName("h1").getLength());
    }

    @Test
    public void testW3CBuilderHeadWithElement() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("a");
        
        w3cBuilder.head(sourceEl, 0);
        
        Element dest = w3cBuilder.dest;
        
        assertNotNull(dest);
        assertEquals("a", dest.getTagName());
    }
    
    @Test
    public void testW3CBuilderHeadWithTextNode() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("text", "");
        
        w3cBuilder.head(sourceText, 0);
        
        Text textNode = w3cBuilder.doc.createTextNode(sourceText.getWholeText());
        
        assertNotNull(textNode);
        assertEquals("text", textNode.getNodeValue());
    }
    
    @Test
    public void testW3CBuilderHeadWithComment() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Comment sourceComment = new org.jsoup.nodes.Comment("comment", "");
        
        w3cBuilder.head(sourceComment, 0);
        
        Comment comment = w3cBuilder.doc.createComment(sourceComment.getData());
        
        assertNotNull(comment);
        assertEquals("comment", comment.getData());
    }
    
    @Test
    public void testW3CBuilderHeadWithDataNode() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.DataNode sourceData = new org.jsoup.nodes.DataNode("data", "");
        
        w3cBuilder.head(sourceData, 0);
        
        Text node = w3cBuilder.doc.createTextNode(sourceData.getWholeData());
        
        assertNotNull(node);
        assertEquals("data", node.getNodeValue());
    }
    
    @Test
    public void testW3CBuilderTailWithElement() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("a");
        
        w3cBuilder.head(sourceEl, 0);
        w3cBuilder.tail(sourceEl, 0);
        
        Element dest = w3cBuilder.dest;
        
        assertNotNull(dest);
        assertTrue(dest.getParentNode() instanceof Element);
        assertTrue(w3cBuilder.namespacesStack.empty());
    }

    @Test
    public void testCopyAttributes() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("a");
        sourceEl.attr("href", "http://example.com");
        
        Element dest = w3cDoc.createElement("a");
        
        w3cBuilder.copyAttributes(sourceEl, dest);
        
        assertEquals("http://example.com", dest.getAttribute("href"));
    }
    
    @Test
    public void testUpdateNamespaces() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("a");
        el.attr("xmlns", "http://www.w3.org/1999/xhtml");
        el.attr("xmlns:ns1", "http://example.com");
        
        String prefix = w3cBuilder.updateNamespaces(el);
        
        assertEquals("a", prefix);
        assertEquals("http://www.w3.org/1999/xhtml", w3cBuilder.namespacesStack.peek().get(""));
        assertEquals("http://example.com", w3cBuilder.namespacesStack.peek().get("ns1"));
    }

    @Test
    public void testAsString() throws ParserConfigurationException, TransformerConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();

        Element rootEl = w3cDoc.createElement("root");
        w3cDoc.appendChild(rootEl);
        
        W3CDom w3cDom = new W3CDom();
        String docString = w3cDom.asString(w3cDoc);
        
        assertNotNull(docString);
        assertTrue(docString.contains("<root></root>"));
    }
}