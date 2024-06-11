package org.jsoup.parser;

import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Tag;
import org.jsoup.helper.Validate;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

public class TagTest {

    @Test
    public void testName() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("div");
        assertNotNull(tag);
        assertTrue(tag.isKnownTag());
        assertFalse(tag.isEmpty());
    }

    @Test
    public void testValueOfUnknown() {
        Tag tag = Tag.valueOf("abc");
        assertNotNull(tag);
        assertFalse(tag.isKnownTag());
        assertFalse(tag.isEmpty());
    }

    @Test
    public void testValueOfNull() {
        Tag tag = Tag.valueOf(null);
        assertNull(tag);
    }

    @Test
    public void testIsBlock() {
        Tag tag1 = Tag.valueOf("div");
        assertTrue(tag1.isBlock());

        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag1 = Tag.valueOf("div");
        assertTrue(tag1.formatAsBlock());

        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag1 = Tag.valueOf("div");
        assertTrue(tag1.canContainBlock());

        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag1 = Tag.valueOf("div");
        assertFalse(tag1.isInline());

        Tag tag2 = Tag.valueOf("span");
        assertTrue(tag2.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag1 = Tag.valueOf("div");
        assertFalse(tag1.isData());

        Tag tag2 = Tag.valueOf("span");
        assertTrue(tag2.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag1 = Tag.valueOf("div");
        assertFalse(tag1.isEmpty());

        Tag tag2 = Tag.valueOf("img");
        assertTrue(tag2.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag1 = Tag.valueOf("div");
        assertFalse(tag1.isSelfClosing());

        Tag tag2 = Tag.valueOf("img");
        assertTrue(tag2.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("abc"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag1 = Tag.valueOf("div");
        assertFalse(tag1.preserveWhitespace());

        Tag tag2 = Tag.valueOf("pre");
        assertTrue(tag2.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag1 = Tag.valueOf("input");
        assertTrue(tag1.isFormListed());

        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag1 = Tag.valueOf("input");
        assertTrue(tag1.isFormSubmittable());

        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.isFormSubmittable());
    }

}