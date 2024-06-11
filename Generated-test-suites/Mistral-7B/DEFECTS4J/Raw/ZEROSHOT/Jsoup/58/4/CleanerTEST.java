package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Test file for Cleaner class
 */
public class CleanerTest {

    /**
     * Test constructor
     */
    @org.junit.Test
    public void testConstructor() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    /**
     * Test clean method with valid input
     */
    @org.junit.Test
    public void testCleanValidInput() throws IOException {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnlyWhitelist());
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals(cleanDocument.html(), "<html><body><h1>Hello World</h1></body></html>");
    }

    /**
     * Test clean method with invalid input
     */
    @org.junit.Test
    public void testCleanInvalidInput() throws IOException {
        String html = "<html><head><script>alert('XSS')</script></head><body><h1>Hello World</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnlyWhitelist());
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals(cleanDocument.html(), "<html><body></body></html>");
    }

    /**
     * Test isValid method with valid input
     */
    @org.junit.Test
    public void testIsValidValidInput() throws IOException {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnlyWhitelist());
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    /**
     * Test isValid method with invalid input
     */
    @org.junit.Test
    public void testIsValidInvalidInput() throws IOException {
        String html = "<html><head><script>alert('XSS')</script></head><body><h1>Hello World</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnlyWhitelist());
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    /**
     * Test copySafeNodes method
     */
    @org.junit.Test
    public void testCopySafeNodes() {
        String html = "<html><head><script>alert('XSS')</script></head><body><h1 id='test' class='test' data-test='test'>Hello World</h1><script>alert('XSS')</script></body></html>";
        Element source = Jsoup.parse(html).body();
        Element dest = new Element("body", "");
        Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnlyWhitelist());
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        Elements elements = dest.children();
        assertEquals(elements.size(), 1);
        Element destElement = elements.get(0);
        assertEquals(destElement.tagName(), "h1");
        assertEquals(destElement.id(), "test");
        assertEquals(destElement.classes().size(), 1);
        assertEquals(destElement.classes().get(0), "test");
        assertEquals(destElement.attributes().size(), 1);
        assertEquals(destElement.attribute("data-test"), "test");
        assertTrue(numDiscarded > 3); // scripts and other discarded nodes
    }
}