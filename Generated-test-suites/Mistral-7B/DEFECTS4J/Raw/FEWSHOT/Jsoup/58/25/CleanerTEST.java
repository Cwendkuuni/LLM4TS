package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;

public class CleanerTest {

    @Test
    public void testCreateCleaner() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNull(cleaner.whitelist);
        assertSame(whitelist, cleaner.whitelist);
    }

    @Test
    public void testCleanDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<html><body><div id='test'>foo <script>alert('xss');</script> bar</div></body></html>", null);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><div>foo bar</div></body></html>", cleanDocument.html());
        assertFalse(cleanDocument.body().childNodes().hasTextNodes());
        assertFalse(cleanDocument.body().childNodes().hasElementSiblings());
        assertFalse(cleanDocument.body().childNodes().hasAttributeSiblings());
    }

    @Test
    public void testIsValidDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<html><body><div id='test'>foo <script>alert('xss');</script> bar</div></body></html>", null);
        assertTrue(cleaner.isValid(dirtyDocument));
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertFalse(cleaner.isValid(dirtyDocument));
        assertFalse(dirtyDocument.body().childNodes().hasTextNodes());
        assertFalse(dirtyDocument.body().childNodes().hasElementSiblings());
        assertFalse(dirtyDocument.body().childNodes().hasAttributeSiblings());
        assertEquals(cleanDocument.body().childNodes().size(), 1);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<html><body><div id='test' foo='bar'><p><script>alert('xss');</script></p></div></body></html>", null);
        Element destination = new Element("div");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), destination);

        assertEquals("<div></div>", destination.html());
        assertEquals(numDiscarded, 3); // 'script' tag and 'foo' attribute are discarded
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceElement = new Element("div", null, null, null, null, null, null, null, null);
        sourceElement.attributes().put("id", "test");
        sourceElement.attributes().put("foo", "bar");

        Element destElement = cleaner.createSafeElement(sourceElement);

        assertEquals("<div></div>", destElement.html());
        assertNull(destElement.attributes().get("foo"));
    }
}