package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class CleanerTest {

    private static Whitelist getDefaultWhitelist() {
        return Whitelist.simpleText();
    }

    @Test
    public void testCreateCleaner() {
        Whitelist whitelist = getDefaultWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateCleanerNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testCleanNullDocument() {
        Cleaner cleaner = new Cleaner(getDefaultWhitelist());
        assertNull(cleaner.clean(null));
    }

    @Test
    public void testCleanEmptyDocument() {
        Cleaner cleaner = new Cleaner(getDefaultWhitelist());
        Document dirtyDocument = Jsoup.parseBodyFragment("");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals(0, cleanDocument.body().childNodeSize());
    }

    @Test
    public void testCleanSimpleDocument() {
        Cleaner cleaner = new Cleaner(getDefaultWhitelist());
        Document dirtyDocument = Jsoup.parseBodyFragment("<p>Hello World</p>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("p", cleanDocument.tagName());
        assertEquals("Hello World", cleanDocument.body().text());
    }

    @Test
    public void testCleanComplexDocument() {
        Cleaner cleaner = new Cleaner(getDefaultWhitelist());
        Document dirtyDocument = Jsoup.parseBodyFragment("<html><head><title>Title</title></head><body><p id='p1'>Text <a href='link'>Link</a></p><script>alert('Bad')</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("html", cleanDocument.tagName());
        assertEquals("head", cleanDocument.select("head").tagName());
        Element titleElement = cleanDocument.select("title").first();
        assertEquals("Title", titleElement.text());
        assertEquals("body", cleanDocument.select("body").tagName());
        assertEquals("p", cleanDocument.select("p").first().tagName());
        assertNull(cleanDocument.getElementsByTag("script").first());
        Element pElement = cleanDocument.select("p").first();
        assertEquals("Text ", pElement.text().substring(0, pElement.text().length() - 1));
        assertEquals("Link", pElement.select("a").text());
        assertEquals(1, pElement.id().hashCode());
    }

    @Test
    public void testIsValidNullDocument() {
        Cleaner cleaner = new Cleaner(getDefaultWhitelist());
        assertFalse(cleaner.isValid(null));
    }

    @Test
    public void testIsValidEmptyDocument() {
        Cleaner cleaner = new Cleaner(getDefaultWhitelist());
        Document dirtyDocument = Jsoup.parseBodyFragment("");
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidSimpleDocument() {
        Cleaner cleaner = new Cleaner(getDefaultWhitelist());
        Document dirtyDocument = Jsoup.parseBodyFragment("<p>Hello World</p>");
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidComplexDocument() {
        Cleaner cleaner = new Cleaner(getDefaultWhitelist());
        Document dirtyDocument = Jsoup.parseBodyFragment("<html><head><title>Title</title></head><body><p id='p1'>Text <a href='link'>Link</a></p><script>alert('Bad')</script></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidValidDocument() {
        Cleaner cleaner = new Cleaner(getDefaultWhitelist());
        Document dirtyDocument = Jsoup.parseBodyFragment("<html><head><title>Title</title></head><body><p id='p1'>Text <a href='link'>Link</a></p></body></html>");
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodesNullSource() {
        Cleaner cleaner = new Cleaner(getDefaultWhitelist());
        Element dest = new Element(Tag.BODY, "", new Attributes());
        int numDiscarded = cleaner.copySafeNodes(null, dest);
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodesEmptySource() {
        Cleaner cleaner = new Cleaner(getDefaultWhitelist());
        Element source = new Element(Tag.BODY, "", new Attributes());
        Element dest = new Element(Tag.BODY, "", new Attributes());
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodesSimpleSource() {
        Cleaner cleaner = new Cleaner(getDefaultWhitelist());
        Element source = new Element(Tag.P, "", new Attributes().put("id", "p1"));
        Element dest = new Element(Tag.BODY, "", new Attributes());
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(0, numDiscarded);
        assertEquals(1, dest.childNodeSize());
        assertEquals("p", dest.child(0).tagName());
    }

    @Test
    public void testCopySafeNodesDiscardedAttributes() {
        Cleaner cleaner = new Cleaner(getDefaultWhitelist());
        Element source = new Element(Tag.P, "", new Attributes().put("id", "p1").put("other", "attribute"));
        Element dest = new Element(Tag.BODY, "", new Attributes());
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(1, numDiscarded);
        assertEquals(0, dest.childNodeSize());
    }

    @Test
    public void testCopySafeNodesDiscardedTextNode() {
        Cleaner cleaner = new Cleaner(getDefaultWhitelist());
        Element source = new Element(Tag.SCRIPT, "", new Attributes());
        source.appendChild(new TextNode("alert('Bad')"));
        Element dest = new Element(Tag.BODY, "", new Attributes());
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(1, numDiscarded);
        assertEquals(0, dest.childNodeSize());
    }

    @Test
    public void testCopySafeNodesRecursive() {
        Cleaner cleaner = new Cleaner(getDefaultWhitelist());
        Element source = new Element(Tag.P, "", new Attributes().put("id", "p1"));
        Element child = new Element(Tag.A, "", new Attributes().put("href", "link"));
        child.appendChild(new TextNode("Link"));
        source.appendChild(child);
        Element dest = new Element(Tag.BODY, "", new Attributes());
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(0, numDiscarded);
        assertEquals(1, dest.childNodeSize());
        assertEquals("p", dest.child(0).tagName());
        assertEquals(1, dest.child(0).childNodeSize());
        assertEquals("a", dest.child(0).child(0).tagName());
        assertEquals("Link", dest.child(0).child(0).text());
    }
}