package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

    @Test
    public void testCleaner() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals(0, cleanDocument.body().childNodeSize());
        assertNull(cleanDocument.head());
    }

    @Test
    public void testCleanValidDocument() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("Hello World", cleanDocument.body().text());
        assertNotNull(cleanDocument.head());
    }

    @Test
    public void testIsValidDocument() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World</p></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertEquals(true, isValid);

        Document invalidDocument = Jsoup.parse("<html><body><p>Hello World</p><script>alert('XSS');</script></body></html>");
        isValid = cleaner.isValid(invalidDocument);
        assertEquals(false, isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p id='p1'>Hello World <span id='s1'>Span</span></p></body></html>");
        Element root = dirtyDocument.body();
        Element destination = Jsoup.parse("<html><body></body></html>").body();

        int numDiscarded = cleaner.copySafeNodes(root, destination);

        assertEquals("Hello World Span", destination.text());
        assertEquals(2, destination.childNodeSize());
        assertEquals("p", destination.child(0).tagName());
        assertEquals("span", destination.child(1).tagName());
        assertEquals("p1", destination.child(0).attribute(0).attributeName());
        assertEquals("id", destination.child(0).attribute(0).attributeName());
        assertEquals("p1", destination.child(0).attribute(0).attributeValue());
        assertEquals("s1", destination.child(1).attribute(0).attributeName());
        assertEquals("id", destination.child(1).attribute(0).attributeName());
        assertEquals("s1", destination.child(1).attribute(0).attributeValue());
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodesWithDiscardedAttributes() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml().addAttributeStartingWith("test-", false);
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p id='p1' test-foo='bar' test-baz='qux'>Hello World <span id='s1' test-foo='quux'>Span</span></p></body></html>");
        Element root = dirtyDocument.body();
        Element destination = Jsoup.parse("<html><body></body></html>").body();

        int numDiscarded = cleaner.copySafeNodes(root, destination);

        assertEquals("Hello World Span", destination.text());
        assertEquals(2, destination.childNodeSize());
        assertEquals("p", destination.child(0).tagName());
        assertEquals("span", destination.child(1).tagName());
        assertNull(destination.child(0).attribute(0));
        assertNull(destination.child(1).attribute(0));
        assertEquals(2, numDiscarded);
    }
}