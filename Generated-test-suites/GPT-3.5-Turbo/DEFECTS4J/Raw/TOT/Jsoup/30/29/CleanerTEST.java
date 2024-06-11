package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {

    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.relaxed());
        dirtyDocument = Jsoup.parse("<html><head></head><body><div>Hello World</div></body></html>");
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assert.assertEquals("body", cleanDocument.body().tagName());
        Assert.assertEquals(1, cleanDocument.body().children().size());
        Assert.assertEquals("div", cleanDocument.body().child(0).tagName());
        Assert.assertEquals("Hello World", cleanDocument.body().child(0).text());
    }

    @Test
    public void testClean_emptyDocument() {
        Document cleanDocument = cleaner.clean(Jsoup.parse("<html><head></head><body></body></html>"));
        Assert.assertEquals("body", cleanDocument.body().tagName());
        Assert.assertEquals(0, cleanDocument.body().children().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClean_nullDocument() {
        cleaner.clean(null);
    }

    @Test
    public void testIsValid() {
        Assert.assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValid_invalidDocument() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><script>alert('XSS')</script></body></html>");
        Assert.assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValid_emptyDocument() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><body></body></html>");
        Assert.assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValid_nullDocument() {
        cleaner.isValid(null);
    }
}