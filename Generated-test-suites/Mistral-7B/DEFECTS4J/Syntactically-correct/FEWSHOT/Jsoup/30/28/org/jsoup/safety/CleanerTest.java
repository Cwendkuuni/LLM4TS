package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;

public class CleanerTest {
    private Whitelist whitelist;
    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setUp() {
        whitelist = mock(Whitelist.class);
        cleaner = new Cleaner(whitelist);

        dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().appendElement(new Element(Tag.BODY));
        dirtyDocument.body().appendElement(new Element("div", "mydiv"));
        dirtyDocument.body().appendElement(new TextNode("This is a test."));
    }

    @Test
    public void testCleanDocument() {
        Document cleanedDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanedDocument != null);
        assertTrue(cleanedDocument.body() != null);
        assertTrue(cleanedDocument.body().childNodeSize() == 1);
        assertTrue(cleanedDocument.body().childNode(0) instanceof Element);
        assertTrue(cleanedDocument.body().childNode(0).tagName().equals("div"));
    }

    @Test
    public void testIsValidDocument() {
        when(whitelist.isSafeTag("div")).thenReturn(true);
        when(whitelist.isSafeAttribute("div", dirtyDocument, new Attribute("class", "mydiv"))).thenReturn(true);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValidDocumentWithUnsafeAttribute() {
        when(whitelist.isSafeTag("div")).thenReturn(true);
        when(whitelist.isSafeAttribute("div", dirtyDocument, new Attribute("class", "mydiv"))).thenReturn(true);
        when(whitelist.isSafeAttribute("div", dirtyDocument, new Attribute("xss", ""))).thenReturn(false);

        dirtyDocument.body().childNode(0).attributes().put("xss", "");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodesWithSafeElement() {
        when(whitelist.isSafeTag("div")).thenReturn(true);
        when(whitelist.isSafeAttribute("div", dirtyDocument, new Attribute("class", "mydiv"))).thenReturn(true);

        Document cleanDocument = Document.createShell("http://example.com");
        Element cleanElement = cleanDocument.body().appendElement(new Element("div", "cleandiv"));

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body().get(0), cleanElement);

        assertTrue(numDiscarded == 0);
        assertTrue(cleanElement.text().equals("This is a test."));
    }

    @Test
    public void testCopySafeNodesWithUnsafeElement() {
        when(whitelist.isSafeTag("div")).thenReturn(false);

        Document cleanDocument = Document.createShell("http://example.com");
        Element cleanElement = cleanDocument.body().appendElement(new Element("div", "cleandiv"));

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body().get(0), cleanElement);

        assertTrue(numDiscarded > 0);
        assertTrue(cleanElement.childNodeSize() == 0);
    }

    @Test
    public void testCopySafeNodesWithTextNode() {
        when(whitelist.isSafeTag("div")).thenReturn(true);
        when(whitelist.isSafeAttribute("div", dirtyDocument, new Attribute("class", "mydiv"))).thenReturn(true);

        Document cleanDocument = Document.createShell("http://example.com");
        Element cleanElement = cleanDocument.body().appendElement(new Element("div", "cleandiv"));

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body().get(2), cleanElement);

        assertTrue(numDiscarded == 0);
        assertTrue(cleanElement.childNode(0) instanceof TextNode);
        assertTrue(cleanElement.childNode(0).getWholeText().equals("This is a test."));
    }
}