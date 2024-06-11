package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class CleanerTest {

    @Test
    public void testCleanerWithEmptyWhitelist() {
        Whitelist emptyWhitelist = Whitelist.empty();
        Cleaner cleaner = new Cleaner(emptyWhitelist);

        String dirtyHtml = "<html><body><script>alert('XSS');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue("Clean document should be empty", cleanDocument.body().childElementCount() == 0);
    }

    @Test
    public void testCleanerWithBasicWhitelist() {
        String whitelistXml = "<whitelist xmlns:html='http://www.w3.org/1999/xhtml' xmlns:xs='http://www.w3.org/2001/XMLSchema'> " +
                "<html xmlns='http://www.w3.org/1999/xhtml'><head/><body>" +
                "<tag name='p'/>" +
                "<tag name='a' href='*'/>" +
                "</body></html>" +
                "</whitelist>";
        Whitelist whitelist = Whitelist.parse(whitelistXml);
        Cleaner cleaner = new Cleaner(whitelist);

        String dirtyHtml = "<html><body><p id='test'>Hello World</p><script>alert('XSS');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("Clean document should contain <p> tag", 1, cleanDocument.body().childNodeCount());
        Element pElement = cleanDocument.body().child(0);
        assertEquals("Clean <p> tag should have correct id", "test", pElement.id());
        assertEquals("Clean document should not contain <script> tag", 0, cleanDocument.body().getElementsByTag("script").size());
    }

    @Test
    public void testIsValidWithEmptyWhitelist() {
        Whitelist emptyWhitelist = Whitelist.empty();
        Cleaner cleaner = new Cleaner(emptyWhitelist);

        String dirtyHtml = "<html><body><p id='test'>Hello World</p><script>alert('XSS');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        assertFalse("Invalid document with empty whitelist should return false", cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidWithBasicWhitelist() {
        String whitelistXml = "<whitelist xmlns:html='http://www.w3.org/1999/xhtml' xmlns:xs='http://www.w3.org/2001/XMLSchema'> " +
                "<html xmlns='http://www.w3.org/1999/xhtml'><head/><body>" +
                "<tag name='p'/>" +
                "</body></html>" +
                "</whitelist>";
        Whitelist whitelist = Whitelist.parse(whitelistXml);
        Cleaner cleaner = new Cleaner(whitelist);

        String dirtyHtml = "<html><body><p id='test'>Hello World</p><script>alert('XSS');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        assertTrue("Invalid document with basic whitelist should return true", cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodesWithEmptyWhitelist() {
        Whitelist emptyWhitelist = Whitelist.empty();
        Cleaner cleaner = new Cleaner(emptyWhitelist);

        String dirtyHtml = "<html><body><p id='test'>Hello World</p><script>alert('XSS');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Element root = dirtyDocument.body();

        Element dest = Jsoup.parse("<html><body></body></html>").body();
        int numDiscarded = cleaner.copySafeNodes(root, dest);

        assertEquals("No nodes should be copied with empty whitelist", 0, numDiscarded);
    }

    @Test
    public void testCopySafeNodesWithBasicWhitelist() {
        String whitelistXml = "<whitelist xmlns:html='http://www.w3.org/1999/xhtml' xmlns:xs='http://www.w3.org/2001/XMLSchema'> " +
                "<html xmlns='http://www.w3.org/1999/xhtml'><head/><body>" +
                "<tag name='p'/>" +
                "</body></html>" +
                "</whitelist>";
        Whitelist whitelist = Whitelist.parse(whitelistXml);
        Cleaner cleaner = new Cleaner(whitelist);

        String dirtyHtml = "<html><body><p id='test'>Hello World</p><script>alert('XSS');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Element root = dirtyDocument.body();

        Element dest = Jsoup.parse("<html><body></body></html>").body();
        int numDiscarded = cleaner.copySafeNodes(root, dest);

        assertEquals("One <p> tag should be copied with basic whitelist", 1, numDiscarded);
        Element pElement = dest.getElementsByTag("p").first();
        assertEquals("Copied <p> tag should have correct id", "test", pElement.id());
    }

    @Test
    public void testCreateSafeElementWithEmptyWhitelist() {
        Whitelist emptyWhitelist = Whitelist.empty();
        Cleaner cleaner = new Cleaner(emptyWhitelist);

        Element sourceElement = Jsoup.parse("<p id='test'>Hello World</p>").first();

        Element destElement = cleaner.createSafeElement(sourceElement);

        assertTrue("Safe element should be null with empty whitelist", destElement == null);
    }

    @Test
    public void testCreateSafeElementWithBasicWhitelist() {
        String whitelistXml = "<whitelist xmlns:html='http://www.w3.org/1999/xhtml' xmlns:xs='http://www.w3.org/2001/XMLSchema'> " +
                "<html xmlns='http://www.w3.org/1999/xhtml'><head/><body>" +
                "<tag name='p'/>" +
                "</body></html>" +
                "</whitelist>";
        Whitelist whitelist = Whitelist.parse(whitelistXml);
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceElement = Jsoup.parse("<p id='test'>Hello World</p>").first();

        Element destElement = cleaner.createSafeElement(sourceElement);

        assertEquals("Safe element should be instance of <p>", destElement.tagName(), Tag.P.name());
        assertEquals("Safe element should have correct id", "test", destElement.id());
    }
}