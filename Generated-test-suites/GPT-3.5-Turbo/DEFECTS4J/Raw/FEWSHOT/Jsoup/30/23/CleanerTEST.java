package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse("<html><head><title>Dirty Document</title></head><body><div>Dirty Div</div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><head><title>Dirty Document</title></head><body><div>Dirty Div</div></body></html>", cleanDocument.outerHtml());
        assertFalse(cleanDocument == dirtyDocument);
    }

    @Test
    public void testIsValid() {
        Document validDocument = Jsoup.parse("<html><head><title>Valid Document</title></head><body><div>Valid Div</div></body></html>");
        assertTrue(cleaner.isValid(validDocument));

        Document invalidDocument = Jsoup.parse("<html><head><title>Invalid Document</title></head><body><script>alert('XSS')</script></body></html>");
        assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");

        Element safeElement = new Element(Tag.valueOf("p"), "");
        safeElement.appendText("Safe Text");
        root.appendChild(safeElement);

        Element unsafeElement = new Element(Tag.valueOf("script"), "");
        unsafeElement.appendText("alert('XSS')");
        root.appendChild(unsafeElement);

        int numDiscarded = cleaner.copySafeNodes(root, destination);

        assertEquals(1, destination.children().size());
        assertEquals("Safe Text", destination.children().get(0).text());
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("a"), "");
        sourceEl.attr("href", "example.com");
        sourceEl.attr("onclick", "alert('XSS')");

        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        assertEquals("a", elementMeta.el.tagName());
        assertEquals(1, elementMeta.el.attributes().size());
        assertEquals("example.com", elementMeta.el.attr("href"));
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}