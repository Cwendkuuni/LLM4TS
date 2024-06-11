package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {
    @Test
    public void testCreateCleaner() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNull(cleaner.whitelist);
        assertSame(whitelist, cleaner.whitelist);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateCleanerWithNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().appendElement("html").appendElement("head").appendElement("title", "Test");
        dirtyDocument.body().appendElement("body").appendElement("p", "This is a test");

        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element body = cleanDocument.body();
        assertNull(body.child(0));
        assertTrue(body.child(1) instanceof TextNode);
        assertEquals("This is a test", body.child(1).text());

        // Test with empty document
        Document emptyDocument = Document.createShell("http://example.com");
        Document cleanEmptyDocument = cleaner.clean(emptyDocument);
        assertTrue(cleanEmptyDocument.body().isEmpty());

        // Test with invalid document (empty body)
        Document invalidDocument = Document.createShell("http://example.com");
        Document cleanInvalidDocument = cleaner.clean(invalidDocument);
        assertTrue(cleanInvalidDocument.body().isEmpty());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().appendElement("html").appendElement("head").appendElement("title", "Test");
        dirtyDocument.body().appendElement("body").appendElement("p", "This is a test");

        assertTrue(cleaner.isValid(dirtyDocument));

        dirtyDocument.body().appendElement("script", "alert('XSS');");
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test(expected = NullPointerException.class)
    public void testIsValidWithNullDocument() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        cleaner.isValid(null);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.createShell("http://example.com");
        Element root = dirtyDocument.body().appendElement("html").appendElement("body");

        Element dest = new Element(Tag.BODY, root.baseUri());
        int numDiscarded = cleaner.copySafeNodes(root, dest);
        assertTrue(dest.childNodes().size() == 0);
        assertSame(numDiscarded, root.childNodes().size());

        root.appendElement("p", "Test");
        Element dest2 = new Element(Tag.BODY, root.baseUri());
        numDiscarded = cleaner.copySafeNodes(root, dest2);
        assertTrue(dest2.childNodes().size() == 1);
        assertSame(dest2.child(0), root.child(0));
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceEl = new Element("div", "class", "test");
        sourceEl.attr("data-test", "testdata");

        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        Element destEl = meta.el;

        assertTrue(destEl.tagName().equals("div"));
        assertFalse(destEl.className().isEmpty());
        assertFalse(destEl.className().contains("test"));
        assertFalse(destEl.hasAttr("data-test"));
        assertTrue(destEl.hasAttr("class"));

        // Test with empty element
        Element emptySourceEl = new Element("div");
        Element emptyDestEl = cleaner.createSafeElement(emptySourceEl);
        assertTrue(emptyDestEl.tagName().equals("div"));
        assertTrue(emptyDestEl.attributes().isEmpty());

        // Test with disallowed tag
        Element disallowedSourceEl = new Element("script");
        Element disallowedDestEl = cleaner.createSafeElement(disallowedSourceEl);
        assertTrue(disallowedDestEl == null);
    }
}