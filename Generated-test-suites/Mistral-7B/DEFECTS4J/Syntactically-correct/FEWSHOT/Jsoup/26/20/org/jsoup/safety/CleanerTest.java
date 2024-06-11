package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class CleanerTest {
    @Test
    public void testCleanerConstruction() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);
        assertEquals("Cleaner created with valid whitelist", cleaner, new Cleaner(whitelist));
    }

    @Test(expected = NullPointerException.class)
    public void testCleanerConstructionNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testIsValidNullDocument() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
        assertFalse("Invalid document should not be valid", cleaner.isValid(null));
    }

    @Test
    public void testIsValidValidDocument() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
        assertTrue("Valid document should be valid", cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidInvalidDocument() {
        String html = "<html><body><script>alert('XSS')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
        assertFalse("Invalid document should not be valid", cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCleanNullDocument() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
        assertNull("Clean method should return null for null document", cleaner.clean(null));
    }

    @Test
    public void testCleanValidDocument() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("Clean document should have same base uri as dirty document", dirtyDocument.baseUri(), cleanDocument.baseUri());
        Element body = cleanDocument.body();
        assertEquals("Clean document body should only contain text", 1, body.childNodes().size());
        TextNode textNode = (TextNode) body.childNodes().get(0);
        assertEquals("Clean document text should be same as dirty document text", "Hello World", textNode.getWholeText());
    }

    @Test
    public void testCleanInvalidDocument() {
        String html = "<html><body><script>alert('XSS')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("Clean document should have same base uri as dirty document", dirtyDocument.baseUri(), cleanDocument.baseUri());
        Element body = cleanDocument.body();
        assertEquals("Clean document body should not contain script tag", 0, body.childNodes().size());
    }

    @Test
    public void testCopySafeNodesNoChildren() {
        Element sourceElement = new Element("div");
        Element destElement = new Element("div");
        int numDiscarded = Cleaner.copySafeNodes(sourceElement, destElement);
        assertEquals("No children, no discards", 0, numDiscarded);
    }

    @Test
    public void testCopySafeNodesTextChild() {
        Element sourceElement = new Element("div");
        sourceElement.appendChild(new TextNode("Hello World"));
        Element destElement = new Element("div");
        int numDiscarded = Cleaner.copySafeNodes(sourceElement, destElement);
        assertEquals("Text child, no discards", 0, numDiscarded);
        TextNode textNode = (TextNode) destElement.childNodes().get(0);
        assertEquals("Text child should be same as source text", "Hello World", textNode.getWholeText());
    }

    @Test
    public void testCopySafeNodesSafeElementChild() {
        Element sourceElement = new Element("p");
        Element destElement = new Element("div");
        int numDiscarded = Cleaner.copySafeNodes(sourceElement, destElement);
        assertEquals("Safe element child, no discards", 0, numDiscarded);
    }

    @Test
    public void testCopySafeNodesUnsafeElementChild() {
        Element sourceElement = new Element("script");
        Element destElement = new Element("div");
        int numDiscarded = Cleaner.copySafeNodes(sourceElement, destElement);
        assertEquals("Unsafe element child, one discard", 1, numDiscarded);
        assertTrue("Dest element should not have any children", destElement.childNodes().isEmpty());
    }

    @Test
    public void testCopySafeNodesSafeElementWithAttributes() {
        String expectedHtml = "<p id='testId' class='testClass'>Hello World</p>";
        Element sourceElement = new Element("p", null, new Attributes().put("id", "testId").put("class", "testClass"));
        Element destElement = new Element("div");
        int numDiscarded = Cleaner.copySafeNodes(sourceElement, destElement);
        assertEquals("Safe element with attributes, no discards", 0, numDiscarded);
        Element copiedElement = (Element) destElement.childNodes().get(0);
        assertEquals("Copied element should have same tag name as source", "p", copiedElement.tagName());
        assertTrue("Copied element should have same attributes as source", sourceElement.attributes().equals(copiedElement.attributes()));
        TextNode textNode = (TextNode) copiedElement.childNodes().get(0);
        assertEquals("Copied text should be same as source text", "Hello World", textNode.getWholeText());
    }

    @Test
    public void testCopySafeNodesUnsafeElementWithAttributes() {
        Element sourceElement = new Element("script", null, new Attributes().put("id", "testId"));
        Element destElement = new Element("div");
        int numDiscarded = Cleaner.copySafeNodes(sourceElement, destElement);
        assertEquals("Unsafe element with attributes, one discard", 1, numDiscarded);
        assertTrue("Dest element should not have any children", destElement.childNodes().isEmpty());
    }

    @Test
    public void testCreateSafeElementWithValidAttributes() {
        String tagName = "p";
        Attributes sourceAttributes = new Attributes().put("id", "testId").put("class", "testClass");
        ElementMeta meta = Cleaner.createSafeElement(new Element(Tag.valueOf(tagName), null, sourceAttributes));
        assertEquals("Safe element with valid attributes, correct tag name", tagName, meta.el.tagName());
        Attributes destAttributes = meta.el.attributes();
        assertEquals("Safe element with valid attributes, correct number of attributes", 2, destAttributes.size());
        assertTrue("Safe element with valid attributes, correct attribute values", sourceAttributes.equals(destAttributes));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateSafeElementInvalidTagName() {
        Attributes attributes = new Attributes();
        Cleaner.createSafeElement(new Element("invalidTag", null, attributes));
    }

    @Test
    public void testCreateSafeElementWithInvalidAttributes() {
        String tagName = "p";
        Attributes sourceAttributes = new Attributes().put("onclick", "alert('XSS')");
        ElementMeta meta = Cleaner.createSafeElement(new Element(Tag.valueOf(tagName), null, sourceAttributes));
        assertEquals("Safe element with invalid attributes, correct tag name", tagName, meta.el.tagName());
        Attributes destAttributes = meta.el.attributes();
        assertEquals("Safe element with invalid attributes, correct number of attributes", 0, destAttributes.size());
    }
}