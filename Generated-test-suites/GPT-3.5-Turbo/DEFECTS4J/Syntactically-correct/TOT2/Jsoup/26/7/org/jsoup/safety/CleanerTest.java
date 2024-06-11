package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><div>Hello</div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertEquals("<html><head></head><body><div>Hello</div></body></html>", cleanDocument.outerHtml());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><div>Hello</div></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = new Element(Tag.valueOf("div"), "");

        ElementMeta elementMeta = cleaner.createSafeElement(source);
        assertNotNull(elementMeta);
    }

}