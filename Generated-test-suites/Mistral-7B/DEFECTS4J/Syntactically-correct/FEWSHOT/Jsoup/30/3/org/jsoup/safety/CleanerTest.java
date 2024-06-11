package org.jsoup.safety;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Document dirtyDocument = Document.fromHtml("<html><body><p>foo<script>alert('xss')</script></p><div>bar</div></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);
        Document clean = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><p></p></body></html>", clean.html());
        assertNull(clean.body().childNode(1));
    }

    @Test
    public void testIsValid_ValidDocument() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Document dirtyDocument = Document.fromHtml("<html><body><p>foo</p><div>bar</div></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);

        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValid_InvalidDocument() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Document dirtyDocument = Document.fromHtml("<html><body><p>foo<script>alert('xss')</script></p></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);

        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Document dirtyDocument = Document.fromHtml("<html><body><p><span id='foo'>foo</span></p></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);
        Document clean = cleaner.clean(dirtyDocument);

        Element body = clean.body();
        assertEquals(1, body.childNodeCount());
        assertEquals("foo", body.childNode(0).text());
        assertNull(body.childNode(0).id());

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), body);
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Element sourceEl = new Element("span", "id", "foo");
        Cleaner cleaner = new Cleaner(whitelist);

        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        Element destChild = meta.el;

        assertEquals("span", destChild.tagName());
        assertNull(destChild.id());
        assertEquals(1, meta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_SafeElement() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Element sourceEl = new Element("p");
        Cleaner cleaner = new Cleaner(whitelist);

        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        Element destChild = meta.el;

        assertEquals("p", destChild.tagName());
        assertTrue(destChild.hasAttrs());
        assertTrue(destChild.hasText());
        assertEquals(0, meta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_SafeTagWithAttributes() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Element sourceEl = new Element("div", "class", "foo bar", "id", "id123");
        Cleaner cleaner = new Cleaner(whitelist);

        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        Element destChild = meta.el;

        assertEquals("div", destChild.tagName());
        assertTrue(destChild.hasAttr("class", "foo bar"));
        assertTrue(destChild.hasAttr("id", "id123"));
        assertEquals(0, meta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_UnsafeTag() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Element sourceEl = new Element("script");
        Cleaner cleaner = new Cleaner(whitelist);

        Element destChild = cleaner.createSafeElement(sourceEl).el;
        assertNull(destChild);
    }

    @Test
    public void testCreateSafeElement_UnsafeAttribute() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Element sourceEl = new Element("input", null, null, "onclick", "javascript:alert('xss')");
        Cleaner cleaner = new Cleaner(whitelist);

        Element destChild = cleaner.createSafeElement(sourceEl).el;
        assertNull(destChild);
    }
}