package org.jsoup.parser; // update package as needed

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.parser.Tag.*;

public class TagTest {

    

    @Test
    public void testTagName() {
        Tag tag = new Tag("div");
        String name = tag.getName();
        assertEquals("div", name);
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("div");
        assertEquals(tag.getName(), "div");

        Tag unknownTag = Tag.valueOf("unknownTag");
        assertEquals(unknownTag.getName(), "unknownTag");
        assertFalse(unknownTag.isKnownTag());
    }

    @Test(expected = NullPointerException.class)
    public void testValueOfNullTagName() {
        Tag.valueOf(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfEmptyTagName() {
        Tag.valueOf("");
    }

    @Test
    public void testIsBlock() {
        Tag div = new Tag("div");
        assertTrue(div.isBlock());

        Tag span = new Tag("span");
        assertFalse(span.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag div = new Tag("div");
        assertTrue(div.formatAsBlock());

        Tag span = new Tag("span");
        assertFalse(span.formatAsBlock());

        Tag pre = new Tag("pre");
        assertTrue(pre.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag div = new Tag("div");
        assertTrue(div.canContainBlock());

        Tag p = new Tag("p");
        assertFalse(p.canContainBlock());
    }

    @Test
    public void testIsData() {
        Tag script = new Tag("script");
        assertTrue(script.isData());

        Tag p = new Tag("p");
        assertFalse(p.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag img = new Tag("img");
        assertFalse(img.isEmpty());

        Tag br = new Tag("br");
        assertTrue(br.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag img = new Tag("img");
        assertFalse(img.isSelfClosing());

        Tag imgSelfClosing = img.setSelfClosing();
        assertTrue(imgSelfClosing.isSelfClosing());

        Tag imgTag = Tag.valueOf("img");
        assertTrue(imgTag.isSelfClosing());

        Tag input = new Tag("input");
        assertTrue(input.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(isKnownTag("div"));
        assertFalse(isKnownTag("unknownTag"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag pre = new Tag("pre");
        assertTrue(pre.preserveWhitespace());

        Tag p = new Tag("p");
        assertFalse(p.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag input = new Tag("input");
        assertTrue(input.isFormListed());

        Tag p = new Tag("p");
        assertFalse(p.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag input = new Tag("input");
        assertTrue(input.isFormSubmittable());

        Tag p = new Tag("p");
        assertFalse(p.isFormSubmittable());
    }

    
}