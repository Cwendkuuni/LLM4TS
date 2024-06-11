package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

public class CleanerTest {

    @Test
    public void testCleaner() {
        Whitelist whitelist = new Whitelist(); // assuming Whitelist is a class with a no-arg constructor
        Cleaner cleaner = new Cleaner(whitelist);

        Element dirtyElement = new Element(Tag.valueOf("html"), "http://example.com");
        dirtyElement.appendText("Invalid content");

        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().appendChild(dirtyElement);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanDocument.body().text().isEmpty());
        assertFalse(cleanDocument.body().hasText());
        assertFalse(cleanDocument.body().hasChild(dirtyElement));
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist(); // assuming Whitelist is a class with a no-arg constructor
        Cleaner cleaner = new Cleaner(whitelist);

        Element dirtyElement1 = new Element(Tag.valueOf("p"), "http://example.com");
        dirtyElement1.className("allowed-class");
        dirtyElement1.setAttribute("data-allowed-attribute", "allowed-value");
        dirtyElement1.appendChild(new TextNode("Allowed content"));

        Element dirtyElement2 = new Element(Tag.valueOf("script"), "http://malicious-site.com");
        dirtyElement2.attributes().put("src", "malicious-script.js");

        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().appendChild(dirtyElement1);
        dirtyDocument.body().appendChild(dirtyElement2);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);

        Element cleanElement1 = new Element(Tag.valueOf("p"), "http://example.com");
        cleanElement1.className("allowed-class");
        cleanElement1.appendChild(new TextNode("Allowed content"));

        Document cleanDocument = Document.createShell("http://example.com");
        cleanDocument.body().appendChild(cleanElement1);

        assertSame(cleanElement1, cleaner.clean(dirtyDocument).body().getElementsByTag("p").first());
    }

    @Test
    public void testIsValidWithInvalidDocument() {
        Whitelist whitelist = new Whitelist(); // assuming Whitelist is a class with a no-arg constructor
        Cleaner cleaner = new Cleaner(whitelist);

        Element dirtyElement = new Element(Tag.valueOf("script"), "http://malicious-site.com");
        dirtyElement.setAttribute("src", "malicious-script.js");

        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().appendChild(dirtyElement);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNull(cleanDocument.body().getElementsByTag("script").first());
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist(); // assuming Whitelist is a class with a no-arg constructor
        Cleaner cleaner = new Cleaner(whitelist);

        Element dirtyRoot = new Element(Tag.valueOf("html"), "http://example.com");
        dirtyRoot.appendChild(new Element(Tag.valueOf("head"), "http://example.com"));
        dirtyRoot.appendChild(new Element(Tag.valueOf("body"), "http://example.com"));

        Element dirtyBody = dirtyRoot.body();
        dirtyBody.appendChild(new Element(Tag.valueOf("p"), "http://example.com")).text("Valid content");
        dirtyBody.appendChild(new Element(Tag.valueOf("script"), "http://malicious-site.com"));

        Element cleanRoot = new Element(Tag.valueOf("html"), "http://example.com");
        Element cleanBody = cleanRoot.body();

        int numDiscarded = cleaner.copySafeNodes(dirtyBody, cleanBody);

        assertEquals(1, numDiscarded);
        assertTrue(cleanBody.text().isEmpty());
        assertFalse(cleanBody.hasChild(dirtyBody.getElementsByTag("script").first()));
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist(); // assuming Whitelist is a class with a no-arg constructor

        Element sourceElement = new Element(Tag.valueOf("p"), "http://example.com")
                .className("allowed-class")
                .setAttribute("data-allowed-attribute", "allowed-value")
                .text("Allowed content");

        ElementMeta expected = new ElementMeta(sourceElement, 0);
        ElementMeta actual = cleaner.createSafeElement(sourceElement);

        assertEquals(expected.el.className(), actual.el.className());
        assertEquals(expected.el.tagName(), actual.el.tagName());
        assertEquals(expected.numAttribsDiscarded, actual.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElementWithInvalidAttribute() {
        Whitelist whitelist = new Whitelist(); // assuming Whitelist is a class with a no-arg constructor

        Element sourceElement = new Element(Tag.valueOf("p"), "http://example.com")
                .className("allowed-class")
                .setAttribute("data-allowed-attribute", "allowed-value")
                .setAttribute("data-disallowed-attribute", "disallowed-value")
                .text("Allowed content");

        ElementMeta expected = new ElementMeta(sourceElement, 1);
        ElementMeta actual = cleaner.createSafeElement(sourceElement);

        assertEquals(expected.el.className(), actual.el.className());
        assertEquals(expected.el.tagName(), actual.el.tagName());
        assertEquals(expected.numAttribsDiscarded, actual.numAttribsDiscarded + 1);
    }
}