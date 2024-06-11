
package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class CleanerTest {
    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Create a dirty document
        Document dirtyDocument = new Document("");
        Element body = dirtyDocument.appendElement("body");
        Element div = body.appendElement("div");
        div.appendElement("script").attr("src", "http://www.example.com/malicious.js");
        TextNode textNode = new TextNode("Hello, <b>World</b>!", "");
        body.appendChild(textNode);
        
        // Clean the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        // Assert that only safe nodes are retained
        assertEquals(1, cleanDocument.body().childNodes().size());
        assertTrue(cleanDocument.body().childNodes().get(0) instanceof TextNode);
        assertEquals("Hello, <b>World</b>!", cleanDocument.body().text());
    }
    
    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Create a dirty document with unsafe nodes
        Document dirtyDocument = new Document("");
        Element body = dirtyDocument.appendElement("body");
        body.appendElement("script").attr("src", "http://www.example.com/malicious.js");
        
        // Check validity of the dirty document
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        // Assert that the document is not valid
        assertEquals(false, isValid);
    }
    
    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Create a dirty document
        Document dirtyDocument = new Document("");
        Element body = dirtyDocument.appendElement("body");
        Element div = body.appendElement("div");
        div.appendElement("script").attr("src", "http://www.example.com/malicious.js");
        TextNode textNode = new TextNode("Hello, <b>World</b>!", "");
        body.appendChild(textNode);
        
        // Create a clean document
        Document cleanDocument = new Document("");
        Element cleanBody = cleanDocument.appendElement("body");
        
        // Copy safe nodes from the dirty document to the clean document
        int numDiscarded = cleaner.copySafeNodes(body, cleanBody);
        
        // Assert that only safe nodes are retained
        assertEquals(1, cleanBody.childNodes().size());
        assertTrue(cleanBody.childNodes().get(0) instanceof TextNode);
        assertEquals("Hello, <b>World</b>!", cleanBody.text());
        
        // Assert that the number of discarded nodes is correct
        assertEquals(1, numDiscarded);
    }
    
    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Create a source element
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        sourceEl.attr("src", "http://www.example.com/malicious.js");
        
        // Create a safe element
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        Element safeElement = elementMeta.el;
        
        // Assert that only safe attributes are retained
        assertEquals("div", safeElement.tagName());
        assertEquals(0, safeElement.attributes().size());
        
        // Assert that the number of discarded attributes is correct
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}
