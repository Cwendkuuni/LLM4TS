package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.junit.BeforeClass;
import org.junit.Test;

public class CleanerTest {

    private static Cleaner cleaner;

    @BeforeClass
    public static void setUp() {
        Whitelist whitelist = Whitelist.relaxed();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean_NormalBody() {
        String html = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        Document dirtyDocument = Parser.parseBodyFragment(html, "");

        Document clean = cleaner.clean(dirtyDocument);

        assertEquals("<html><head></head><body><h1>Hello World!</h1></body></html>", clean.outerHtml());
    }

    @Test
    public void testClean_EmptyBody() {
        String html = "<html><head></head><body></body></html>";
        Document dirtyDocument = Parser.parseBodyFragment(html, "");

        Document clean = cleaner.clean(dirtyDocument);

        assertEquals("<html><head></head><body></body></html>", clean.outerHtml());
    }

    @Test
    public void testClean_InvalidTag() {
        String html = "<html><head></head><body><script>alert('Hello World!')</script></body></html>";
        Document dirtyDocument = Parser.parseBodyFragment(html, "");

        Document clean = cleaner.clean(dirtyDocument);

        assertEquals("<html><head></head><body></body></html>", clean.outerHtml());
    }

    @Test
    public void testClean_InvalidAttribute() {
        String html = "<html><head></head><body><h1 onclick=\"alert('Hello World!')\">Hello</h1></body></html>";
        Document dirtyDocument = Parser.parseBodyFragment(html, "");

        Document clean = cleaner.clean(dirtyDocument);

        assertEquals("<html><head></head><body><h1>Hello</h1></body></html>", clean.outerHtml());
    }

    @Test
    public void testClean_MultipleInvalidTagsAndAttributes() {
        String html = "<html><head></head><body><h1 onclick=\"alert('Hello World!')\"><script>alert('Invalid')</script></h1></body></html>";
        Document dirtyDocument = Parser.parseBodyFragment(html, "");

        Document clean = cleaner.clean(dirtyDocument);

        assertEquals("<html><head></head><body><h1></h1></body></html>", clean.outerHtml());
    }

    @Test
    public void testIsValid_ValidBody() {
        String html = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        Document dirtyDocument = Parser.parseBodyFragment(html, "");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValid_InvalidTag() {
        String html = "<html><head></head><body><script>alert('Hello World!')</script></body></html>";
        Document dirtyDocument = Parser.parseBodyFragment(html, "");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void testIsValid_InvalidAttribute() {
        String html = "<html><head></head><body><h1 onclick=\"alert('Hello World!')\">Hello</h1></body></html>";
        Document dirtyDocument = Parser.parseBodyFragment(html, "");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void testIsValid_MultipleInvalidTagsAndAttributes() {
        String html = "<html><head></head><body><h1 onclick=\"alert('Hello World!')\"><script>alert('Invalid')</script></h1></body></html>";
        Document dirtyDocument = Parser.parseBodyFragment(html, "");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }
}