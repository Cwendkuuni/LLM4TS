package org.jsoup.safety;

import org.junit.Test;
import org.jsoup.Jsoup;

import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        assertNotNull(cleaner);
        Document dirtyDocument = Jsoup.parse("<html><body><div><p>Hello World!</p></div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <div>\n" +
                "   <p>Hello World!</p>\n" +
                "  </div>\n" +
                " </body>\n" +
                "</html>", cleanDocument.outerHtml());
    }

    @Test
    public void testIsValid_validDocument() {
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        assertNotNull(cleaner);
        Document validDocument = Jsoup.parse("<html><body><p>Hello World!</p></body></html>");
        assertTrue(cleaner.isValid(validDocument));
    }

    @Test
    public void testIsValid_invalidDocument() {
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        assertNotNull(cleaner);
        Document invalidDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
        assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        assertNotNull(cleaner);
        Element source = Jsoup.parse("<div><p>Hello World!</p></div>").body();
        Element dest = new Element("div");
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(0, numDiscarded);
        assertEquals("<div>\n" +
                " <p>Hello World!</p>\n" +
                "</div>", dest.outerHtml());
    }

    @Test
    public void testCreateSafeElement() {
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        assertNotNull(cleaner);
        Element sourceEl = new Element("p");
        sourceEl.attr("style", "font-size: 12px;");
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        Element dest = elementMeta.el;
        assertEquals("<p style=\"font-size: 12px;\"></p>", dest.outerHtml());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_withUnsafeAttribute() {
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        assertNotNull(cleaner);
        Element sourceEl = new Element("p");
        sourceEl.attr("onclick", "alert('XSS');");
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        Element dest = elementMeta.el;
        assertEquals("<p></p>", dest.outerHtml());
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}