package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;

public class CleanerTest {

    private Cleaner cleaner;
    private Whitelist whitelist;
    private Document dirtyDocument;

    @Before
    public void setUp() {
        Parser parser = new Parser();
        whitelist = new Whitelist(new ElementsTagFilter(new HashSet<Tag>() {{
            add(Tag.BODY);
            add(Tag.A);
            add(Tag.IMG);
        }}));
        cleaner = new Cleaner(whitelist);
        dirtyDocument = parser.parse("<html><body><a href=\"https://www.example.com\">Link</a><img src=\"image.jpg\" alt=\"Image\"/><script>alert('XSS')</script></body></html>");
    }

    @Test
    public void testCreateCleaner() {
        assertNotNull(cleaner);
    }

    @Test
    public void testCleanDocument() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertEquals(dirtyDocument.body().childNodes().size(), cleanDocument.body().childNodes().size());
        assertThat(cleanDocument.body().childNodes(), hasSize(2));
        Element link = cleanDocument.selectFirst("a");
        assertEquals("Link", link.text());
        Element img = cleanDocument.selectFirst("img");
        assertEquals("image.jpg", img.attr("src"));
        assertEquals("Image", img.attr("alt"));
    }

    @Test
    public void testIsValidDocument() {
        assertTrue(cleaner.isValid(dirtyDocument));
        Document invalidDocument = parser.parse("<html><body><script>alert('XSS')</script></body></html>");
        assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element root = cleanDocument.body();
        Element destination = new Element(Tag.BODY);
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        assertEquals(0, numDiscarded);
        assertEquals(2, destination.childNodes().size());
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceElement = new Element(Tag.A, "Link");
        sourceElement.attributes().put("href", "https://www.example.com");
        sourceElement.attributes().put("class", "example");
        ElementMeta safeElement = cleaner.createSafeElement(sourceElement);
        Element destElement = safeElement.el;
        assertEquals("a", destElement.tagName());
        assertEquals("Link", destElement.text());
        assertEquals(2, destElement.attributes().size());
        assertEquals("https://www.example.com", destElement.attr("href"));
        assertNull(destElement.attr("class"));
    }

    @Test
    public void testIsSafeAttribute() {
        Element sourceElement = new Element(Tag.A, "Link");
        Attribute sourceAttribute = new Attribute("data-xss", "evil");
        assertFalse(cleaner.whitelist.isSafeAttribute(Tag.A, sourceElement, sourceAttribute));
        sourceElement.attributes().put("class", "example");
        assertTrue(cleaner.whitelist.isSafeAttribute(Tag.A, sourceElement, sourceAttribute));
    }

    @Test
    public void testCleaningVisitor() {
        Element root = dirtyDocument.body();
        Element destination = new Element(Tag.BODY);
        CleaningVisitor cleaningVisitor = new CleaningVisitor(root, destination);
        NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
        traversor.traverse(root);
        assertEquals(2, destination.childNodes().size());
    }

}