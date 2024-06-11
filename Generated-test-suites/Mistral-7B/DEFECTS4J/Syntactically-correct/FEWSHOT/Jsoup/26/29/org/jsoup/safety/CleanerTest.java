package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {
    @Test
    public void testCleanerInstance() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNull(cleaner.whitelist);
        assertTrue(cleaner instanceof Cleaner);
    }

    @Test(expected = NullPointerException.class)
    public void testCleanerNullWhitelist() {
        new Cleaner(null);
    }

    @Test
    public void testCleanDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.parse("<html><body><div id='test'><script>alert('XSS')</script><h1>Hello World</h1></div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element body = cleanDocument.body();
        assertTrue(body.tagName().equals("body"));
        assertFalse(body.childElementCount() > 0);
    }

    @Test
    public void testIsValidDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.parse("<html><body><div id='test'><h1>Hello World</h1></div></body></html>");
        assertTrue(cleaner.isValid(dirtyDocument));

        Document invalidDocument = Document.parse("<html><body><div id='test'><script>alert('XSS')</script><h1>Hello World</h1></div></body></html>");
        assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.parse("<html><body><div id='test'><script>alert('XSS')</script><h1 id='header' class='header' style='color:red'>Hello World</h1></div></body></html>");
        Element body = dirtyDocument.body();
        Element cleanBody = new Element(Tag.BODY, body.baseUri());

        int numDiscarded = cleaner.copySafeNodes(body, cleanBody);

        assertTrue(numDiscarded > 0);
        assertTrue(cleanBody.childElementCount() > 0);

        // Check that the header element was copied correctly
        Element header = cleanBody.getElementById("header");
        assertTrue(header.tagName().equals("h1"));
        assertTrue(header.hasClass("header"));
        assertFalse(header.hasAttr("id"));
        assertTrue(header.hasAttr("style"));
        assertEquals(header.attr("style"), "color:red");
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceElement = new Element("div", null, new Attributes().put("id", "test"));
        ElementMeta meta = cleaner.createSafeElement(sourceElement);

        assertTrue(meta.el instanceof Element);
        assertTrue(meta.numAttribsDiscarded > 0);

        Element destElement = meta.el;
        assertTrue(destElement.tagName().equals("div"));
        assertFalse(destElement.hasAttr("id"));
    }

    @Test
    public void testWhitelistSafeTags() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element tag = new Element("p", null, null);
        assertTrue(cleaner.whitelist.isSafeTag(tag.tagName()));

        tag.setTag(Tag.DIV);
        assertFalse(cleaner.whitelist.isSafeTag(tag.tagName()));
    }

    @Test
    public void testWhitelistSafeAttributes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element element = new Element("div", null, new Attributes().put("data-id", "test").put("class", "test"));

        assertFalse(cleaner.whitelist.isSafeAttribute("div", element, new Attribute("data-id", "test")));
        assertFalse(cleaner.whitelist.isSafeAttribute("div", element, new Attribute("class", "test")));

        whitelist.addAttributeRule("div", "class", null);
        assertTrue(cleaner.whitelist.isSafeAttribute("div", element, new Attribute("class", "test")));
    }

    @Test
    public void testWhiteListEnforcedAttributes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element element = new Element("input", null, new Attributes().put("type", "text"));
        Attributes enforcedAttrs = whitelist.getEnforcedAttributes("input");
        assertTrue(enforcedAttrs.containsKey("type"));
        assertTrue(enforcedAttrs.containsKey("name"));

        int numDiscarded = cleaner.copySafeNodes(element, new Element("input", null, new Attributes()));
        assertTrue(numDiscarded > 0);
    }

    @Test
    public void testCopySafeNodesRecursively() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.parse("<html><body><div id='test'><script>alert('XSS')</script><h1 id='header' class='header' style='color:red'>Hello World<p>Nested <div class='nested'><h2>Nested Header</h2></div></p></h1></div></body></html>");
        Element body = dirtyDocument.body();
        Element cleanBody = new Element(Tag.BODY, body.baseUri());

        int numDiscarded = cleaner.copySafeNodes(body, cleanBody);

        // Check that the header element was copied correctly
        Element header = cleanBody.getElementById("header");
        assertTrue(header.tagName().equals("h1"));
        assertTrue(header.hasClass("header"));
        assertFalse(header.hasAttr("id"));
        assertTrue(header.hasAttr("style"));
        assertEquals(header.attr("style"), "color:red");

        Element nestedDiv = header.getFirstChild();
        assertTrue(nestedDiv.tagName().equals("div"));
        assertTrue(nestedDiv.getClassList().contains("nested"));

        Element nestedHeader = nestedDiv.getFirstChild();
        assertTrue(nestedHeader.tagName().equals("h2"));
        assertFalse(nestedHeader.hasText());
    }

    @Test
    public void testCopySafeNodesWithEmptyAttributes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element dirtyElement = new Element("div", null, new Attributes().put("data-id", ""));
        int numDiscarded = cleaner.copySafeNodes(dirtyElement, new Element("div", null, new Attributes()));

        assertTrue(numDiscarded > 0);
        assertTrue(dirtyElement.toString().isEmpty());
    }

    @Test
    public void testCopySafeNodesWithEmptyTextNode() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element dirtyElement = new Element("div", null, null);
        dirtyElement.appendChild(new TextNode(""));

        Element cleanElement = new Element("div", null, null);
        int numDiscarded = cleaner.copySafeNodes(dirtyElement, cleanElement);

        assertTrue(numDiscarded > 0);
        assertTrue(cleanElement.childElementCount() > 0);
        assertTrue(cleanElement.child(0).toString().isEmpty());
    }

    @Test
    public void testCopySafeNodesWithTextNode() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element dirtyElement = new Element("div", null, null);
        dirtyElement.appendChild(new TextNode("Some text"));

        Element cleanElement = new Element("div", null, null);
        int numDiscarded = cleaner.copySafeNodes(dirtyElement, cleanElement);

        assertTrue(numDiscarded > 0);
        assertTrue(cleanElement.childElementCount() > 0);
        assertTrue(cleanElement.child(0).toString().equals("Some text"));
    }
}