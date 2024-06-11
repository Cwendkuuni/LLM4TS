package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
    public void testClean() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><h1>Title</h1><p>Paragraph</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element body = cleanDocument.body();
        assertNotNull(body);

        Elements elements = body.children();
        assertEquals(2, elements.size());

        Element h1 = elements.get(0);
        assertEquals("h1", h1.tagName());
        assertEquals("Title", h1.text());

        Element p = elements.get(1);
        assertEquals("p", p.tagName());
        assertEquals("Paragraph", p.text());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><h1>Title</h1><p>Paragraph</p></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);

        dirtyDocument = Jsoup.parse("<html><head></head><body><script>alert('XSS')</script></body></html>");
        isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

}