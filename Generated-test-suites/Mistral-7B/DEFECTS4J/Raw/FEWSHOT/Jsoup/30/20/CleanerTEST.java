package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CleanerTest {
    @Test
    public void testCleanerInstance() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertEquals("whitelist", cleaner.whitelist.name());
    }

    @Test(expected = NullPointerException.class)
    public void testCleanerNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testCleanNullDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = null;
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNull(cleanDocument);
    }

    @Test
    public void testCleanInvalidDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<script>alert('test')</script>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("", cleanDocument.body().html());
    }

    @Test
    public void testCleanValidDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><h1>Test</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body><h1>Test</h1></body></html>", cleanDocument.body().html());
    }

    @Test
    public void testCopySafeNodesEmpty() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Element root = Element.parse("<html><body></body></html>");
        Element destination = Element.parse("<html><body></body></html>");
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodesTextNode() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Element root = Element.parse("<html><body><textarea>Test</textarea></body></html>");
        Element destination = Element.parse("<html><body></body></html>");
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCopySafeNodesElementWithSafeTag() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTag("h1");
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><h1>Test</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element destination = Element.parse("<html><body></body></html>");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), destination);
        assertEquals(1, numDiscarded);
        Element h1 = destination.select("h1").first();
        assertSame(dirtyDocument.select("h1").first(), h1);
    }

    @Test
    public void testCopySafeNodesElementWithUnsafeTag() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><script>Test</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element destination = Element.parse("<html><body></body></html>");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), destination);
        assertEquals(1, numDiscarded);
        assertNull(destination.body().select("script").first());
    }

    @Test
    public void testCopySafeNodesElementWithAllowedAttribute() {
        Whitelist whitelist = new Whitelist();
        whitelist.addAttribute("a", "href");
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><a href=\"test\">Test</a></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element destination = Element.parse("<html><body></body></html>");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), destination);
        assertEquals(1, numDiscarded);
        Element a = destination.select("a").first();
        assertSame(dirtyDocument.select("a").first(), a);
        assertEquals("test", a.attr("href"));
    }

    @Test
    public void testCopySafeNodesElementWithDisallowedAttribute() {
        Whitelist whitelist = new Whitelist();
        whitelist.addAttribute("a", "onclick");
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><a onclick=\"test()\">Test</a></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element destination = Element.parse("<html><body></body></html>");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), destination);
        assertEquals(2, numDiscarded);
        assertNull(destination.body().select("a").first());
    }

    @Test
    public void testCopySafeNodesElementWithMultipleChildren() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTag("p");
        whitelist.addTag("span");
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><p><span>Test</span></p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element destination = Element.parse("<html><body></body></html>");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), destination);
        assertEquals(3, numDiscarded);
        Element p = destination.select("p").first();
        assertSame(dirtyDocument.select("p").first(), p);
        Element span = p.select("span").first();
        assertSame(dirtyDocument.select("p span").first(), span);
    }

    @Test
    public void testIsValidEmpty() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Element.parse("<html></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testIsValidTextNode() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Element.parse("<html><body><textarea>Test</textarea></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

    @Test
    public void testIsValidElementWithSafeTag() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTag("h1");
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><h1>Test</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testIsValidElementWithUnsafeTag() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><script>Test</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

    @Test
    public void testIsValidElementWithAllowedAttribute() {
        Whitelist whitelist = new Whitelist();
        whitelist.addAttribute("a", "href");
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><a href=\"test\">Test</a></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testIsValidElementWithDisallowedAttribute() {
        Whitelist whitelist = new Whitelist();
        whitelist.addAttribute("a", "onclick");
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><a onclick=\"test()\">Test</a></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

    @Test
    public void testIsValidElementWithNestedChild() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTag("p");
        whitelist.addTag("span");
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><p><span>Test</span></p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testIsValidElementWithMixedChildren() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTag("p");
        whitelist.addTag("span");
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><p><span>Test</span> Test</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }
}