import org.junit.Test;

import org.jsoup.nodes.*;
import org.jsoup.select.NodeTraversor;

import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = new Document("");
        
        // Test when body is null
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNull(cleanDocument.body());
        
        // Test when body is not null
        dirtyDocument.body().append("<p>This is a paragraph</p>");
        cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument.body());
        assertEquals("<p>This is a paragraph</p>", cleanDocument.body().html());
    }

    @Test
    public void testIsValid() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = new Document("");
        
        // Test when body is null
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
        
        // Test when body is not null
        dirtyDocument.body().append("<p>This is a paragraph</p>");
        isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
        
        // Test when unsafe tags are present
        dirtyDocument.body().append("<script>alert('XSS')</script>");
        isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Element source = new Element("div");
        Element dest = new Element("div");
        
        source.appendChild(new Element("p").text("This is a paragraph"));
        
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(0, numDiscarded);
        assertEquals("<p>This is a paragraph</p>", dest.html());
    }

    @Test
    public void testCreateSafeElement() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Element sourceEl = new Element("p");
        sourceEl.attr("class", "test");
        
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        assertNotNull(elementMeta.el);
        assertEquals("p", elementMeta.el.tagName());
        assertEquals("<p class=\"test\"></p>", elementMeta.el.outerHtml());
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}