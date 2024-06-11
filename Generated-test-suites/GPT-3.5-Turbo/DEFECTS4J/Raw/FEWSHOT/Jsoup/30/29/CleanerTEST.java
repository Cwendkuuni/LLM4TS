package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {
    private static final Whitelist whitelist = Whitelist.basic();
    private static final Cleaner cleaner = new Cleaner(whitelist);

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse("<html><body><div><a href=\"#\">Link</a></div></body></html>");
        Document clean = cleaner.clean(dirtyDocument);
        Element body = clean.body();
        
        assertEquals(1, body.children().size());
        Element div = body.child(0);
        assertEquals("div", div.tagName());
        Element a = div.child(0);
        assertEquals("a", a.tagName());
        assertEquals("#", a.attr("href"));
        assertEquals("Link", a.text());
    }

    @Test
    public void testIsValid_validDocument() {
        Document dirtyDocument = Jsoup.parse("<html><body><div><a href=\"#\">Link</a></div></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testIsValid_invalidDocument() {
        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");
        
        Element span = new Element(Tag.valueOf("span"), "");
        span.text("Some text");
        root.appendChild(span);
        
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        
        assertEquals(1, destination.children().size());
        Element copiedSpan = destination.child(0);
        assertEquals("span", copiedSpan.tagName());
        assertEquals("Some text", copiedSpan.text());
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("a"), "");
        sourceEl.attr("href", "#");
        sourceEl.attr("onclick", "alert('XSS')");
        
        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        Element dest = elementMeta.el;
        
        assertEquals("a", dest.tagName());
        assertEquals("#", dest.attr("href"));
        assertFalse(dest.hasAttr("onclick"));
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}