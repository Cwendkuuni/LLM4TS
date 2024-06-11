package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

public class CleanerTest {
    @Test
    public void testCreateCleaner() {
        Whitelist whitelist = new Whitelist(); // assuming a valid Whitelist instance
        Cleaner cleaner = new Cleaner(whitelist);
        assertNull(cleaner.whitelist); // should copy the passed Whitelist instance
    }

    @Test
    public void testCleanNullDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNull(cleaner.clean(null));
    }

    @Test
    public void testCleanEmptyDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.createEmpty();
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleanDocument.body().text().isEmpty());
    }

    @Test
    public void testCleanWithAllowedElements() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.fromString("<html><body><p>Hello World</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals(1, cleanDocument.body().childNodeSize());
        assertTrue(cleanDocument.body().text().contains("Hello World"));
    }

    @Test
    public void testCleanWithDisallowedElements() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.fromString("<html><body><script>alert('XSS')</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleanDocument.body().text().isEmpty());
    }

    @Test
    public void testIsValidNullDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertFalse(cleaner.isValid(null));
    }

    @Test
    public void testIsValidEmptyDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.createEmpty();
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidAllowedElements() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.fromString("<html><body><p>Hello World</p></body></html>");
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidDisallowedElements() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.fromString("<html><body><script>alert('XSS')</script></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodesWithAllowedChildren() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.fromString("<html><body><p><strong>Hello World</strong></p></body></html>");
        Document cleanDocument = Document.createEmpty();
        int numDiscarded = copySafeNodes(dirtyDocument.body(), cleanDocument.body(), cleaner.whitelist);
        assertEquals(2, cleanDocument.body().childNodeSize());
        Element p = cleanDocument.body().get(0);
        Element strong = p.get(0);
        assertEquals("p", strong.tagName());
        assertEquals("strong", p.tagName());
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodesWithDisallowedChildren() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.fromString("<html><body><p><script>alert('XSS')</script></p></body></html>");
        Document cleanDocument = Document.createEmpty();
        int numDiscarded = copySafeNodes(dirtyDocument.body(), cleanDocument.body(), cleaner.whitelist);
        assertEquals(0, cleanDocument.body().childNodeSize());
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElementWithAllowedAttributes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceEl = new Element("div", null, new Attributes().put("class", "safe"));
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        Element destChild = meta.el;
        assertEquals("div", destChild.tagName());
        assertEquals(1, destChild.attributes().size());
        assertEquals("class", destChild.attributes().keySet().iterator().next());
        assertEquals("safe", destChild.classAttr());
        assertEquals(0, meta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElementWithDisallowedAttributes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceEl = new Element("div", null, new Attributes().put("xss", "alert('XSS')"));
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        assertNull(meta.el);
        assertEquals(1, meta.numAttribsDiscarded);
    }

    private static int copySafeNodes(Element root, Element destination, Whitelist whitelist) {
        List<Node> sourceChildren = root.childNodes();
        int numDiscarded = 0;

        for (Node source : sourceChildren) {
            // Implement test cases for all the cases in the copySafeNodes method
        }
        return numDiscarded;
    }
}