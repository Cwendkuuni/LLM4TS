package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {

    @Test
    public void testCleanerWithEmptyWhitelist() {
        Whitelist emptyWhitelist = Whitelist.EMPTY;
        Cleaner cleaner = new Cleaner(emptyWhitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanDocument.body().text().isEmpty());
    }

    @Test
    public void testCleanerWithBasicWhitelist() {
        Whitelist whitelist = new Whitelist.SimpleTagNameWhitelist("p", "div");
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p>Content</p><script>alert('XSS')</script><div>More content</div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element body = cleanDocument.body();
        List<Element> elements = Arrays.asList(body.getElementsByTag("p"), body.getElementsByTag("div"));

        for (Element element : elements) {
            assertTrue(element.text().contains("Content"));
            assertTrue(element.text().contains("More content"));
        }

        assertNull(cleanDocument.getElementsByTag("script").first());
    }

    @Test
    public void testIsValidWithEmptyWhitelist() {
        Whitelist emptyWhitelist = Whitelist.EMPTY;
        Cleaner cleaner = new Cleaner(emptyWhitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p>Content</p></body></html>");

        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidWithBasicWhitelist() {
        Whitelist whitelist = new Whitelist.SimpleTagNameWhitelist("p", "div");
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p>Content</p><script>alert('XSS')</script></body></html>");

        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCleanerWithInvalidInput() {
        Whitelist whitelist = new Whitelist.SimpleTagNameWhitelist("p", "div");
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html>"); // incomplete document

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanDocument.body().text().isEmpty());
    }

    @Test
    public void testCopySafeNodesWithEmptyWhitelist() {
        Whitelist emptyWhitelist = Whitelist.EMPTY;
        Cleaner cleaner = new Cleaner(emptyWhitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p>Content</p></body></html>");
        Element sourceEl = dirtyDocument.body().getElementsByTag("p").first();

        Element dest = new Element(Tag.P, sourceEl.baseUri());
        int numDiscarded = cleaner.copySafeNodes(sourceEl, dest);

        assertTrue(numDiscarded == 0);
        assertSame(sourceEl, dest.children().get(0));
    }

    @Test
    public void testCopySafeNodesWithBasicWhitelist() {
        Whitelist whitelist = new Whitelist.SimpleTagNameWhitelist("p", "div");
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p>Content<script>alert('XSS')</script></p></body></html>");
        Element sourceEl = dirtyDocument.body().getElementsByTag("p").first();

        Element dest = new Element(Tag.P, sourceEl.baseUri());
        int numDiscarded = cleaner.copySafeNodes(sourceEl, dest);

        assertTrue(numDiscarded > 0);
        assertNull(dest.child(0).child(0));
    }
}