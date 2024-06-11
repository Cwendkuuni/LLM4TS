package org.jsoup.safety;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.none());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Document.createShell("");
        dirtyDocument.body().appendChild(Tag.valueOf("div"));
        dirtyDocument.body().appendChild(Tag.valueOf("p"));
        dirtyDocument.body().appendChild(Tag.valueOf("a"));

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals(0, cleanDocument.body().children().size());
    }

    @Test
    public void testIsValid() {
        Document validDocument = Document.createShell("");
        validDocument.body().appendChild(Tag.valueOf("p"));

        assertTrue(cleaner.isValid(validDocument));

        Document invalidDocument = Document.createShell("");
        invalidDocument.body().appendChild(Tag.valueOf("script"));

        assertFalse(cleaner.isValid(invalidDocument));
    }
}