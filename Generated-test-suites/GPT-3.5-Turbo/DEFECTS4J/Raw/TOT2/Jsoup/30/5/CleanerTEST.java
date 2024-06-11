package org.jsoup.safety;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<div><script>alert('XSS')</script></div>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        // Check if script tag is removed
        assertFalse(cleanDocument.html().contains("script"));
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basicWithImages();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<img src='https://example.com/image.jpg'>");
        
        // Check if image tag is allowed
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");

        Elements children = new Elements();
        children.add(new Element(Tag.valueOf("p"), ""));
        root.insertChildren(0, children);

        int numDiscarded = cleaner.copySafeNodes(root, destination);

        // Check if all safe nodes are copied successfully
        assertEquals(1, destination.children().size());
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceElement = new Element(Tag.valueOf("a"), "");
        sourceElement.attr("href", "https://example.com");
        sourceElement.attr("target", "_blank");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        // Check if unsafe attributes are discarded
        assertEquals(1, elementMeta.numAttribsDiscarded);
        assertEquals("a", elementMeta.el.tagName());
        assertEquals(1, elementMeta.el.attributes().size());
    }
}