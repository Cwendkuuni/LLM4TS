package org.jsoup.helper;

import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Document;

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        // Test case 1
        org.jsoup.nodes.Document jsoupDoc1 = new org.jsoup.nodes.Document("");
        Document w3cDoc1 = new W3CDom().fromJsoup(jsoupDoc1);
        assertNotNull(w3cDoc1);
        
        // Test case 2
        org.jsoup.nodes.Document jsoupDoc2 = new org.jsoup.nodes.Document("");
        org.jsoup.nodes.Element rootElement2 = new org.jsoup.nodes.Element("root");
        jsoupDoc2.appendChild(rootElement2);
        Document w3cDoc2 = new W3CDom().fromJsoup(jsoupDoc2);
        assertNotNull(w3cDoc2);
        assertEquals("root", w3cDoc2.getDocumentElement().getTagName());
    }
    
    @Test
    public void testConvert() {
        // Test case 1
        org.jsoup.nodes.Document jsoupDoc1 = new org.jsoup.nodes.Document("");
        Document w3cDoc1 = new W3CDom().fromJsoup(jsoupDoc1);
        org.jsoup.nodes.Element jsoupRoot1 = new org.jsoup.nodes.Element("root");
        jsoupDoc1.appendChild(jsoupRoot1);
        new W3CDom().convert(jsoupDoc1, w3cDoc1);
        assertEquals("root", w3cDoc1.getDocumentElement().getTagName());
        
        // Test case 2
        org.jsoup.nodes.Document jsoupDoc2 = new org.jsoup.nodes.Document("");
        Document w3cDoc2 = new W3CDom().fromJsoup(jsoupDoc2);
        org.jsoup.nodes.Element jsoupRoot2 = new org.jsoup.nodes.Element("root");
        org.jsoup.nodes.Element jsoupElement2 = new org.jsoup.nodes.Element("element");
        jsoupRoot2.appendChild(jsoupElement2);
        jsoupDoc2.appendChild(jsoupRoot2);
        new W3CDom().convert(jsoupDoc2, w3cDoc2);
        assertEquals("root", w3cDoc2.getDocumentElement().getTagName());
        assertEquals("element", w3cDoc2.getDocumentElement().getChildNodes().item(0).getNodeName());
    }
    
    @Test
    public void testW3CBuilderHead() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        
        // Test case 1: org.jsoup.nodes.Element
        org.jsoup.nodes.Element sourceEl1 = new org.jsoup.nodes.Element("tag");
        w3cBuilder.head(sourceEl1, 1);
        assertNotNull(w3cBuilder.dest);
        assertEquals("tag", w3cBuilder.dest.getTagName());
        
        // Test case 2: org.jsoup.nodes.TextNode
        org.jsoup.nodes.TextNode sourceText2 = new org.jsoup.nodes.TextNode("text", "");
        w3cBuilder.head(sourceText2, 1);
        assertNotNull(w3cBuilder.dest);
        assertEquals("#text", w3cBuilder.dest.getNodeName());
        
        // Test case 3: org.jsoup.nodes.Comment
        org.jsoup.nodes.Comment sourceComment3 = new org.jsoup.nodes.Comment("comment");
        w3cBuilder.head(sourceComment3, 1);
        assertNotNull(w3cBuilder.dest);
        assertEquals("#comment", w3cBuilder.dest.getNodeName());
        
        // Test case 4: org.jsoup.nodes.DataNode
        org.jsoup.nodes.DataNode sourceData4 = new org.jsoup.nodes.DataNode("data", "");
        w3cBuilder.head(sourceData4, 1);
        assertNotNull(w3cBuilder.dest);
        assertEquals("#text", w3cBuilder.dest.getNodeName());
    }
    
    @Test
    public void testW3CBuilderTail() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        
        // Test case 1
        org.jsoup.nodes.Element sourceEl1 = new org.jsoup.nodes.Element("tag");
        w3cBuilder.head(sourceEl1, 1);
        w3cBuilder.tail(sourceEl1, 1);
        assertNull(w3cBuilder.dest);
    }
    
    @Test
    public void testCopyAttributes() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("tag");
        sourceEl.attr("attr1", "value1");
        org.w3c.dom.Element destEl = null;
        w3cBuilder.copyAttributes(sourceEl, destEl);
        assertNull(destEl);
        
        Document w3cDoc = new W3CDom().fromJsoup(new org.jsoup.nodes.Document(""));
        destEl = w3cDoc.createElement("tag");
        w3cBuilder.copyAttributes(sourceEl, destEl);
        assertEquals("value1", destEl.getAttribute("attr1"));
    }
    
    @Test
    public void testUpdateNamespaces() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("tag");
        String prefix = w3cBuilder.updateNamespaces(el);
        assertEquals("", prefix);
        
        el.attr("xmlns", "urn:namespace1");
        prefix = w3cBuilder.updateNamespaces(el);
        assertEquals("", prefix);
        
        el.attr("xmlns:prefix", "urn:namespace2");
        prefix = w3cBuilder.updateNamespaces(el);
        assertEquals("prefix", prefix);
    }
    
    @Test
    public void testAsString() {
        Document w3cDoc = new W3CDom().fromJsoup(new org.jsoup.nodes.Document(""));
        assertNotNull(new W3CDom().asString(w3cDoc));
    }
}