package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.canContain(Tag.valueOf("em")));
        assertFalse(tag.canContain(Tag.valueOf("div")));
    }

    @Test
    public void testCanContain() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.canContain(Tag.valueOf("p")));
        assertTrue(tag.canContain(Tag.valueOf("span")));
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("script");
        assertTrue(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("img");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag tag = Tag.valueOf("p");
        assertNull(tag.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag tag = Tag.valueOf("div");
        Tag child = Tag.valueOf("p");
        assertTrue(tag.isValidParent(child));
    }
}
