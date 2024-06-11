package org.jsoup.safety;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Typical use case: clean a dirty document
        Document dirty = Jsoup.parseBodyFragment("<p><script>alert('XSS');</script></p>");
        Document clean = cleaner.clean(dirty);
        Assert.assertEquals("<body><p></p></body>", clean.body().html());

        // Edge case: clean an empty document
        Document empty = Jsoup.parseBodyFragment("");
        Document emptyClean = cleaner.clean(empty);
        Assert.assertEquals("<body></body>", emptyClean.body().html());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Typical use case: check if a dirty document is valid
        Document dirty = Jsoup.parse("<html><head><title>Title</title></head><body><p>Paragraph</p></body></html>");
        boolean valid = cleaner.isValid(dirty);
        Assert.assertTrue(valid);

        // Edge case: check if an empty document is valid
        Document empty = Jsoup.parse("");
        boolean emptyValid = cleaner.isValid(empty);
        Assert.assertTrue(emptyValid);

        // Error scenario: check if a dirty document with disallowed tags is valid
        Document dirtyInvalid = Jsoup.parse("<html><head><title>Title</title></head><body><script>alert('XSS');</script></body></html>");
        boolean invalid = cleaner.isValid(dirtyInvalid);
        Assert.assertFalse(invalid);
    }
}