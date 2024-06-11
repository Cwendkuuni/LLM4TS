package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CleanerTest {

    @Test
    public void testCleanerInstance() {
        Whitelist whitelist = new SimpleNodeFilterElementFilter("div", "class");
        Cleaner cleaner = new Cleaner(whitelist);
        assertTrue(cleaner != null);
    }

    @Test
    public void testCleanInvalidDocument() {
        Whitelist whitelist = new SimpleNodeFilterElementFilter("div", "class");
        Document dirtyDocument = Document.createFromString("<script>alert('XSS');</script>", "http://test.com");
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleanDocument != null);
        assertTrue(cleanDocument.body().childNodeSize() == 0);
    }

    @Test
    public void testCleanValidDocument() {
        Whitelist whitelist = new SimpleNodeFilterElementFilter("div", "class");
        Document dirtyDocument = Document.createFromString("<div class=\"test\"><p>Hello World</p></div>", "http://test.com");
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleanDocument != null);
        assertTrue(cleanDocument.body().childNodeSize() == 1);
        assertTrue(cleanDocument.body().get(0) instanceof Element);
        assertTrue(((Element) cleanDocument.body().get(0)).tagName().equals("div"));
        assertTrue(((Element) cleanDocument.body().get(0)).classAttribute().getValue().equals("test"));
    }

    @Test
    public void testIsValidDocument() {
        Whitelist whitelist = new SimpleNodeFilterElementFilter("div", "class");
        Document dirtyDocument = Document.createFromString("<div class=\"test\"><p>Hello World</p></div>", "http://test.com");
        Cleaner cleaner = new Cleaner(whitelist);
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidInvalidDocument() {
        Whitelist whitelist = new SimpleNodeFilterElementFilter("div", "class");
        Document dirtyDocument = Document.createFromString("<script>alert('XSS');</script>", "http://test.com");
        Cleaner cleaner = new Cleaner(whitelist);
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new SimpleNodeFilterElementFilter("div", "class");
        Document dirtyDocument = Document.createFromString("<div class=\"test\"><p>Hello World</p><script>alert('XSS');</script></div>", "http://test.com");
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element testElement = cleanDocument.body().get(0);
        int numDiscarded = cleaner.copySafeNodes(testElement, new Element("div"));
        assertTrue(numDiscarded == 1);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new SimpleNodeFilterElementFilter("div", "class");
        Document dirtyDocument = Document.createFromString("<div class=\"test\" id=\"testId\" title=\"testTitle\">Hello World</div>", "http://test.com");
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element sourceElement = cleanDocument.body().get(0);
        ElementMeta meta = cleaner.createSafeElement(sourceElement);
        Element destElement = meta.el;
        List<Attribute> attributes = new ArrayList<>();
        for (Attribute attribute : sourceElement.attributes()) {
            Attribute destAttribute = new Attribute(attribute.tag(), attribute.key(), attribute.value());
            attributes.add(destAttribute);
            destElement.attributes().put(destAttribute);
        }
        assertTrue(destElement.tagName().equals("div"));
        assertTrue(destElement.classAttribute().getValue().equals("test"));
        assertTrue(destElement.attributes().size() == 3);
        assertTrue(destElement.idAttribute().getValue().equals("testId"));
        assertTrue(destElement.tagName().equals("div"));
        assertTrue(destElement.tagName().equals("title"));
        assertTrue(destElement.tagName().equals("title"));
        assertTrue(destElement.attributes().get(1).getKey().equals("id"));
        assertTrue(destElement.attributes().get(2).getKey().equals("title"));
    }
}