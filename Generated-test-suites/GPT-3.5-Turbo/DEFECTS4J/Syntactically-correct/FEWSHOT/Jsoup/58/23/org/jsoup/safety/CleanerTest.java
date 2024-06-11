package org.jsoup.safety;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CleanerTest {
    @Test
    public void testClean() {
        // Create a dirty document
        Document dirtyDocument = new Document("");
        Element body = dirtyDocument.appendElement("body");
        Element div1 = body.appendElement("div");
        div1.attr("class", "content");
        div1.appendText("Hello");
        Element div2 = body.appendElement("div");
        div2.appendElement("script").attr("src", "example.js");
        
        // Create a whitelist
        Whitelist whitelist = Whitelist.basic();
        
        // Create a cleaner
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Clean the document
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        // Assert the result
        Elements cleanElements = cleanDocument.getElementsByTag("div");
        assertEquals(1, cleanElements.size());
        assertEquals("content", cleanElements.get(0).attr("class"));
        assertEquals("Hello", cleanElements.get(0).text());
    }

    @Test
    public void testIsValid() {
        // Create a dirty document
        Document dirtyDocument = new Document("");
        Element body = dirtyDocument.appendElement("body");
        Element div1 = body.appendElement("div");
        div1.attr("class", "content");
        div1.appendText("Hello");
        Element div2 = body.appendElement("div");
        div2.appendElement("script").attr("src", "example.js");
        
        // Create a whitelist
        Whitelist whitelist = Whitelist.basic();
        
        // Create a cleaner
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Check if the document is valid
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        // Assert the result
        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // Create a dirty element
        Element dirtyElement = new Element("div");
        dirtyElement.attr("class", "content");
        dirtyElement.appendText("Hello");
        
        // Create a destination element
        Element destElement = new Element("div");
        
        // Create a whitelist
        Whitelist whitelist = Whitelist.basic();
        
        // Create a cleaner
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Copy the safe nodes
        int numDiscarded = cleaner.copySafeNodes(dirtyElement, destElement);
        
        // Assert the result
        assertEquals(0, numDiscarded);
        assertEquals("content", destElement.attr("class"));
        assertEquals("Hello", destElement.text());
    }

    @Test
    public void testCreateSafeElement() {
        // Create a source element
        Element sourceElement = new Element("div");
        sourceElement.attr("class", "content");
        
        // Create a whitelist
        Whitelist whitelist = Whitelist.basic();
        
        // Create a cleaner
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Create a safe element
        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceElement);
        Element safeElement = meta.el;
        
        // Assert the result
        assertEquals("div", safeElement.tagName());
        assertEquals("content", safeElement.attr("class"));
    }
}