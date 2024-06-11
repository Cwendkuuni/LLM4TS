package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {

    @Test
    public void testCreateCleaner() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist();
        Document dirtyDocument = Document.newDocument();
        dirtyDocument.body().appendElement(new Element("div", "class", "test"));
        dirtyDocument.body().appendText("Test content");

        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals(1, cleanDocument.body().childNodeSize());
        assertEquals("div", cleanDocument.body().child(0).tagName());
        assertEquals("test", cleanDocument.body().classAttr());
        assertNull(cleanDocument.body().text());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist();
        Document dirtyDocument = Document.newDocument();
        dirtyDocument.body().appendElement(new Element("div", "class", "test"));
        dirtyDocument.body().appendText("Test content");

        Cleaner cleaner = new Cleaner(whitelist);
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);

        dirtyDocument.body().removeChild(dirtyDocument.body().child(1));
        isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist();
        Document dirtyDocument = Document.newDocument();
        Element root = new Element("html");
        Element body = new Element("body");
        root.appendChild(body);

        body.appendChild(new Element("div", "class", "test"));
        body.appendChild(new Element("script").attr("src", "malicious.js"));
        body.appendChild(new TextNode("Test content"));

        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = Document.newDocument();
        int numDiscarded = cleaner.copySafeNodes(root, cleanDocument);

        assertEquals(1, cleanDocument.body().childNodeSize());
        assertEquals("div", cleanDocument.body().child(0).tagName());
        assertEquals("test", cleanDocument.body().classAttr());
        assertNull(cleanDocument.body().text());
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testIsSafeTag() {
        Whitelist whitelist = new Whitelist();
        List<String> allowedTags = Arrays.asList("div", "p", "span");
        for (String tag : allowedTags) {
            assertTrue(whitelist.isSafeTag(tag));
        }
        assertFalse(whitelist.isSafeTag("script"));
    }

    @Test
    public void testIsSafeAttribute() {
        Whitelist whitelist = new Whitelist();
        String allowedTag = "div";
        String allowedAttribute = "class";

        assertTrue(whitelist.isSafeAttribute(allowedTag, null, null));
        assertTrue(whitelist.isSafeAttribute(allowedTag, null, new Attribute("class", "test")));
        assertFalse(whitelist.isSafeAttribute(allowedTag, null, new Attribute("onclick", "alert('xss')")));
    }

    @Test
    public void testGetEnforcedAttributes() {
        Whitelist whitelist = new Whitelist();
        String tag = "input";

        List<Attribute> enforcedAttributes = whitelist.getEnforcedAttributes(tag);
        assertTrue(enforcedAttributes.size() > 0);
        assertFalse(enforcedAttributes.contains(new Attribute("onclick", "alert('xss')")));
    }
}