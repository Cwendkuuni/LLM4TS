package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

public class CleanerTest {
    @Test
    public void testConstructor() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullWhitelist() {
        new Cleaner(null);
    }
    
    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parseBodyFragment("<html><body><div><span><b>Hello world!</b></span></div><script>alert('XSS');</script></body></html>");
        
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertNotNull(cleanDocument);
        assertEquals("<html><head></head><body><div><span><b>Hello world!</b></span></div></body></html>", cleanDocument.outerHtml());
    }
    
    @Test
    public void testCleanWithEmptyBody() {
        Document dirtyDocument = Jsoup.parseBodyFragment("<html><body></body></html>");
        
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertNotNull(cleanDocument);
        assertEquals("<html><head></head><body></body></html>", cleanDocument.outerHtml());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Jsoup.parseBodyFragment("<html><head><title>Test</title></head><body><div><span><b>Hello world!</b></span></div></body></html>");
        
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertTrue(isValid);
    }
    
    @Test
    public void testIsValidWithTagNotAllowed() {
        Document dirtyDocument = Jsoup.parseBodyFragment("<html><head></head><body><script>alert('XSS');</script></body></html>");
        
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertFalse(isValid);
    }
    
    @Test
    public void testCleaningVisitorHeadWithSafeTag() {
        Element root = new Element(Tag.valueOf("html"), "");
        Element destination = new Element(Tag.valueOf("body"), "");
        Cleaner.CleaningVisitor cleaningVisitor = new Cleaner(root, destination);
        
        Element source = new Element(Tag.valueOf("div"), "");
        
        cleaningVisitor.head(source, 0);
        
        assertEquals(1, destination.childNodeSize());
        assertEquals(source, destination.child(0));
    }
    
    @Test
    public void testCleaningVisitorHeadWithNotSafeTag() {
        Element root = new Element(Tag.valueOf("html"), "");
        Element destination = new Element(Tag.valueOf("body"), "");
        Cleaner.CleaningVisitor cleaningVisitor = new Cleaner(root, destination);
        
        Element source = new Element(Tag.valueOf("script"), "");
        
        cleaningVisitor.head(source, 0);
        
        assertEquals(0, destination.childNodeSize());
    }
    
    @Test
    public void testCleaningVisitorHeadWithText() {
        Element root = new Element(Tag.valueOf("html"), "");
        Element destination = new Element(Tag.valueOf("body"), "");
        Cleaner.CleaningVisitor cleaningVisitor = new Cleaner(root, destination);
        
        TextNode source = new TextNode("Hello world!", "");
        
        cleaningVisitor.head(source, 0);
        
        assertEquals(1, destination.childNodeSize());
        assertEquals(source, destination.child(0));
    }
    
    @Test
    public void testCleaningVisitorHeadWithDataNode() {
        Element root = new Element(Tag.valueOf("html"), "");
        Element destination = new Element(Tag.valueOf("body"), "");
        Cleaner.CleaningVisitor cleaningVisitor = new Cleaner(root, destination);
        
        Element parent = new Element(Tag.valueOf("div"), "");
        parent.appendChild(new TextNode("Hello world!", ""));
        Element source = new Element(Tag.valueOf("script"), "");
        parent.appendChild(source);
        
        cleaningVisitor.head(source, 1);
        
        assertEquals(1, destination.childNodeSize());
        assertEquals(parent, destination.child(0));
    }
    
    @Test
    public void testCleaningVisitorTailWithSafeTag() {
        Element root = new Element(Tag.valueOf("html"), "");
        Element destination = new Element(Tag.valueOf("body"), "");
        Cleaner.CleaningVisitor cleaningVisitor = new Cleaner(root, destination);
        
        Element source = new Element(Tag.valueOf("div"), "");
        destination.appendChild(source);
        
        cleaningVisitor.tail(source, 0);
        
        assertEquals(destination, cleaningVisitor.destination);
    }
    
    @Test
    public void testCleaningVisitorTailWithNotSafeTag() {
        Element root = new Element(Tag.valueOf("html"), "");
        Element destination = new Element(Tag.valueOf("body"), "");
        Cleaner.CleaningVisitor cleaningVisitor = new Cleaner(root, destination);
        
        Element source = new Element(Tag.valueOf("script"), "");
        destination.appendChild(source);
        
        cleaningVisitor.tail(source, 0);
        
        assertEquals(destination.parent(), cleaningVisitor.destination);
    }
    
    @Test
    public void testCopySafeNodes() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        
        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = new Element(Tag.valueOf("body"), "");
        
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        
        assertEquals(-1, numDiscarded);
        assertEquals(1, dest.childNodeSize());
        assertEquals(source, dest.child(0));
    }
    
    @Test
    public void testCreateSafeElement() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        
        Element sourceEl = new Element(Tag.valueOf("a"), "");
        Attributes sourceAttrs = new Attributes();
        sourceAttrs.put("href", "http://example.com");
        sourceEl.attributes().addAll(sourceAttrs);
        
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        
        assertNotNull(elementMeta.el);
        assertEquals(sourceEl.tagName(), elementMeta.el.tagName());
        assertEquals(1, elementMeta.numAttribsDiscarded);
        Attributes destAttrs = elementMeta.el.attributes();
        assertEquals(1, destAttrs.size());
        assertEquals("href", destAttrs.asList().get(0).getKey());
        assertEquals("http://example.com", destAttrs.value("href"));
    }
}