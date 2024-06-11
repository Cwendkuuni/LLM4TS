package org.jsoup.safety;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class CleanerTest {

    @Test
    public void testClean() {
        // Typical use case - cleaning a dirty document
        String dirtyHTML = "<html><body><div><script>alert('XSS attack')</script></div></body></html>";
        String expectedCleanHTML = "<html><head></head><body><div></div></body></html>";
        
        Document dirtyDocument = Jsoup.parse(dirtyHTML);
        Document expectedCleanDocument = Jsoup.parse(expectedCleanHTML);
        
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        Assert.assertEquals(expectedCleanDocument.html(), cleanDocument.html());
    }

    @Test
    public void testIsValid() {
        // Typical use case - checking validity of a document that is valid
        String validHTML = "<html><body><div><p>Hello World!</p></div></body></html>";
        
        Document validDocument = Jsoup.parse(validHTML);
        
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        boolean isValid = cleaner.isValid(validDocument);
        
        Assert.assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // Edge case - copying safe nodes from source to destination element
        Element sourceElement = new Element("div");
        Element destElement = new Element("div");
        
        Element pElement = new Element("p");
        pElement.text("This is a paragraph");
        
        sourceElement.appendChild(pElement);
        
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);
        
        Assert.assertEquals(0, numDiscarded);
        Assert.assertEquals("<div><p>This is a paragraph</p></div>", destElement.outerHtml());
    }

    @Test
    public void testCreateSafeElement() {
        // Error scenario - creating safe element with unsafe attributes
        Element sourceElement = new Element("div");
        sourceElement.attr("onclick", "alert('XSS attack')");
        
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        
        Assert.assertEquals("div", elementMeta.el.tagName());
        Assert.assertEquals(1, elementMeta.numAttribsDiscarded);
        Assert.assertEquals("", elementMeta.el.attr("onclick"));
    }

}