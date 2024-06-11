package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test class for Cleaner class
 */
public class CleanerTest {

    private Whitelist whitelist;
    private Cleaner cleaner;
    private Document dirtyDocument;
    private Document cleanDocument;

    @Before
    public void setUp() {
        whitelist = new BasicWhitelist();
        cleaner = new Cleaner(whitelist);
        dirtyDocument = Document.createFromString("<html><body>" +
                "<h1 id=\"title\" class=\"title\" onclick=\"alert('XSS')\">Title</h1>" +
                "<script src=\"malicious.js\"></script>" +
                "<p>Safe paragraph</p>" +
                "</body></html>", "http://example.com");
        cleanDocument = Document.createShell("http://example.com");
    }

    @Test
    public void testCleanerConstructor() {
        assertNotNull(cleaner);
    }

    @Test
    public void testCleanMethod() {
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body><p></p></body></html>", cleanedDocument.html());
    }

    @Test
    public void testIsValidMethod() {
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodesMethodWithSafeElements() {
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        assertEquals(2, numDiscarded); // h1 and script tags are discarded
        Element title = cleanDocument.selectFirst("h1");
        assertNotNull(title);
        Element paragraph = cleanDocument.selectFirst("p");
        assertNotNull(paragraph);
    }

    @Test
    public void testCopySafeNodesMethodWithUnsafeElements() {
        Document dirtyDocumentWithSafeTag = Document.createFromString("<html><body>" +
                "<div id=\"title\" class=\"title\" onclick=\"alert('XSS')\">Title</div>" +
                "</body></html>", "http://example.com");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocumentWithSafeTag.body(), cleanDocument.body());
        assertEquals(1, numDiscarded); // script tag is discarded
        Element title = cleanDocument.selectFirst("div");
        assertNotNull(title);
    }

    @Test(expected = NullPointerException.class)
    public void testCopySafeNodesMethodWithNullDocument() {
        cleaner.copySafeNodes(null, cleanDocument);
    }

    @Test(expected = NullPointerException.class)
    public void testCleanMethodWithNullDocument() {
        cleaner.clean(null);
    }

    @Test
    public void testIsValidMethodWithEmptyDocument() {
        Document emptyDocument = Document.createShell("http://example.com");
        assertFalse(cleaner.isValid(emptyDocument));
    }

    @Test
    public void testIsValidMethodWithValidDocument() {
        Document validDocument = Document.createFromString("<html><body><p></p></body></html>", "http://example.com");
        assertTrue(cleaner.isValid(validDocument));
    }

    @Test
    public void testIsValidMethodWithMalformedDocument() {
        Document malformedDocument = Document.createFromString("<html><body><p></p></body></html><script src=\"malicious.js\"></script>", "http://example.com");
        assertFalse(cleaner.isValid(malformedDocument));
    }

    @Test
    public void testCreateSafeElementMethod() {
        Element sourceEl = new Element("h1", "http://example.com");
        Attributes sourceAttrs = new Attributes().put("id", "title").put("class", "title").put("onclick", "alert('XSS')");
        sourceEl.attrMap().putAll(sourceAttrs);
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        Element dest = meta.el;
        assertEquals("h1", dest.tagName());
        assertEquals(0, meta.numAttribsDiscarded);
        assertNull(dest.attrMap().get("onclick"));
    }

    @Test
    public void testCreateSafeElementMethodWithUnsafeAttribute() {
        Element sourceEl = new Element("a", "http://example.com");
        Attributes sourceAttrs = new Attributes().put("href", "malicious.js");
        sourceEl.attrMap().putAll(sourceAttrs);
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        Element dest = meta.el;
        assertEquals("a", dest.tagName());
        assertEquals(1, meta.numAttribsDiscarded);
        assertNull(dest.attrMap().get("href"));
    }

    @Test
    public void testCreateSafeElementMethodWithSafeAttribute() {
        Element sourceEl = new Element("a", "http://example.com");
        Attributes sourceAttrs = new Attributes().put("class", "link");
        sourceEl.attrMap().putAll(sourceAttrs);
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        Element dest = meta.el;
        assertEquals("a", dest.tagName());
        assertEquals(0, meta.numAttribsDiscarded);
        assertEquals("link", dest.attrMap().get("class"));
    }

    @Test
    public void testCreateSafeElementMethodWithEmptyAttributes() {
        Element sourceEl = new Element("a", "http://example.com");
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        Element dest = meta.el;
        assertEquals("a", dest.tagName());
        assertEquals(0, meta.numAttribsDiscarded);
        assertNull(dest.attrMap());
    }

    @Test
    public void testCopySafeNodesMethodWithTextNode() {
        String text = "Safe text";
        Document dirtyDocumentWithText = Document.createFromString("<html><body>" +
                "<p>" + text + "</p>" +
                "</body></html>", "http://example.com");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocumentWithText.body(), cleanDocument.body());
        assertEquals(0, numDiscarded);
        TextNode destText = (TextNode) cleanDocument.body().child(0);
        assertEquals(text, destText.getWholeText());
    }
}