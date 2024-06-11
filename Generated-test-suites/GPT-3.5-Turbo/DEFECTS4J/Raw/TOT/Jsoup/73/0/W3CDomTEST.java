
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;

import static org.junit.Assert.*;

public class W3CDomTest {

    @Test
    public void testFromJsoup_normalInput() {
        W3CDom w3cDom = new W3CDom();
        
        org.jsoup.nodes.Document jsoupDoc = Document.createShell("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        assertNotNull(w3cDoc);
        assertEquals(jsoupDoc.location(), w3cDoc.getDocumentURI());
    }
    
    @Test
    public void testFromJsoup_nullInput() {
        W3CDom w3cDom = new W3CDom();
        try {
            w3cDom.fromJsoup(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("in must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testConvert_normalInput() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        org.jsoup.nodes.Document jsoupDoc = Document.createShell("");
        Element rootElement = jsoupDoc.createElement("html");
        jsoupDoc.appendChild(rootElement);
        org.jsoup.nodes.Document.OutputSettings outputSettings = jsoupDoc.outputSettings();
        jsoupDoc.parser(new Parser(outputSettings));
        
        w3cBuilder.convert(jsoupDoc, w3cBuilder.doc);
        
        Node root = w3cBuilder.doc.getFirstChild();
        assertNotNull(root);
        assertTrue(root instanceof org.w3c.dom.Element);
        assertEquals(rootElement.tagName(), root.getNodeName());
    }
    
    @Test
    public void testConvert_nullInput() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        try {
            w3cBuilder.convert(null, w3cBuilder.doc);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("in must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testAsString_normalInput() {
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = Document.createShell("");
        String result = w3cDom.asString(w3cDoc);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void testAsString_nullInput() {
        W3CDom w3cDom = new W3CDom();
        try {
            w3cDom.asString(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("doc must not be null", e.getMessage());
        }
    }
}