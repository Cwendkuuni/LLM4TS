package org.jsoup.nodes;

import org.junit.*;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testConstructor() {
        String baseUri = "http://www.example.com";
        Document doc = new Document(baseUri);
        assertNotNull(doc);
        assertEquals("#root", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testCreateShell() {
        String baseUri = "http://www.example.com";
        Document doc = Document.createShell(baseUri);
        assertNotNull(doc);
        assertEquals("html", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }
    
    @Test
    public void testHead() {
        String baseUri = "http://www.example.com";
        Document doc = new Document(baseUri);
        Element head = doc.head();
        assertNull(head);
        
        Element headElement = doc.createElement("head");
        doc.appendChild(headElement);
        head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }
    
    @Test
    public void testBody() {
        String baseUri = "http://www.example.com";
        Document doc = new Document(baseUri);
        Element body = doc.body();
        assertNull(body);
        
        Element bodyElement = doc.createElement("body");
        doc.appendChild(bodyElement);
        body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }
    
    @Test
    public void testTitle() {
        String baseUri = "http://www.example.com";
        Document doc = new Document(baseUri);
        String title = doc.title();
        assertEquals("", title);
        
        String newTitle = "Example Page";
        doc.title(newTitle);
        title = doc.title();
        assertEquals(newTitle, title);
        
        Element titleElement = doc.getElementsByTag("title").first();
        assertEquals(newTitle, titleElement.text());
    }
    
    @Test
    public void testNormalise() {
        String baseUri = "http://www.example.com";
        Document doc = new Document(baseUri);
        
        Element htmlElement = doc.appendElement("html");
        Element headElement = htmlElement.appendElement("head");
        Element bodyElement = htmlElement.appendElement("body");
        
        Element divElement = headElement.appendElement("div");
        Node textNode1 = doc.createTextNode("Hello");
        divElement.appendChild(textNode1);
        
        Element pElement = bodyElement.appendElement("p");
        Node textNode2 = doc.createTextNode("World");
        pElement.appendChild(textNode2);
        
        doc.normalise();
        
        assertEquals(0, headElement.children().size());
        assertEquals(1, bodyElement.children().size());
        assertEquals("Hello World", bodyElement.text());
    }
    
    @Test
    public void testClone() {
        String baseUri = "http://www.example.com";
        Document doc = new Document(baseUri);
        Document clone = doc.clone();
        
        assertNotSame(doc, clone);
        assertEquals(doc.baseUri(), clone.baseUri());
        assertEquals(doc.outputSettings(), clone.outputSettings());
    }
    
    @Test
    public void testOutputSettings() {
        String baseUri = "http://www.example.com";
        Document doc = new Document(baseUri);
        
        Document.OutputSettings outputSettings = doc.outputSettings();
        assertNotNull(outputSettings);
        
        String charsetName = "UTF-8";
        outputSettings.charset(charsetName);
        assertEquals(charsetName, outputSettings.charset().name());
        
        boolean prettyPrint = false;
        outputSettings.prettyPrint(prettyPrint);
        assertEquals(prettyPrint, outputSettings.prettyPrint());
        
        int indentAmount = 4;
        outputSettings.indentAmount(indentAmount);
        assertEquals(indentAmount, outputSettings.indentAmount());
    }
    
    @Test
    public void testQuirksMode() {
        String baseUri = "http://www.example.com";
        Document doc = new Document(baseUri);
        Document.QuirksMode quirksMode = Document.QuirksMode.noQuirks;
        
        doc.quirksMode(quirksMode);
        assertEquals(quirksMode, doc.quirksMode());
    }
}