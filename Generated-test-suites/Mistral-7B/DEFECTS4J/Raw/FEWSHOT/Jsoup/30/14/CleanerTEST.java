package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {

    @Test
    public void testCleanerConstruction() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertEquals(whitelist, cleaner.whitelist);
    }

    @Test(expected = NullPointerException.class)
    public void testCleanerNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><div>Hello <script>alert('XSS')</script></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanDocument.body().text().isEmpty());
        assertEquals(0, cleanDocument.body().childNodeSize());
    }

    @Test
    public void testCleanWithAllowedTags() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("div");

        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><div>Hello <script>alert('XSS')</script></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element body = cleanDocument.body();
        Element div = body.getFirstElementChild();
        assertEquals("div", div.tagName());
        assertTrue(div.text().contains("Hello"));
        assertEquals(0, div.childNodeSize());
    }

    @Test
    public void testCleanWithDisallowedTags() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("div");

        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><script>alert('XSS')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanDocument.body().text().isEmpty());
        assertEquals(0, cleanDocument.body().childNodeSize());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><div id='test'>Hello World</div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidWithDisallowedTag() {
        Whitelist whitelist = new Whitelist();

        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><script>alert('XSS')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidWithDisallowedAttribute() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTag("div");
        whitelist.addAttribute("div", "*", "on*");

        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><div onclick='alert(\"XSS\")'>Hello World</div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidWithAllowedAttribute() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTag("div");
        whitelist.addAttribute("div", "*", "class");

        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><div class='test'>Hello World</div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodesWithAllowedTag() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("div");

        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><div id='test'>Hello <script>alert('XSS')</script> World</div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = Jsoup.parse("<html><body></body></html>");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals(1, cleanDocument.body().childNodeSize());
        Element div = cleanDocument.body().getFirstElementChild();
        assertEquals("div", div.tagName());
        assertEquals("test", div.attr("id"));
        assertTrue(div.text().contains("Hello World"));
        assertEquals(0, div.childNodeSize());
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCopySafeNodesWithDisallowedTag() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("div");

        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><script>alert('XSS')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = Jsoup.parse("<html><body></body></html>");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals(1, cleanDocument.body().childNodeSize());
        TextNode textNode = cleanDocument.body().childNode(0);
        assertNull(textNode);
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCopySafeNodesWithAllowedAttribute() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTag("div");
        whitelist.addAttribute("div", "*", "class");

        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><div class='test' id='test'>Hello World</div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = Jsoup.parse("<html><body></body></html>");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals(1, cleanDocument.body().childNodeSize());
        Element div = cleanDocument.body().getFirstElementChild();
        assertEquals("div", div.tagName());
        assertEquals("test", div.attr("class"));
        assertEquals("test", div.attr("id"));
        assertTrue(div.text().contains("Hello World"));
        assertEquals(0, div.childNodeSize());
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodesWithDisallowedAttribute() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTag("div");
        whitelist.addAttribute("div", "*", "on*");

        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><div onclick='alert(\"XSS\")'>Hello World</div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = Jsoup.parse("<html><body></body></html>");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals(1, cleanDocument.body().childNodeSize());
        TextNode textNode = cleanDocument.body().childNode(0);
        assertNull(textNode);
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElementWithAllowedTag() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("div");

        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><div id='test'>Hello World</div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element safeElement = cleaner.createSafeElement(dirtyDocument.body().getFirstElementChild());

        assertEquals("div", safeElement.tagName());
        assertEquals("test", safeElement.attr("id"));
        assertTrue(safeElement.text().contains("Hello World"));
        assertEquals(0, safeElement.childNodeSize());
    }

    @Test
    public void testCreateSafeElementWithDisallowedTag() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("div");

        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><script>alert('XSS')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element safeElement = cleaner.createSafeElement(dirtyDocument.body().getFirstElementChild());

        assertNull(safeElement);
    }

    @Test
    public void testCreateSafeElementWithAllowedAttribute() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTag("div");
        whitelist.addAttribute("div", "*", "class");

        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><div class='test' id='test'>Hello World</div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element safeElement = cleaner.createSafeElement(dirtyDocument.body().getFirstElementChild());

        assertEquals("div", safeElement.tagName());
        assertEquals("test", safeElement.attr("class"));
        assertEquals("test", safeElement.attr("id"));
        assertTrue(safeElement.text().contains("Hello World"));
        assertEquals(0, safeElement.childNodeSize());
    }

    @Test
    public void testCreateSafeElementWithDisallowedAttribute() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTag("div");
        whitelist.addAttribute("div", "*", "on*");

        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><div onclick='alert(\"XSS\")'>Hello World</div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element safeElement = cleaner.createSafeElement(dirtyDocument.body().getFirstElementChild());

        assertNull(safeElement);
    }
}