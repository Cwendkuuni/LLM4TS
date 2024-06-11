
package org.jsoup.safety;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        // Test case 1: Empty document
        Document dirtyDocument1 = Document.createShell("");
        Document clean1 = cleaner.clean(dirtyDocument1);
        Assert.assertEquals("Cleaned document's body should be empty", "", clean1.body().html());

        // Test case 2: Document with only safe elements
        Document dirtyDocument2 = Document.createShell("");
        dirtyDocument2.body().append("<p>Safe text</p>");
        Document clean2 = cleaner.clean(dirtyDocument2);
        Assert.assertEquals("Cleaned document should have only safe elements", "<p>Safe text</p>", clean2.body().html());

        // Test case 3: Document with unsafe elements
        Document dirtyDocument3 = Document.createShell("");
        dirtyDocument3.body().append("<script>alert('XSS')</script>");
        Document clean3 = cleaner.clean(dirtyDocument3);
        Assert.assertEquals("Cleaned document should not contain unsafe elements", "", clean3.body().html());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        // Test case 1: Empty document is valid
        Document dirtyDocument1 = Document.createShell("");
        Assert.assertTrue("Empty document should be valid", cleaner.isValid(dirtyDocument1));

        // Test case 2: Document with only safe elements is valid
        Document dirtyDocument2 = Document.createShell("");
        dirtyDocument2.body().append("<p>Safe text</p>");
        Assert.assertTrue("Document with only safe elements should be valid", cleaner.isValid(dirtyDocument2));

        // Test case 3: Document with unsafe elements is not valid
        Document dirtyDocument3 = Document.createShell("");
        dirtyDocument3.body().append("<script>alert('XSS')</script>");
        Assert.assertFalse("Document with unsafe elements should not be valid", cleaner.isValid(dirtyDocument3));
    }
}
