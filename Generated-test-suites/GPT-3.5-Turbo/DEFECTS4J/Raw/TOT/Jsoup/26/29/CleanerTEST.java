package org.jsoup.safety;

import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
        dirtyDocument = new Document("<html><head></head><body><div id=\"content\"><script>alert('XSS');</script><p>Hello world!</p></div></body></html>");
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assert.assertEquals("body", cleanDocument.head().tagName());
        Assert.assertEquals("div", cleanDocument.body().child(0).tagName());
        Assert.assertEquals("p", cleanDocument.body().child(0).child(0).tagName());
    }

    @Test
    public void testIsValid() {
        boolean isValid = cleaner.isValid(dirtyDocument);
        Assert.assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Element source = dirtyDocument.body();
        Element dest = new Element("body");
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        Assert.assertEquals(1, numDiscarded);
        Assert.assertEquals(2, dest.children().size());
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = dirtyDocument.body().child(0);
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        Element dest = elementMeta.el;
        Assert.assertEquals("div", dest.tagName());
        Assert.assertEquals(1, dest.children().size());
        Assert.assertEquals(1, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCleanerConstructor() {
        Assert.assertNotNull(cleaner);
    }
}