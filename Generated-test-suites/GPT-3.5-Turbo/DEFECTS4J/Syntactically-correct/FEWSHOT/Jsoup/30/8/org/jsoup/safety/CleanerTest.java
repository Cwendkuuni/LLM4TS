
package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.List;
import java.util.Set;

public class CleanerTest {

    @Test
    public void testClean() {
        // Test with null dirtyDocument
        Document cleanedDoc = new Cleaner(new Whitelist()).clean(null);
        assertNotNull(cleanedDoc);

        // Test with empty dirtyDocument
        cleanedDoc = new Cleaner(new Whitelist()).clean(new Document(""));
        assertNotNull(cleanedDoc);

        // Test with dirtyDocument having no body
        Document dirtyDocument = new Document("");
        Document cleanDocument = new Cleaner(new Whitelist()).clean(dirtyDocument);
        assertEquals("", cleanDocument.outerHtml());

        // Test with dirtyDocument having a body
        dirtyDocument = Jsoup.parse("<html><body><p>Hello World</p></body></html>");
        cleanDocument = new Cleaner(new Whitelist()).clean(dirtyDocument);
        assertEquals("<html><head></head><body><p>Hello World</p></body></html>", cleanDocument.outerHtml());
    }

    @Test
    public void testIsValid() {
        // Test with null dirtyDocument
        boolean isValid = new Cleaner(new Whitelist()).isValid(null);
        assertFalse(isValid);

        // Test with empty dirtyDocument
        isValid = new Cleaner(new Whitelist()).isValid(new Document(""));
        assertFalse(isValid);

        // Test with valid dirtyDocument
        Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World</p></body></html>");
        isValid = new Cleaner(new Whitelist()).isValid(dirtyDocument);
        assertTrue(isValid);

        // Test with invalid dirtyDocument
        dirtyDocument = Jsoup.parse("<html><body><script>alert('xss');</script></body></html>");
        isValid = new Cleaner(new Whitelist()).isValid(dirtyDocument);
        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Element root = new Element(Tag.valueOf("root"), "");
        Element dest = new Element(Tag.valueOf("dest"), "");
        int numDiscarded = new Cleaner(new Whitelist()).copySafeNodes(root, dest);
        assertEquals(0, numDiscarded);

        // Test with null root and dest
        numDiscarded = new Cleaner(new Whitelist()).copySafeNodes(null, null);
        assertEquals(0, numDiscarded);

        // Test with root having element children
        Element child1 = new Element(Tag.valueOf("child1"), "");
        child1.text("Child 1");
        Element child2 = new Element(Tag.valueOf("child2"), "");
        child2.text("Child 2");
        Element child3 = new Element(Tag.valueOf("child3"), "");
        child3.text("Child 3");
        root.appendChild(child1);
        root.appendChild(child2);
        root.appendChild(child3);
        numDiscarded = new Cleaner(new Whitelist()).copySafeNodes(root, dest);
        assertEquals(0, numDiscarded);
        assertEquals("<dest><child1>Child 1</child1><child2>Child 2</child2><child3>Child 3</child3></dest>", dest.html());

        // Test with root having text children
        TextNode text1 = new TextNode("Text 1", "");
        TextNode text2 = new TextNode("Text 2", "");
        root.appendChild(text1);
        root.appendChild(text2);
        numDiscarded = new Cleaner(new Whitelist()).copySafeNodes(root, dest);
        assertEquals(0, numDiscarded);
        assertEquals("<dest><child1>Child 1</child1><child2>Child 2</child2><child3>Child 3</child3>Text 1Text 2</dest>", dest.html());

        // Test with root having mixed element and text children
        root.text("Mixed Text");
        numDiscarded = new Cleaner(new Whitelist()).copySafeNodes(root, dest);
        assertEquals(0, numDiscarded);
        assertEquals("<dest>Mixed Text</dest>", dest.html());

        // Test with unsafe tag
        Element unsafeTag = new Element(Tag.valueOf("script"), "");
        unsafeTag.text("alert('xss')");
        root.appendChild(unsafeTag);
        numDiscarded = new Cleaner(new Whitelist()).copySafeNodes(root, dest);
        assertEquals(1, numDiscarded);
        assertEquals("<dest>Mixed Text</dest>", dest.html());
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("unsafeTag"), "");
        sourceEl.text("Unsafe Text");
        Whitelist whitelist = new Whitelist();
        ElementMeta meta = new Cleaner(whitelist).createSafeElement(sourceEl);

        // Test with unsafe tag name
        assertEquals(Tag.valueOf("unsafeTag"), meta.el.tag());
        assertEquals("Unsafe Text", meta.el.text());
        assertEquals(0, meta.numAttribsDiscarded);

        // Test with safe tag name
        sourceEl = new Element(Tag.valueOf("safeTag"), "");
        sourceEl.text("Safe Text");
        whitelist.addTags("safeTag");
        meta = new Cleaner(whitelist).createSafeElement(sourceEl);

        assertEquals(Tag.valueOf("safeTag"), meta.el.tag());
        assertEquals("Safe Text", meta.el.text());
        assertEquals(0, meta.numAttribsDiscarded);

        // Test with safe and unsafe attributes
        Attributes attributes = sourceEl.attributes();
        attributes.put("safeAttr1", "value1");
        attributes.put("unsafeAttr1", "value2");
        meta = new Cleaner(whitelist).createSafeElement(sourceEl);

        assertEquals("value1", meta.el.attr("safeAttr1"));
        Set<String> attrs = meta.el.attributes().dataset().keySet();
        assertFalse(attrs.contains("unsafeAttr1"));
        assertEquals(1, meta.numAttribsDiscarded);
        assertEquals(1, attrs.size());

        // Test with enforced attributes
        whitelist.addEnforcedAttribute("safeTag", "enforcedAttr1", "defaultValue");
        meta = new Cleaner(whitelist).createSafeElement(sourceEl);

        assertEquals("defaultValue", meta.el.attr("enforcedAttr1"));
        assertEquals(1, attrs.size());
    }

}
