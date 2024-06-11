import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.Whitelist;

import static org.junit.Assert.*;
import org.junit.Test;

public class CleanerTest {
    
    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = new Document("");
        Element dirtyBody = new Element(Tag.valueOf("body"), "");
        dirtyBody.appendChild(new Element(Tag.valueOf("div"), ""));
        dirtyDocument.appendChild(dirtyBody);
        
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertEquals("Expected body element", 1, cleanDocument.body().children().size());
    }
    
    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = new Document("");
        Element dirtyBody = new Element(Tag.valueOf("body"), "");
        dirtyBody.appendChild(new Element(Tag.valueOf("div"), ""));
        dirtyDocument.appendChild(dirtyBody);
        
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertFalse("Document should not be valid", isValid);
    }
    
    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = new Document("");
        Element dirtyBody = new Element(Tag.valueOf("body"), "");
        dirtyBody.appendChild(new Element(Tag.valueOf("div"), ""));
        dirtyDocument.appendChild(dirtyBody);
        
        Element cleanBody = new Element(Tag.valueOf("body"), "");
        int numDiscarded = cleaner.copySafeNodes(dirtyBody, cleanBody);
        
        assertEquals("Expected 1 discarded element", 1, numDiscarded);
    }
    
    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Element dirtyElement = new Element(Tag.valueOf("div"), "");
        dirtyElement.attr("id", "test");
        
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(dirtyElement);
        
        assertEquals("Expected element with tag div", "div", elementMeta.el.tagName());
        assertEquals("Expected 1 discarded attribute", 1, elementMeta.numAttribsDiscarded);
    }
}