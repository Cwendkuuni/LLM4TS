import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }
    
    @Test
    public void testConvert() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("tagName", "");
        w3cDom.convert(jsoupDoc, w3cDoc);
        assertNotNull(w3cDoc);
    }
    
    @Test
    public void testW3CBuilder() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("tagName", "");
        builder.head(sourceEl, 0);
        assertNull(builder.dest);
        
        org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("text", "");
        builder.head(sourceText, 0);
        assertNotNull(builder.dest);
        
        org.jsoup.nodes.Comment sourceComm = new org.jsoup.nodes.Comment("comment", "");
        builder.head(sourceComm, 0);
        assertNotNull(builder.dest);
        
        org.jsoup.nodes.DataNode sourceData = new org.jsoup.nodes.DataNode("data", "");
        builder.head(sourceData, 0);
        assertNotNull(builder.dest);
        
        builder.tail(sourceEl, 0);
        assertNull(builder.dest.getParentNode());
    }
    
    @Test
    public void testCopyAttributes() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("tagName", "");
        Element w3cEl = builder.doc.createElementNS("", "tagName");
        builder.copyAttributes(sourceEl, w3cEl);
        assertNotNull(w3cEl);
    }
    
    @Test
    public void testUpdateNamespaces() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("tagName", "");
        String prefix = builder.updateNamespaces(el);
        assertEquals("", prefix);
        
        el.attributes().put("xmlns:test", "http://www.example.com");
        prefix = builder.updateNamespaces(el);
        assertEquals("test", prefix);
    }
    
    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = null;
        String result = w3cDom.asString(w3cDoc);
        assertNotNull(result);
    }
}