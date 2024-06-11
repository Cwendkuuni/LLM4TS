package org.jsoup.safety;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = new Document("");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = new Document("");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Element root = new Element(Tag.valueOf("body"), "");
        Element destination = new Element(Tag.valueOf("body"), "");
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceElement = new Element(Tag.valueOf("a"), "");
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        assertNotNull(elementMeta);
    }

}