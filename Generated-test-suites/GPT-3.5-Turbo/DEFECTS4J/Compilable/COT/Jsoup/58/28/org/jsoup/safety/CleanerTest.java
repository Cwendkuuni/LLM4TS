package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;
    private Document dirtyDocument;
    
    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
        dirtyDocument = Jsoup.parse("<html><body><h1>Hello, World!</h1></body></html>");
    }
    
    @Test
    public void testClean() {
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        Assert.assertNotNull(cleanedDocument);
    }
    
    @Test
    public void testIsValid() {
        boolean isValid = cleaner.isValid(dirtyDocument);
        Assert.assertTrue(isValid);
    }
}
