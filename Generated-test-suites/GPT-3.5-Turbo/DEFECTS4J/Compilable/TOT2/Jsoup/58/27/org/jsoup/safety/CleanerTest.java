package org.jsoup.safety;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {
    
    @Test
    public void testClean() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        
        Document dirtyDocument = new Document("");
        Element bodyElement = new Element("body");
        dirtyDocument.appendChild(bodyElement);
        
        Element divElement = new Element("div");
        divElement.text("Hello, World!");
        bodyElement.appendChild(divElement);
        
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        
        assertEquals("Cleaned document has correct base URI", dirtyDocument.baseUri(), cleanedDocument.baseUri());
        assertTrue("Cleaned document contains body element", cleanedDocument.body() != null);
        assertEquals("Cleaned document contains correct text", divElement.text(), cleanedDocument.body().text());
    }
    
    @Test
    public void testIsValid() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        
        Document validDocument = new Document("");
        Element validBodyElement = new Element("body");
        validDocument.appendChild(validBodyElement);
        
        assertTrue("Valid document returns true for isValid", cleaner.isValid(validDocument));
        
        Document invalidDocument = new Document("");
        Element invalidHeadElement = new Element("head");
        invalidDocument.appendChild(invalidHeadElement);
        
        assertFalse("Invalid document returns false for isValid", cleaner.isValid(invalidDocument));
    }
    
    // Add more test cases for other methods here
    
}