package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.List;

public class CleanerTest {

    @Test
    public void testCreateCleaner() {
        Whitelist whitelist = new Whitelist(); // or any other instance of Whitelist
        Cleaner cleaner = new Cleaner(whitelist);
        assertNull(cleaner.whitelist); // whitelist should be set in constructor
    }

    @Test
    public void testCleanDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.newDocument();
        dirtyDocument.body().appendChild(new Element("div").text("Invalid HTML"));

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals(0, cleanDocument.body().childNodes().size());
    }

    @Test
    public void testIsValidDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.newDocument();
        dirtyDocument.body().appendChild(new Element("div").tagName("script").text("alert('XSS')"));

        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

    @Test
    public void testIsValidDocumentWithEmptyInput() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.newDocument();

        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testIsValidDocumentWithAllowedInput() {
        Whitelist whitelist = new Whitelist().addTag("div");
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.newDocument();
        dirtyDocument.body().appendChild(new Element("div").text("Allowed HTML"));

        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist().addTag("div").addAttribute("class", true);
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.newDocument();
        Element root = dirtyDocument.body().appendChild(new Element("div").tagName("div").text("Hello World").classes("test"));

        Element destination = new Element("div");

        int numDiscarded = cleaner.copySafeNodes(root, destination);

        Element destChild = destination.childNodes().get(0);
        assertEquals("Hello World", destChild.text());
        assertEquals("test", destChild.classes().get(0));
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCopySafeNodesWithEmptyInput() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element root = new Element("div");
        Element destination = new Element("div");

        int numDiscarded = cleaner.copySafeNodes(root, destination);

        assertEquals(0, destination.childNodes().size());
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodesWithDisallowedInput() {
        Whitelist whitelist = new Whitelist().addTag("div");
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.newDocument();
        Element root = dirtyDocument.body().appendChild(new Element("script").text("alert('XSS')"));

        Element destination = new Element("div");

        int numDiscarded = cleaner.copySafeNodes(root, destination);

        assertEquals(1, numDiscarded);
        assertTrue(destination.childNodes().isEmpty());
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist().addTag("div").addAttribute("class", true);

        Element sourceEl = new Element("div").tagName("div").classes("test").text("Hello World");
        ElementMeta meta = new Cleaner().createSafeElement(sourceEl);

        Element destChild = meta.el;
        assertEquals("Hello World", destChild.text());
        assertEquals("test", destChild.classes().get(0));
        assertEquals(0, meta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElementWithDisallowedAttributes() {
        Whitelist whitelist = new Whitelist().addTag("div").addAttribute("class", true).addAttribute("onclick", false);

        Element sourceEl = new Element("div").tagName("div").classes("test").attributes(new Attributes().put("onclick", "alert('XSS')"));
        ElementMeta meta = new Cleaner().createSafeElement(sourceEl);

        Element destChild = meta.el;
        assertTrue(destination.childNodes().isEmpty());
        assertEquals(1, meta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElementWithEmptyInput() {
        Whitelist whitelist = new Whitelist();

        Element sourceEl = new Element("div");
        ElementMeta meta = new Cleaner().createSafeElement(sourceEl);

        Element destChild = meta.el;
        assertTrue(destination.childNodes().isEmpty());
        assertEquals(0, meta.numAttribsDiscarded);
    }
}