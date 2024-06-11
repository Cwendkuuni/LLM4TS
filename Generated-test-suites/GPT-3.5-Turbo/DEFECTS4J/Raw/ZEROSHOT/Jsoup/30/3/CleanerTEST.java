
package org.jsoup.safety;

//import statements
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class CleanerTest {
    //create a test instance of the Cleaner class
    private Cleaner cleaner = new Cleaner(new Whitelist());

    //test clean method
    @Test
    public void testClean() {
        //create a dirty document with body
        Document dirtyDocument = Document.createShell("");
        
        //create an element with unsafe tag and attributes
        Element unsafeElement = new Element(Tag.valueOf("script"), "");
        unsafeElement.attr("src", "http://example.com/script.js");
        unsafeElement.appendText("alert('XSS Attack');");
        
        //append the unsafe element to the dirty document body
        dirtyDocument.body().appendChild(unsafeElement);
        
        //clean the document
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        
        //check if the cleaned document still contains the unsafe element
        assertEquals(0, cleanedDocument.body().childNodes().size());
    }

    //test isValid method
    @Test
    public void testIsValid() {
        //create a dirty document with body
        Document dirtyDocument = Document.createShell("");
        
        //create a safe element
        Element safeElement = new Element(Tag.valueOf("a"), "");
        safeElement.attr("href", "http://example.com");
        safeElement.appendText("Link");
        
        //append the safe element to the dirty document body
        dirtyDocument.body().appendChild(safeElement);
        
        //check if the dirty document is valid
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    //test copySafeNodes method
    @Test
    public void testCopySafeNodes() {
        //create a root element and destination element
        Element root = new Element(Tag.valueOf("body"), "");
        Element destination = new Element(Tag.valueOf("div"), "");
        
        //create a safe element with safe attributes
        Element safeElement = new Element(Tag.valueOf("a"), "");
        safeElement.attr("href", "http://example.com");
        safeElement.appendText("Link");
        
        //append the safe element to the root element
        root.appendChild(safeElement);
        
        //copy safe nodes from root to destination
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        
        //check if the root element's child is copied to the destination
        assertEquals(1, destination.childNodes().size());
        
        //check if the number of discarded nodes is 0
        assertEquals(0, numDiscarded);
    }

    //test createSafeElement method
    @Test
    public void testCreateSafeElement() {
        //create a source element with an unsafe tag and attributes
        Element sourceElement = new Element(Tag.valueOf("script"), "");
        sourceElement.attr("src", "http://example.com/script.js");
        sourceElement.appendText("alert('XSS Attack');");
        
        //create a safe element using createSafeElement method
        ElementMeta meta = cleaner.createSafeElement(sourceElement);
        Element safeElement = meta.el;
        
        //check if the safe element has the same tag as the source element
        assertEquals(sourceElement.tagName(), safeElement.tagName());
        
        //check if the safe element has no unsafe attributes
        assertFalse(safeElement.hasAttr("src"));
    }
}
