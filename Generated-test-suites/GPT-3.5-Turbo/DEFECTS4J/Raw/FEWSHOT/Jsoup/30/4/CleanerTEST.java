
package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setup() {
        Whitelist whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        String dirtyHtml = "<html><body><div class=\"unsafe\">Some <span>unsafe</span> HTML</div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        String expectedHtml = "<html><head></head><body><div>Some <span>unsafe</span> HTML</div></body></html>";
        assertEquals(expectedHtml, cleanDocument.outerHtml());
    }

    @Test
    public void testClean_EmptyBody() {
        String dirtyHtml = "<html><body></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        String expectedHtml = "<html><head></head><body></body></html>";
        assertEquals(expectedHtml, cleanDocument.outerHtml());
    }

    @Test
    public void testIsValid_ValidDocument() {
        String html = "<html><body><div>Safe HTML</div></body></html>";
        Document document = Jsoup.parse(html);
        boolean isValid = cleaner.isValid(document);

        assertTrue(isValid);
    }

    @Test
    public void testIsValid_InvalidDocument() {
        String html = "<html><body><div class=\"unsafe\">Unsafe HTML</div></body></html>";
        Document document = Jsoup.parse(html);
        boolean isValid = cleaner.isValid(document);

        assertFalse(isValid);
    }
}
