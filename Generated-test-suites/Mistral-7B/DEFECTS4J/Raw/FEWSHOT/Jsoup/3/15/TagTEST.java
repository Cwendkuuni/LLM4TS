package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import org.jsoup.parser.Tag;

public class TagTest {

    @Test
    public void testCanContain_ValidParentChild() {
        Tag parent = Tag.valueOf("div");
        Tag child = Tag.valueOf("p");

        assertTrue(parent.canContain(child));
    }

    @Test
    public void testCanContain_InvalidParentChild() {
        Tag parent = Tag.valueOf("p");
        Tag child = Tag.valueOf("div");

        assertFalse(parent.canContain(child));
    }

    @Test
    public void testCanContain_SelfAsParent() {
        Tag tag = Tag.valueOf("p");

        assertTrue(tag.canContain(tag));
    }

    @Test
    public void testCanContain_EmptyTag() {
        Tag tag = Tag.valueOf("img");

        assertTrue(tag.isEmpty());
        assertTrue(tag.canContain(Tag.VALUE_OF("p")));
    }

    @Test
    public void testCanContain_DataTag() {
        Tag tag = Tag.VALUE_OF("script");

        assertTrue(tag.isData());
        assertTrue(tag.canContain(Tag.VALUE_OF("script")));
        assertFalse(tag.canContain(Tag.VALUE_OF("p")));
    }

    @Test
    public void testCanContain_HeadTag() {
        Tag tag = Tag.VALUE_OF("head");

        assertTrue(tag.canContain(Tag.VALUE_OF("base")));
        assertTrue(tag.canContain(Tag.VALUE_OF("script")));
        assertFalse(tag.canContain(Tag.VALUE_OF("p")));
    }

    @Test
    public void testCanContain_DTDDTag() {
        Tag dtTag = Tag.VALUE_OF("dt");
        Tag ddTag = Tag.VALUE_OF("dd");

        assertFalse(dtTag.canContain(ddTag));
        assertFalse(ddTag.canContain(dtTag));
    }

    @Test
    public void testCanContain_EmptyParent() {
        Tag tag = Tag.VALUE_OF("body");

        assertTrue(tag.isEmpty());
        assertFalse(tag.canContain(Tag.VALUE_OF("p")));
    }

    @Test
    public void testCanContain_FormTag() {
        Tag formTag = Tag.VALUE_OF("form");

        assertTrue(formTag.canContain(Tag.VALUE_OF("input")));
        assertFalse(formTag.canContain(Tag.VALUE_OF("p")));
    }

    @Test
    public void testCanContain_TableTag() {
        Tag tableTag = Tag.VALUE_OF("table");

        assertTrue(tableTag.canContain(Tag.VALUE_OF("tr")));
        assertFalse(tableTag.canContain(Tag.VALUE_OF("p")));
    }

    @Test
    public void testCanContain_ULTag() {
        Tag ulTag = Tag.VALUE_OF("ul");

        assertTrue(ulTag.canContain(Tag.VALUE_OF("li")));
        assertFalse(ulTag.canContain(Tag.VALUE_OF("p")));
    }
}