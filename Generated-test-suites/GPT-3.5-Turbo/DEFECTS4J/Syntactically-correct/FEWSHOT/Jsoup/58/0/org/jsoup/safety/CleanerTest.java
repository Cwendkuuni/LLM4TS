
package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class CleanerTest {
    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = Jsoup.parse("<html><body><div><h1>Hello</h1><p>World</p></div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        Element body = cleanDocument.body();
        assertFalse(body.getElementsByTag("h1").isEmpty());
        assertFalse(body.getElementsByTag("p").isEmpty());
        
        Element div = body.getElementsByTag("div").get(0);
        assertTrue(div.getElementsByTag("h1").isEmpty());
        assertTrue(div.getElementsByTag("p").isEmpty());
    }
    
    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = Jsoup.parse("<html><body><div><h1>Hello</h1><script>alert('XSS')</script></div></body></html>");
        boolean valid = cleaner.isValid(dirtyDocument);
        
        assertFalse(valid);
        
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element body = cleanDocument.body();
        
        assertFalse(body.getElementsByTag("h1").isEmpty());
        assertTrue(body.getElementsByTag("script").isEmpty());
    }
    
    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = new Element(Tag.valueOf("div"), "");
        
        source.appendChild(new Element(Tag.valueOf("p"), "").text("Hello"));
        source.appendChild(new TextNode("World"));
        
        int discarded = cleaner.copySafeNodes(source, dest);
        
        assertFalse(dest.getElementsByTag("p").isEmpty());
        assertFalse(dest.textNodes().isEmpty());
        assertEquals(0, discarded);
    }
    
    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        sourceEl.attr("class", "container");
        sourceEl.attr("style", "font-size: 12px");
        
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        Element dest = elementMeta.el;
        
        assertFalse(dest.hasClass("container"));
        assertFalse(dest.hasAttr("style"));
        assertEquals("div", dest.tagName());
    }
}
