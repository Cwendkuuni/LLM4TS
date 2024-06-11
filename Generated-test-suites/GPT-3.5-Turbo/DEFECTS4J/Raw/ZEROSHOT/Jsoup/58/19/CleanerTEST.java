package org.jsoup.safety;

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
    private Whitelist whitelist;

    @Before
    public void setUp() {
        whitelist = Whitelist.relaxed();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Document.createShell("");
        Element body = new Element(Tag.valueOf("body"), "");
        dirtyDocument.appendChild(body);
        Element div = new Element(Tag.valueOf("div"), "");
        body.appendChild(div);
        div.text("Hello world");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertEquals("", cleanDocument.baseUri());
        assertTrue(cleanDocument.body().children().contains(div));
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Document.createShell("");
        Element body = new Element(Tag.valueOf("body"), "");
        dirtyDocument.appendChild(body);
        Element div = new Element(Tag.valueOf("div"), "");
        body.appendChild(div);
        div.text("Hello world");

        boolean valid = cleaner.isValid(dirtyDocument);

        assertTrue(valid);
    }

    @Test
    public void testCopySafeNodes() {
        Element source = new Element(Tag.valueOf("body"), "");
        Element dest = new Element(Tag.valueOf("body"), "");
        cleaner.copySafeNodes(source, dest);

        assertEquals(source.children().size(), dest.children().size());
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceElement = new Element(Tag.valueOf("div"), "");
        Attributes sourceAttrs = new Attributes();
        sourceAttrs.put("class", "test");
        sourceAttrs.put("id", "123");
        sourceElement.attr("data-attr", "attr-value");
        sourceElement.attributes().addAll(sourceAttrs);

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertNotNull(elementMeta.el);
        assertEquals(Tag.valueOf("div"), elementMeta.el.tagName());
        assertEquals(sourceElement.baseUri(), elementMeta.el.baseUri());
        assertEquals(0, elementMeta.numAttribsDiscarded);
        assertTrue(elementMeta.el.attributes().containsAll(sourceAttrs));
    }
}