package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {
    
    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<div><p>Hello, <a href=\"#\">world</a></p></div>";
        Document dirtyDocument = Jsoup.parse(html);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<div><p>Hello, <a href=\"#\">world</a></p></div>", cleanDocument.body().html());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<div><p>Hello, <a href=\"#\">world</a></p></div>";
        Document dirtyDocument = Jsoup.parse(html);

        assertFalse(cleaner.isValid(dirtyDocument));

        String validHtml = "<div><p>Hello, world</p></div>";
        Document validDocument = Jsoup.parse(validHtml);

        assertTrue(cleaner.isValid(validDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceElement = new Element(Tag.valueOf("div"), "");
        Element destElement = new Element(Tag.valueOf("div"), "");

        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        Element safeElement = elementMeta.el;

        assertEquals("<div></div>", safeElement.outerHtml());
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceElement = new Element(Tag.valueOf("a"), "");
        sourceElement.attr("href", "#");
        
        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        Element safeElement = elementMeta.el;

        assertEquals("<a href=\"#\"></a>", safeElement.outerHtml());
    }

}