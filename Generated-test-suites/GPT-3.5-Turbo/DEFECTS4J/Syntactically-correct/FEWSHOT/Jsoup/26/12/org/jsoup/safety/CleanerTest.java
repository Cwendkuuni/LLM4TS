
package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.List;
import java.util.ArrayList;

public class CleanerTest {
    private Cleaner cleaner;
    
    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.relaxed());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse("<html><body><p>Hello, <b>world</b>!</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertEquals("Hello, world!", cleanDocument.body().html());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Jsoup.parse("<html><body><p>Hello, <b>world</b>!</p></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Element source = Jsoup.parseBodyFragment("<div><p>Hello,</p><b>world</b>!</div>").body();
        Element dest = new Element(Tag.valueOf("div"), "");
        
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        
        assertEquals(2, numDiscarded);
        assertEquals("<p>Hello,</p>!", dest.html());
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = Jsoup.parse("<div class='test' style='color: red;'>Hello, world!</div>").body();
        
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        
        assertEquals("div", meta.el.tagName());
        assertEquals(2, meta.numAttribsDiscarded);
        assertEquals("<div>Hello, world!</div>", meta.el.outerHtml());
    }
    
    @Test
    public void testCleanerWithDifferentWhitelist() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = Jsoup.parse("<html><body><p>Hello, <b>world</b>!</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertEquals("Hello, world!", cleanDocument.body().html());
    }
}

