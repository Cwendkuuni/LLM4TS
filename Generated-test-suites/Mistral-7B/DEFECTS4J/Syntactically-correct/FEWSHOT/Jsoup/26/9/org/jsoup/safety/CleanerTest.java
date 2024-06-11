package org.jsoup.safety;

import static org.junit.Assert.*;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {
    @Test
    public void testCreateCleaner() {
        // Test creating a cleaner with a valid whitelist
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateCleanerNullWhitelist() {
        // Test creating a cleaner with a null whitelist
        Whitelist whitelist = null;
        Cleaner cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testCleanDocument() {
        // Test cleaning a document with a simple whitelist
        Whitelist whitelist = Whitelist.simpleText();
        Document dirtyDocument = Document.parse("<html><body><div id='test'>Hello, World!</div></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body></body></html>", cleanDocument.body().html());

        // Test cleaning a document with a more complex whitelist
        Whitelist complexWhitelist = new Whitelist.Builder()
                .addTags("p", "div")
                .addAttributes("class", "id")
                .build();
        Document complexDirtyDocument = Document.parse("<html><body><p id='test' class='test'>Hello, <script>World!</script></p></body></html>");
        Cleaner complexCleaner = new Cleaner(complexWhitelist);
        Document complexCleanDocument = complexCleaner.clean(complexDirtyDocument);
        assertEquals("<html><body><p class='test'></p></body></html>", complexCleanDocument.body().html());
    }

    @Test
    public void testIsValidDocument() {
        // Test validating a document with a simple whitelist
        Whitelist whitelist = Whitelist.simpleText();
        Document dirtyDocument = Document.parse("<html><body><div id='test'>Hello, World!</div></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);
        assertTrue(cleaner.isValid(dirtyDocument));

        // Test validating a document with a more complex whitelist
        Whitelist complexWhitelist = new Whitelist.Builder()
                .addTags("p", "div")
                .addAttributes("class", "id")
                .build();
        Document complexDirtyDocument = Document.parse("<html><body><p id='test' class='test'><script>Hello, World!</script></p></body></html>");
        Cleaner complexCleaner = new Cleaner(complexWhitelist);
        assertFalse(complexCleaner.isValid(complexDirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        // Test copying safe nodes from a simple source element
        Whitelist whitelist = Whitelist.simpleText();
        Document dirtyDocument = Document.parse("<html><body><div id='test'>Hello, World!</div></body></html>");
        Element sourceElement = dirtyDocument.selectFirst("div");
        Element destElement = new Element(Tag.DIV);
        int numDiscarded = Cleaner.copySafeNodes(sourceElement, destElement);
        assertEquals(0, numDiscarded);

        // Test copying safe nodes from a more complex source element
        Whitelist complexWhitelist = new Whitelist.Builder()
                .addTags("p", "div")
                .addAttributes("class", "id")
                .build();
        Document complexDirtyDocument = Document.parse("<html><body><p id='test' class='test'><script>Hello, World!</script></p></body></html>");
        Element complexSourceElement = complexDirtyDocument.selectFirst("p");
        Element complexDestElement = new Element(Tag.P);
        numDiscarded = Cleaner.copySafeNodes(complexSourceElement, complexDestElement);
        assertEquals(1, numDiscarded);

        // Test copying safe nodes from a source element with children
        Document nestedDirtyDocument = Document.parse("<html><body><div id='test'><p>Hello, World!</p></div></body></html>");
        Element nestedSourceElement = nestedDirtyDocument.selectFirst("div");
        Element nestedDestElement = new Element(Tag.DIV);
        numDiscarded = Cleaner.copySafeNodes(nestedSourceElement, nestedDestElement);
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        // Test creating a safe element with a simple source element
        Whitelist whitelist = Whitelist.simpleText();
        Document dirtyDocument = Document.parse("<html><body><div id='test'>Hello, World!</div></body></html>");
        Element sourceElement = dirtyDocument.selectFirst("div");
        Element destElement = new Element(Tag.DIV);
        int numDiscarded = Cleaner.copySafeNodes(sourceElement, destElement);
        Element metaElement = Cleaner.createSafeElement(sourceElement);
        assertEquals(0, numDiscarded);
        assertEquals("div", metaElement.el.tagName());

        // Test creating a safe element with a more complex source element
        Whitelist complexWhitelist = new Whitelist.Builder()
                .addTags("p", "div")
                .addAttributes("class", "id")
                .build();
        Document complexDirtyDocument = Document.parse("<html><body><p id='test' class='test'><script>Hello, World!</script></p></body></html>");
        Element complexSourceElement = complexDirtyDocument.selectFirst("p");
        Element complexDestElement = new Element(Tag.P);
        numDiscarded = Cleaner.copySafeNodes(complexSourceElement, complexDestElement);
        Element complexMetaElement = Cleaner.createSafeElement(complexSourceElement);
        assertEquals(1, numDiscarded);
        assertEquals("p", complexMetaElement.el.tagName());
    }

    @Test
    public void testElementMetaClass() {
        // Test creating an ElementMeta instance
        Element sourceElement = new Element(Tag.DIV);
        Attributes sourceAttrs = new Attributes();
        sourceAttrs.put("class", "test");
        sourceAttrs.put("id", "test");
        Element destElement = new Element(Tag.DIV, sourceAttrs);
        int numDiscarded = 0;
        Attributes enforcedAttrs = new Attributes();
        ElementMeta metaElement = new Cleaner.ElementMeta(destElement, numDiscarded);
        assertEquals(destElement, metaElement.el);
        assertEquals(numDiscarded, metaElement.numAttribsDiscarded);

        // Test creating an ElementMeta instance with enforced attributes
        Whitelist whitelist = new Whitelist.Builder()
                .addAttributes("class", "test")
                .build();
        Element sourceElementWithEnforced = new Element(Tag.DIV);
        sourceAttrs = new Attributes();
        sourceAttrs.put("class", "test");
        sourceAttrs.put("id", "test");
        sourceAttrs.put("unsafe", "value");
        Element destElementWithEnforced = new Element(Tag.DIV, sourceAttrs);
        numDiscarded = 1;
        enforcedAttrs = whitelist.getEnforcedAttributes("div");
        ElementMeta metaElementWithEnforced = new Cleaner.ElementMeta(destElementWithEnforced, numDiscarded);
        assertEquals(destElementWithEnforced, metaElementWithEnforced.el);
        assertEquals(numDiscarded, metaElementWithEnforced.numAttribsDiscarded);
        assertEquals("class", metaElementWithEnforced.el.tagName());
        assertEquals("test", metaElementWithEnforced.el.classes().get(0));
        assertFalse(metaElementWithEnforced.el.hasAttr("unsafe"));
    }
}