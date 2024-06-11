package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeVisitor;
import org.jsoup.select.NodeTraversor;

public class CleanerTest {

    @Test
    public void testCleanerWithEmptyInput() {
        Whitelist whitelist = new SimpleWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parseBodyElements("", "");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleanDocument.body().children().isEmpty());
    }

    @Test
    public void testCleanerWithValidInput() throws Exception {
        Whitelist whitelist = new SimpleWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = IOUtils.resourceToString(getClass().getResource("/valid_input.html"));
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals(dirtyDocument.body().children().size(), cleanDocument.body().children().size());
        assertTrue(cleanDocument.body().children().toString().contains("Hello, World!"));
    }

    @Test
    public void testCleanerWithInvalidInput() {
        Whitelist whitelist = new SimpleWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<script>alert('XSS Attack!');</script>";
        Document dirtyDocument = Jsoup.parseBodyElements(html, "");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleanDocument.body().children().isEmpty());
    }

    @Test
    public void testIsValidWithValidInput() {
        Whitelist whitelist = new SimpleWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<p>Hello, World!</p>";
        Document dirtyDocument = Jsoup.parseBodyElements(html, "");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testIsValidWithInvalidInput() {
        Whitelist whitelist = new SimpleWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<script>alert('XSS Attack!');</script>";
        Document dirtyDocument = Jsoup.parseBodyElements(html, "");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

    @Test
    public void testCleaningVisitorWithSafeTag() {
        Whitelist whitelist = new SimpleWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<p><i>Hello, World!</i></p>";
        Document dirtyDocument = Jsoup.parseBodyElements(html, "");
        Element root = dirtyDocument.body();
        Element destination = new Element("div");
        CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);
        NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
        traversor.traverse(root);
        assertEquals(1, destination.children().size());
        assertTrue(destination.child(0).tagName().equals("i"));
    }

    @Test
    public void testCleaningVisitorWithUnsafeTag() {
        Whitelist whitelist = new SimpleWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<script>alert('XSS Attack!');</script>";
        Document dirtyDocument = Jsoup.parseBodyElements(html, "");
        Element root = dirtyDocument.body();
        Element destination = new Element("div");
        CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);
        NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
        traversor.traverse(root);
        assertTrue(destination.children().isEmpty());
    }

    @Test
    public void testCleaningVisitorWithSafeTextNode() {
        Whitelist whitelist = new SimpleWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<p>Hello, World!</p>";
        Document dirtyDocument = Jsoup.parseBodyElements(html, "");
        Element root = dirtyDocument.body();
        Element destination = new Element("div");
        CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);
        NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
        traversor.traverse(root);
        assertEquals(1, destination.children().size());
        assertTrue(destination.child(0).tagName().equals("p"));
        assertEquals(1, destination.child(0).children().size());
        assertTrue(destination.child(0).child(0).tagName().equals("text"));
        assertEquals("Hello, World!", destination.child(0).child(0).text());
    }

    @Test
    public void testCleaningVisitorWithUnsafeDataNode() {
        Whitelist whitelist = new SimpleWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<img src='xss.js' />";
        Document dirtyDocument = Jsoup.parseBodyElements(html, "");
        Element root = dirtyDocument.body();
        Element destination = new Element("div");
        CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);
        NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
        traversor.traverse(root);
        assertTrue(destination.children().isEmpty());
    }

    @Test
    public void testCleaningVisitorWithEmptyNode() {
        Whitelist whitelist = new SimpleWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<comment></comment>";
        Document dirtyDocument = Jsoup.parseBodyElements(html, "");
        Element root = dirtyDocument.body();
        Element destination = new Element("div");
        CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);
        NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
        traversor.traverse(root);
        assertTrue(destination.children().isEmpty());
    }

    @Test
    public void testCopySafeNodesWithEmptySource() {
        Whitelist whitelist = new SimpleWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Element source = null;
        Element destination = new Element("div");
        int numDiscarded = cleaner.copySafeNodes(source, destination);
        assertTrue(numDiscarded == 0);
        assertTrue(destination.children().isEmpty());
    }

    @Test
    public void testCopySafeNodesWithValidInput() {
        Whitelist whitelist = new SimpleWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<p><i>Hello, World!</i></p>";
        Document dirtyDocument = Jsoup.parseBodyElements(html, "");
        Element source = dirtyDocument.body();
        Element destination = new Element("div");
        int numDiscarded = cleaner.copySafeNodes(source, destination);
        assertEquals(0, numDiscarded);
        assertEquals(1, destination.children().size());
        assertTrue(destination.child(0).tagName().equals("p"));
        assertEquals(1, destination.child(0).children().size());
        assertTrue(destination.child(0).child(0).tagName().equals("i"));
    }

    @Test
    public void testCopySafeNodesWithInvalidInput() {
        Whitelist whitelist = new SimpleWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<script>alert('XSS Attack!');</script>";
        Document dirtyDocument = Jsoup.parseBodyElements(html, "");
        Element source = dirtyDocument.body();
        Element destination = new Element("div");
        int numDiscarded = cleaner.copySafeNodes(source, destination);
        assertTrue(numDiscarded > 0);
        assertTrue(destination.children().isEmpty());
    }

    @Test
    public void testCreateSafeElementWithSafeTag() {
        Whitelist whitelist = new SimpleWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceEl = new Element("i");
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        assertEquals("i", meta.el.tagName());
        assertTrue(meta.numAttribsDiscarded == 0);
    }

    @Test
    public void testCreateSafeElementWithUnsafeTag() {
        Whitelist whitelist = new SimpleWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceEl = new Element("script");
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        assertNull(meta.el);
        assertTrue(meta.numAttribsDiscarded > 0);
    }

    @Test
    public void testCreateSafeElementWithSafeTextNode() {
        Whitelist whitelist = new SimpleWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceEl = new Element("p");
        sourceEl.text("Hello, World!");
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        assertEquals("p", meta.el.tagName());
        assertTrue(meta.numAttribsDiscarded == 0);
        assertEquals("Hello, World!", meta.el.text());
    }

    @Test
    public void testCreateSafeElementWithUnsafeTextNode() {
        Whitelist whitelist = new SimpleWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceEl = new Element("p");
        sourceEl.text("<script>alert('XSS Attack!');</script>");
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        assertNull(meta.el);
        assertTrue(meta.numAttribsDiscarded > 0);
    }

    @Test
    public void testCreateSafeElementWithEmptyNode() {
        Whitelist whitelist = new SimpleWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceEl = new Element("comment");
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        assertNull(meta.el);
        assertTrue(meta.numAttribsDiscarded == 0);
    }

    @Test
    public void testWhitelistIsSafeTag() {
        Whitelist whitelist = new SimpleWhitelist();
        assertTrue(whitelist.isSafeTag("i"));
        assertFalse(whitelist.isSafeTag("script"));
    }

    @Test
    public void testWhitelistIsSafeAttribute() {
        Whitelist whitelist = new SimpleWhitelist();
        String tagName = "i";
        String attributeName = "style";
        String attributeValue = "color: red;";
        assertTrue(whitelist.isSafeAttribute(tagName, null, new Attribute(attributeName, attributeValue)));
    }

    @Test
    public void testWhitelistIsSafeAttributeWithInvalidAttributeValue() {
        Whitelist whitelist = new SimpleWhitelist();
        String tagName = "i";
        String attributeName = "style";
        String attributeValue = "<script>alert('XSS Attack!');</script>";
        assertFalse(whitelist.isSafeAttribute(tagName, null, new Attribute(attributeName, attributeValue)));
    }

    @Test
    public void testWhitelistIsSafeAttributeWithUnsafeTag() {
        Whitelist whitelist = new SimpleWhitelist();
        String tagName = "script";
        String attributeName = "src";
        String attributeValue = "xss.js";
        assertFalse(whitelist.isSafeAttribute(tagName, null, new Attribute(attributeName, attributeValue)));
    }

    @Test
    public void testWhitelistGetEnforcedAttributes() {
        Whitelist whitelist = new SimpleWhitelist();
        String tagName = "i";
        Attributes attributes = new Attributes();
        attributes.put("style", "color: red;");
        Attributes enforcedAttributes = whitelist.getEnforcedAttributes(tagName);
        assertEquals(1, enforcedAttributes.size());
        assertTrue(enforcedAttributes.containsKey("style"));
        assertTrue(StringUtils.equals("color: red;", enforcedAttributes.get("style").value()));
    }

    private static class SimpleWhitelist implements Whitelist {
        @Override
        public boolean isSafeTag(String tagName) {
            return tagName.equals("i") || tagName.equals("p");
        }

        @Override
        public boolean isSafeAttribute(String tagName, Element contextElement, Attribute attribute) {
            return tagName.equals("i") && attribute.getKey().equals("style");
        }

        @Override
        public Attributes getEnforcedAttributes(String tagName) {
            Attributes attributes = new Attributes();
            attributes.put("style", "color: red;");
            return attributes;
        }

        @Override
        public void addTag(String tagName) {
            // not implemented
        }

        @Override
        public void addAttribute(String tagName, String attributeName, String attributeValue) {
            // not implemented
        }
    }
}