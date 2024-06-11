package org.jsoup.safety;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(new Whitelist());
    }

    @Test
    public void testClean() {
        // Typical use case
        String html = "<html><body><div id=\"content\">Hello, world!</div></body></html>";
        Document dirtyDocument = Parser.parse(html, "");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertNotNull(cleanDocument.body());
        assertEquals(1, cleanDocument.body().children().size());
        assertEquals("div", cleanDocument.body().child(0).tagName());
        assertEquals("content", cleanDocument.body().child(0).attr("id"));
        assertEquals("Hello, world!", cleanDocument.body().child(0).text());

        // Edge case: Empty body
        html = "<html><body></body></html>";
        dirtyDocument = Parser.parse(html, "");
        cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertNotNull(cleanDocument.body());
        assertEquals(0, cleanDocument.body().children().size());

        // Error scenario: Null input
        try {
            cleaner.clean(null);
            fail("Exception not thrown");
        } catch (NullPointerException e) {
            assertTrue(e.getMessage().contains("dirtyDocument"));
        }
    }

    @Test
    public void testIsValid() {
        // Typical use case
        String html = "<html><body><div id=\"content\">Hello, world!</div></body></html>";
        Document dirtyDocument = Parser.parse(html, "");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);

        // Edge case: Invalid input with disallowed tags
        html = "<html><body><script>alert('Hello, world!');</script></body></html>";
        dirtyDocument = Parser.parse(html, "");
        isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);

        // Error scenario: Null input
        try {
            cleaner.isValid(null);
            fail("Exception not thrown");
        } catch (NullPointerException e) {
            assertTrue(e.getMessage().contains("dirtyDocument"));
        }
    }
}