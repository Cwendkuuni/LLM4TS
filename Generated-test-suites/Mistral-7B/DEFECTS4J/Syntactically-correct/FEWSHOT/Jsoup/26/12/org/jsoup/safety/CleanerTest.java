package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

public class CleanerTest {

    @Test
    public void testCreateCleaner() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Validate.notNull(cleaner);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateCleanerWithNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.parse("<html><body><p>foo<script>bar()</script></p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><p></p></body></html>", cleanDocument.body().html());
        assertTrue(cleanDocument.body().head() == null);
    }

    @Test
    public void testCleanWithEmptyDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.parse("");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("", cleanDocument.body().html());
        assertTrue(cleanDocument.body().head() == null);
    }

    @Test
    public void testCleanWithValidDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.parse("<html><body><p>foo</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><p>foo</p></body></html>", cleanDocument.body().html());
        assertTrue(cleanDocument.body().head() == null);
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.parse("<html><body><p>foo</p></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);

        Document dirtyDocumentWithScript = Document.parse("<html><body><p>foo<script>bar()</script></p></body></html>");
        boolean isValidWithScript = cleaner.isValid(dirtyDocumentWithScript);
        assertFalse(isValidWithScript);
    }

    @Test
    public void testIsValidWithEmptyDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.parse("");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodesWithEmptySource() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = Element.parse("<html></html>");
        Element dest = new Element(Tag.valueOf("html"), null);

        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodesWithEmptyDestination() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = Element.parse("<html><body></body></html>");
        Element dest = null;

        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertNull(numDiscarded);
    }

    @Test
    public void testCopySafeNodesWithValidSource() {
        Whitelist whitelist = new Whitelist();
        Whitelist tagWhitelist = new Whitelist();
        tagWhitelist.addTag("p");
        whitelist.setTagFilter(tagWhitelist);

        Cleaner cleaner = new Cleaner(whitelist);

        Element source = Element.parse("<html><body><p id=\"foo\">foo</p><p id=\"bar\">bar</p></body></html>");
        Element dest = new Element(Tag.valueOf("html"), null);

        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(0, numDiscarded);

        Element pFoo = dest.selectFirst("p#foo");
        assertEquals("foo", pFoo.text());

        Element pBar = dest.selectFirst("p#bar");
        assertEquals("bar", pBar.text());
    }

    @Test
    public void testCopySafeNodesWithInvalidSource() {
        Whitelist whitelist = new Whitelist();

        Cleaner cleaner = new Cleaner(whitelist);

        Element source = Element.parse("<html><body><p id=\"foo\">foo<script>bar()</script></p></body></html>");
        Element dest = new Element(Tag.valueOf("html"), null);

        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(1, numDiscarded);

        Element pFoo = dest.select("p#foo");
        assertNull(pFoo);
    }

    @Test
    public void testCreateSafeElementWithEmptySource() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = Element.parse("<html></html>");
        ElementMeta meta = cleaner.createSafeElement(source);

        assertEquals(0, meta.numAttribsDiscarded);
        assertNull(meta.el);
    }

    @Test
    public void testCreateSafeElementWithValidSource() {
        Whitelist whitelist = new Whitelist();
        Whitelist tagWhitelist = new Whitelist();
        tagWhitelist.addTag("p");
        whitelist.setTagFilter(tagWhitelist);

        Cleaner cleaner = new Cleaner(whitelist);

        Element source = Element.parse("<html><body><p id=\"foo\">foo</p></body></html>");
        ElementMeta meta = cleaner.createSafeElement(source);

        assertEquals("html", meta.el.tagName());
        assertEquals(0, meta.numAttribsDiscarded);

        assertEquals("foo", meta.el.selectFirst("p").text());
    }

    @Test
    public void testCreateSafeElementWithInvalidSource() {
        Whitelist whitelist = new Whitelist();

        Cleaner cleaner = new Cleaner(whitelist);

        Element source = Element.parse("<html><body><p id=\"foo\">foo<script>bar()</script></p></body></html>");
        ElementMeta meta = cleaner.createSafeElement(source);

        assertEquals(1, meta.numAttribsDiscarded);
        assertNull(meta.el);
    }

    @Test
    public void testCopySafeNodesWithInvalidSourceChild() {
        Whitelist whitelist = new Whitelist();

        Cleaner cleaner = new Cleaner(whitelist);

        Element source = Element.parse("<html><body><script>alert('Hello World');</script></body></html>");
        Element dest = new Element(Tag.valueOf("html"), null);

        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(1, numDiscarded);

        Element script = dest.selectFirst("script");
        assertNull(script);
    }

    @Test
    public void testCopySafeNodesWithValidSourceChild() {
        Whitelist whitelist = new Whitelist();
        Whitelist tagWhitelist = new Whitelist();
        tagWhitelist.addTag("p");
        whitelist.setTagFilter(tagWhitelist);

        Cleaner cleaner = new Cleaner(whitelist);

        Element source = Element.parse("<html><body><script><p>foo</p></script></body></html>");
        Element dest = new Element(Tag.valueOf("html"), null);

        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(1, numDiscarded);

        Element p = dest.selectFirst("p");
        assertEquals("foo", p.text());
    }

    private static class WhitelistTest {
        @Test
        public void testIsSafeTag() {
            Whitelist whitelist = new Whitelist();

            assertTrue(whitelist.isSafeTag("p"));
            assertTrue(whitelist.isSafeTag("div"));
            assertFalse(whitelist.isSafeTag("script"));
        }

        @Test
        public void testIsSafeAttribute() {
            Whitelist whitelist = new Whitelist();

            assertTrue(whitelist.isSafeAttribute("p", null, "class"));
            assertTrue(whitelist.isSafeAttribute("p", null, "id"));
            assertFalse(whitelist.isSafeAttribute("p", null, "onclick"));
        }

        @Test
        public void testGetEnforcedAttributes() {
            Whitelist whitelist = new Whitelist();

            List<Attribute> attributes = new ArrayList<>();
            attributes.add(Attribute.parse("class", "foo"));
            attributes.add(Attribute.parse("id", "bar"));

            assertEquals(attributes, whitelist.getEnforcedAttributes("p"));
        }
    }

    @Test
    public void testWhitelist() {
        new WhitelistTest();
    }
}