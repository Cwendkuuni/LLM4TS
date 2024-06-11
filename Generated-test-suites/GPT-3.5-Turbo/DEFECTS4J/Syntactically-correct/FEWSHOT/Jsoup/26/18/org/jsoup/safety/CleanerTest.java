
package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    
    private Cleaner cleaner;
    
    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.relaxed());
    }
    
    @Test
    public void testClean() {
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        body.append("<div>Hello World</div>");
        dirtyDocument.appendChild(body);
        
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><head></head><body><div>Hello World</div></body></html>", cleanDocument.html());
    }
    
    @Test
    public void testIsValid() {
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        body.append("<div>Hello World</div>");
        dirtyDocument.appendChild(body);
        
        assertTrue(cleaner.isValid(dirtyDocument));
        
        dirtyDocument.append("<script>alert('XSS');</script>");
        assertFalse(cleaner.isValid(dirtyDocument));
    }
    
    @Test
    public void testCopySafeNodes() {
        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = new Element(Tag.valueOf("div"), "");
        
        source.append("<span id='test'>Hello World</span>");
        
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(0, numDiscarded);
        assertEquals("<span id='test'>Hello World</span>", dest.html());
    }
    
    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        sourceEl.addClass("test");
        sourceEl.attr("data-value", "123");
        
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        Element dest = elementMeta.el;
        assertEquals("<div class=\"test\" data-value=\"123\"></div>", dest.outerHtml());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
    
}
