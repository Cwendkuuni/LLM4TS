package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class W3CDomTest {
    
    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        
        org.w3c.dom.Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        assertEquals(w3cDoc, null);
    }
    
    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document(null);
        Document w3cDoc = new DocumentBuilder().newDocument();
        
        new W3CDom().convert(jsoupDoc, w3cDoc);
        assertEquals(w3cDoc.getDocumentURI(), null);
        
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element(null, "", "");
        jsoupDoc.appendChild(jsoupEl);
        
        new W3CDom().convert(jsoupDoc, w3cDoc);
        assertEquals(w3cDoc.getDocumentURI(), null);
    }
    
    @Test
    public void testHead() {
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element(null, "", "");
        jsoupEl.appendChild(new org.jsoup.nodes.Element(null, "", ""));
        jsoupEl.appendChild(new org.jsoup.nodes.TextNode(""));
        jsoupEl.appendChild(new org.jsoup.nodes.Comment(""));
        jsoupEl.appendChild(new org.jsoup.nodes.DataNode(""));
        
        Document w3cDoc = new DocumentBuilder().newDocument();
        Element w3cEl = w3cDoc.createElement("");
        w3cDoc.appendChild(w3cEl);
        
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.head(jsoupEl, 0);
        assertEquals(w3cDoc.getChildNodes().getLength(), 2);
        
        builder.head(new org.jsoup.nodes.TextNode(""), 0);
        assertEquals(w3cDoc.getChildNodes().getLength(), 2);
        
        builder.head(new org.jsoup.nodes.Comment(""), 0);
        assertEquals(w3cDoc.getChildNodes().getLength(), 2);
        
        builder.head(new org.jsoup.nodes.DataNode(""), 0);
        assertEquals(w3cDoc.getChildNodes().getLength(), 2);
    }
    
    @Test
    public void testTail() {
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element(null, "", "");
        
        Document w3cDoc = new DocumentBuilder().newDocument();
        Element w3cEl = w3cDoc.createElement("");
        w3cDoc.appendChild(w3cEl);
        builder.dest = w3cEl;
        
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.tail(jsoupEl, 0);
        assertEquals(builder.dest, null);
        
        jsoupEl.appendChild(new org.jsoup.nodes.Element(null, "", ""));
        builder.dest = w3cEl;
        
        builder.tail(jsoupEl, 0);
        assertEquals(builder.dest, w3cEl);
    }
    
    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element(null, "", "");
        Attribute attr = new Attribute("", "");
        Attribute attr2 = new Attribute("key", "value");
        jsoupEl.attributes().add(attr);
        jsoupEl.attributes().add(attr2);
        
        Document w3cDoc = new DocumentBuilder().newDocument();
        Element w3cEl = w3cDoc.createElement("");
        w3cDoc.appendChild(w3cEl);
        
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.copyAttributes(jsoupEl, w3cEl);
        assertEquals(w3cEl.getAttributes().getLength(), 0);
        
        jsoupEl.tagName("tag");
        builder.copyAttributes(jsoupEl, w3cEl);
        assertEquals(w3cEl.getAttributes().getLength(), 0);
        
        jsoupEl.tagName("xml-tag");
        builder.copyAttributes(jsoupEl, w3cEl);
        assertEquals(w3cEl.getAttributes().getLength(), 1);
        assertEquals(w3cEl.getAttributes().item(0).getNodeValue(), "value");
    }
    
    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element(null, "", "");
        
        Document w3cDoc = new DocumentBuilder().newDocument();
        Element w3cEl = w3cDoc.createElement("");
        w3cDoc.appendChild(w3cEl);
        
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        String prefix = builder.updateNamespaces(jsoupEl);
        assertEquals(prefix, "");
        
        Attribute attr = new Attribute("xmlns", "");
        jsoupEl.attributes().add(attr);
        prefix = builder.updateNamespaces(jsoupEl);
        assertEquals(prefix, "");
        
        attr = new Attribute("xmlns:prefix", "");
        jsoupEl.attributes().add(attr);
        prefix = builder.updateNamespaces(jsoupEl);
        assertEquals(prefix, "prefix");
    }
    
    @Test
    public void testAsString() {
        Document w3cDoc = new DocumentBuilder().newDocument();
        String docStr = new W3CDom().asString(w3cDoc);
        assertEquals(docStr, "");
    }
    
}