package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

public class CleanerTest {

    @Test
    public void testCleanerSetup() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        assertEquals("relaxed", cleaner.whitelist.toString());
    }

    @Test
    public void testCleanValidDocument() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p>Hello World</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><p>Hello World</p></body></html>", cleanDocument.html());
    }

    @Test
    public void testCleanInvalidDocument() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><script>alert('Hello World');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanDocument.body().text().isEmpty());
    }

    @Test
    public void testIsValidDocument() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p>Hello World</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        boolean valid = cleaner.isValid(dirtyDocument);
        assertTrue(valid);

        html = "<html><body><script>alert('Hello World');</script></body></html>";
        dirtyDocument = Jsoup.parse(html);
        valid = cleaner.isValid(dirtyDocument);
        assertFalse(valid);
    }

    @Test
    public void testIsValidDocumentWithAllowedScriptTag() {
        Whitelist whitelist = new Whitelist.SimpleTagNameElementFilter("script", Tag.SCRIPT);
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><script>alert('Hello World');</script><p>Hello World</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        boolean valid = cleaner.isValid(dirtyDocument);
        assertTrue(valid);
    }

    @Test(expected = NullPointerException.class)
    public void testCleanNullDocument() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = null;
        cleaner.clean(dirtyDocument);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p id='test' class='test'><span>Hello World</span></p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        Element pElement = cleanDocument.body().getElementById("test");
        Element spanElement = pElement.getElementsByTag("span").first();
        assertEquals("span", spanElement.tagName());
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodesWithDiscardedAttributes() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p id='test' class='test' style='color: red;'><span style='color: blue;'>Hello World</span></p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        Element pElement = cleanDocument.body().getElementById("test");
        Element spanElement = pElement.getElementsByTag("span").first();
        assertEquals("span", spanElement.tagName());
        assertTrue(pElement.attributes().isEmpty());
        assertTrue(spanElement.attributes().isEmpty());
        assertEquals(2, numDiscarded);
    }

    @Test
    public void testCopySafeNodesWithDiscardedScriptTag() {
        Whitelist whitelist = new Whitelist.SimpleTagNameElementFilter("script", Tag.SCRIPT);
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><script>alert('Hello World');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertTrue(cleanDocument.body().text().isEmpty());
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceElement = new Element("div", new Attributes()
                .put("id", "test")
                .put("class", "test")
                .put("style", "color: red;"));

        Element destinationElement = cleaner.createSafeElement(sourceElement);
        assertEquals("div", destinationElement.tagName());
        assertTrue(destinationElement.attributes().isEmpty());
    }
}