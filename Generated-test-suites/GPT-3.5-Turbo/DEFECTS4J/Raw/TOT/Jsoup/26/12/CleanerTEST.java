
package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean_validBodyFragment_cleanedDocument() {
        String dirtyHtml = "<p>This is a <i>dirty</i> HTML fragment.</p>";
        Document dirtyDocument = Jsoup.parseBodyFragment(dirtyHtml);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        String expectedHtml = "<html><head></head><body><p>This is a <i>dirty</i> HTML fragment.</p></body></html>";
        assertEquals(expectedHtml, cleanDocument.outerHtml());
    }

    @Test
    public void testClean_emptyBodyFragment_cleanedDocumentWithEmptyBody() {
        String dirtyHtml = "";
        Document dirtyDocument = Jsoup.parseBodyFragment(dirtyHtml);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        String expectedHtml = "<html><head></head><body></body></html>";
        assertEquals(expectedHtml, cleanDocument.outerHtml());
    }

    @Test
    public void testIsValid_validBodyFragment_true() {
        String dirtyHtml = "<p>This is a valid HTML fragment.</p>";
        Document dirtyDocument = Jsoup.parseBodyFragment(dirtyHtml);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValid_invalidBodyFragment_false() {
        String dirtyHtml = "<p>This is an <script>unsafe</script> HTML fragment.</p>";
        Document dirtyDocument = Jsoup.parseBodyFragment(dirtyHtml);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes_validElement_copyNodesToDestination() {
        Element sourceElement = new Element(Tag.valueOf("div"), "");
        Element destElement = new Element(Tag.valueOf("div"), "");

        int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);

        assertEquals(0, numDiscarded);
        assertEquals("<div></div>", destElement.outerHtml());
    }

    @Test
    public void testCopySafeNodes_invalidElement_copyNodesButDiscardUnsafeElements() {
        Element sourceElement = new Element(Tag.valueOf("script"), "");
        Element destElement = new Element(Tag.valueOf("div"), "");

        int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);

        assertEquals(1, numDiscarded);
        assertEquals("<div></div>", destElement.outerHtml());
    }

    @Test
    public void testCreateSafeElement_validElementWithSafeAttributes_createSafeElement() {
        Element sourceElement = new Element(Tag.valueOf("a"), "");
        sourceElement.attr("href", "http://www.example.com");
        sourceElement.attr("target", "_blank");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        Element destElement = elementMeta.el;
        assertEquals("a", destElement.tagName());
        assertEquals("http://www.example.com", destElement.attr("href"));
        assertEquals("_blank", destElement.attr("target"));
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_validElementWithUnsafeAttribute_createSafeElementButDiscardUnsafeAttribute() {
        Element sourceElement = new Element(Tag.valueOf("a"), "");
        sourceElement.attr("href", "javascript:alert('XSS')");
        sourceElement.attr("target", "_blank");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        Element destElement = elementMeta.el;
        assertEquals("a", destElement.tagName());
        assertFalse(destElement.hasAttr("href"));
        assertEquals("_blank", destElement.attr("target"));
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }
    
    @Test
    public void testCreateSafeElement_invalidElement_createSafeElementWithoutAttributes() {
        Element sourceElement = new Element(Tag.valueOf("script"), "");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        Element destElement = elementMeta.el;
        assertEquals("script", destElement.tagName());
        assertEquals(0, destElement.attributes().size());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}
