package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;
    
    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.none());
    }
    
    @Test
    public void testClean() {
        Document dirtyDoc = Jsoup.parse("<html><head></head><body><p>Hello, World!</p></body></html>");
        Document cleanDoc = cleaner.clean(dirtyDoc);
        
        Element body = cleanDoc.body();
        assertNotNull(body);
        
        Element paragraph = body.getElementsByTag("p").first();
        assertNotNull(paragraph);
        
        String expectedText = "Hello, World!";
        String actualText = paragraph.text();
        assertEquals(expectedText, actualText);
    }
    
    @Test
    public void testClean_withFramesetDocument() {
        Document dirtyDoc = Jsoup.parse("<html><frameset></frameset></html>");
        Document cleanDoc = cleaner.clean(dirtyDoc);
        
        Element body = cleanDoc.body();
        assertNotNull(body);
        
        int expectedNumChildren = 0;
        int actualNumChildren = body.children().size();
        assertEquals(expectedNumChildren, actualNumChildren);
    }
    
    @Test
    public void testIsValid() {
        Document validDoc = Jsoup.parse("<div><p>Hello, World!</p></div>");
        assertTrue(cleaner.isValid(validDoc));
        
        Document invalidDoc = Jsoup.parse("<div onmouseover=\"alert('XSS')\"><p>Hello, World!</p></div>");
        assertFalse(cleaner.isValid(invalidDoc));
    }
    
    @Test
    public void testCopySafeNodes() {
        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");
        
        Element span = new Element(Tag.valueOf("span"), "");
        span.text("Safe Text");
        
        root.appendChild(span);
        
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        assertEquals(0, numDiscarded);
        
        Element resultSpan = destination.getElementsByTag("span").first();
        assertNotNull(resultSpan);
        
        String expectedText = "Safe Text";
        String actualText = resultSpan.text();
        assertEquals(expectedText, actualText);
    }
    
    @Test
    public void testCopySafeNodes_withUnsafeTag() {
        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");
        
        Element script = new Element(Tag.valueOf("script"), "");
        script.text("alert('XSS')");
        
        root.appendChild(script);
        
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        assertEquals(1, numDiscarded);
        
        int expectedNumChildren = 0;
        int actualNumChildren = destination.children().size();
        assertEquals(expectedNumChildren, actualNumChildren);
    }
    
    @Test
    public void testCopySafeNodes_withTextNodes() {
        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");
        
        Element textNode = new Element(Tag.valueOf("#text"), "");
        textNode.text("Hello, World!");
        
        root.appendChild(textNode);
        
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        assertEquals(0, numDiscarded);
        
        String expectedText = "Hello, World!";
        String actualText = destination.text();
        assertEquals(expectedText, actualText);
    }
    
    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        sourceEl.attr("id", "safeDiv");
        sourceEl.attr("class", "safeClass");
        sourceEl.attr("safeAttr", "true");
        sourceEl.attr("unsafeAttr", "false");
        
        Cleaner.Whitelist whitelist = Whitelist.relaxed();
        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl, whitelist);
        
        Element dest = elementMeta.el;
        assertEquals("div", dest.tagName());
        
        String expectedId = "safeDiv";
        String actualId = dest.attr("id");
        assertEquals(expectedId, actualId);
        
        String expectedClass = "safeClass";
        String actualClass = dest.attr("class");
        assertEquals(expectedClass, actualClass);
        
        String expectedSafeAttr = "true";
        String actualSafeAttr = dest.attr("safeAttr");
        assertEquals(expectedSafeAttr, actualSafeAttr);
    }
    
    @Test
    public void testCreateSafeElement_withUnsafeAttr() {
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        sourceEl.attr("id", "safeDiv");
        sourceEl.attr("class", "safeClass");
        sourceEl.attr("safeAttr", "true");
        sourceEl.attr("unsafeAttr", "false");
        
        Cleaner.Whitelist whitelist = Whitelist.relaxed();
        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl, whitelist);
        
        Element dest = elementMeta.el;
        assertEquals("div", dest.tagName());
        
        String expectedId = "safeDiv";
        String actualId = dest.attr("id");
        assertEquals(expectedId, actualId);
        
        String expectedClass = "safeClass";
        String actualClass = dest.attr("class");
        assertEquals(expectedClass, actualClass);
        
        String expectedSafeAttr = "true";
        String actualSafeAttr = dest.attr("safeAttr");
        assertEquals(expectedSafeAttr, actualSafeAttr);
        
        String expectedUnsafeAttr = ""; // Unsafe attribute should be discarded
        String actualUnsafeAttr = dest.attr("unsafeAttr");
        assertEquals(expectedUnsafeAttr, actualUnsafeAttr);
    }
    
}