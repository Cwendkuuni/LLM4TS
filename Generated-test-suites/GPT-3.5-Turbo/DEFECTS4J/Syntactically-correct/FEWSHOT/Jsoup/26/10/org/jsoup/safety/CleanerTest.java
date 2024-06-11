package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(new Whitelist());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCleanNullDocument() {
        cleaner.clean(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidNullDocument() {
        cleaner.isValid(null);
    }

    @Test
    public void testClean() {
        Document dirtyDocument = createDirtyDocument();
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals(1, cleanDocument.childNodeSize());
        assertTrue(cleanDocument.childNode(0) instanceof Element);
        Element body = (Element) cleanDocument.childNode(0);

        assertEquals(2, body.childNodeSize());
        assertTrue(body.childNode(0) instanceof Element);
        assertTrue(body.childNode(1) instanceof TextNode);
        Element div = (Element) body.childNode(0);
        TextNode text = (TextNode) body.childNode(1);

        assertEquals("div", div.tagName());
        assertEquals("Hello world!", text.text());
        assertEquals(1, div.childNodeSize());
        assertTrue(div.childNode(0) instanceof TextNode);
        TextNode childText = (TextNode) div.childNode(0);
        assertEquals("This is a test.", childText.text());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = createDirtyDocument();

        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidWithUnsafeTag() {
        Document dirtyDocument = createDirtyDocumentWithUnsafeTag();

        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodesWithSafeTag() {
        Element dirtyElement = new Element("div");
        TextNode textNode = new TextNode("This is a test.");
        dirtyElement.appendChild(textNode);

        Element dest = new Element(Tag.valueOf("div"), "", new Attributes());

        int numDiscarded = cleaner.copySafeNodes(dirtyElement, dest);

        assertEquals(0, numDiscarded);
        assertEquals(1, dest.childNodeSize());
        assertTrue(dest.childNode(0) instanceof TextNode);
        TextNode destText = (TextNode) dest.childNode(0);
        assertEquals("This is a test.", destText.text());
    }

    @Test
    public void testCopySafeNodesWithUnsafeTag() {
        Element dirtyElement = new Element("script");
        TextNode textNode = new TextNode("alert('XSS attack!');");
        dirtyElement.appendChild(textNode);

        Element dest = new Element(Tag.valueOf("div"), "", new Attributes());

        int numDiscarded = cleaner.copySafeNodes(dirtyElement, dest);

        assertEquals(1, numDiscarded);
        assertEquals(0, dest.childNodeSize());
    }

    @Test
    public void testCreateSafeElementWithSafeAttribute() {
        Element sourceEl = new Element("a");
        sourceEl.attr("href", "http://www.example.com");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        Element dest = elementMeta.el;
        assertEquals("a", dest.tagName());
        assertEquals(1, dest.attributes().size());
        Attribute attr = dest.attributes().get("href");
        assertEquals("http://www.example.com", attr.getValue());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElementWithUnsafeAttribute() {
        Element sourceEl = new Element("a");
        sourceEl.attr("onclick", "alert('XSS attack!');");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        Element dest = elementMeta.el;
        assertEquals("a", dest.tagName());
        assertEquals(0, dest.attributes().size());
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }

    private Document createDirtyDocument() {
        Document dirtyDocument = new Document("");
        Element body = new Element("body");
        Element div = new Element("div");
        TextNode text = new TextNode("Hello world!");
        TextNode childText = new TextNode("This is a test.");

        dirtyDocument.appendChild(body);
        body.appendChild(div);
        body.appendChild(text);
        div.appendChild(childText);

        return dirtyDocument;
    }

    private Document createDirtyDocumentWithUnsafeTag() {
        Document dirtyDocument = new Document("");
        Element body = new Element("body");
        Element script = new Element("script");
        TextNode text = new TextNode("alert('XSS attack!');");

        dirtyDocument.appendChild(body);
        body.appendChild(script);
        script.appendChild(text);

        return dirtyDocument;
    }
}