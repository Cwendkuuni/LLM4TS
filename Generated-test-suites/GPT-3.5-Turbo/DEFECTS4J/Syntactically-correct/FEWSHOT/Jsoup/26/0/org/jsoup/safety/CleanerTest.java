package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.List;

@SuppressWarnings("serial")
public class CleanerTest {
    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        // create input document
        Document dirtyDocument = createDirtyDocument();
        
        // test clean method
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("utf-8", cleanDocument.outputSettings().charset().displayName());
        
        // validate clean document
        boolean isValid = cleaner.isValid(cleanDocument);
        assertTrue(isValid);
    }
    
    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        
        // create input document
        Document dirtyDocument = createDirtyDocument();
        
        // test isValid method
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }
    
    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        
        // create input and destination elements
        Element source = createSourceElement();
        Element dest = createDestinationElement();
        
        // test copySafeNodes method
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(1, numDiscarded);
        
        // validate destination element
        List<Node> destChildren = dest.childNodes();
        assertEquals(2, destChildren.size());
        assertTrue(destChildren.get(0) instanceof Element);
        assertTrue(destChildren.get(1) instanceof TextNode);
    }
    
    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        
        // create source element
        Element sourceEl = createSourceElement();
        
        // test createSafeElement method
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        Element dest = elementMeta.el;
        assertEquals("div", dest.tagName());
        assertEquals("http://example.com", dest.baseUri());
        
        // validate destination element attributes
        Attributes destAttrs = dest.attributes();
        assertEquals(2, destAttrs.size());
    }
    
    // helper methods
    private Document createDirtyDocument() {
        Document dirtyDocument = Document.createShell("http://example.com");
        Element body = dirtyDocument.body();
        Element div = new Element(Tag.valueOf("div"), dirtyDocument.baseUri());
        div.addClass("container");
        Element p = new Element(Tag.valueOf("p"), dirtyDocument.baseUri());
        p.text("Hello, world!");
        div.appendChild(p);
        body.appendChild(div);
        return dirtyDocument;
    }
    
    private Element createSourceElement() {
        Element sourceEl = new Element(Tag.valueOf("div"), "http://example.com");
        sourceEl.addClass("container");
        sourceEl.attr("data-id", "12345");
        sourceEl.attr("style", "color: red;");
        TextNode textNode = new TextNode("Hello, world!", "http://example.com");
        sourceEl.appendChild(textNode);
        return sourceEl;
    }
    
    private Element createDestinationElement() {
        Element dest = new Element(Tag.valueOf("div"), "http://example.com");
        return dest;
    }
}