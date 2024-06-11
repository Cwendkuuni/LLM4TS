package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner.Whitelist;
import org.junit.Test;

import java.util.List;

public class CleanerTest {
    private Whitelist whitelist = Whitelist.basic();

    @Test
    public void testClean() {
        Cleaner cleaner = new Cleaner(whitelist);

        // Test Case 1: Empty Dirty Document
        Document dirtyDocument = Document.createShell("");
        Document clean = cleaner.clean(dirtyDocument);
        assertTrue(clean.body().childNodes().isEmpty());

        // Test Case 2: Dirty Document with Safe Content
        dirtyDocument = Document.createShell("");
        Element body = dirtyDocument.body();
        Element p = new Element(Tag.valueOf("p"), "");
        TextNode text = new TextNode("Hello World");
        body.appendChild(p);
        p.appendChild(text);

        clean = cleaner.clean(dirtyDocument);
        List<Node> cleanNodes = clean.body().childNodes();
        assertEquals(1, cleanNodes.size());
        assertTrue(cleanNodes.get(0) instanceof Element);
        Element cleanP = (Element) cleanNodes.get(0);
        assertEquals("p", cleanP.tagName());
        assertEquals("Hello World", cleanP.text());
    }

    @Test
    public void testIsValid() {
        Cleaner cleaner = new Cleaner(whitelist);

        // Test Case 1: Empty Dirty Document
        Document dirtyDocument = Document.createShell("");
        assertTrue(cleaner.isValid(dirtyDocument));

        // Test Case 2: Dirty Document with Safe Content
        dirtyDocument = Document.createShell("");
        Element body = dirtyDocument.body();
        Element p = new Element(Tag.valueOf("p"), "");
        TextNode text = new TextNode("Hello World");
        body.appendChild(p);
        p.appendChild(text);

        assertTrue(cleaner.isValid(dirtyDocument));

        // Test Case 3: Dirty Document with Unsafe Content
        dirtyDocument = Document.createShell("");
        body = dirtyDocument.body();
        Element script = new Element(Tag.valueOf("script"), "");
        script.text("alert('XSS')");
        body.appendChild(script);

        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Cleaner cleaner = new Cleaner(whitelist);

        // Test Case 1: Empty Source
        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = new Element(Tag.valueOf("div"), "");
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertTrue(dest.childNodes().isEmpty());
        assertEquals(0, numDiscarded);

        // Test Case 2: Source with Safe Elements
        source = new Element(Tag.valueOf("div"), "");
        Element p = new Element(Tag.valueOf("p"), "");
        TextNode text = new TextNode("Hello World");
        source.appendChild(p);
        p.appendChild(text);

        dest = new Element(Tag.valueOf("div"), "");
        numDiscarded = cleaner.copySafeNodes(source, dest);

        List<Node> destChildren = dest.childNodes();
        assertEquals(1, destChildren.size());
        assertTrue(destChildren.get(0) instanceof Element);
        Element destP = (Element) destChildren.get(0);
        assertEquals("p", destP.tagName());
        assertEquals("Hello World", destP.text());
        assertEquals(0, numDiscarded);

        // Test Case 3: Source with Unsafe Elements
        source = new Element(Tag.valueOf("div"), "");
        Element script = new Element(Tag.valueOf("script"), "");
        script.text("alert('XSS')");
        source.appendChild(script);

        dest = new Element(Tag.valueOf("div"), "");
        numDiscarded = cleaner.copySafeNodes(source, dest);

        destChildren = dest.childNodes();
        assertTrue(destChildren.isEmpty());
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Cleaner cleaner = new Cleaner(whitelist);

        // Test Case 1: Safe Source Element
        Element sourceEl = new Element(Tag.valueOf("p"), "");
        Attributes sourceAttrs = new Attributes();
        sourceAttrs.put("class", "content");
        sourceAttrs.put("style", "font-weight: bold");
        sourceEl.attributes().addAll(sourceAttrs);

        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);

        Element dest = meta.el;
        assertEquals("p", dest.tagName());
        assertEquals("content", dest.attr("class"));
        assertEquals("font-weight: bold", dest.attr("style"));
        assertEquals(0, meta.numAttribsDiscarded);

        // Test Case 2: Unsafe Source Element
        sourceEl = new Element(Tag.valueOf("script"), "");
        sourceAttrs = new Attributes();
        sourceAttrs.put("src", "http://example.com/script.js");
        sourceEl.attributes().addAll(sourceAttrs);

        meta = cleaner.createSafeElement(sourceEl);

        dest = meta.el;
        assertEquals("script", dest.tagName());
        assertFalse(dest.hasAttr("src"));
        assertEquals(1, meta.numAttribsDiscarded);
    }

}