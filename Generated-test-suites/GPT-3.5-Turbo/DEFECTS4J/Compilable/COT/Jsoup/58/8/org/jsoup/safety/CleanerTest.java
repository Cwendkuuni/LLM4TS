package org.jsoup.safety;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(new Whitelist());
    }

    @Test
    public void testClean() {
        // Test case for clean method
        Document dirtyDocument = new Document("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Assert.assertTrue(cleanDocument != null);
        Assert.assertTrue(cleanDocument.getElementsByTag("body").size() == 1);
        Assert.assertTrue(cleanDocument.getElementsByTag("p").size() == 1);
    }

    @Test
    public void testIsValid() {
        // Test case for isValid method
        Document dirtyDocument = new Document("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);

        Assert.assertTrue(isValid);
    }

    @Test
    public void testCleanWithEmptyDirtyDocument() {
        // Test case for clean method with empty dirtyDocument
        Document dirtyDocument = new Document("");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Assert.assertTrue(cleanDocument != null);
        Assert.assertTrue(cleanDocument.getElementsByTag("body").size() == 1);
        Assert.assertTrue(cleanDocument.getElementsByTag("body").get(0).children().size() == 0);
    }

    @Test
    public void testCleanWithFramesetDirtyDocument() {
        // Test case for clean method with dirtyDocument containing frameset
        Document dirtyDocument = new Document("<html><frameset></frameset></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Assert.assertTrue(cleanDocument != null);
        Assert.assertTrue(cleanDocument.getElementsByTag("body").size() == 1);
        Assert.assertTrue(cleanDocument.getElementsByTag("body").get(0).children().size() == 0);
    }

    @Test
    public void testCleanWithInvalidTagsAndAttributes() {
        // Test case for clean method with dirtyDocument containing invalid tags and attributes
        Document dirtyDocument = new Document("<html><head><title>Test</title></head><body><p><script>alert('Hello World!');</script></p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Assert.assertTrue(cleanDocument != null);
        Assert.assertTrue(cleanDocument.getElementsByTag("body").size() == 1);
        Assert.assertTrue(cleanDocument.getElementsByTag("p").size() == 1);
        Assert.assertTrue(cleanDocument.getElementsByTag("script").size() == 0);
    }

    @Test
    public void testIsValidWithInvalidTagsAndAttributes() {
        // Test case for isValid method with dirtyDocument containing invalid tags and attributes
        Document dirtyDocument = new Document("<html><head><title>Test</title></head><body><p><script>alert('Hello World!');</script></p></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);

        Assert.assertFalse(isValid);
    }
}
