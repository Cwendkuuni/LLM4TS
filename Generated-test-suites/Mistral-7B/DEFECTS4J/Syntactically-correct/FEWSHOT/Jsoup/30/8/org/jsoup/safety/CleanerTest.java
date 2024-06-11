package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {

    @Test
    public void testCreateCleaner() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNull(cleaner.whitelist);
        assertSame(whitelist, cleaner.whitelist);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateCleanerNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testCleanDocument() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('xss')</script><div>Hello</div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element body = cleanDocument.body();
        assertTrue(body.children().isEmpty());
    }

    @Test
    public void testCleanDocumentEmptyBody() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element body = cleanDocument.body();
        assertTrue(body.children().isEmpty());
    }

    @Test
    public void testCleanDocumentValid() {
        Whitelist whitelist = new Whitelist.SimpleTagNameWhitelist("div");
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><div>Hello</div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element body = cleanDocument.body();
        List<Element> elements = body.children();
        assertEquals(1, elements.size());
        assertEquals("div", elements.get(0).tagName());
    }

    @Test
    public void testCleanDocumentInvalid() {
        Whitelist whitelist = new Whitelist.SimpleTagNameWhitelist("div");
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('xss')</script><div>Hello</div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element body = cleanDocument.body();
        assertTrue(body.children().isEmpty());
    }

    @Test
    public void testIsValidDocumentEmptyBody() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testIsValidDocumentValid() {
        Whitelist whitelist = new Whitelist.SimpleTagNameWhitelist("div");
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><div>Hello</div></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testIsValidDocumentInvalid() {
        Whitelist whitelist = new Whitelist.SimpleTagNameWhitelist("div");
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('xss')</script><div>Hello</div></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodesElement() {
        Whitelist whitelist = new Whitelist.SimpleTagNameWhitelist("div");
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><div id='one' class='one'><div id='two' class='two'>Hello</div></div></body></html>");
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        Element body = cleanDocument.body();
        List<Element> elements = body.children();
        assertEquals(1, elements.size());
        assertEquals("div", elements.get(0).tagName());
        assertEquals(2, elements.get(0).childNodes().size());
        assertEquals("div", elements.get(0).childNodes().get(0).tagName());
        assertEquals("Hello", elements.get(0).childNodes().get(1).text());
        assertEquals(2, numDiscarded);
    }

    @Test
    public void testCopySafeNodesText() {
        Whitelist whitelist = new Whitelist.SimpleTagNameWhitelist("div");
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><div id='one'>Hello <script>alert('xss')</script> World</div></body></html>");
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        Element body = cleanDocument.body();
        List<Node> children = body.childNodes();
        assertEquals(1, children.size());
        assertEquals(Text.class, children.get(0).getClass());
        assertEquals("Hello World", children.get(0).toString());
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testIsSafeTag() {
        Whitelist whitelist = new Whitelist.SimpleTagNameWhitelist("div");
        Cleaner cleaner = new Cleaner(whitelist);

        assertTrue(cleaner.whitelist.isSafeTag("div"));
        assertFalse(cleaner.whitelist.isSafeTag("script"));
    }

    @Test
    public void testIsSafeAttribute() {
        Whitelist whitelist = new Whitelist.SimpleTagNameAttributeWhitelist("div", Arrays.asList("class"));
        Cleaner cleaner = new Cleaner(whitelist);

        Element element = new Element("div", null, new Attributes().put("class", "test"));

        assertTrue(cleaner.whitelist.isSafeAttribute("div", element, new Attribute("class", "test")));
        assertFalse(cleaner.whitelist.isSafeAttribute("div", element, new Attribute("script", "alert('xss')")));
    }

    @Test
    public void testGetEnforcedAttributes() {
        Whitelist whitelist = new Whitelist.SimpleTagNameAttributeWhitelist("div", Arrays.asList("class"));
        Cleaner cleaner = new Cleaner(whitelist);

        List<Attribute> attributes = cleaner.whitelist.getEnforcedAttributes("div");
        assertEquals(1, attributes.size());
        assertEquals("class", attributes.get(0).getName());
    }
}