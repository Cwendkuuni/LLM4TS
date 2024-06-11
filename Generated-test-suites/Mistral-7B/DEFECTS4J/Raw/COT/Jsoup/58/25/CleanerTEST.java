package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CleanerTest {

    @org.junit.Before
    public void setup() {
        Whitelist.setupDefaults();
    }

    @org.junit.Test
    public void testCleanerConstruction() {
        Whitelist whitelist = new Whitelist.SimpleTagFilter();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @org.junit.Test
    public void testCleanMethod() throws Exception {
        String html = "<html><body><h1 id='title'>Hello</h1><script>alert('XSS');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertFalse("Expected no script tag", cleanDocument.body().select("script").isEmpty());
        assertNull("Expected no script tag", cleanDocument.body().getElementsByTag("script").first());
        assertTrue("Expected title tag", cleanDocument.body().select("h1").size() > 0);
        assertNotNull("Expected title tag", cleanDocument.body().getElementsByTag("h1").first());
    }

    @org.junit.Test
    public void testIsValidMethod() {
        String html = "<html><body><h1 id='title'>Hello</h1><script>alert('XSS');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse("Expected false", isValid);

        String safeHtml = "<html><body><h1 id='title'>Hello</h1></body></html>";
        Document safeDocument = Jsoup.parse(safeHtml);
        isValid = cleaner.isValid(safeDocument);
        assertTrue("Expected true", isValid);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCleanerInvalidWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @org.junit.Test
    public void testCleaningVisitor() {
        String html = "<html><body><h1 id='title'>Hello</h1><script>alert('XSS');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element body = cleanDocument.body();
        assertFalse("Expected no script tag", body.select("script").isEmpty());
        assertNull("Expected no script tag", body.getElementsByTag("script").first());
        assertTrue("Expected title tag", body.select("h1").size() > 0);
        assertNotNull("Expected title tag", body.getElementsByTag("h1").first());

        Element visitorDestination = new Element("div");
        Cleaner.CleaningVisitor cleaningVisitor = new Cleaner.CleaningVisitor(dirtyDocument.body(), visitorDestination);
        NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
        traversor.traverse(dirtyDocument.body());

        Element visitorRoot = visitorDestination;
        assertNotNull("Expected root element", visitorRoot);
        List<String> expectedTags = Arrays.asList("h1");
        List<String> actualTags = new ArrayList<>();
        for (Element child = visitorRoot.children(); child != null; child = child.nextSibling()) {
            actualTags.add(child.tagName());
        }
        assertTrue("Expected only h1 tag", actualTags.equals(expectedTags));
    }

    @org.junit.Test(expected = ParseError.class)
    public void testIsValidMethodWithParseError() {
        String invalidHtml = "<html><body><script>alert('XSS');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(invalidHtml, new Parser.MalformedHtmlException());
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        cleaner.isValid(dirtyDocument);
    }
}