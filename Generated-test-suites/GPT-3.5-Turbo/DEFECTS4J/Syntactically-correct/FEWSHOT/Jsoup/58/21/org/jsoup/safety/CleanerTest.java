package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        // Test with an empty document
        Document dirtyDocument = Document.createShell("");
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        assertNull(cleanedDocument.body());

        // Test with a document containing only a safe tag
        dirtyDocument = Document.createShell("");
        Element safeElement = new Element("p");
        dirtyDocument.body().appendChild(safeElement);
        cleanedDocument = cleaner.clean(dirtyDocument);
        assertEquals(dirtyDocument.body().parentNode(), cleanedDocument.body().parentNode());
        assertEquals(safeElement.tagName(), cleanedDocument.body().child(0).tagName());

        // Test with a document containing an unsafe tag
        dirtyDocument = Document.createShell("");
        Element unsafeElement = new Element("script");
        dirtyDocument.body().appendChild(unsafeElement);
        cleanedDocument = cleaner.clean(dirtyDocument);
        assertNull(cleanedDocument.body().child(0));

        // Test with a document containing a safe tag and an unsafe tag
        dirtyDocument = Document.createShell("");
        dirtyDocument.body().appendChild(safeElement);
        dirtyDocument.body().appendChild(unsafeElement);
        cleanedDocument = cleaner.clean(dirtyDocument);
        assertEquals(dirtyDocument.body().parentNode(), cleanedDocument.body().parentNode());
        assertEquals(safeElement.tagName(), cleanedDocument.body().child(0).tagName());
        assertNull(cleanedDocument.body().child(1));
    }

    @Test
    public void testIsValid() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        // Test with an empty document
        Document dirtyDocument = Document.createShell("");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);

        // Test with a document containing only whitelisted tags
        dirtyDocument = Document.createShell("");
        Element safeElement1 = new Element("p");
        Element safeElement2 = new Element("div");
        dirtyDocument.body().appendChild(safeElement1);
        dirtyDocument.body().appendChild(safeElement2);
        isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);

        // Test with a document containing an unsafe tag
        dirtyDocument = Document.createShell("");
        Element unsafeElement = new Element("script");
        dirtyDocument.body().appendChild(unsafeElement);
        isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);

        // Test with a document containing an unsafe tag and an attribute not allowed by the whitelist
        cleaner = new Cleaner(Whitelist.relaxed());
        dirtyDocument = Document.createShell("");
        Element unsafeElementWithAttribute = new Element("a");
        unsafeElementWithAttribute.attr("href", "javascript:alert('XSS')");
        dirtyDocument.body().appendChild(unsafeElementWithAttribute);
        isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

    @Test
    public void testCleaningVisitorHead() {
        Element root = new Element("html");
        Element destination = new Element("body");
        String baseUri = "https://www.example.com";

        Cleaner.CleaningVisitor visitor = new Cleaner.CleaningVisitor(root, destination);

        // Test with a safe tag
        Element safeElement = new Element("p");
        visitor.head(safeElement, 0);
        assertNotNull(destination.child(0));
        assertEquals(safeElement.tagName(), destination.child(0).tagName());

        // Test with an unsafe tag
        Element unsafeElement = new Element("script");
        visitor.head(unsafeElement, 0);
        assertNull(destination.child(1));
    }

    @Test
    public void testCleaningVisitorTail() {
        Element root = new Element("html");
        Element destination = new Element("body");
        String baseUri = "https://www.example.com";

        Cleaner.CleaningVisitor visitor = new Cleaner.CleaningVisitor(root, destination);

        Element safeElement = new Element("p");
        visitor.head(safeElement, 0);
        visitor.tail(safeElement, 0);

        assertNull(visitor.destination);
    }

    @Test
    public void testCopySafeNodes() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        // Test with an empty source element
        Element source = new Element("div");
        Element dest = new Element("div");
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(0, numDiscarded);
        assertNull(dest.child(0));

        // Test with a source element containing a safe tag
        source = new Element("div");
        dest = new Element("div");
        Element safeElement = new Element("p");
        source.appendChild(safeElement);
        numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(0, numDiscarded);
        assertNotNull(dest.child(0));

        // Test with a source element containing an unsafe tag
        source = new Element("div");
        dest = new Element("div");
        Element unsafeElement = new Element("script");
        source.appendChild(unsafeElement);
        numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(1, numDiscarded);
        assertNull(dest.child(0));
    }

    @Test
    public void testCreateSafeElement() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        // Test with a source element containing no attributes
        Element sourceEl = new Element("p");
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        assertNotNull(elementMeta.el);
        assertEquals(sourceEl.tagName(), elementMeta.el.tagName());
        assertEquals(0, elementMeta.numAttribsDiscarded);

        // Test with a source element containing a safe attribute
        sourceEl = new Element("a");
        sourceEl.attr("href", "https://www.example.com");
        elementMeta = cleaner.createSafeElement(sourceEl);
        assertNotNull(elementMeta.el);
        assertEquals(sourceEl.tagName(), elementMeta.el.tagName());
        assertEquals(0, elementMeta.numAttribsDiscarded);

        // Test with a source element containing an unsafe attribute
        sourceEl = new Element("a");
        sourceEl.attr("href", "javascript:alert('XSS')");
        elementMeta = cleaner.createSafeElement(sourceEl);
        assertNotNull(elementMeta.el);
        assertEquals(sourceEl.tagName(), elementMeta.el.tagName());
        assertEquals(1, elementMeta.numAttribsDiscarded);
        assertEquals(0, elementMeta.el.attributes().size());
    }
    
}