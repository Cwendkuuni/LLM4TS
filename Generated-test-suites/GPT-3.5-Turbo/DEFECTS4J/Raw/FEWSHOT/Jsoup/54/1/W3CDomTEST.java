package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {
    
    private static final String XML_STRING = "<root><div id=\"test\"><span>Hello</span><p>World</p></div></root>";
    
    private W3CDom w3cDom;
    
    @Before
    public void setup() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(XML_STRING);
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        assertNotNull(w3cDoc);
        assertEquals(XML_STRING, w3cDom.asString(w3cDoc));
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(XML_STRING);
        org.w3c.dom.Document w3cDoc = createW3CDocument();
        
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        assertNotNull(w3cDoc);
        assertEquals(XML_STRING, w3cDom.asString(w3cDoc));
    }
    
    @Test
    public void testW3CBuilder() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(XML_STRING);
        org.w3c.dom.Document w3cDoc = createW3CDocument();
        
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        
        org.jsoup.nodes.Element sourceEl = jsoupDoc.getElementsByTag("div").first();
        w3cBuilder.head(sourceEl, 0);
        
        Element dest = w3cBuilder.getDest();
        assertNotNull(dest);
        assertEquals("div", dest.getNodeName());
        assertEquals("test", dest.getAttribute("id"));
        
        org.jsoup.nodes.Element childEl = sourceEl.children().first();
        w3cBuilder.head(childEl, 1);
        
        dest = w3cBuilder.getDest();
        assertNotNull(dest);
        assertEquals("span", dest.getNodeName());
        
        org.jsoup.nodes.TextNode text = (TextNode) childEl.childNode(0);
        w3cBuilder.head(text, 2);
        
        TextNode textNode = (TextNode) dest.getLastChild();
        assertNotNull(textNode);
        assertEquals("Hello", textNode.getTextContent());
        
        org.jsoup.nodes.Element siblingEl = (Element) sourceEl.childNode(1);
        w3cBuilder.head(siblingEl, 1);
        
        dest = w3cBuilder.getDest();
        assertNotNull(dest);
        assertEquals("p", dest.getNodeName());
        
        org.jsoup.nodes.TextNode text2 = (TextNode) siblingEl.childNode(0);
        w3cBuilder.head(text2, 2);
        
        TextNode textNode2 = (TextNode) dest.getLastChild();
        assertNotNull(textNode2);
        assertEquals("World", textNode2.getTextContent());
        
    }
    
    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(XML_STRING);
        org.w3c.dom.Document w3cDoc = createW3CDocument();
        
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        
        org.jsoup.nodes.Element sourceEl = jsoupDoc.getElementsByTag("div").first();
        org.w3c.dom.Element dest = w3cDoc.createElement("div");
        
        w3cBuilder.copyAttributes(sourceEl, dest);
        
        assertNotNull(dest.getAttributes());
        assertEquals("test", dest.getAttribute("id"));
    }
    
    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(XML_STRING);
        org.jsoup.nodes.Element el = jsoupDoc.getElementsByTag("div").first();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        
        String prefix = w3cBuilder.updateNamespaces(el);
        assertNotNull(prefix);
        assertTrue(prefix.isEmpty());
        
        el.attr("xmlns:foo", "http://www.foo.com");
        prefix = w3cBuilder.updateNamespaces(el);
        assertNotNull(prefix);
        assertTrue(prefix.isEmpty());
        assertNotNull(w3cBuilder.getNamespaces());
        assertEquals("http://www.foo.com", w3cBuilder.getNamespaces().get("foo"));
        
        el.attr("xmlns", "http://www.bar.com");
        prefix = w3cBuilder.updateNamespaces(el);
        assertNotNull(prefix);
        assertTrue(prefix.isEmpty());
        assertNotNull(w3cBuilder.getNamespaces());
        assertEquals("http://www.bar.com", w3cBuilder.getNamespaces().get(""));
    }
    
    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(XML_STRING);
        org.w3c.dom.Document w3cDoc = createW3CDocument();
        
        String w3cString = w3cDom.asString(w3cDoc);
        
        assertNotNull(w3cString);
        assertEquals(XML_STRING, w3cString);
    }
    
    private org.w3c.dom.Document createW3CDocument() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            return factory.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }
}