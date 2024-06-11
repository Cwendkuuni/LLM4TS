
package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.simpleText());
    }

    @Test
    public void testClean() {
        String dirtyHtml = "<p>Hello <strong>World</strong></p>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("Hello World", cleanDocument.body().html());
        assertNull(cleanDocument.body().child(0).selectFirst("strong"));
    }

    @Test
    public void testIsValid() {
        String dirtyHtml = "<p>Hello <strong>World</strong></p>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        assertFalse(cleaner.isValid(dirtyDocument));

        dirtyDocument.body().empty();

        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        String dirtyHtml = "<p>Hello <strong>World</strong></p>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Element dirtyElement = dirtyDocument.body().child(0);

        Document cleanDocument = new Document("");
        Element cleanElement = cleanDocument.appendElement("div");

        int discarded = cleaner.copySafeNodes(dirtyElement, cleanElement);

        assertEquals(1, discarded);
        assertEquals("Hello World", cleanElement.html());
        assertNull(cleanElement.selectFirst("strong"));
    }

    @Test
    public void testCreateSafeElement() {
        String tagName = "p";
        String baseUri = "http://example.com";
        Attributes attrs = new Attributes();
        attrs.put("class", "text");
        attrs.put("data-id", "1234");
        attrs.put("style", "color:red;");
        Element sourceEl = new Element(Tag.valueOf(tagName), baseUri, attrs);

        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        Element dest = meta.el;
        int numDiscarded = meta.numAttribsDiscarded;

        assertEquals(tagName, dest.tagName());
        assertEquals(baseUri, dest.baseUri());
        assertEquals(1, dest.attributes().size());
        assertNotNull(dest.attributes().get("class"));
        assertEquals("text", dest.attributes().get("class"));
        assertNull(dest.attributes().get("data-id"));
        assertNull(dest.attributes().get("style"));
        assertEquals(2, numDiscarded);
    }

}
