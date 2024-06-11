package org.jsoup.safety;

import static org.junit.Assert.*;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testCreateCleaner() {
        Whitelist whitelist = new Whitelist(); // Assuming you have a valid Whitelist instance
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test
    public void testCleanDocument() {
        Document dirtyDocument = Document.fromString("<html><body><script>alert('Hello World')</script><p>Hello</p></body></html>");
        Cleaner cleaner = new Cleaner(new Whitelist());
        Document clean = cleaner.clean(dirtyDocument);
        assertEquals("<html><body><p>Hello</p></body></html>", clean.toString());
    }

    @Test
    public void testIsValidDocument() {
        Document dirtyDocument = Document.fromString("<html><body><script>alert('Hello World')</script><p>Hello</p></body></html>");
        Cleaner cleaner = new Cleaner(new Whitelist());
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testInvalidDocument() {
        Document dirtyDocument = Document.fromString("<html><body><script>alert('Hello World')</script><p>Hello</p><img src='malicious.js' onerror='alert(\"XSS\")'></body></html>");
        Cleaner cleaner = new Cleaner(new Whitelist());
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes_ValidElement() {
        Element sourceElement = new Element(Tag.valueOf("div"), null, new Attributes().put("class", "safe"));
        Element destinationElement = new Element(Tag.valueOf("p"), null, new Attributes());
        int numDiscarded = CleanerTest.copySafeNodes(sourceElement, destinationElement);
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodes_InvalidElement() {
        Element sourceElement = new Element(Tag.valueOf("script"), null, new Attributes().put("src", "malicious.js"));
        Element destinationElement = new Element(Tag.valueOf("p"), null, new Attributes());
        int numDiscarded = CleanerTest.copySafeNodes(sourceElement, destinationElement);
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCopySafeNodes_ValidTextNode() {
        TextNode sourceTextNode = new TextNode("Hello World");
        Element destinationElement = new Element(Tag.valueOf("p"), null, new Attributes());
        int numDiscarded = CleanerTest.copySafeNodes(null, destinationElement);
        Element copiedTextNode = (Element) destinationElement.childNodes().get(0);
        assertEquals("Hello World", copiedTextNode.text());
        assertEquals(0, numDiscarded);
    }

    private static int copySafeNodes(Node source, Element destination) {
        // Implementation left out for brevity
    }

    static Element createSafeElement(Element sourceElement) {
        // Implementation left out for brevity
    }

    static class ElementMeta {
        Element el;
        int numAttribsDiscarded;

        ElementMeta(Element el, int numAttribsDiscarded) {
            this.el = el;
            this.numAttribsDiscarded = numAttribsDiscarded;
        }
    }
}