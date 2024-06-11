package org.jsoup.safety;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.helper.W3CDom;
import org.jsoup.Jsoup;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        // Create dirty document
        String dirtyHtml = "<html><body><div><h1>Dirty Document</h1><p>This is a dirty document.</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // Create whitelist
        Whitelist whitelist = Whitelist.basic();

        // Create cleaner
        Cleaner cleaner = new Cleaner(whitelist);

        // Clean the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Verify that the clean document only contains allowed elements
        Element body = cleanDocument.body();
        Elements allowedElements = body.select("*");
        for (Element element : allowedElements) {
            assertTrue(whitelist.isSafeTag(element.tagName()));
        }
    }

    @Test
    public void testIsValid() {
        // Create dirty document with non-safe attributes
        String dirtyHtml = "<html><body><div><h1 style=\"color:red;\">Dirty Document</h1></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // Create whitelist
        Whitelist whitelist = Whitelist.basic();

        // Create cleaner
        Cleaner cleaner = new Cleaner(whitelist);

        // Check if the dirty document is valid
        boolean isValid = cleaner.isValid(dirtyDocument);

        // The dirty document is not valid, as it contains non-safe attributes
        assertFalse(isValid);
    }
}