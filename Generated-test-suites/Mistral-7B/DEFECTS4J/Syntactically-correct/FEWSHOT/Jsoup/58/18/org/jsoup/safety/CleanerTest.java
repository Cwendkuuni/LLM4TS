package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

public class CleanerTest {

    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setUp() {
        whitelist = Whitelist.simple();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testCreateCleaner() {
        assertNull(cleaner.whitelist);
    }

    @Test
    public void testCleanDocument() {
        Document dirtyDocument = Parser.parseBodyFragment("<html><body><script>alert('XSS')</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertFalse(dirtyDocument.body().text().contains("alert('XSS')"));
        assertTrue(cleanDocument.body().text().isEmpty());
    }

    @Test
    public void testIsValidDocument() {
        Document dirtyDocument = Parser.parseBodyFragment("<html><body><script>alert('XSS')</script></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidValidDocument() {
        Document dirtyDocument = Parser.parseBodyFragment("<html><body><p>Hello World</p></body></html>");
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCleaningVisitor() {
        Document dirtyDocument = Parser.parseBodyFragment("<html><body><script>alert('XSS')</script><p>Hello <b id='test' title='World'>World</b></p></body></html>");
        Element root = dirtyDocument.body();
        Element destination = new Element(Tag.BODY);

        CleaningVisitor cleaningVisitor = new CleaningVisitor(root, destination);
        NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
        traversor.traverse(root);

        Element cleanElement = cleaningVisitor.destination;
        assertEquals(1, cleanElement.childNodeSize());
        Element child = cleanElement.child(0);
        assertEquals("World", child.text());
        assertFalse(child.hasAttr("id"));
        assertFalse(child.hasAttr("title"));
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceElement = new Element("div", null);
        sourceElement.classes().add("test");
        sourceElement.attr("data-test", "test");

        Attributes attributes = cleaner.createSafeElement(sourceElement).destAttrs;
        assertTrue(attributes.get("class").containsAll(sourceElement.classes()));
        assertTrue(attributes.value("data-test").equals("test"));
    }

    @Test
    public void testCopySafeNodes() {
        Document dirtyDocument = Parser.parseBodyFragment("<html><body><script>alert('XSS')</script><p>Hello <b id='test' title='World'>World</b></p></body></html>");
        Element root = dirtyDocument.body();
        Element destination = new Element(Tag.BODY);

        int numDiscarded = cleaner.copySafeNodes(root, destination);
        assertEquals(2, numDiscarded);
    }
}