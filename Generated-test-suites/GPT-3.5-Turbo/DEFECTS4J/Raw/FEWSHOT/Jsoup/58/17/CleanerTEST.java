
package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNull(cleanDocument.head());
        Element bodyElement = cleanDocument.body();
        assertNotNull(bodyElement);

        Elements paragraphs = bodyElement.getElementsByTag("p");
        assertEquals(1, paragraphs.size());
        Element paragraph = paragraphs.get(0);
        assertEquals("Hello World", paragraph.text());
    }

    @Test
    public void testIsValid_ValidDocument() {
        Document dirtyDocument = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValid_InvalidDocument() {
        Document dirtyDocument = Jsoup.parse("<html><head><title>Test</title></head><body><script>alert('Hello World')</script></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void testIsValid_EmptyHead() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><p>Hello World</p></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }
}
