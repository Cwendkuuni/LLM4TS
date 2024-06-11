package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
        dirtyDocument = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1><p>This is a test</p><img src=\"test.jpg\"></body></html>");
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
    }

    @Test
    public void testIsValid() {
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Element source = dirtyDocument.body();
        Element dest = new Element(Tag.valueOf("body"), dirtyDocument.baseUri(), new Attributes());
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        ElementMeta elementMeta = cleaner.createSafeElement(dirtyDocument.body());
        assertNotNull(elementMeta);
        assertNotNull(elementMeta.el);
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}