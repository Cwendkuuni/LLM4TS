package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit tests for the Tag class.
 */
public class TagTest {

    @Test
    public void testTagsAreRegistered() {
        // Check if some common tags are registered
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");
        Tag head = Tag.valueOf("head");
        Tag script = Tag.valueOf("script");

        assertTrue(html instanceof Tag);
        assertTrue(p instanceof Tag);
        assertTrue(head instanceof Tag);
        assertTrue(script instanceof Tag);

        assertEquals(Tag.class, html.getClass());
        assertEquals(Tag.class, p.getClass());
        assertEquals(Tag.class, head.getClass());
        assertEquals(Tag.class, script.getClass());

        assertEquals("html", html.getName());
        assertEquals("p", p.getName());
        assertEquals("head", head.getName());
        assertEquals("script", script.getName());

        assertTrue(html.isKnownTag());
        assertTrue(p.isKnownTag());
        assertTrue(head.isKnownTag());
        assertTrue(script.isKnownTag());
    }

    @Test
    public void testUnknownTag() {
        // Test unknown tag creation
        Tag unknownTag = Tag.valueOf("unknownTag");

        assertFalse(unknownTag.isKnownTag());
        assertEquals("unknownTag", unknownTag.getName());
    }

    @Test
    public void testIsBlock() {
        // Test block tag
        Tag p = Tag.valueOf("p");
        assertTrue(p.isBlock());

        Tag input = Tag.valueOf("input");
        assertFalse(input.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        // Test format as block tag
        Tag p = Tag.valueOf("p");
        assertTrue(p.formatAsBlock());

        Tag input = Tag.valueOf("input");
        assertFalse(input.formatAsBlock());
    }

    @Test
    public void testIsEmpty() {
        // Test empty tag
        Tag img = Tag.valueOf("img");
        assertFalse(img.isEmpty());

        Tag meta = Tag.valueOf("meta");
        assertTrue(meta.isEmpty());
    }

    @Test
    public void testIsData() {
        // Test data only tag
        Tag script = Tag.valueOf("script");
        assertFalse(script.isData());

        Tag data = Tag.valueOf("data");
        assertTrue(data.isData());
    }

    @Test
    public void testIsSelfClosing() {
        // Test self closing tag
        Tag img = Tag.valueOf("img");
        assertTrue(img.isSelfClosing());

        Tag p = Tag.valueOf("p");
        assertFalse(p.isSelfClosing());
    }

    @Test
    public void testIsFormListed() {
        // Test form listed tag
        Tag input = Tag.valueOf("input");
        assertTrue(input.isFormListed());

        Tag p = Tag.valueOf("p");
        assertFalse(p.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        // Test form submittable tag
        Tag input = Tag.valueOf("input");
        assertTrue(input.isFormSubmittable());

        Tag p = Tag.valueOf("p");
        assertFalse(p.isFormSubmittable());
    }

    @Test
    public void testPreserveWhitespace() {
        // Test preserve whitespace tag
        Tag pre = Tag.valueOf("pre");
        assertTrue(pre.preserveWhitespace());

        Tag p = Tag.valueOf("p");
        assertFalse(p.preserveWhitespace());
    }
}