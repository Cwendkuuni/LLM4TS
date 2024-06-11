package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {

    @Test
    public void testCreateCleaner() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertEquals(whitelist, cleaner.whitelist);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateCleanerNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testCleanNullDocument() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        assertNull(cleaner.clean(null));
    }

    @Test
    public void testCleanEmptyDocument() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        Document dirtyDocument = Document.createShell("");
        Document clean = cleaner.clean(dirtyDocument);
        assertTrue(clean.body().childNodeSize() == 0);
    }

    @Test
    public void testCleanValidDocument() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        String html = "<html><body><p>Hello world</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document clean = cleaner.clean(dirtyDocument);
        assertEquals(1, clean.body().childNodeSize());
        Element element = clean.body().getFirstElementChild();
        assertEquals(Tag.HTML, element.tag());
        assertEquals(Tag.BODY, element.tagName());
        assertEquals("p", element.child(0).tagName());
        assertEquals("Hello world", element.child(0).text());
    }

    @Test
    public void testCleanInvalidDocument() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        String html = "<html><body><script>alert('Hello world')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document clean = cleaner.clean(dirtyDocument);
        assertEquals(1, clean.body().childNodeSize());
        Element element = clean.body().getFirstElementChild();
        assertEquals(Tag.HTML, element.tag());
        assertEquals(Tag.BODY, element.tagName());
        assertNull(element.child(0));
    }

    @Test
    public void testIsValidNullDocument() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        assertFalse(cleaner.isValid(null));
    }

    @Test
    public void testIsValidEmptyDocument() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        Document dirtyDocument = Document.createShell("");
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidValidDocument() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        String html = "<html><body><p>Hello world</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidInvalidDocument() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        String html = "<html><body><script>alert('Hello world')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodesEmptySource() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        Element source = Element.empty();
        Element dest = Element.empty();
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(0, numDiscarded);
        assertEquals(0, dest.childNodeSize());
    }

    @Test
    public void testCopySafeNodesEmptyDestination() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        Element source = new Element(Tag.H1, "test.com");
        Element dest = Element.empty();
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(1, numDiscarded);
        assertEquals(0, dest.childNodeSize());
    }

    @Test
    public void testCopySafeNodesValidSource() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        String html = "<html><body><p>Hello world</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element source = dirtyDocument.body();
        Element dest = new Element(Tag.DIV, "test.com");
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(1, numDiscarded);
        assertEquals(1, dest.childNodeSize());
        Element element = dest.child(0);
        assertEquals(Tag.P, element.tagName());
        assertEquals("Hello world", element.text());
    }

    @Test
    public void testCopySafeNodesInvalidSource() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        String html = "<html><body><script>alert('Hello world')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element source = dirtyDocument.body();
        Element dest = new Element(Tag.DIV, "test.com");
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(1, numDiscarded);
        assertEquals(0, dest.childNodeSize());
    }

    @Test
    public void testCreateSafeElementEmptyAttributes() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        Element sourceEl = new Element(Tag.P, "test.com");
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        assertEquals(Tag.P, meta.el.tagName());
        assertEquals(0, meta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElementSingleAttribute() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        String tagName = "input";
        String attributeName = "type";
        String attributeValue = "text";
        Element sourceEl = new Element(Tag.valueOf(tagName), "test.com", new Attributes().put(attributeName, attributeValue));
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        assertEquals(tagName, meta.el.tagName());
        assertEquals(1, meta.numAttribsDiscarded);
        assertEquals(attributeName, meta.el.attributeNames().get(0));
        assertEquals(attributeValue, meta.el.attributeValue(attributeName));
    }

    @Test
    public void testCreateSafeElementMultipleAttributes() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        String tagName = "input";
        String attributeName1 = "type";
        String attributeValue1 = "text";
        String attributeName2 = "name";
        String attributeValue2 = "username";
        Element sourceEl = new Element(Tag.valueOf(tagName), "test.com", new Attributes()
                .put(attributeName1, attributeValue1).put(attributeName2, attributeValue2));
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        assertEquals(tagName, meta.el.tagName());
        assertEquals(2, meta.numAttribsDiscarded);
        assertEquals(attributeName1, meta.el.attributeNames().get(0));
        assertEquals(attributeValue1, meta.el.attributeValue(attributeName1));
        assertEquals(attributeName2, meta.el.attributeNames().get(1));
        assertEquals(attributeValue2, meta.el.attributeValue(attributeName2));
    }

    @Test
    public void testIsSafeTag() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        List<String> tags = Arrays.asList("p", "input", "div", "script");
        for (String tag : tags) {
            assertTrue(cleaner.whitelist.isSafeTag(tag));
            assertTrue(cleaner.whitelist.isSafeTag(Tag.valueOf(tag)));
            assertTrue(cleaner.whitelist.isSafeTag(Tag.valueOf(tag), null, null));
            assertTrue(cleaner.whitelist.isSafeTag(Tag.valueOf(tag), null, new Element("", "test.com")));
            assertTrue(cleaner.whitelist.isSafeTag(Tag.valueOf(tag), new Element("", "test.com"), null));
            assertTrue(cleaner.whitelist.isSafeTag(Tag.valueOf(tag), new Element("", "test.com"), new Element("", "test.com")));
        }
        assertFalse(cleaner.whitelist.isSafeTag("script"));
        assertFalse(cleaner.whitelist.isSafeTag("button"));
    }

    @Test
    public void testIsSafeAttribute() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        List<String> tags = Arrays.asList("input", "button");
        String[] attributeNames = {"class", "type", "disabled"};
        for (String tag : tags) {
            for (String attributeName : attributeNames) {
                assertFalse(cleaner.whitelist.isSafeAttribute(tag, null, null, attributeName));
                assertFalse(cleaner.whitelist.isSafeAttribute(tag, null, null, attributeName, null));
                assertFalse(cleaner.whitelist.isSafeAttribute(tag, null, new Element("", "test.com"), attributeName));
                assertFalse(cleaner.whitelist.isSafeAttribute(tag, null, new Element("", "test.com"), attributeName, null));
                assertTrue(cleaner.whitelist.isSafeAttribute(tag, new Element("", "test.com"), null, attributeName));
                assertTrue(cleaner.whitelist.isSafeAttribute(tag, new Element("", "test.com"), new Element("", "test.com"), attributeName, null));
                assertTrue(cleaner.whitelist.isSafeAttribute(tag, new Element("", "test.com"), new Element("", "test.com"), attributeName, new Element("", "test.com")));
            }
        }
        Element sourceEl = new Element("input", "test.com", new Attributes().put("class", "button"));
        assertFalse(cleaner.whitelist.isSafeAttribute("input", null, null, "class"));
        assertFalse(cleaner.whitelist.isSafeAttribute("input", null, null, "class", null));
        assertFalse(cleaner.whitelist.isSafeAttribute("input", null, new Element("", "test.com"), "class"));
        assertFalse(cleaner.whitelist.isSafeAttribute("input", null, new Element("", "test.com"), "class", null));
        assertTrue(cleaner.whitelist.isSafeAttribute("input", new Element("button", "test.com"), null, "class"));
        assertTrue(cleaner.whitelist.isSafeAttribute("input", new Element("button", "test.com"), new Element("", "test.com"), "class", null));
        assertTrue(cleaner.whitelist.isSafeAttribute("input", new Element("button", "test.com"), new Element("", "test.com"), "class", new Element("", "test.com")));
        assertFalse(cleaner.whitelist.isSafeAttribute("input", new Element("button", "test.com"), null, "class", new Element("script", "test.com")));
        assertFalse(cleaner.whitelist.isSafeAttribute("input", new Element("button", "test.com"), new Element("script", "test.com"), "class", null));
        assertFalse(cleaner.whitelist.isSafeAttribute("input", new Element("button", "test.com"), new Element("script", "test.com"), "class", new Element("script", "test.com")));
        assertTrue(cleaner.whitelist.isSafeAttribute("input", new Element("button", "test.com"), new Element("div", "test.com"), "class", null));
        assertTrue(cleaner.whitelist.isSafeAttribute("input", new Element("button", "test.com"), new Element("div", "test.com"), "class", new Element("div", "test.com")));
        assertTrue(cleaner.whitelist.isSafeAttribute("input", new Element("button", "test.com"), new Element("div", "test.com"), "class", new Element("div", "test.com")));
    }
}