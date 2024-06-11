package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    private static Cleaner cleaner;
    private static Whitelist whitelist;

    @BeforeClass
    public static void setup() {
        whitelist = Whitelist.relaxed();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        String dirtyHtml = "<html><body><div><script>alert('XSS')</script><p>Valid HTML</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml, "", Parser.xmlParser());

        Document cleanDocument = cleaner.clean(dirtyDocument);
        Elements scriptTags = cleanDocument.getElementsByTag("script");

        assertEquals("Valid HTML", cleanDocument.body().text());
        assertEquals(0, scriptTags.size());
    }

    @Test
    public void testClean_WithEmptyDocument_returnsEmptyDocument() {
        String dirtyHtml = "";
        Document dirtyDocument = Jsoup.parse(dirtyHtml, "", Parser.xmlParser());

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertEquals("", cleanDocument.html());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClean_WithNullDocument_throwsIllegalArgumentException() {
        cleaner.clean(null);
    }

    @Test
    public void testIsValid() {
        String dirtyHtml = "<html><body><div><p>Valid HTML</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml, "", Parser.xmlParser());

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValid_WithUnsafeTag_returnsFalse() {
        String dirtyHtml = "<html><body><div><script>alert('XSS')</script><p>Valid HTML</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml, "", Parser.xmlParser());

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValid_WithNullDocument_throwsIllegalArgumentException() {
        cleaner.isValid(null);
    }
}