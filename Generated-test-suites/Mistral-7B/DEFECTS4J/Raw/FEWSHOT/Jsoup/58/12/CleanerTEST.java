package org.jsoup.safety;

import static org.junit.Assert.*;
import static org.jsoup.helper.Validate.*;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;

public class CleanerTest {

    @Test
    public void testCleanerWithValidWhitelist() {
        Whitelist validWhitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(validWhitelist);

        String html = "<html><head></head><body><div id='foo' class='bar baz'>Hello, world! <script>alert('XSS');</script></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><head></head><body><div id='foo' class='bar'>Hello, world!</div></body></html>", cleanDocument.html());
    }

    @Test
    public void testCleanerWithEmptyWhitelist() {
        Whitelist emptyWhitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(emptyWhitelist);

        String html = "<html><body></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body></body></html>", cleanDocument.html());
    }

    @Test
    public void testCleanerWithCustomWhitelist() {
        Whitelist customWhitelist = new Whitelist().addTags("div", "p").addAttribute("class");
        Cleaner cleaner = new Cleaner(customWhitelist);

        String html = "<html><head></head><body><div id='foo' class='bar baz'>Hello, world! <script>alert('XSS');</script></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><head></head><body><div id='foo' class='bar'>Hello, world!</div></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValidWithValidHtml() {
        Whitelist validWhitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(validWhitelist);

        String html = "<html><head></head><body><div id='foo' class='bar'>Hello, world!</div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testIsValidWithInvalidHtml() {
        Whitelist validWhitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(validWhitelist);

        String html = "<html><head><script>alert('XSS');</script></head><body><div id='foo' class='bar'>Hello, world! <script>alert('XSS');</script></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

    @Test
    public void testCleaningVisitor() {
        Whitelist validWhitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(validWhitelist);

        String html = "<html><head><meta charset='UTF-8' /><title>Test</title></head><body><div id='foo' class='bar baz'>Hello, world! <img src='image.jpg' alt='Image' /> <script>alert('XSS');</script></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><head><meta charset='UTF-8' /><title>Test</title></head><body><div id='foo' class='bar'>Hello, world! <img src='image.jpg' alt='Image' /></div></body></html>", cleanDocument.html());

        Element root = dirtyDocument.body();
        CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, null);
        NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
        traversor.traverse(root);

        assertEquals(7, cleaningVisitor.numDiscarded);
    }
}