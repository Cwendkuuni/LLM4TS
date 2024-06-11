package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

/**
 * Test class for Cleaner.java
 */
public class CleanerTest {

    private final static Whitelist whitelist = new Whitelist.SimpleWhitelist.Builder()
            .addTag("p")
            .addTag("a")
            .build();

    @org.junit.BeforeClass
    public static void setupLogger() {
        LogFactory.getFactory().getInstance(org.apache.commons.logging.Log.class)
                .setLevel(org.apache.commons.logging.LogFactory.LEVEL_OFF);
    }

    @org.junit.Test
    public void testCreateCleaner() {
        assertNotNull(new Cleaner(whitelist));
    }

    @org.junit.Test
    public void testCleanDocument() throws IOException {
        String dirtyHtml = "<html><body><p id=\"p1\">Hello <a href=\"http://example.com\">world</a></p></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><p id=\"p1\"></p></body></html>", cleanDocument.html());

        assertFalse(cleanDocument.body().hasText());
        assertNotNull(cleanDocument.body().child(0));
        assertSame(cleanDocument.body().child(0).tagName(), "p");
        assertNull(cleanDocument.head());
    }

    @org.junit.Test
    public void testIsValidDocument() throws IOException {
        String dirtyHtml = "<html><body><p id=\"p1\">Hello <a href=\"javascript:alert('XSS')\">world</a></p></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        Cleaner cleaner = new Cleaner(whitelist);
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @org.junit.Test
    public void testIsValidDocumentValid() throws IOException {
        String dirtyHtml = "<html><body><p id=\"p1\">Hello <a href=\"http://example.com\">world</a></p></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        Cleaner cleaner = new Cleaner(whitelist);
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @org.junit.Test
    public void testCleaningVisitor() {
        Cleaner cleaner = new Cleaner(whitelist);
        Element source = new Element("div");
        source.text("source text");

        Element destination = new Element("p");
        CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(source, destination);
        NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
        traversor.traverse(source);

        assertEquals("source text", destination.text());
        assertEquals(0, cleaningVisitor.numDiscarded);
    }

    @org.junit.Test
    public void testCleaningVisitorDiscarded() {
        Cleaner cleaner = new Cleaner(whitelist);
        Element source = new Element("script");
        source.text("javascript:alert('XSS')");

        Element destination = new Element("p");
        CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(source, destination);
        NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
        traversor.traverse(source);

        assertTrue(destination.children().isEmpty());
        assertTrue(cleaningVisitor.numDiscarded > 0);
    }

    @org.junit.Test
    public void testCreateSafeElement() {
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceElement = new Element("p");
        sourceElement.attr("id", "p1");
        sourceElement.attr("title", "test title");

        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals("p", elementMeta.el.tagName());
        assertEquals(2, elementMeta.el.childElementCount());
        assertEquals("id", elementMeta.el.attributeNames().get(0));
        assertEquals("p1", elementMeta.el.getAttribute("id"));
        assertNull(elementMeta.el.getAttribute("title"));
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }

    @org.junit.Test
    public void testCreateSafeElementNoAttributes() {
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceElement = new Element("p");

        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals("p", elementMeta.el.tagName());
        assertEquals(0, elementMeta.el.childElementCount());
        assertTrue(elementMeta.el.hasAttrs());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @org.junit.Test
    public void testCreateSafeElementWithEmptyAttributes() {
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceElement = new Element("p");
        sourceElement.attributes().put("test", "");

        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals("p", elementMeta.el.tagName());
        assertEquals(0, elementMeta.el.childElementCount());
        assertTrue(elementMeta.el.hasAttrs());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @org.junit.Test
    public void testCreateSafeElementWithDisallowedAttributes() {
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceElement = new Element("p");
        sourceElement.attributes().put("test", "script:alert('XSS')");

        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals("p", elementMeta.el.tagName());
        assertEquals(0, elementMeta.el.childElementCount());
        assertTrue(elementMeta.el.hasAttrs());
        assertFalse(elementMeta.el.hasAttr("test"));
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }

    @org.junit.Test
    public void testCreateSafeElementWithAllowedAttributes() {
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceElement = new Element("p");
        sourceElement.attributes().put("test", "id");

        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals("p", elementMeta.el.tagName());
        assertEquals(0, elementMeta.el.childElementCount());
        assertTrue(elementMeta.el.hasAttrs());
        assertTrue(elementMeta.el.hasAttr("test"));
        assertEquals("id", elementMeta.el.getAttribute("test"));
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @org.junit.Test
    public void testCreateSafeElementWithAllowedAttributesEnforce() {
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceElement = new Element("p");
        sourceElement.attributes().put("test", "id test2");

        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals("p", elementMeta.el.tagName());
        assertEquals(0, elementMeta.el.childElementCount());
        assertTrue(elementMeta.el.hasAttrs());
        assertTrue(elementMeta.el.hasAttr("test"));
        assertEquals("id", elementMeta.el.getAttribute("test"));
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }

    @org.junit.Test
    public void testCreateSafeElementWithAllowedAttributesEnforceMultiple() {
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceElement = new Element("p");
        sourceElement.attributes().put("test", "id test2 class test3");

        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals("p", elementMeta.el.tagName());
        assertEquals(0, elementMeta.el.childElementCount());
        assertTrue(elementMeta.el.hasAttrs());
        assertTrue(elementMeta.el.hasAttr("test"));
        assertEquals("id", elementMeta.el.getAttribute("test"));
        assertEquals(2, elementMeta.numAttribsDiscarded);
    }

    @org.junit.Test
    public void testCreateSafeElementWithAllowedAttributesEnforceMultipleAndEmpty() {
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceElement = new Element("p");
        sourceElement.attributes().put("test", "id test2 class test3 test4 test5 test6");

        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals("p", elementMeta.el.tagName());
        assertEquals(0, elementMeta.el.childElementCount());
        assertTrue(elementMeta.el.hasAttrs());
        assertTrue(elementMeta.el.hasAttr("test"));
        assertEquals("id", elementMeta.el.getAttribute("test"));
        assertEquals(5, elementMeta.numAttribsDiscarded);
    }

    @org.junit.Test
    public void testCreateSafeElementWithAllowedAttributesEnforceEmpty() {
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceElement = new Element("p");
        sourceElement.attributes().put("test", "");

        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals("p", elementMeta.el.tagName());
        assertEquals(0, elementMeta.el.childElementCount());
        assertTrue(elementMeta.el.hasAttrs());
        assertFalse(elementMeta.el.hasAttr("test"));
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }

    @org.junit.Test
    public void testCreateSafeElementWithAllowedAttributesEnforceEmptyAndDisabled() {
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceElement = new Element("p");
        sourceElement.attributes().put("test", "id test2 class test3 disabled");

        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals("p", elementMeta.el.tagName());
        assertEquals(0, elementMeta.el.childElementCount());
        assertTrue(elementMeta.el.hasAttrs());
        assertTrue(elementMeta.el.hasAttr("test"));
        assertEquals("id", elementMeta.el.getAttribute("test"));
        assertEquals(2, elementMeta.numAttribsDiscarded);
    }

    @org.junit.Test
    public void testCreateSafeElementWithAllowedAttributesEnforceEmptyAndDisabledMultiple() {
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceElement = new Element("p");
        sourceElement.attributes().put("test", "id test2 class test3 disabled class2 disabled2");

        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals("p", elementMeta.el.tagName());
        assertEquals(0, elementMeta.el.childElementCount());
        assertTrue(elementMeta.el.hasAttrs());
        assertTrue(elementMeta.el.hasAttr("test"));
        assertEquals("id", elementMeta.el.getAttribute("test"));
        assertEquals(4, elementMeta.numAttribsDiscarded);
    }

    @org.junit.Test
    public void testCreateSafeElementWithTextNode() {
        Cleaner cleaner = new Cleaner(whitelist);
        TextNode textNode = new TextNode("TextNodeTest");

        ElementMeta elementMeta = cleaner.createSafeElement(textNode.parent());

        assertNull(elementMeta.el);
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @org.junit.Test
    public void testCreateSafeElementWithDataNode() {
        Cleaner cleaner = new Cleaner(whitelist);
        DataNode dataNode = new DataNode("DataNodeTest");

        ElementMeta elementMeta = cleaner.createSafeElement(dataNode.parent());

        assertNull(elementMeta.el);
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @org.junit.Test
    public void testCreateSafeElementWithCommentNode() {
        Cleaner cleaner = new Cleaner(whitelist);
        Element commentNode = new Element("!--comment--");

        ElementMeta elementMeta = cleaner.createSafeElement(commentNode.parent());

        assertNull(elementMeta.el);
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @org.junit.Test
    public void testCreateSafeElementWithProcessingInstructionNode() {
        Cleaner cleaner = new Cleaner(whitelist);
        Element processingInstructionNode = new Element("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        ElementMeta elementMeta = cleaner.createSafeElement(processingInstructionNode.parent());

        assertNull(elementMeta.el);
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @org.junit.Test
    public void testCreateSafeElementWithDocumentFragment() {
        Cleaner cleaner = new Cleaner(whitelist);
        DocumentFragment documentFragment = new DocumentFragment();
        documentFragment.appendChild(new Element("p"));

        ElementMeta elementMeta = cleaner.createSafeElement(documentFragment.parent());

        assertEquals(Collections.singletonList("p"), Arrays.asList(elementMeta.el.children()));
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}