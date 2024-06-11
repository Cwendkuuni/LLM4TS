package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

    private static final Whitelist WHITELIST = Whitelist.basic();

    @Test
    public void testClean() {
        Cleaner cleaner = new Cleaner(WHITELIST);
        Document dirtyDocument = new Document("");

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNull(cleanDocument.body());
    }

    @Test
    public void testIsValid() {
        Cleaner cleaner = new Cleaner(WHITELIST);
        Document dirtyDocument = new Document("");

        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes_WithSafeTag() {
        Cleaner cleaner = new Cleaner(WHITELIST);
        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");
        root.appendChild(destination);
        Element sourceElement = new Element(Tag.valueOf("span"), "");
        Attributes attributes = new Attributes();
        attributes.put("class", "safe");
        sourceElement.attributes().addAll(attributes);
        root.appendChild(sourceElement);

        int numDiscarded = cleaner.copySafeNodes(root, destination);

        assertEquals(0, numDiscarded);
        assertEquals(1, destination.childNodes().size());
    }

    @Test
    public void testCopySafeNodes_WithUnsafeTag() {
        Cleaner cleaner = new Cleaner(WHITELIST);
        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");
        root.appendChild(destination);
        Element sourceElement = new Element(Tag.valueOf("script"), "");
        Attributes attributes = new Attributes();
        attributes.put("src", "unsafe.js");
        sourceElement.attributes().addAll(attributes);
        root.appendChild(sourceElement);

        int numDiscarded = cleaner.copySafeNodes(root, destination);

        assertEquals(1, numDiscarded);
        assertEquals(0, destination.childNodes().size());
    }

    @Test
    public void testCopySafeNodes_WithTextNode() {
        Cleaner cleaner = new Cleaner(WHITELIST);
        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");
        root.appendChild(destination);
        TextNode sourceText = new TextNode("Hello World!", "");
        root.appendChild(sourceText);

        int numDiscarded = cleaner.copySafeNodes(root, destination);

        assertEquals(0, numDiscarded);
        assertEquals(1, destination.childNodes().size());
    }

    @Test
    public void testCreateSafeElement_WithSafeAttributes() {
        Cleaner cleaner = new Cleaner(WHITELIST);
        Element sourceElement = new Element(Tag.valueOf("a"), "");
        Attributes sourceAttributes = new Attributes();
        sourceAttributes.put("href", "http://example.com");
        sourceElement.attributes().addAll(sourceAttributes);

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals("a", elementMeta.el.tagName());
        assertEquals(0, elementMeta.numAttribsDiscarded);
        assertEquals(1, elementMeta.el.attributes().size());
    }

    @Test
    public void testCreateSafeElement_WithUnsafeAttributes_Discarded() {
        Cleaner cleaner = new Cleaner(WHITELIST);
        Element sourceElement = new Element(Tag.valueOf("a"), "");
        Attributes sourceAttributes = new Attributes();
        sourceAttributes.put("style", "color:red");
        sourceElement.attributes().addAll(sourceAttributes);

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals("a", elementMeta.el.tagName());
        assertEquals(1, elementMeta.numAttribsDiscarded);
        assertEquals(0, elementMeta.el.attributes().size());
    }

    @Test
    public void testCreateSafeElement_WithEnforcedAttributes() {
        Cleaner cleaner = new Cleaner(WHITELIST);
        Element sourceElement = new Element(Tag.valueOf("a"), "");
        Attributes sourceAttributes = new Attributes();
        sourceAttributes.put("href", "http://example.com");
        sourceElement.attributes().addAll(sourceAttributes);

        WHITELIST.addEnforcedAttribute("a", "target", "_blank");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals("a", elementMeta.el.tagName());
        assertEquals(0, elementMeta.numAttribsDiscarded);
        assertEquals(2, elementMeta.el.attributes().size());
    }
}