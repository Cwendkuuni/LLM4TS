package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Cleaner.ElementMeta;
import org.jsoup.safety.Whitelist;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;
    private Document dirtyDocument;
    private Document cleanDocument;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());

        dirtyDocument = Jsoup.parse("<html><head><title>Dirty Document</title></head><body><div><h1>Unsafe Heading</h1><p>Unsafe Paragraph</p></div></body></html>");
        cleanDocument = Jsoup.parse("<html><head><title>Clean Document</title></head><body><div><h1>Safe Heading</h1><p>Safe Paragraph</p></div></body></html>");
    }

    @Test
    public void testClean() {
        Document expectedCleanedDocument = Jsoup.parse("<html><head><title>Clean Document</title></head><body><div><h1>Safe Heading</h1><p>Safe Paragraph</p></div></body></html>");

        Document cleanedDocument = cleaner.clean(dirtyDocument);
        assertEquals(expectedCleanedDocument.html(), cleanedDocument.html());
    }

    @Test
    public void testIsValid() {
        assertFalse(cleaner.isValid(dirtyDocument));
        assertTrue(cleaner.isValid(cleanDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Element source = Jsoup.parseBodyFragment("<div><h1>Unsafe Heading</h1><p>Unsafe Paragraph</p></div>").body();
        Element dest = new Element(Tag.valueOf("div"), "");
        
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(2, numDiscarded);
        
        Element expectedDest = Jsoup.parseBodyFragment("<div><h1>Unsafe Heading</h1><p>Unsafe Paragraph</p></div>").body();
        assertEquals(expectedDest.html(), dest.html());
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("h1"), "");
        sourceEl.attributes().put("id", "heading");
        sourceEl.attributes().put("style", "color: red;");

        Tag expectedTag = Tag.valueOf("h1");
        String expectedBaseUri = "";
        Attributes expectedAttrs = new Attributes();
        expectedAttrs.put("id", "heading");

        Element expectedDest = new Element(expectedTag, expectedBaseUri, expectedAttrs);
        int expectedNumDiscarded = 1;
        
        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        assertEquals(expectedDest, elementMeta.el);
        assertEquals(expectedNumDiscarded, elementMeta.numAttribsDiscarded);
    }
}