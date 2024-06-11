package org.jsoup.safety;

import static org.junit.Assert.*;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testCleanerInstance() {
        Whitelist whitelist = new Whitelist(); // assuming Whitelist is a valid class
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test
    public void testCleanDocument() {
        Document dirtyDocument = Document.fromHtml("<html><body><script>alert('XSS')</script><h1>Hello</h1></body></html>");
        Cleaner cleaner = new Cleaner(new Whitelist());
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertEquals("<html><body><h1>Hello</h1></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValidDocument() {
        Document dirtyDocument = Document.fromHtml("<html><body><h1 id='test' class='test'>Hello</h1></body></html>");
        Cleaner cleaner = new Cleaner(new Whitelist());
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testInvalidDocument() {
        Document dirtyDocument = Document.fromHtml("<html><body><script>alert('XSS')</script><h1>Hello</h1></body></html>");
        Cleaner cleaner = new Cleaner(new Whitelist());
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Document dirtyDocument = Document.fromHtml("<html><body><h1 id='test' class='test'><script>alert('XSS')</script>Hello</h1></body></html>");
        Element sourceEl = dirtyDocument.body().child(0);
        Cleaner cleaner = new Cleaner(new Whitelist());
        Element dest = new Element(Tag.valueOf("div"));
        int numDiscarded = cleaner.copySafeNodes(sourceEl, dest);
        assertEquals(1, numDiscarded); // script tag is discarded
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("h1"), null, new Attributes().put("id", "test").put("class", "test"));
        Cleaner cleaner = new Cleaner(new Whitelist());
        Element dest = new Element(Tag.valueOf("div"));
        int numDiscarded = cleaner.copySafeNodes(sourceEl, dest);
        assertEquals(2, numDiscarded); // id and class attributes are discarded, assuming they are not in the whitelist
    }

    @Test
    public void testIsSafeTag() {
        Whitelist whitelist = new Whitelist();
        assertTrue(whitelist.isSafeTag("h1"));
        assertFalse(whitelist.isSafeTag("script"));
    }

    @Test
    public void testIsSafeAttribute() {
        Whitelist whitelist = new Whitelist();
        Attributes attributes = new Attributes().put("id", "test").put("class", "test");
        Element sourceEl = new Element(Tag.valueOf("h1"), null, attributes);
        assertTrue(whitelist.isSafeAttribute("h1", sourceEl, attributes.get("id")));
        assertTrue(whitelist.isSafeAttribute("h1", sourceEl, attributes.get("class")));
        assertFalse(whitelist.isSafeAttribute("h1", sourceEl, new Attribute("onclick", "alert('XSS')")));
    }

    @Test
    public void testGetEnforcedAttributes() {
        Whitelist whitelist = new Whitelist();
        Attributes enforcedAttributes = whitelist.getEnforcedAttributes("h1");
        assertEquals(0, enforcedAttributes.size()); // assuming no enforced attributes for h1
    }
}