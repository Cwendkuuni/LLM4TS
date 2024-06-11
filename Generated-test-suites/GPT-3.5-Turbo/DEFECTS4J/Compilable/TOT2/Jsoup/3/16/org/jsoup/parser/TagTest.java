package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOf_unknownTag() {
        Tag tag = Tag.valueOf("unknown");
        assertNotNull(tag);
    }

    @Test
    public void testValueOf_definedTag() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testCanContain_blockTagContainingInlineTag() {
        Tag parent = Tag.valueOf("div");
        Tag child = Tag.valueOf("span");
        assertTrue(parent.canContain(child));
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
        Tag tag = Tag.valueOf("span");
        assertTrue(tag.isInline());
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
    public void testIsValidParentValid() {
        Tag parent = Tag.valueOf("div");
        Tag child = Tag.valueOf("span");
        assertTrue(parent.isValidParent(child));
    }

    @Test
    public void testIsValidParentInvalid() {
        Tag parent = Tag.valueOf("div");
        Tag child = Tag.valueOf("p");
        assertFalse(parent.isValidParent(child));
    }

    @Test
    public void testEquals_sameTag() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testEquals_differentTag() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag1.equals(tag2));
    }

}