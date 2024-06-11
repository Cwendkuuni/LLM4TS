
package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {
    
    @Test
    public void testClean() {
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        
        Document dirtyDocument = Jsoup.parse("<html><body><div><p>Hello World!</p></div></body></html>");
        
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        Element body = cleanDocument.body();
        
        assertTrue(body.hasText());
        assertEquals(1, body.children().size());
    }
    
    @Test
    public void testIsValid() {
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        
        Document dirtyDocument = Jsoup.parse("<html><body><div><script>alert('XSS')</script></div></body></html>");
        
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertEquals(false, isValid);
    }
    
    @Test
    public void testCopySafeNodes() {
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        
        Element root = Jsoup.parseBodyFragment("<div><p>Hello World!</p></div>").body();
        Element destination = new Element("div");
        
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        
        assertEquals(0, numDiscarded);
        assertTrue(destination.hasText());
        assertEquals(1, destination.children().size());
    }
    
    @Test
    public void testCreateSafeElement() {
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        
        Element sourceElement = new Element("script");
        sourceElement.attr("src", "http://example.com");
        
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        
        assertEquals("script", elementMeta.el.tagName());
        assertEquals(0, elementMeta.numAttribsDiscarded);
        assertEquals("http://example.com", elementMeta.el.attr("src"));
    }
}
