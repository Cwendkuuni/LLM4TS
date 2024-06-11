package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {

    @Test
    public void testCleanerInstance() {
        Whitelist whitelist = new Whitelist(); // Assuming Whitelist is a valid class
        Cleaner cleaner = new Cleaner(whitelist);
        assertTrue(cleaner != null);
    }

    @Test
    public void testCleanDocument() {
        Whitelist whitelist = new Whitelist(); // Assuming Whitelist is a valid class
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.parse("<html><body><h1 id='title'>Test</h1><script>alert('XSS')</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertFalse(cleanDocument.body() instanceof Document); // cleanDocument should not be a Document
        assertNull(cleanDocument.body().childNodes().get(0) instanceof ScriptElement); // script tag should be removed
        assertTrue(cleanDocument.body().childNodes().get(0) instanceof Element); // h1 tag should remain
    }

    @Test
    public void testIsValidDocument() {
        Whitelist whitelist = new Whitelist(); // Assuming Whitelist is a valid class
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.parse("<html><body><h1 id='title'>Test</h1></body></html>"); // Valid document
        assertTrue(cleaner.isValid(dirtyDocument));

        Document invalidDocument = Document.parse("<html><body><h1 id='title'>Test</h1><script>alert('XSS')</script></body></html>"); // Invalid document with script tag
        assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist(); // Assuming Whitelist is a valid class
        Cleaner cleaner = new Cleaner(whitelist);

        Element root = new Element(Tag.HTML, null);
        Element invalidSourceEl = new Element(Tag.SCRIPT, null);
        Element validSourceEl = new Element(Tag.H1, null, new Attributes().put("id", "title"));
        root.appendChild(validSourceEl);
        root.appendChild(invalidSourceEl);

        Element destination = new Element(Tag.BODY, null);

        int numDiscarded = cleaner.copySafeNodes(root, destination);
        List<Node> destinationChildren = destination.childNodes();

        assertTrue(destinationChildren.size() == 1); // Only valid tag should be copied
        assertTrue(destinationChildren.get(0) instanceof Element); // Valid tag should remain as Element
        assertFalse(destinationChildren.get(0) instanceof Document); // Valid tag should not be a Document
        assertNull(destinationChildren.get(0).childNodes().get(0) instanceof ScriptElement); // script tag should be removed
    }
}