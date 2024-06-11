package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testConstructor() {
        assertNotNull(cleaner);
    }

    @Test
    public void testClean() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello, world!</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(input);
        
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("Test", cleanDocument.head().selectFirst("title").text());
        assertEquals("Hello, world!", cleanDocument.body().selectFirst("h1").text());
    }

    @Test
    public void testClean_NullDocument() {
        assertNull(cleaner.clean(null));
    }

    @Test
    public void testClean_EmptyDocument() {
        Document emptyDocument = new Document("");
        Document cleanDocument = cleaner.clean(emptyDocument);

        assertTrue(cleanDocument.head().children().isEmpty());
        assertTrue(cleanDocument.body().children().isEmpty());
    }

    @Test
    public void testIsValid_ValidDocument() {
        String input = "<html><body><p>Hello, world!</p></body></html>";
        Document validDocument = Jsoup.parse(input);

        assertTrue(cleaner.isValid(validDocument));
    }

    @Test
    public void testIsValid_InvalidDocument() {
        String input = "<html><head><title>Test</title></head><body><p>Hello, world!</p></body></html>";
        Document invalidDocumnet = Jsoup.parse(input);

        assertFalse(cleaner.isValid(invalidDocumnet));
    }

    @Test
    public void testIsValid_NullDocument() {
        assertFalse(cleaner.isValid(null));
    }

    @Test
    public void testIsValid_EmptyDocument() {
        Document emptyDocument = new Document("");

        assertTrue(cleaner.isValid(emptyDocument));
    }

    @Test
    public void testClean_Integration() {
        String input = "<html><head><script>alert('Hello, world!');</script></head><body><p>Hello, world!</p></body></html>";
        Document dirtyDocument = Jsoup.parse(input);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertFalse(cleanDocument.head().hasText());
        assertTrue(cleanDocument.body().hasText());
    }
}