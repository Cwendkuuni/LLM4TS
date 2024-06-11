
package org.jsoup.safety;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;

public class CleanerTest {

    @Test
    public void testCleanerConstructor() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test
    public void testClean() {
        // Test case with valid input
        String html = "<html><body><div>Hello World!</div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Cleaner cleaner = new Cleaner(new Whitelist());
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assert.assertNotNull(cleanDocument);

        // Test case with null input
        Document cleanNullDocument = cleaner.clean(null);
        Assert.assertNull(cleanNullDocument);
    }

    @Test
    public void testIsValid() {
        // Test case with valid input
        String html = "<html><body><div>Hello World!</div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Cleaner cleaner = new Cleaner(new Whitelist());
        boolean isValid = cleaner.isValid(dirtyDocument);
        Assert.assertTrue(isValid);

        // Test case with invalid input
        String invalidHtml = "<html><head></head><body><div>Hello World!</div></body></html>";
        Document invalidDirtyDocument = Jsoup.parse(invalidHtml);
        boolean isInvalid = cleaner.isValid(invalidDirtyDocument);
        Assert.assertFalse(isInvalid);

        // Test case with null input
        boolean isNullValid = cleaner.isValid(null);
        Assert.assertFalse(isNullValid);
    }
}
