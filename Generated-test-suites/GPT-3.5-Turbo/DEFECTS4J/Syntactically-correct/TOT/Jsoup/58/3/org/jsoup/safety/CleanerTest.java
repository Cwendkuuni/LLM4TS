package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        String dirtyHtml = "<html><body><h1>This is a test</h1><script>alert('XSS attack');</script><p>Hello, world!</p></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        Elements elements = cleanDocument.select("script");
        Assert.assertEquals(0, elements.size());
        Assert.assertEquals("<h1>This is a test</h1><p>Hello, world!</p>", cleanDocument.body().html());
    }

    @Test
    public void testIsValid_ValidDocument() {
        String html = "<html><body><h1>This is a valid document</h1><p>Hello, world!</p></body></html>";
        Document document = Jsoup.parse(html);
        
        boolean isValid = cleaner.isValid(document);
        
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValid_InvalidDocument() {
        String html = "<html><head><title>Invalid Document</title></head><body><h1>This is an invalid document</h1><p>Hello, world!</p></body></html>";
        Document document = Jsoup.parse(html);
        
        boolean isValid = cleaner.isValid(document);
        
        Assert.assertFalse(isValid);
    }
}