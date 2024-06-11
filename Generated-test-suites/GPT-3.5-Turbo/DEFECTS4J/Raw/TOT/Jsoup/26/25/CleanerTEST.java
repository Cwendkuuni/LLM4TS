package org.jsoup.safety;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;

public class CleanerTest {
    
    private Cleaner cleaner;
    
    @Before
    public void setUp() {
        Whitelist whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
    }
    
    @Test
    public void testClean() {
        // Typical use case: Clean the dirty document and validate the cleaned document
        Document dirty = Document.createShell("http://example.com");
        String dirtyHtml = "<body><h1>Hello World!</h1><script>console.log('XSS');</script></body>";
        dirty.body().html(dirtyHtml);
        
        Document cleaned = cleaner.clean(dirty);
        assertEquals("<body><h1>Hello World!</h1></body>", cleaned.body().html());
        
        // Edge case: Clean an empty document
        Document emptyDirty = Document.createShell("http://example.com");
        cleaned = cleaner.clean(emptyDirty);
        assertEquals("<body></body>", cleaned.body().html());
        
        // Error scenario: Clean a null document
        try {
            Document nullDirty = null;
            cleaned = cleaner.clean(nullDirty);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Test passed
        }
    }
    
    @Test
    public void testIsValid() {
        // Typical use case: Validate a valid document
        Document valid = Document.createShell("http://example.com");
        String validHtml = "<body><h1>Hello World!</h1></body>";
        valid.body().html(validHtml);
        
        boolean isValid = cleaner.isValid(valid);
        assertTrue(isValid);
        
        // Edge case: Validate a document with unsafe tags
        Document unsafe = Document.createShell("http://example.com");
        String unsafeHtml = "<body><h1>Hello World!</h1><script>console.log('XSS');</script></body>";
        unsafe.body().html(unsafeHtml);
        
        isValid = cleaner.isValid(unsafe);
        assertFalse(isValid);
        
        // Error scenario: Validate a null document
        try {
            Document nullDirty = null;
            isValid = cleaner.isValid(nullDirty);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Test passed
        }
    }
    
    @Test
    public void testCopySafeNodes() {
        // Typical use case: Copy safe nodes from source to destination
        Element source = new Element(Tag.valueOf("div"), "http://example.com");
        source.html("<span>Safe</span><script>console.log('XSS');</script>");
        
        Element dest = new Element(Tag.valueOf("div"), "http://example.com");
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        
        assertEquals("<div><span>Safe</span></div>", dest.html());
        assertEquals(1, numDiscarded);
        
        // Edge case: Copy safe nodes from an element without children
        Element emptySource = new Element(Tag.valueOf("div"), "http://example.com");
        
        dest = new Element(Tag.valueOf("div"), "http://example.com");
        numDiscarded = cleaner.copySafeNodes(emptySource, dest);
        
        assertEquals("<div></div>", dest.html());
        assertEquals(0, numDiscarded);
        
        // Error scenario: Copy safe nodes with a null source element
        try {
            Element nullSource = null;
            dest = new Element(Tag.valueOf("div"), "http://example.com");
            numDiscarded = cleaner.copySafeNodes(nullSource, dest);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Test passed
        }
    }
    
    @Test
    public void testCreateSafeElement() {
        // Typical use case: Create a safe element with safe attributes
        Element source = new Element(Tag.valueOf("a"), "http://example.com");
        source.html("<span>Safe</span>");
        
        ElementMeta meta = cleaner.createSafeElement(source);
        Element dest = meta.el;
        int numDiscarded = meta.numAttribsDiscarded;
        
        assertEquals("<a><span>Safe</span></a>", dest.html());
        assertEquals(0, numDiscarded);
        
        // Edge case: Create a safe element with unsafe attributes
        Element unsafeSource = new Element(Tag.valueOf("a"), "http://example.com");
        unsafeSource.attr("href", "javascript:alert('XSS')");
        
        meta = cleaner.createSafeElement(unsafeSource);
        dest = meta.el;
        numDiscarded = meta.numAttribsDiscarded;
        
        assertEquals("<a></a>", dest.html());
        assertEquals(1, numDiscarded);
        
        // Error scenario: Create safe element with a null source element
        try {
            Element nullSource = null;
            meta = cleaner.createSafeElement(nullSource);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Test passed
        }
    }
}