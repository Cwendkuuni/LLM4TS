package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {

    @Test
    public void testCreateCleaner() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);
        assertEquals(whitelist, cleaner.whitelist);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateCleanerWithNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testCleanDocument() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);

        Element dirtyDocument = Document.newDocument().bodyElement();
        dirtyDocument.appendText("Hello <script>world</script>");
        Document cleanDocument = cleaner.clean(Document.newDocument().bodyElement());

        assertFalse(cleanDocument.body().hasText());
        assertFalse(cleanDocument.body().hasMetacdata());
        assertFalse(cleanDocument.body().hasChildren());

        Document originalCleanDocument = Document.newDocument().bodyElement();
        cleaner.clean(originalCleanDocument);
        assertSame(originalCleanDocument, cleanDocument);
    }

    @Test
    public void testCleanDocumentWithValidDocument() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);

        Element dirtyDocument = Document.newDocument().bodyElement();
        dirtyDocument.appendElement(new Element("p", null).text("Hello world"));
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("Hello world", cleanDocument.body().text());
        assertFalse(cleanDocument.body().hasMetacdata());
        assertFalse(cleanDocument.body().hasChildren());
    }

    @Test
    public void testIsValidDocument() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);

        Element dirtyDocument = Document.newDocument().bodyElement();
        dirtyDocument.appendElement(new Element("p", null).text("Hello world"));

        assertTrue(cleaner.isValid(dirtyDocument));

        dirtyDocument.appendElement(new Element("script", null).text("alert('XSS');"));
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = new Element("div", null).tagName("div");
        source.appendElement(new Element("p", null).text("Hello world"));
        source.appendElement(new Element("script", null).text("alert('XSS');"));

        Element dest = new Element("body", null).tagName("body");
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(1, numDiscarded);
        assertEquals(1, dest.childNodes().size());
        assertEquals("Hello world", dest.childNodes().get(0).text());
    }

    @Test
    public void testCopySafeNodesWithEmptyDocument() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = Document.newDocument().bodyElement();
        Element dest = new Element("body", null).tagName("body");
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(0, numDiscarded);
        assertEquals(0, dest.childNodes().size());
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();

        Element sourceEl = new Element("div", null).tagName("div");
        sourceEl.attr("id", "test");
        sourceEl.attr("title", "Test title");
        sourceEl.attr("data-test", "test");

        Element dest = new Element("div", null).tagName("div");
        ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertEquals("div", meta.el.tagName());
        assertEquals("id", meta.el.tagName());
        assertEquals("test", meta.el.attr("id"));
        assertEquals("test", meta.numAttribsDiscarded, 0);
        assertEquals(2, meta.el.attributeSize());
        assertEquals("title", meta.el.attr("title"));
        assertEquals("data-test", meta.el.attr("data-test"));
    }

    @Test
    public void testCreateSafeElementWithDiscardedAttributes() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();

        Element sourceEl = new Element("div", null).tagName("div");
        sourceEl.attr("id", "test");
        sourceEl.attr("title", "Test title");
        sourceEl.attr("data-test", "test");
        sourceEl.attr("style", "color: red; background-color: blue;");

        Element dest = new Element("div", null).tagName("div");
        ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertEquals("div", meta.el.tagName());
        assertEquals("id", meta.el.tagName());
        assertEquals("test", meta.el.attr("id"));
        assertEquals(2, meta.numAttribsDiscarded, 2);
        assertEquals(1, meta.el.attributeSize());
        assertNull(meta.el.attr("title"));
        assertNull(meta.el.attr("data-test"));
        assertNull(meta.el.attr("style"));
    }

    @Test
    public void testCreateSafeElementWithWhitelistedAttributes() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml()
                .addAttribute("div", "data-test", Function.constant("safe"));

        Element sourceEl = new Element("div", null).tagName("div");
        sourceEl.attr("id", "test");
        sourceEl.attr("title", "Test title");
        sourceEl.attr("data-test", "test");
        sourceEl.attr("style", "color: red; background-color: blue;");

        Element dest = new Element("div", null).tagName("div");
        ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertEquals("div", meta.el.tagName());
        assertEquals("id", meta.el.tagName());
        assertEquals("test", meta.el.attr("id"));
        assertEquals(1, meta.numAttribsDiscarded, 1);
        assertEquals(4, meta.el.attributeSize());
        assertEquals("test", meta.el.attr("data-test"));
        assertNull(meta.el.attr("title"));
        assertNull(meta.el.attr("style"));
    }

    @Test
    public void testCreateSafeElementWithEmptyAttributes() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();

        Element sourceEl = new Element("div", null).tagName("div");
        sourceEl.attributes().clear();

        Element dest = new Element("div", null).tagName("div");
        ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertEquals("div", meta.el.tagName());
        assertEquals(0, meta.el.attributeSize());
    }

    @Test
    public void testCreateSafeElementWithEmptyTags() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();

        Element sourceEl = new Element("div", null).tagName("div");

        Element dest = new Element("div", null).tagName("div");
        ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertEquals("div", meta.el.tagName());
        assertEquals(0, meta.el.childNodes().size());
    }

    @Test
    public void testCreateSafeElementWithChildren() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();

        Element sourceEl = new Element("div", null).tagName("div");
        sourceEl.appendElement(new Element("p", null).text("Hello world"));
        sourceEl.appendElement(new Element("script", null).text("alert('XSS');"));

        Element dest = new Element("div", null).tagName("div");
        ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertEquals("div", meta.el.tagName());
        assertEquals(1, meta.el.childNodes().size());
        assertEquals("p", meta.el.childNodes().get(0).tagName());
        assertEquals("Hello world", meta.el.childNodes().get(0).text());
        assertEquals(1, meta.numAttribsDiscarded, 1);
    }
}