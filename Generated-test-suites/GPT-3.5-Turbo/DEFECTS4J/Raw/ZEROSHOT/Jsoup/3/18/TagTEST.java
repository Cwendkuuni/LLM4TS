package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    private Tag tag;

    @Before
    public void setUp() {
        tag = new Tag("test");
    }

    @Test
    public void testName() {
        assertEquals("test", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag defaultTag = Tag.valueOf("unknown");
        assertEquals("unknown", defaultTag.getName());
        assertFalse(defaultTag.isBlock());
        assertTrue(defaultTag.canContainBlock());
    }

    @Test
    public void testCanContain() {
        Tag childBlock = new Tag("childBlock");
        childBlock.isBlock = true;
        assertTrue(tag.canContain(childBlock));

        Tag childInline = new Tag("childInline");
        childInline.isBlock = false;
        assertTrue(tag.canContain(childInline));

        Tag childSame = new Tag("same");
        childSame.isBlock = true;
        assertTrue(tag.canContain(childSame));

        Tag childEmpty = new Tag("empty");
        childEmpty.empty = true;
        assertFalse(tag.canContain(childEmpty));
    }

    @Test
    public void testIsBlock() {
        assertTrue(tag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        assertNull(tag.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag childTag = new Tag("child");
        childTag.ancestors.add(tag);
        assertTrue(tag.isValidParent(childTag));
    }

    @Test
    public void testEquals() {
        Tag equalTag = new Tag("test");
        assertTrue(tag.equals(equalTag));

        Tag differentTag = new Tag("different");
        assertFalse(tag.equals(differentTag));
    }

    @Test
    public void testHashCode() {
        Tag equalTag = new Tag("test");
        assertEquals(tag.hashCode(), equalTag.hashCode());

        Tag differentTag = new Tag("different");
        assertNotEquals(tag.hashCode(), differentTag.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("test", tag.toString());
    }

}