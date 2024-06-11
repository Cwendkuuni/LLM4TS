package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

public class CleanerTest {

    @Test
    public void testCleaner() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<html><body><p>foo</p><script>alert('xss')</script></body></html>", "");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><p>foo</p></body></html>", cleanDocument.body().html());

        // Check that original document is unchanged
        assertEquals("<html><body><p>foo</p><script>alert('xscript')</script></body></html>", dirtyDocument.body().html());
    }

    @Test
    public void testIsValid_ValidDocument() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document validDocument = Parser.parseBodyFragment("<html><body><p>foo</p></body></html>", "");
        assertTrue(cleaner.isValid(validDocument));

        Document invalidDocumentWithScript = Parser.parseBodyFragment("<html><body><p>foo</p><script>alert('xss')</script></body></html>", "");
        assertFalse(cleaner.isValid(invalidDocumentWithScript));
    }

    @Test
    public void testIsValid_InvalidDocumentWithSafeTags() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document invalidDocumentWithSafeTags = Parser.parseBodyFragment("<html><body><p>foo</p><div id='myDiv'>safe tag</div></body></html>", "");
        assertTrue(cleaner.isValid(invalidDocumentWithSafeTags));
    }

    @Test
    public void testIsValid_InvalidDocumentWithScriptTag() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document invalidDocumentWithScriptTag = Parser.parseBodyFragment("<html><body><p>foo</p><script src='malicious.js'></script></body></html>", "");
        assertFalse(cleaner.isValid(invalidDocumentWithScriptTag));
    }

    @Test
    public void testIsValid_InvalidDocumentWithStyleTag() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document invalidDocumentWithStyleTag = Parser.parseBodyFragment("<html><body><p>foo</p><style>body{background-color: red;}</style></body></html>", "");
        assertFalse(cleaner.isValid(invalidDocumentWithStyleTag));
    }

    @Test
    public void testIsValid_InvalidDocumentWithComment() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document invalidDocumentWithComment = Parser.parseBodyFragment("<html><body><p>foo</p><!-- comment --></body></html>", "");
        assertTrue(cleaner.isValid(invalidDocumentWithComment));
    }

    @Test
    public void testIsValid_InvalidDocumentWithUnknownTag() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document invalidDocumentWithUnknownTag = Parser.parseBodyFragment("<html><body><p>foo</p><unknownTag></body></html>", "");
        assertFalse(cleaner.isValid(invalidDocumentWithUnknownTag));
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<html><body><p>foo<script>alert('xss')</script></p></body></html>", "");
        Element root = dirtyDocument.body();

        Element dest = new Element(Tag.BODY, "");
        int numDiscarded = cleaner.copySafeNodes(root, dest);

        assertEquals("foo", dest.text());
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCopySafeNodesWithEmptyDocument() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = null;
        Element dest = new Element(Tag.BODY, "");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument, dest);

        assertNull(dest.text());
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceEl = new Element(Tag.P, "", new Attributes().put("id", "foo"));

        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        Element dest = meta.el;

        assertEquals("p", dest.tagName());
        assertTrue(dest.hasAttr("id"));
        assertEquals("foo", dest.attr("id"));
    }

    @Test
    public void testCreateSafeElementWithSafeTagAndAttributes() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceEl = new Element(Tag.DIV, "", new Attributes().put("id", "foo").put("class", "bar"));

        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        Element dest = meta.el;

        assertEquals("div", dest.tagName());
        assertTrue(dest.hasAttr("id"));
        assertEquals("foo", dest.attr("id"));
        assertTrue(dest.hasAttr("class"));
        assertEquals("bar", dest.attr("class"));
    }

    @Test
    public void testCreateSafeElementWithUnsafeTag() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceEl = new Element(Tag.SCRIPT, "", new Attributes().put("src", "malicious.js"));

        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        Element dest = meta.el;

        assertNull(dest);
        assertEquals(0, meta.numAttribsDiscarded);
    }

}