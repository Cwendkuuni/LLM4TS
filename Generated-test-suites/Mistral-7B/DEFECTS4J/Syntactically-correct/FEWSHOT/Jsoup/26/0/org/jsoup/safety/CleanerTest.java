package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

public class CleanerTest {

    @Test
    public void testCleanerInstance() {
        Whitelist whitelist = new Whitelist(); // assuming Whitelist is initialized properly
        Cleaner cleaner = new Cleaner(whitelist);
        Validate.notNull(cleaner);
    }

    @Test
    public void testCleanEmptyDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.create();
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("", cleanDocument.body().html());
    }

    @Test
    public void testCleanDocumentWithSafeContent() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Element body = new Element("body");
        Element div1 = new Element("div").text("Test Content");
        body.appendChild(div1);
        Document dirtyDocument = new Document("", body);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("div\nTest Content", cleanDocument.body().html());
    }

    @Test
    public void testCleanDocumentWithUnsafeContent() {
        Whitelist whitelist = new Whitelist(); // assuming Whitelist is initialized properly with appropriate whitelist
        Cleaner cleaner = new Cleaner(whitelist);
        Element body = new Element("body");
        Element script = new Element("script").text("alert('XSS Attack!')");
        body.appendChild(script);
        Document dirtyDocument = new Document("", body);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("", cleanDocument.body().html());
    }

    @Test
    public void testIsValidDocumentWithSafeContent() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Element body = new Element("body");
        Element div1 = new Element("div").text("Test Content");
        body.appendChild(div1);
        Document dirtyDocument = new Document("", body);
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidDocumentWithUnsafeContent() {
        Whitelist whitelist = new Whitelist(); // assuming Whitelist is initialized properly with appropriate whitelist
        Cleaner cleaner = new Cleaner(whitelist);
        Element body = new Element("body");
        Element script = new Element("script").text("alert('XSS Attack!')");
        body.appendChild(script);
        Document dirtyDocument = new Document("", body);
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidDocumentWithEmptyDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.create();
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodesWithSafeContent() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceBody = new Element("body");
        Element sourceDiv = new Element("div").text("Test Content");
        sourceBody.appendChild(sourceDiv);
        Element destBody = new Element("body");

        int numDiscarded = cleaner.copySafeNodes(sourceBody, destBody);
        assertEquals(0, numDiscarded);
        assertEquals("div\nTest Content", destBody.html());
    }

    @Test
    public void testCopySafeNodesWithUnsafeContent() {
        Whitelist whitelist = new Whitelist(); // assuming Whitelist is initialized properly with appropriate whitelist
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceBody = new Element("body");
        Element script = new Element("script").text("alert('XSS Attack!')");
        sourceBody.appendChild(script);
        Element destBody = new Element("body");

        int numDiscarded = cleaner.copySafeNodes(sourceBody, destBody);
        assertEquals(1, numDiscarded);
        assertNull(destBody.childNodes());
    }

    @Test
    public void testCreateSafeElementWithSafeContent() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceEl = new Element("div").text("Test Content");
        Element destEl = cleaner.createSafeElement(sourceEl);

        assertEquals("div", destEl.tagName());
        assertEquals("Test Content", destEl.text());
        assertNull(destEl.attributes());
    }

    @Test
    public void testCreateSafeElementWithUnsafeContent() {
        Whitelist whitelist = new Whitelist(); // assuming Whitelist is initialized properly with appropriate whitelist
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceEl = new Element("div").text("Test Content").attributes().put("onclick", "alert('XSS Attack!')");
        Element destEl = cleaner.createSafeElement(sourceEl);

        assertEquals("div", destEl.tagName());
        assertEquals("", destEl.attributes().toString());
    }
}