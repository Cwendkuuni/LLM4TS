package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {
    @Test
    public void testClean() {
        // Create dirty document
        String html = "<html><head></head><body><h1>Title</h1><p>Paragraph</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        // Create whitelist
        Whitelist whitelist = Whitelist.basic();

        // Create cleaner
        Cleaner cleaner = new Cleaner(whitelist);

        // Clean the document
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Verify that only allowed elements are present in the clean document
        Assert.assertEquals("<html><head></head><body><h1>Title</h1><p>Paragraph</p></body></html>", cleanDocument.toString());
    }

    @Test
    public void testIsValid() {
        // Create dirty document
        String html = "<html><head></head><body><h1>Title</h1><script>alert('XSS')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        // Create whitelist
        Whitelist whitelist = Whitelist.basic();

        // Create cleaner
        Cleaner cleaner = new Cleaner(whitelist);

        // Check if the document is valid
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Verify that the document is not valid
        Assert.assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // Create source element
        Document document = Jsoup.parse("<div><p>Paragraph</p><span>Span</span></div>");
        Element source = document.select("div").first();

        // Create destination element
        Element destination = new Element(Tag.valueOf("div"), "");

        // Create whitelist
        Whitelist whitelist = Whitelist.basic();

        // Create cleaner
        Cleaner cleaner = new Cleaner(whitelist);

        // Copy safe nodes from source to destination
        int numDiscarded = cleaner.copySafeNodes(source, destination);

        // Verify that the safe nodes are copied properly
        Assert.assertEquals(1, numDiscarded);
        Assert.assertEquals("<div><p>Paragraph</p><span>Span</span></div>", destination.toString());
    }

    @Test
    public void testCreateSafeElement() {
        // Create source element
        Document document = Jsoup.parse("<p><a href='https://example.com'>Link</a><img src='https://example.com/image.jpg'></p>");
        Element source = document.select("p").first();

        // Create whitelist
        Whitelist whitelist = Whitelist.basic();

        // Create cleaner
        Cleaner cleaner = new Cleaner(whitelist);

        // Create safe element
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(source);

        // Verify that the safe element is created properly
        Assert.assertEquals("<p><a href=\"https://example.com\">Link</a><img src=\"https://example.com/image.jpg\"></p>", elementMeta.el.toString());
        Assert.assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}