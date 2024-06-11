import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class W3CDomTest {

    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;

    @Before
    public void setup() {
        w3cDom = new W3CDom();
        jsoupDoc = new org.jsoup.nodes.Document("");
    }

    @Test
    public void testFromJsoup() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }
    
    @Test
    public void testConvert() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        
        w3cDom.convert(jsoupDoc, w3cDoc);
        // Add assertion here
        
    }

    @Test
    public void testW3CBuilderHeadElement() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("tag", "");
        Node destNode = w3cDoc.createElement("tag");
        
        w3cBuilder.head(sourceEl, 0);
        // Add assertion here
        
    }
    
    @Test
    public void testW3CBuilderHeadTextNode() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("text", "");
        Node destNode = w3cDoc.createTextNode("text");
        
        w3cBuilder.head(sourceText, 0);
        // Add assertion here
        
    }
    
    @Test
    public void testW3CBuilderHeadComment() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Comment sourceComment = new org.jsoup.nodes.Comment("comment", "");
        Node destNode = w3cDoc.createComment("comment");
        
        w3cBuilder.head(sourceComment, 0);
        // Add assertion here
        
    }
    
    @Test
    public void testW3CBuilderHeadDataNode() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.DataNode sourceData = new org.jsoup.nodes.DataNode("data", "");
        Node destNode = w3cDoc.createTextNode("data");

        w3cBuilder.head(sourceData, 0);
        // Add assertion here
        
    }
    
    @Test
    public void testW3CBuilderTailElement() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("tag", "");
        Node destNode = w3cDoc.createElement("tag");

        w3cBuilder.tail(sourceEl, 0);
        // Add assertion here
        
    }

    @Test
    public void testCopyAttributes() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("tag", "");
        Element destEl = w3cDoc.createElement("tag");

        w3cBuilder.copyAttributes(sourceEl, destEl);
        // Add assertion here
        
    }

    @Test
    public void testUpdateNamespaces() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("tag", "");
        w3cBuilder.updateNamespaces(sourceEl);
        // Add assertion here
        
    }

    @Test
    public void testAsString() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        
        String w3cDocString = w3cDom.asString(w3cDoc);
        // Add assertion here
        
    }
}