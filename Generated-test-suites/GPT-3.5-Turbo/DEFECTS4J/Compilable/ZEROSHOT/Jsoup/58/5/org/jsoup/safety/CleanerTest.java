package org.jsoup.safety;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        String dirtyHtml = "<html><head><title>Dirty HTML</title></head><body><h1>Hello</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        String expectedCleanHtml = "<html><head></head><body><h1>Hello</h1></body></html>";
        Assert.assertEquals(expectedCleanHtml, cleanDocument.html());
    }

    @Test
    public void testIsValid() {
        String dirtyHtml = "<html><head><title>Dirty HTML</title></head><body><h1>Hello</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        boolean isValid = cleaner.isValid(dirtyDocument);

        Assert.assertTrue(isValid);
    }
}