package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        // Create a dirty document with unsafe elements and attributes
        String dirtyHtml = "<html><head></head><body><div onclick=\"alert('XSS')\">Unsafe Div</div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // Define the whitelist to allow only safe tags and attributes
        Whitelist whitelist = Whitelist.basicWithImages();
        Cleaner cleaner = new Cleaner(whitelist);

        // Clean the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Check if the unsafe elements and attributes are removed
        assertFalse(cleanDocument.select("div[onclick]").isEmpty());
        assertTrue(cleanDocument.select("div[onclick]").attr("onclick").isEmpty());
    }

    @Test
    public void testIsValid_ValidDocument() {
        // Create a valid document with safe elements and attributes
        String validHtml = "<html><head></head><body><p>Safe Paragraph</p></body></html>";
        Document validDocument = Jsoup.parse(validHtml);

        // Define the whitelist to allow only safe tags and attributes
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Check if the document is valid against the whitelist
        assertTrue(cleaner.isValid(validDocument));
    }

    @Test
    public void testIsValid_InvalidDocument() {
        // Create an invalid document with unsafe elements and attributes
        String invalidHtml = "<html><head></head><body><div onclick=\"alert('XSS')\">Unsafe Div</div></body></html>";
        Document invalidDocument = Jsoup.parse(invalidHtml);

        // Define the whitelist to allow only safe tags and attributes
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Check if the document is valid against the whitelist
        assertFalse(cleaner.isValid(invalidDocument));
    }

}