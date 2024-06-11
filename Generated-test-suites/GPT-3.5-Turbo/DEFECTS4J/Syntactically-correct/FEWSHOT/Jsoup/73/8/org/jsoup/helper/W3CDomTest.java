package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import org.w3c.dom.DOMException;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {
    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;
    private org.jsoup.nodes.Element jsoupRootEl;
    private Document w3cDoc;
    
    @Before
    public void setup() {
        w3cDom = new W3CDom();
        jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupRootEl = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("html"), "");
        jsoupDoc.appendChild(jsoupRootEl);
        w3cDoc = w3cDom.fromJsoup(jsoupDoc);
    }

    @Test
    public void testFromJsoup() {
        assertNotNull(w3cDoc);
    }
    
    @Test
    public void testConvert() {
        w3cDom.convert(jsoupDoc, w3cDoc);
        // verify conversion
        Element rootEl = w3cDoc.getDocumentElement();
        assertEquals("html", rootEl.getTagName());
        assertEquals(1, rootEl.getChildNodes().getLength());
        assertNotNull(rootEl.getAttributes());
    }
    
    @Test
    public void testW3CBuilderHead() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.head(jsoupRootEl, 0);
        Element createdEl = w3cDoc.getDocumentElement();
        assertEquals("html", createdEl.getTagName());
    }
    
    @Test
    public void testW3CBuilderTail() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.head(jsoupRootEl, 0);
        w3cBuilder.tail(jsoupRootEl, 0);
        Element createdEl = w3cDoc.getDocumentElement();
        assertEquals("html", createdEl.getTagName());
    }
    
    @Test
    public void testCopyAttributes() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        Element createdEl = w3cDoc.createElement("div");
        w3cBuilder.copyAttributes(jsoupRootEl, createdEl);
        assertEquals(0, createdEl.getAttributes().getLength());
        
        org.jsoup.nodes.Attribute attrClass = new org.jsoup.nodes.Attribute(org.jsoup.parser.AttributeKey.valueOf("class"), "testClass");
        jsoupRootEl.attributes().put(attrClass);
        w3cBuilder.copyAttributes(jsoupRootEl, createdEl);
        assertEquals(1, createdEl.getAttributes().getLength());
        assertEquals("testClass", createdEl.getAttribute("class"));
    }
    
    @Test
    public void testUpdateNamespaces() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Attribute attrXmlns1 = new org.jsoup.nodes.Attribute(org.jsoup.parser.AttributeKey.valueOf("xmlns"), "http://www.w3.org/1999/xhtml");
        org.jsoup.nodes.Attribute attrXmlns2 = new org.jsoup.nodes.Attribute(org.jsoup.parser.AttributeKey.valueOf("xmlns:ns1"), "http://www.w3.org/1999/xhtml/ns1");
        jsoupRootEl.attributes().put(attrXmlns1);
        jsoupRootEl.attributes().put(attrXmlns2);
        
        assertEquals("", w3cBuilder.updateNamespaces(jsoupRootEl));
        assertEquals("http://www.w3.org/1999/xhtml", w3cBuilder.namespaces.get(""));
        assertEquals("http://www.w3.org/1999/xhtml/ns1", w3cBuilder.namespaces.get("ns1"));
    }
    
    @Test
    public void testAsString() {
        String result = w3cDom.asString(w3cDoc);
        assertNotNull(result);
    }
}