package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

    @Test
    public void testCreateCleaner() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        assertEquals("Whitelist: simpleText", cleaner.whitelist.toString());
    }

    @Test
    public void testCreateCleanDocument() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><h1></h1></body></html>", cleanDocument.body().html());
    }

    @Test
    public void testInvalidDocument() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><script>alert('XSS')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse("Invalid document should not be valid", isValid);
    }

    @Test
    public void testValidDocument() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p>Hello World</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue("Valid document should be valid", isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><h1 id='title' class='title'><span>Hello World</span></h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element body = dirtyDocument.body();
        Element newBody = new Element(Tag.BODY, body.baseUri());

        int numDiscarded = cleaner.copySafeNodes(body, newBody);
        assertEquals(0, numDiscarded);
        assertEquals("<html><body><h1 id='title' class='title'><span></span></h1></body></html>", newBody.html());
    }

    @Test
    public void testCopySafeNodesWithDiscardedElements() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><script src='malicious.js'></script><h1>Hello World</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element body = dirtyDocument.body();
        Element newBody = new Element(Tag.BODY, body.baseUri());

        int numDiscarded = cleaner.copySafeNodes(body, newBody);
        assertEquals(1, numDiscarded);
        assertNull(newBody.child(0));
    }

    @Test
    public void testCopySafeNodesWithDiscardedAttributes() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p id='title' class='title' style='color:red;'>Hello World</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element body = dirtyDocument.body();
        Element newBody = new Element(Tag.BODY, body.baseUri());

        int numDiscarded = cleaner.copySafeNodes(body, newBody);
        assertEquals(2, numDiscarded);
        assertTrue(newBody.hasAttr("class"));
        assertFalse(newBody.hasAttr("id"));
        assertFalse(newBody.hasAttr("style"));
    }

}