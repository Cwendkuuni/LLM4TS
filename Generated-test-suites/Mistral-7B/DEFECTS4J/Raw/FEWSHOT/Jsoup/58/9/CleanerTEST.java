package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

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
    public void testCleanerIsValid_ValidInput() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<html><body><p>Hello</p></body></html>", null);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleaner.isValid(dirtyDocument));
        assertEquals(dirtyDocument, cleanDocument);
    }

    @Test
    public void testCleanerIsValid_InvalidInput() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<html><body><script>alert('XSS')</script></body></html>", null);
        assertFalse(cleaner.isValid(dirtyDocument));

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertTrue(cleanDocument.body().child(0).nodeName().equals("p"));
        assertTrue(cleanDocument.body().text().equals(""));
    }

    @Test
    public void testCleaner_CopySafeNodes() {
        Whitelist whitelist = new Whitelist.SimpleElementWhitelist("p");
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<html><body><p>Hello</p><p>World</p></body></html>", null);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element root = cleanDocument.body();
        Node child = root.child(0);
        assertTrue(child.nodeName().equals("p"));
        assertTrue(child.text().equals("Hello"));

        child = root.child(1);
        assertTrue(child.nodeName().equals("p"));
        assertTrue(child.text().equals("World"));
    }

    @Test
    public void testCleaner_IsValidWithEnforcedAttributes() {
        Whitelist whitelist = new Whitelist.SimpleElementWhitelist("p");
        whitelist.addAttributeFilter("p", "style", null);

        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<html><body><p style=\"color: red;\" >Hello</p></body></html>", null);
        assertTrue(cleaner.isValid(dirtyDocument));

        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element p = cleanDocument.body().child(0);
        assertTrue(p.className().isEmpty());
        assertTrue(p.hasAttr("style"));
        assertEquals(p.attr("style"), "");
    }

    @Test
    public void testCleaner_IsValidWithDataNodes() {
        Whitelist whitelist = new Whitelist.SimpleElementWhitelist("p");
        whitelist.addElementFilter("p", DataNode.class);

        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<html><body><p data-foo=\"bar\">Hello</p></body></html>", null);
        assertTrue(cleaner.isValid(dirtyDocument));

        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element p = cleanDocument.body().child(0);
        assertTrue(p.className().isEmpty());
        assertTrue(p.hasAttr("data-foo"));
        assertEquals(p.attr("data-foo"), "bar");
    }

    @Test
    public void testCleaner_CopySafeNodesWithEmptyAttributes() {
        Whitelist whitelist = new Whitelist.SimpleElementWhitelist("p");

        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<html><body><p class=\"foo\" >Hello</p></body></html>", null);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element root = cleanDocument.body();
        Node child = root.child(0);
        assertTrue(child.nodeName().equals("p"));
        assertTrue(child.text().equals("Hello"));
        assertTrue(child.className().isEmpty());
    }

    @Test
    public void testCleaner_CopySafeNodesWithAttributesToRemove() {
        Whitelist whitelist = new Whitelist.SimpleElementWhitelist("p");
        whitelist.removeAttributeFilter("p", "class");

        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<html><body><p class=\"foo\" >Hello</p></body></html>", null);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element root = cleanDocument.body();
        Node child = root.child(0);
        assertTrue(child.nodeName().equals("p"));
        assertTrue(child.text().equals("Hello"));
        assertNull(child.className());
    }
}