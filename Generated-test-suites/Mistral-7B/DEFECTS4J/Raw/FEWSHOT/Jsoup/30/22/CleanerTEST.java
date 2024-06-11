package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {
    @Test
    public void testCreateCleaner() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateCleanerNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testCleanDocumentWithAllowedTags() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><h1></h1></body></html>", cleanDocument.html());
    }

    @Test
    public void testCleanDocumentWithDisallowedTags() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><script>alert('XSS');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNull(cleanDocument);
    }

    @Test
    public void testCleanDocumentWithAllowedAttributes() {
        Whitelist whitelist = new Whitelist()
            .addAttributeAllowed("*,*,class");

        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><a class='test' href='#'>Test</a></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><a class='test'></a></body></html>", cleanDocument.html());
    }

    @Test
    public void testCleanDocumentWithDisallowedAttributes() {
        Whitelist whitelist = new Whitelist();

        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><a href='javascript:alert(\"XSS\");'>Test</a></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNull(cleanDocument);
    }

    @Test
    public void testIsValidDocumentWithAllowedTags() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testIsValidDocumentWithDisallowedTags() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><script>alert('XSS');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

    @Test
    public void testIsValidDocumentWithAllowedAttributes() {
        Whitelist whitelist = new Whitelist()
            .addAttributeAllowed("*,*,class");

        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><a class='test' href='#'>Test</a></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testIsValidDocumentWithDisallowedAttributes() {
        Whitelist whitelist = new Whitelist();

        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><a href='javascript:alert(\"XSS\");'>Test</a></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodesWithAllowedTags() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodesWithDisallowedTags() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><script>alert('XSS');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElementWithAllowedTagsAndAttributes() {
        Whitelist whitelist = new Whitelist()
            .addTagAllowed("h1")
            .addAttributeAllowed("h1", "*", "class")
            .addAttributeAllowed("h1", "*", "style");

        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceElement = new Element("h1", "test");
        sourceElement.classes("test-class");
        sourceElement.style("color: red;");

        Element destElement = cleaner.createSafeElement(sourceElement);

        assertEquals(sourceElement.tagName(), destElement.tagName());
        assertEquals(sourceElement.classes(), destElement.classes());
        assertEquals(sourceElement.style(), destElement.style());
    }

    @Test
    public void testCreateSafeElementWithDisallowedTagsAndAttributes() {
        Whitelist whitelist = new Whitelist();

        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceElement = new Element("script", null);
        sourceElement.classes("test-class");
        sourceElement.style("color: red;");

        Element destElement = cleaner.createSafeElement(sourceElement);

        assertNull(destElement);
    }

    @Test
    public void testCreateSafeElementWithAllowedAttributesOnDisallowedTag() {
        Whitelist whitelist = new Whitelist()
            .addTagAllowed("script")
            .addAttributeAllowed("script", "script", "type");

        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceElement = new Element("script", null);
        sourceElement.attr("type", "text/javascript");

        Element destElement = cleaner.createSafeElement(sourceElement);

        assertEquals("text/javascript", destElement.attr("type"));
    }

    @Test
    public void testCreateSafeElementWithDisallowedAttributesOnAllowedTag() {
        Whitelist whitelist = new Whitelist()
            .addTagAllowed("h1")
            .addAttributeAllowed("h1", "*", "class");

        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceElement = new Element("h1", null);
        sourceElement.attr("style", "color: red;");

        Element destElement = cleaner.createSafeElement(sourceElement);

        assertNull(destElement.attr("style"));
    }

    @Test
    public void testCreateSafeElementWithEnforcedAttributes() {
        Whitelist whitelist = new Whitelist()
            .addTagAllowed("h1")
            .addAttributeAllowed("h1", "*", "class")
            .addAttributeAllowed("h1", "*,*,id");

        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceElement = new Element("h1", null);
        sourceElement.classes("test-class");
        sourceElement.id("test-id");

        Element destElement = cleaner.createSafeElement(sourceElement);

        assertEquals("test-id", destElement.id());
    }

    @Test
    public void testCopySafeNodesWithNestedElements() {
        Whitelist whitelist = new Whitelist()
            .addTagAllowed("div")
            .addTagAllowed("p");

        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><div><p>Test</p><p>Test</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        Element div = cleanDocument.select("div").first();
        List<Element> pElements = div.select("p");

        assertEquals(2, pElements.size());
        for (Element pElement : pElements) {
            assertEquals(0, pElement.childNodes().size());
        }
    }

    @Test
    public void testCopySafeNodesWithTextNodes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p>Test Test Test</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        Element pElement = cleanDocument.select("p").first();
        List<Node> textNodes = pElement.childNodes();

        assertEquals(1, textNodes.size());
        TextNode textNode = (TextNode) textNodes.get(0);
        assertEquals("Test Test Test", textNode.getWholeText());
    }

    @Test
    public void testCopySafeNodesWithTextNodesAndElements() {
        Whitelist whitelist = new Whitelist()
            .addTagAllowed("p")
            .addTagAllowed("strong");

        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p>Test <strong>Test</strong> Test</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        Element pElement = cleanDocument.select("p").first();
        List<Node> nodes = pElement.childNodes();

        assertEquals(2, nodes.size());
        TextNode textNode = (TextNode) nodes.get(0);
        assertEquals("Test ", textNode.getWholeText());
        Element strongElement = (Element) nodes.get(1);
        assertEquals("Test", strongElement.text());
    }

    private static void assertNotNull(Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
    }

    private static void assertNull(Object object) {
        if (object != null) {
            throw new IllegalArgumentException("Expected null but was: " + object);
        }
    }

    private static void assertEquals(String expected, String actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected: " + expected + "\nbut was: " + actual);
        }
    }

    private static void assertEquals(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected: " + expected + "\nbut was: " + actual);
        }
    }

    private static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected: true\nbut was: false");
        }
    }

    private static void assertFalse(boolean condition) {
        if (condition) {
            throw new AssertionError("Expected: false\nbut was: true");
        }
    }
}