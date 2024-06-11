package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

/**
 * Test file for the Cleaner class.
 */
public class CleanerTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    /**
     * Test clean method with a valid document.
     */
    @org.junit.Test
    public void testCleanValidDocument() {
        String html = "<html><body><p>Hello World!</p></body></html>";
        Document dirtyDocument = Jsoup.parseBodyFragment(html);
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertEquals(html, cleanDocument.body().html());
    }

    /**
     * Test clean method with an invalid document.
     */
    @org.junit.Test
    public void testCleanInvalidDocument() {
        String html = "<html><body><script>alert('XSS attack!');</script></body></html>";
        Document dirtyDocument = Jsoup.parseBodyFragment(html);
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertFalse(cleanDocument.body().text().contains("XSS attack!"));
    }

    /**
     * Test isValid method with a valid document.
     */
    @org.junit.Test
    public void testIsValidValidDocument() {
        String html = "<html><body><p>Hello World!</p></body></html>";
        Document dirtyDocument = Jsoup.parseBodyFragment(html);
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    /**
     * Test isValid method with an invalid document.
     */
    @org.junit.Test
    public void testIsValidInvalidDocument() {
        String html = "<html><body><script>alert('XSS attack!');</script></body></html>";
        Document dirtyDocument = Jsoup.parseBodyFragment(html);
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    /**
     * Test copySafeNodes method with safe nodes.
     */
    @org.junit.Test
    public void testCopySafeNodesSafeNodes() {
        String html = "<html><body><p id='p1' class='c1'><span>Hello World!</span></p></body></html>";
        Document dirtyDocument = Jsoup.parseBodyFragment(html);
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        Element p = cleanDocument.body().getElementById("p1");
        Element span = p.getElementsByTag("span").first();
        assertNotNull(p);
        assertNotNull(span);
        assertEquals(0, numDiscarded);
    }

    /**
     * Test copySafeNodes method with unsafe nodes.
     */
    @org.junit.Test
    public void testCopySafeNodesUnsafeNodes() {
        String html = "<html><body><p id='p1' class='c1'><script>alert('XSS attack!');</script></p></body></html>";
        Document dirtyDocument = Jsoup.parseBodyFragment(html);
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        Element p = cleanDocument.body().getElementById("p1");
        assertNull(p);
        assertEquals(1, numDiscarded);
    }

    /**
     * Test createSafeElement method.
     */
    @org.junit.Test
    public void testCreateSafeElement() {
        String html = "<p id='p1' class='c1' style='color:red'><span>Hello World!</span></p>";
        Element sourceEl = Jsoup.parseBodyFragment(html).body().getElementsByTag("p").first();
        Cleaner cleaner = new Cleaner(Whitelist.simpleTextAndHtml());
        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        Element destChild = elementMeta.el;
        assertNotNull(destChild);
        assertTrue(destChild.tagName().equals("p"));
        assertEquals(2, elementMeta.numAttribsDiscarded);
        Attribute idAttr = destChild.tagInfo().attributes().get("id");
        Attribute classAttr = destChild.tagInfo().attributes().get("class");
        Attribute styleAttr = destChild.tagInfo().attributes().get("style");
        assertNull(idAttr);
        assertNull(classAttr);
        assertNull(styleAttr);
    }

}