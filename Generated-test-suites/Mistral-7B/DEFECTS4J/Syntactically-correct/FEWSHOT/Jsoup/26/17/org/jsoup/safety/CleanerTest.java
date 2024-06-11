package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.jsoup.Helper.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

public class CleanerTest {

    @Test
    public void testCleanerCreation() {
        Whitelist whitelist = mock(Whitelist.class);
        Cleaner cleaner = new Cleaner(whitelist);
        assertEquals(whitelist, cleaner.whitelist);
    }

    @Test(expected = NullPointerException.class)
    public void testCleanerCreationWithNullWhitelist() {
        new Cleaner(null);
    }

    @Test
    public void testCleanDocument() {
        Whitelist whitelist = setupMockWhitelist();

        Document dirtyDocument = setupMockDocument();
        Document cleanDocument = new Cleaner(whitelist).clean(dirtyDocument);

        assertEquals(dirtyDocument.body(), cleanDocument.body().getAllElements());
        assertEquals(dirtyDocument.baseUri(), cleanDocument.baseUri());
        assertNull(cleanDocument.head());
        assertNull(cleanDocument.title());
        assertNull(cleanDocument.body().getElementsByTag("script"));
    }

    @Test
    public void testIsValidDocument() {
        Whitelist whitelist = setupMockWhitelist();

        Document dirtyDocument = setupMockDocument();
        boolean isValid = new Cleaner(whitelist).isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValidDocumentWithInvalidDocument() {
        Whitelist whitelist = setupMockWhitelistWithInvalidTag();

        Document dirtyDocument = setupMockDocumentWithInvalidTag();
        boolean isValid = new Cleaner(whitelist).isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = setupMockWhitelist();
        Document dirtyDocument = setupMockDocument();
        Element dest = new Element(Tag.valueOf("div"), null, new Attributes());

        int numDiscarded = new Cleaner(whitelist).copySafeNodes(dirtyDocument.body(), dest);

        Element body = dirtyDocument.body();
        List<Element> elements = new ArrayList<>(Arrays.asList(body.children()));
        for (Element element : elements) {
            if (!(element instanceof TextNode)) {
                Element safeElement = new Cleaner(whitelist).createSafeElement(element);
                Element clonedSafeElement = safeElement.el;
                dest.appendChild(clonedSafeElement);
                assertEquals(safeElement.numAttribsDiscarded, numDiscarded--);
            }
        }
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = setupMockWhitelist();
        Element sourceElement = new Element(Tag.valueOf("p"), null, new Attributes().put("class", "test"));

        ElementMeta safeElement = new Cleaner(whitelist).createSafeElement(sourceElement);
        Element destElement = safeElement.el;

        assertEquals(Tag.valueOf("p"), destElement.tagName());
        assertEquals(1, destElement.childrenSize());
        assertEquals("test", destElement.classAttr());
        assertEquals(0, safeElement.numAttribsDiscarded);

        Attribute attribute = new Attribute("data-test", "value");
        safeElement.numAttribsDiscarded += 1;
        assertFalse(destElement.hasAttr("data-test"));
    }

    private Whitelist setupMockWhitelist() {
        Whitelist whitelist = mock(Whitelist.class);
        when(whitelist.isSafeTag(anyString())).thenReturn(true);
        when(whitelist.isSafeAttribute(anyString(), any(Element.class), any(Attribute.class))).thenReturn(true);
        when(whitelist.getEnforcedAttributes(anyString())).thenReturn(new Attributes());
        return whitelist;
    }

    private Whitelist setupMockWhitelistWithInvalidTag() {
        Whitelist whitelist = mock(Whitelist.class);
        when(whitelist.isSafeTag(eq("script"))).thenReturn(false);
        return whitelist;
    }

    private Document setupMockDocument() {
        Document document = Document.createShell("http://example.com");
        document.body().appendElement(new Element(Tag.valueOf("p"), null, new Attributes().put("class", "test")));
        document.body().appendElement(new TextNode("Test text."));
        return document;
    }

    private Document setupMockDocumentWithInvalidTag() {
        Document document = Document.createShell("http://example.com");
        document.body().appendElement(new Element(Tag.valueOf("script"), null, new Attributes()));
        return document;
    }
}