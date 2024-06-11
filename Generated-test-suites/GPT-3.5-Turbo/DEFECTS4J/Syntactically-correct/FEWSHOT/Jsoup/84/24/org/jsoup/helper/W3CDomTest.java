import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {
    
    @Test
    public void testFromJsoupNotNull() {
        Document jsoupDoc = new Document();
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }
    
    @Test
    public void testFromJsoupDocumentURI() {
        Document jsoupDoc = new Document();
        jsoupDoc.location("http://example.com");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertEquals("http://example.com", w3cDoc.getDocumentURI());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testFromJsoupParserConfigurationException() {
        Document jsoupDoc = new Document();
        W3CDom w3cDom = new W3CDom();
        w3cDom.factory = null;
        w3cDom.fromJsoup(jsoupDoc);
    }
    
    @Test
    public void testConvert() {
        Document jsoupDoc = new Document();
        Element rootEl = new Element("root");
        jsoupDoc.appendChild(rootEl);
        
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        assertNotNull(w3cDoc);
        assertEquals("root", w3cDoc.getDocumentElement().getTagName());
    }
    
    @Test
    public void testW3CBuilderHeadElement() {
        Document w3cDoc = new Document();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
        w3cBuilder.head(jsoupElement, 0);
        
        Element w3cElement = w3cDoc.createElementNS(null, "div");
        assertEquals(w3cElement, w3cDoc.getDocumentElement());
    }
    
    @Test
    public void testW3CBuilderHeadTextNode() {
        Document w3cDoc = new Document();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        
        org.jsoup.nodes.TextNode jsoupTextNode = new org.jsoup.nodes.TextNode("text");
        w3cBuilder.head(jsoupTextNode, 0);
        
        Text w3cText = w3cDoc.createTextNode("text");
        assertEquals(w3cText, w3cDoc.getDocumentElement().getFirstChild());
    }
    
    @Test
    public void testW3CBuilderHeadComment() {
        Document w3cDoc = new Document();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        
        org.jsoup.nodes.Comment jsoupComment = new org.jsoup.nodes.Comment("comment");
        w3cBuilder.head(jsoupComment, 0);
        
        Comment w3cComment = w3cDoc.createComment("comment");
        assertEquals(w3cComment, w3cDoc.getDocumentElement().getFirstChild());
    }
    
    @Test
    public void testW3CBuilderHeadDataNode() {
        Document w3cDoc = new Document();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        
        org.jsoup.nodes.DataNode jsoupDataNode = new org.jsoup.nodes.DataNode("data");
        w3cBuilder.head(jsoupDataNode, 0);
        
        Text w3cText = w3cDoc.createTextNode("data");
        assertEquals(w3cText, w3cDoc.getDocumentElement().getFirstChild());
    }
    
    @Test
    public void testW3CBuilderTail() {
        Document w3cDoc = new Document();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
        w3cBuilder.head(jsoupElement, 0);
        w3cBuilder.tail(jsoupElement, 0);
        assertNull(w3cDoc.getDocumentElement().getParentNode());
    }
    
    @Test
    public void testCopyAttributes() {
        Document w3cDoc = new Document();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
        jsoupElement.attr("id", "myId");
        jsoupElement.attr("class", "myClass");
        
        Element w3cElement = w3cDoc.createElementNS(null, "div");
        w3cBuilder.copyAttributes(jsoupElement, w3cElement);
        
        assertEquals("myId", ((Attr) w3cElement.getAttributes().item(0)).getValue());
        assertEquals("myClass", ((Attr) w3cElement.getAttributes().item(1)).getValue());
    }
    
    @Test
    public void testUpdateNamespaces() {
        Document w3cDoc = new Document();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
        jsoupElement.attr("xmlns", "urn:example:namespace");
        jsoupElement.attr("xmlns:prefix", "urn:example:prefix");
        
        assertEquals("prefix", w3cBuilder.updateNamespaces(jsoupElement));
        assertEquals("urn:example:namespace", w3cBuilder.namespacesStack.peek().get("prefix"));
    }
    
    @Test
    public void testAsString() {
        Document w3cDoc = new Document();
        W3CDom w3cDom = new W3CDom();
        String xmlString = w3cDom.asString(w3cDoc);
        assertNotNull(xmlString);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAsStringTransformerException() {
        Document w3cDoc = new Document();
        W3CDom w3cDom = new W3CDom();
        w3cDom.factory = null;
        w3cDom.asString(w3cDoc);
    }
}