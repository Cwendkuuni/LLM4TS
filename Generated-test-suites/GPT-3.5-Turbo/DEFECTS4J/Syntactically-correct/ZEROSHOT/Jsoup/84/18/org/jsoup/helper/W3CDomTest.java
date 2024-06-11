import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.w3c.dom.*;

import javax.xml.transform.TransformerException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Stack;

import static org.junit.Assert.*;

public class W3CDomTest {
    
    @Test
    public void testFromJsoupNotNull() {
        Document doc = new Document("");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(doc);
        assertNotNull(w3cDoc);
    }
    
    @Test
    public void testConvert() {
        Document doc = new Document("");
        Element rootEl = new Element("root");
        doc.appendChild(rootEl);
        
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(rootEl);
        Document w3cDoc = w3cBuilder.getDoc();
        w3cBuilder.convert(doc, w3cDoc);
        
        assertNotNull(w3cDoc);
        assertEquals("root", w3cDoc.getDocumentElement().getTagName());
    }
    
    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("tag");
        sourceEl.attr("attr1", "value1");
        sourceEl.attr("attr2", "value2");
        
        Element el = new Element("tag");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        w3cBuilder.copyAttributes(sourceEl, el);
        
        assertEquals("value1", el.getAttribute("attr1"));
        assertEquals("value2", el.getAttribute("attr2"));
    }
    
    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("ns:tag");
        el.attr("xmlns", "namespace");
        
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        String prefix = w3cBuilder.updateNamespaces(el);
        
        assertEquals("ns", prefix);
        assertEquals("namespace", w3cBuilder.getNamespacesStack().peek().get("ns"));
    }
    
    @Test
    public void testAsString() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element rootEl = doc.createElement("root");
            doc.appendChild(rootEl);
            
            W3CDom w3cDom = new W3CDom();
            String result = w3cDom.asString(doc);
            
            assertNotNull(result);
        } catch (ParserConfigurationException e) {
            fail("ParserConfigurationException: " + e.getMessage());
        }
    }
}