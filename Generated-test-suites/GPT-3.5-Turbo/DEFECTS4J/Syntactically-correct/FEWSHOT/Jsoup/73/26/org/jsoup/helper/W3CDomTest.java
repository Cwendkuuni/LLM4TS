import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

public class W3CDomTest {
    private W3CDom w3cDom;
    
    @Before
    public void setup() {
        w3cDom = new W3CDom();
    }
    
    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.location("http://www.example.com");
        jsoupDoc.appendChild(new Element("html"));
        
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        assertNotNull(w3cDoc);
        assertEquals("http://www.example.com", w3cDoc.getDocumentURI());
    }
    
    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.appendChild(new Element("html"));
        org.jsoup.nodes.Element body = new org.jsoup.nodes.Element("body");
        body.appendChild(new org.jsoup.nodes.TextNode("Hello World"));
        jsoupDoc.child(0).appendChild(body);
        
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        assertNotNull(w3cDoc);
        assertEquals("http://www.example.com", w3cDoc.getDocumentURI());
        
        org.w3c.dom.Element rootEl = w3cDoc.getDocumentElement();
        assertEquals("html", rootEl.getTagName());
        
        org.w3c.dom.Element bodyEl = (org.w3c.dom.Element) rootEl.getChildNodes().item(0);
        assertEquals("body", bodyEl.getTagName());
        
        Text textNode = (Text) bodyEl.getChildNodes().item(0);
        assertEquals("Hello World", textNode.getWholeText());
    }
    
    @Test
    public void testW3CBuilderHead() {
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("div");
        Element dest = new org.w3c.dom.Element("div", null);
        
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(null);
        w3cBuilder.dest = dest;
        
        w3cBuilder.head(element, 0);
        
        org.w3c.dom.Element createdElement = (org.w3c.dom.Element) dest.getChildNodes().item(0);
        
        assertNotNull(createdElement);
        assertEquals("div", createdElement.getTagName());
    }
    
    @Test
    public void testW3CBuilderHeadTextNode() {
        org.jsoup.nodes.TextNode textNode = new org.jsoup.nodes.TextNode("Hello");
        Element dest = new org.w3c.dom.Element("div", null);
        
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(null);
        w3cBuilder.dest = dest;
        
        w3cBuilder.head(textNode, 0);
        
        TextNode createdTextNode = (TextNode) dest.getChildNodes().item(0);
        
        assertNotNull(createdTextNode);
        assertEquals("Hello", createdTextNode.getWholeText());
    }
    
    @Test
    public void testW3CBuilderHeadComment() {
        org.jsoup.nodes.Comment comment = new org.jsoup.nodes.Comment("comment");
        Element dest = new org.w3c.dom.Element("div", null);
        
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(null);
        w3cBuilder.dest = dest;
        
        w3cBuilder.head(comment, 0);
        
        Comment createdComment = (Comment) dest.getChildNodes().item(0);
        
        assertNotNull(createdComment);
        assertEquals("comment", createdComment.getData());
    }
    
    @Test
    public void testW3CBuilderHeadDataNode() {
        org.jsoup.nodes.DataNode dataNode = new org.jsoup.nodes.DataNode("data");
        Element dest = new org.w3c.dom.Element("div", null);
        
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(null);
        w3cBuilder.dest = dest;
        
        w3cBuilder.head(dataNode, 0);
        
        DataNode createdDataNode = (DataNode) dest.getChildNodes().item(0);
        
        assertNotNull(createdDataNode);
        assertEquals("data", createdDataNode.getWholeData());
    }
    
    @Test
    public void testW3CBuilderTail() {
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("div");
        Element dest = new org.w3c.dom.Element("div", null);
        
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(null);
        w3cBuilder.dest = dest;
        
        w3cBuilder.tail(element, 0);
        
        assertFalse(dest.hasChildNodes());
    }
    
    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element source = new org.jsoup.nodes.Element("div");
        source.attr("id", "div1");
        source.attr("class", "container");
        
        org.w3c.dom.Element el = new org.w3c.dom.Element("div", null);
        
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(null);
        
        w3cBuilder.copyAttributes(source, el);
        
        assertTrue(el.hasAttribute("id"));
        assertEquals("div1", el.getAttribute("id"));
        
        assertTrue(el.hasAttribute("class"));
        assertEquals("container", el.getAttribute("class"));
    }
    
    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("div");
        el.attr("xmlns", "http://www.example.com");
        el.attr("xmlns:prefix", "http://www.prefix.com");
        
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(null);
        
        String prefix = w3cBuilder.updateNamespaces(el);
        
        assertEquals("prefix", prefix);
        assertEquals(2, w3cBuilder.namespaces.size());
        assertEquals("http://www.example.com", w3cBuilder.namespaces.get(""));
        assertEquals("http://www.prefix.com", w3cBuilder.namespaces.get("prefix"));
    }
    
    @Test
    public void testAsString() {
        Document w3cDoc = w3cDom.fromJsoup(new org.jsoup.nodes.Document(""));
        String result = w3cDom.asString(w3cDoc);
        
        assertNotNull(result);
        assertTrue(result.contains("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"));
    }
}