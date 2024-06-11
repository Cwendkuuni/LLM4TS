package org.jsoup.safety;

import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {

    private Cleaner cleaner;
    
    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.simpleText());
    }

    @Test
    public void testClean() {
        String dirtyHtml = "<html><head><title>Dirty HTML</title></head><body><div><p>Dirty Text</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        
        assertEquals("<html><head></head><body><div><p>Dirty Text</p></div></body></html>", cleanedDocument.html());
    }

    @Test
    public void testIsValid() {
        String validHtml = "<html><head><title>Valid HTML</title></head><body><div><p>Valid Text</p></div></body></html>";
        Document validDocument = Jsoup.parse(validHtml);
        
        boolean isValid = cleaner.isValid(validDocument);
        
        assertEquals(true, isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Element rootElement = new Element("div");
        
        Element unsafeElement = new Element("script");
        unsafeElement.text("unsafe script");
        rootElement.appendChild(unsafeElement);
        
        Element safeElement = new Element("p");
        safeElement.text("safe text");
        rootElement.appendChild(safeElement);
        
        Element destination = new Element("body");
        
        int numDiscarded = cleaner.copySafeNodes(rootElement, destination);
        
        assertEquals(1, numDiscarded);
        assertEquals("<body><p>safe text</p></body>", destination.html());
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceElement = new Element("a");
        sourceElement.attr("href", "http://example.com");
        
        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        
        assertEquals("a", elementMeta.el.tagName());
        assertEquals(0, elementMeta.numAttribsDiscarded);
        assertEquals("http://example.com", elementMeta.el.attr("href"));
    }
}