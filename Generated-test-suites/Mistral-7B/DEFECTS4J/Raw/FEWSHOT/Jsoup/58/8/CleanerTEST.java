package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.whitelist.Elements;
import org.jsoup.whitelist.Whitelist;

public class CleanerTest {

    private Cleaner cleaner;
    private Document dirtyDocument;
    private Whitelist whitelist;

    @Before
    public void setUp() {
        whitelist = Whitelist.simpleText();
        cleaner = new Cleaner(whitelist);

        dirtyDocument = Parser.parser().parse("<html><body><div id='test'><p>Hello <b>World</b></p></div></body></html>");
    }

    @Test
    public void testCreateCleanerWithNullWhitelist() {
        try {
            new Cleaner(null);
            fail("Expected IllegalArgumentException with message: 'whitelist' must not be null.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateCleaner() {
        assertNotNull(cleaner);
    }

    @Test
    public void testCleanNullDocument() {
        assertNull(cleaner.clean(null));
    }

    @Test
    public void testCleanEmptyDocument() {
        Document emptyDocument = Document.createShell("http://example.com");
        Document clean = cleaner.clean(emptyDocument);
        assertEquals(emptyDocument.body().nodeName(), clean.body().nodeName());
        assertNull(clean.body().text());
    }

    @Test
    public void testCleanDocumentWithAllowedContent() {
        Document clean = cleaner.clean(dirtyDocument);
        assertEquals(dirtyDocument.body().nodeName(), clean.body().nodeName());
        assertEquals(4, clean.body().childNodes().size());
        Element testDiv = clean.selectFirst("div#test");
        assertEquals("Hello World", testDiv.selectFirst("p text").text());
        assertEquals(3, testDiv.childNodes().size());
        assertSame(testDiv, clean.selectFirst("div#test"));
    }

    @Test
    public void testCleanDocumentWithDisallowedContent() {
        Whitelist whitelist = new Whitelist.SimpleTagBasedWhitelist();
        whitelist.addTags("p", "div");
        cleaner = new Cleaner(whitelist);

        Document clean = cleaner.clean(dirtyDocument);
        assertEquals(dirtyDocument.body().nodeName(), clean.body().nodeName());
        assertEquals(4, clean.body().childNodes().size());
        Element testDiv = clean.selectFirst("div#test");
        assertNull(testDiv);
    }

    @Test
    public void testIsValidNullDocument() {
        assertFalse(cleaner.isValid(null));
    }

    @Test
    public void testIsValidEmptyDocument() {
        Document emptyDocument = Document.createShell("http://example.com");
        assertTrue(cleaner.isValid(emptyDocument));
    }

    @Test
    public void testIsValidDocumentWithAllowedContent() {
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidDocumentWithDisallowedContent() {
        Whitelist whitelist = new Whitelist.SimpleTagBasedWhitelist();
        whitelist.addTags("p", "div");
        cleaner = new Cleaner(whitelist);

        Document dirtyDocumentWithScript = Parser.parser().parse("<html><body><div id='test'><p>Hello <b>World</b></p><script>alert('XSS');</script></div></body></html>");
        assertFalse(cleaner.isValid(dirtyDocumentWithScript));
    }
}

class ElementMeta {
    // empty for this test
}