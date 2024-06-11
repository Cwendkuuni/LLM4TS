package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CleanerTest {

    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setUp() {
        whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        Document dirtyDocument = new Document("<html><body><div>Dirty content</div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element body = cleanDocument.body();
        assertEquals(1, body.children().size());
        assertEquals("div", body.child(0).tagName());
        assertEquals("Dirty content", body.child(0).text());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = new Document("<html><body><div>Dirty content</div></body></html>");
        assertTrue(cleaner.isValid(dirtyDocument));

        Document invalidDocument = new Document("<html><body><a href=\"javascript:alert(1);\">Invalid link</a></body></html>");
        assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Document document = new Document("<html><body><div>Safe content</div><a href=\"https://example.com\">Link</a></body></html>");
        Element source = document.body();
        Element dest = new Element(Tag.valueOf("body"), document.baseUri());
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(0, numDiscarded);
        assertEquals(2, dest.children().size());
        assertEquals("div", dest.child(0).tagName());
        assertEquals("Safe content", dest.child(0).text());
        assertEquals("a", dest.child(1).tagName());
        assertEquals("Link", dest.child(1).text());
        assertEquals(1, dest.child(1).attributes().size());
        assertEquals("https://example.com", dest.child(1).attr("href"));
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("a"), "https://example.com");
        sourceEl.attr("href", "https://example.com");
        sourceEl.attr("onclick", "alert(1)");
        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        Element dest = elementMeta.el;
        assertEquals("a", dest.tagName());
        assertEquals("https://example.com", dest.baseUri());
        assertEquals(1, dest.attributes().size());
        assertEquals("https://example.com", dest.attr("href"));
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}