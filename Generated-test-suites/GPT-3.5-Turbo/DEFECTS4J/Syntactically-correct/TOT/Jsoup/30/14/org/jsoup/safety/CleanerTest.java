package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;


public class CleanerTest {

    @Test
    public void testClean() {
        // Test 1: Typical use case
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        String dirtyHtml = "<html><head><title>Dirty HTML</title></head><body><p>Dirty paragraph</p></body></html>";
        Document dirtyDoc = Jsoup.parse(dirtyHtml);
        Document cleanDoc = cleaner.clean(dirtyDoc);

        Assert.assertEquals("<html><head><title>Dirty HTML</title></head><body><p>Dirty paragraph</p></body></html>", dirtyDoc.html());
        Assert.assertEquals("<html><head></head><body><p>Dirty paragraph</p></body></html>", cleanDoc.html());

        // Test 2: Edge case - empty document
        dirtyHtml = "";
        dirtyDoc = Jsoup.parse(dirtyHtml);
        cleanDoc = cleaner.clean(dirtyDoc);

        Assert.assertEquals("", dirtyDoc.html());
        Assert.assertEquals("<html><head></head><body></body></html>", cleanDoc.html());

        // Test 3: Edge case - no body tag
        dirtyHtml = "<html><head><title>Dirty HTML</title></head></html>";
        dirtyDoc = Jsoup.parse(dirtyHtml);
        cleanDoc = cleaner.clean(dirtyDoc);

        Assert.assertEquals("<html><head><title>Dirty HTML</title></head></html>", dirtyDoc.html());
        Assert.assertEquals("<html><head></head><body></body></html>", cleanDoc.html());

        // Test 4: Error scenario - null input document
        Assert.assertThrows(NullPointerException.class, () -> {
            cleaner.clean(null);
        });
    }

    @Test
    public void testIsValid() {
        // Test 1: Typical use case
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        String dirtyHtml = "<html><head><title>Dirty HTML</title></head><body><p>Dirty paragraph</p></body></html>";
        Document dirtyDoc = Jsoup.parse(dirtyHtml);
        boolean isValid = cleaner.isValid(dirtyDoc);

        Assert.assertTrue(isValid);

        // Test 2: Typical use case - invalid document
        dirtyHtml = "<html><head><title>Dirty HTML</title></head><body><script>alert('XSS attack');</script></body></html>";
        dirtyDoc = Jsoup.parse(dirtyHtml);
        isValid = cleaner.isValid(dirtyDoc);

        Assert.assertFalse(isValid);

        // Test 3: Edge case - empty document
        dirtyHtml = "";
        dirtyDoc = Jsoup.parse(dirtyHtml);
        isValid = cleaner.isValid(dirtyDoc);

        Assert.assertTrue(isValid);

        // Test 4: Edge case - no body tag
        dirtyHtml = "<html><head><title>Dirty HTML</title></head></html>";
        dirtyDoc = Jsoup.parse(dirtyHtml);
        isValid = cleaner.isValid(dirtyDoc);

        Assert.assertTrue(isValid);

        // Test 5: Error scenario - null input document
        Assert.assertThrows(NullPointerException.class, () -> {
            cleaner.isValid(null);
        });
    }

    @Test
    public void testCopySafeNodes() {
        // Test 1: Typical use case
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        Element dirtyElement = new Element(Tag.valueOf("div"), "");
        Element destElement = new Element(Tag.valueOf("div"), "");
        cleaner.copySafeNodes(dirtyElement, destElement);

        List<Node> destChildren = destElement.childNodes();
        Assert.assertEquals(0, destChildren.size());

        // Test 2: Edge case - empty element
        dirtyElement = new Element(Tag.valueOf("div"), "");
        destElement = new Element(Tag.valueOf("div"), "");
        dirtyElement.appendChild("");
        cleaner.copySafeNodes(dirtyElement, destElement);

        destChildren = destElement.childNodes();
        Assert.assertEquals(1, destChildren.size());

        // Test 3: Error scenario - null destination element
        dirtyElement = new Element(Tag.valueOf("div"), "");
        Assert.assertThrows(NullPointerException.class, () -> {
            cleaner.copySafeNodes(dirtyElement, null);
        });
    }

    @Test
    public void testCreateSafeElement() {
        // Test 1: Typical use case - empty element
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        Element dirtyElement = new Element(Tag.valueOf("div"), "");
        Cleaner.ElementMeta destElementMeta = cleaner.createSafeElement(dirtyElement);

        Element destElement = destElementMeta.el;
        Assert.assertEquals("<div></div>", destElement.outerHtml());
        Assert.assertEquals(0, destElementMeta.numAttribsDiscarded);

        // Test 2: Typical use case - element with attributes
        dirtyElement = new Element(Tag.valueOf("a"), "");
        dirtyElement.attr("href", "http://example.com");
        dirtyElement.attr("target", "_blank");
        destElementMeta = cleaner.createSafeElement(dirtyElement);

        destElement = destElementMeta.el;
        Assert.assertEquals("<a href=\"http://example.com\"></a>", destElement.outerHtml());
        Assert.assertEquals(1, destElementMeta.numAttribsDiscarded);

        // Test 3: Error scenario - null source element
        Assert.assertThrows(NullPointerException.class, () -> {
            cleaner.createSafeElement(null);
        });
    }

    @Test
    public void testCleanerConstructor() {
        // Test 1: Typical use case
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        Assert.assertEquals(whitelist, cleaner.getWhitelist());

        // Test 2: Error scenario - null whitelist
        Assert.assertThrows(NullPointerException.class, () -> {
            new Cleaner(null);
        });
    }

    @Test
    public void testElementMetaConstructor() {
        // Test 1: Typical use case
        Element destElement = new Element(Tag.valueOf("a"), "");
        Cleaner.ElementMeta elementMeta = new Cleaner.ElementMeta(destElement, 1);

        Assert.assertEquals(destElement, elementMeta.el);
        Assert.assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}