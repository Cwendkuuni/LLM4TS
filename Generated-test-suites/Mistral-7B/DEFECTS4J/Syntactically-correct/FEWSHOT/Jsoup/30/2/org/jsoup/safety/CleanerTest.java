package org.jsoup.safety;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testCleanerConstruction() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test(expected = NullPointerException.class)
    public void testCleanerConstructionWithNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p>Hello <b>world</b></p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><p></p></body></html>", cleanDocument.html());
    }

    @Test
    public void testCleanFramesetDocument() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><frameset><frame src=\"frame1.html\"></frameset></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p>Hello <b>world</b></p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        assertTrue(cleaner.isValid(dirtyDocument));

        String invalidHtml = "<html><body><script>alert('Hello World');</script></body></html>";
        Document invalidDocument = Jsoup.parse(invalidHtml);

        assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test
    public void testIsValidWithEmptyInput() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);

        Document emptyDocument = Document.createShell("http://example.com/");

        assertTrue(cleaner.isValid(emptyDocument));
    }

    @Test
    public void testCopySafeNodesWithSafeTags() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p><b>Hello <i>world</i></b></p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = Document.createShell("http://example.com/");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals("<html><body><p><b></b><i></i></p></body></html>", cleanDocument.html());
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodesWithUnsafeTags() {
        Whitelist whitelist = Whitelist.safeList();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><script>alert('Hello World');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = Document.createShell("http://example.com/");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals("<html><body></body></html>", cleanDocument.html());
        assertEquals(1, numDiscarded);
    }
}