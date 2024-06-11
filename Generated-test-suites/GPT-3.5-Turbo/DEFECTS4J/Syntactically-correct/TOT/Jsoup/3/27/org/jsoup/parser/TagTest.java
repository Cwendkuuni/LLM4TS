package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());

        Tag unknown = Tag.valueOf("xyz");
        assertEquals("xyz", unknown.getName());
        assertFalse(Tag.tags.containsKey(unknown.getName()));
    }

    @Test
    public void testCanContain() {
        Tag parent = new Tag("div");
        parent.canContainBlock = true;
        parent.canContainInline = true;

        Tag blockChild = new Tag("p");
        blockChild.isBlock = true;

        Tag inlineChild = new Tag("span");
        inlineChild.isBlock = false;

        Tag selfChild = new Tag("div");
        selfChild.isBlock = true;

        assertTrue(parent.canContain(blockChild));
        assertTrue(parent.canContain(inlineChild));
        assertFalse(parent.canContain(selfChild));
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("p");
        tag.isBlock = true;
        assertTrue(tag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("div");
        tag.canContainBlock = true;
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("span");
        tag.isBlock = false;
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("p");
        tag.canContainInline = false;
        tag.empty = false;
        assertTrue(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("img");
        tag.empty = true;
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("pre");
        tag.preserveWhitespace = true;
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag parent = new Tag("div");
        parent.ancestors.add(new Tag("body"));
        parent.ancestors.add(new Tag("html"));

        assertEquals(parent.ancestors.get(0), parent.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag parent = new Tag("div");
        parent.ancestors.add(new Tag("body"));
        parent.ancestors.add(new Tag("html"));

        Tag child = new Tag("p");

        assertTrue(parent.isValidParent(child));

        Tag invalidParent = new Tag("span");
        assertFalse(invalidParent.isValidParent(child));
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        assertTrue(tag1.equals(tag2));

        tag1.isBlock = true;
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        assertEquals(tag1.hashCode(), tag2.hashCode());

        tag1.preserveWhitespace = true;
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.toString());
    }

    // additional test cases should be added to cover all methods and scenarios

}