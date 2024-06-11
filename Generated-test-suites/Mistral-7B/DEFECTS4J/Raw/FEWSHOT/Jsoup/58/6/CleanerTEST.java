package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;

public class CleanerTest {

    private static Whitelist createWhitelist() {
        return Whitelist.simpleTextOnly();
    }

    @Test
    public void testNewCleaner() {
        Whitelist whitelist = createWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNull(cleaner.whitelist);
        assertSame(whitelist, cleaner.whitelist);
    }

    @Test
    public void testClean() {
        Whitelist whitelist = createWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><div>foo</div><script>alert('xss')</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertFalse(cleanDocument.body().hasText());
        assertTrue(cleanDocument.body().tagName().equals("body"));
        assertTrue(cleanDocument.body().children().isEmpty());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = createWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><div>foo</div></body></html>");
        boolean valid = cleaner.isValid(dirtyDocument);
        assertTrue(valid);

        dirtyDocument = Jsoup.parse("<html><body><div>foo<script>alert('xss')</script></div></body></html>");
        valid = cleaner.isValid(dirtyDocument);
        assertFalse(valid);
    }

    @Test
    public void testCleaningVisitor() {
        Whitelist whitelist = createWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><div id='foo'>foo <b>bar</b></div></body></html>");
        Element dest = new Element("body");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), dest);

        Element div = dest.child(0);
        assertEquals(2, div.childNodeSize());
        assertEquals("foo", div.child(0).text());
        assertEquals("bar", div.child(1).text());
        assertEquals(2, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = createWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceEl = new Element("div", "id", "foo");
        Element destEl = cleaner.createSafeElement(sourceEl);
        assertEquals("div", destEl.tagName());
        assertTrue(destEl.attributes().containsKey("id"));
        assertEquals("foo", destEl.attributes().get("id"));
        assertTrue(destEl.attributes().isEmpty());

        sourceEl = new Element("div", "id", "foo", "class", "foo-bar");
        destEl = cleaner.createSafeElement(sourceEl);
        assertEquals("div", destEl.tagName());
        assertTrue(destEl.attributes().containsKey("id"));
        assertEquals("foo", destEl.attributes().get("id"));
        assertTrue(destEl.attributes().containsKey("class"));
        assertEquals("foo-bar", destEl.attributes().get("class"));
    }

    @Test
    public void testIsSafeTag() {
        Whitelist whitelist = createWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Tag divTag = Tag.valueOf("div");
        assertTrue(cleaner.whitelist.isSafeTag(divTag.getName()));

        Tag scriptTag = Tag.valueOf("script");
        assertFalse(cleaner.whitelist.isSafeTag(scriptTag.getName()));
    }

    @Test
    public void testIsSafeAttribute() {
        Whitelist whitelist = createWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceEl = new Element("div", "id", "foo");
        Attributes sourceAttrs = sourceEl.attributes();
        Attribute idAttr = sourceAttrs.get("id");
        Attribute classAttr = new Attribute("class", "foo-bar");

        assertTrue(cleaner.whitelist.isSafeAttribute("div", sourceEl, idAttr));
        assertFalse(cleaner.whitelist.isSafeAttribute("div", sourceEl, classAttr));
    }

    @Test
    public void testGetEnforcedAttributes() {
        Whitelist whitelist = createWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Attributes enforcedAttrs = whitelist.getEnforcedAttributes("div");
        assertTrue(enforcedAttrs.isEmpty());

        enforcedAttrs = whitelist.getEnforcedAttributes("img");
        Attributes expectedEnforcedAttrs = new Attributes().putHTML("src", true).putHTML("alt", true);
        assertEquals(expectedEnforcedAttrs.toString(), enforcedAttrs.toString());
    }
}