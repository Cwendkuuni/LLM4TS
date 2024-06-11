package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;
    private String html;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
        html = "<html><body><div><p>This is a paragraph.</p><a href='example.com'>Link</a></div></body></html>";
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertNotNull(cleanDocument.body());
        assertEquals(1, cleanDocument.body().children().size());
        assertEquals("div", cleanDocument.body().child(0).tagName());
        assertEquals(1, cleanDocument.body().child(0).children().size());
        assertEquals("p", cleanDocument.body().child(0).child(0).tagName());
        assertEquals(0, cleanDocument.body().child(0).child(0).children().size());
        assertEquals("This is a paragraph.", cleanDocument.body().child(0).child(0).text());
    }

    @Test
    public void testClean_withEmptyBody() {
        Document dirtyDocument = Jsoup.parseBodyFragment("");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertNotNull(cleanDocument.body());
        assertEquals(0, cleanDocument.body().children().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClean_withNullDocument() {
        cleaner.clean(null);
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Jsoup.parseBodyFragment(html);
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValid_withInvalidDocument() {
        String invalidHtml = "<html><body><script>alert('XSS')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(invalidHtml);
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValid_withNullDocument() {
        cleaner.isValid(null);
    }
}