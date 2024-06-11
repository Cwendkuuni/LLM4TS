package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class TagTest {

    private Tag tag;

    @Before
    public void setUp() {
        tag = new Tag("testTag");
    }

    @Test
    public void testGetName() {
        assertEquals("testTag", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("test1");
        Tag tag2 = Tag.valueOf("test1");
        Tag tag3 = Tag.valueOf("test2");

        assertEquals(tag1, tag2);
        assertNotEquals(tag1, tag3);
        assertNotEquals(tag2, tag3);
    }

    @Test
    public void testCanContain() {
        Tag childBlock = new Tag("childBlock");
        childBlock.isBlock = true;
        childBlock.canContainBlock = true;
        Tag childInline = new Tag("childInline");
        childInline.isBlock = false;
        childInline.canContainInline = true;
        Tag childData = new Tag("childData");
        childData.canContainInline = false;
        childData.empty = false;

        assertTrue(tag.canContain(childBlock));
        assertTrue(tag.canContain(childInline));
        assertFalse(tag.canContain(childData));
    }

    @Test
    public void testIsBlock() {
        assertTrue(tag.isBlock());
        tag.isBlock = false;
        assertFalse(tag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        assertTrue(tag.canContainBlock());
        tag.canContainBlock = false;
        assertFalse(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        assertFalse(tag.isInline());
        tag.isBlock = false;
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        assertFalse(tag.isData());
        tag.canContainInline = false;
        tag.empty = false;
        assertTrue(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tag.isEmpty());
        tag.empty = true;
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        assertFalse(tag.preserveWhitespace());
        tag.preserveWhitespace = true;
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        assertEquals(null, tag.getImplicitParent());

        Tag parent = new Tag("parent");
        tag.ancestors = new ArrayList<>();
        tag.ancestors.add(parent);
        assertEquals(parent, tag.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag child1 = new Tag("child1");
        Tag child2 = new Tag("child2");
        Tag child3 = new Tag("child3");

        assertTrue(tag.isValidParent(child1));

        tag.ancestors = new ArrayList<>();
        tag.ancestors.add(child1);
        assertTrue(tag.isValidParent(child1));
        assertFalse(tag.isValidParent(child2));
        assertFalse(tag.isValidParent(child3));
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag1");
        Tag tag3 = new Tag("tag2");

        assertTrue(tag1.equals(tag2));
        assertFalse(tag1.equals(tag3));
        assertFalse(tag2.equals(tag3));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag1");
        Tag tag3 = new Tag("tag2");

        assertEquals(tag1.hashCode(), tag2.hashCode());
        assertNotEquals(tag1.hashCode(), tag3.hashCode());
        assertNotEquals(tag2.hashCode(), tag3.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("testTag", tag.toString());
    }
}