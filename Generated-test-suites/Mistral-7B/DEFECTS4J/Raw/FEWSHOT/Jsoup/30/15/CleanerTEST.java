package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {

    @Test
    public void testCleanerValidityWithEmptyDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.createShell("http://test.com");
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCleanerValidityWithSimpleTags() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("p", "div", "h1", "a");
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p>Test 1</p><div id='test2'><h1>Test 3</h1><a href='http://test.com'>Test Link</a></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleaner.isValid(dirtyDocument));
        assertEquals(html, cleanDocument.html());
    }

    @Test
    public void testCleanerValidityWithDisallowedTag() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("p", "div", "h1", "a");
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p>Test 1</p><script type='text/javascript'>alert('XSS');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertFalse(cleaner.isValid(dirtyDocument));
        assertTrue(cleanDocument.body().text().isEmpty());
    }

    @Test
    public void testCleanerValidityWithDisallowedAttribute() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("p", "div", "h1", "a");
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p>Test 1</p><a href='http://test.com' onclick='alert('XSS');'>Test Link</a></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertFalse(cleaner.isValid(dirtyDocument));
        assertTrue(cleanDocument.body().text().isEmpty());
    }

    @Test
    public void testCleanerValidityWithDisallowedAttributeOnTag() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("p", "div", "h1", "a");
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p onmouseover='alert('XSS');'>Test 1</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertFalse(cleaner.isValid(dirtyDocument));
        assertTrue(cleanDocument.body().text().equals("Test 1"));
    }

    @Test
    public void testCleanerValidityWithAttributesOnMultipleElements() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("p", "div", "h1", "a");
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p id='test1' onmouseover='alert('XSS1');'>Test 1</p><div class='test2' id='test3' onmouseover='alert('XSS2');'>Test 2<a href='http://test.com' onmouseover='alert('XSS3');' target='_blank'>Test Link</a></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertFalse(cleaner.isValid(dirtyDocument));
        assertTrue(cleanDocument.body().text().isEmpty());
    }

    @Test
    public void testCleanerWithAllowedAttributes() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("p", "div", "h1", "a");
        whitelist.addAttribute("p", "id");
        whitelist.addAttribute("a", "href");

        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p id='test1'>Test 1</p><div id='test2' class='test3'><h1>Test 3</h1><a href='http://test.com'>Test Link</a></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleaner.isValid(dirtyDocument));
        assertEquals(html, cleanDocument.html());
    }

    @Test
    public void testCleanerWithDisallowedTagWithChildren() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("p", "div", "h1", "a");

        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p>Test 1<script type='text/javascript'>alert('XSS');</script></p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertFalse(cleaner.isValid(dirtyDocument));
        assertTrue(cleanDocument.body().text().isEmpty());
    }

    @Test
    public void testCleanerWithComplexNestedStructure() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("p", "div", "h1", "a");

        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p>Test 1<div><h1>Test 3<a href='http://test.com'>Test Link</a></h1><p>Test 4</p></div></p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleaner.isValid(dirtyDocument));
        assertEquals(html, cleanDocument.html());
    }

    @Test
    public void testCleanerWithEmptyTextNodes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p></p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleanDocument.body().text().isEmpty());
        assertTrue(cleanDocument.body().children().size() == 1);
    }

    @Test
    public void testCleanerWithNonEmptyTextNodes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p>Test 1</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleanDocument.body().text().equals("Test 1"));
        assertTrue(cleanDocument.body().children().size() == 1);
    }

    @Test
    public void testCleanerWithMultipleTextNodes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p>Test 1 Test 2</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleanDocument.body().text().equals("Test 1 Test 2"));
        assertTrue(cleanDocument.body().children().size() == 1);
    }

    @Test
    public void testCleanerWithComments() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p>Test 1 <!-- comment 1 --></p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleanDocument.body().text().equals("Test 1"));
        assertTrue(cleanDocument.body().children().size() == 1);
    }

    @Test
    public void testCleanerWithScriptTags() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p>Test 1<script type='text/javascript'>alert('XSS');</script></p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleanDocument.body().text().isEmpty());
        assertTrue(cleanDocument.body().children().size() == 1);
    }

    @Test
    public void testCleanerWithStyleTags() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p>Test 1<style type='text/css'>body { background-color: red; }</style></p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleanDocument.body().text().equals("Test 1"));
        assertTrue(cleanDocument.body().children().size() == 1);
    }

    private static Element getElementWithTag(String tagName, Document document) {
        Elements elements = document.select("*" + tagName + "*");
        if (elements.size() > 0) {
            return elements.get(0);
        }
        return null;
    }

    @Test
    public void testCleanerWithMultipleElementsWithSameTag() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p>Test 1</p><p>Test 2</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleanDocument.body().text().equals("Test 1Test 2"));
        assertTrue(cleanDocument.body().children().size() == 2);
    }

    @Test
    public void testCleanerWithMultipleElementsWithDifferentTags() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("p", "div");
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p>Test 1</p><div>Test 2</div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleanDocument.body().text().equals("Test 1Test 2"));
        assertTrue(cleanDocument.body().children().size() == 2);
    }
}