package org.jsoup.parser;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.junit.Test;
import org.jsoup.parser.Tag;

/**
 * Tag test cases.
 *
 * @author XYZ
 */
public class TagTest {

    @Test
    public void testgetValueOf() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());

        tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());

        tag = Tag.valueOf("unknown");
        assertEquals(Tag.defaultAncestor, tag);
    }

    @Test
    public void testCanContain() {
        Tag parent = new Tag("div");
        Tag child1 = new Tag("p");
        Tag child2 = new Tag("div");
        Tag child3 = new Tag("p");
        Tag child4 = new Tag("div");

        assertTrue(parent.canContain(child1));
        assertTrue(parent.canContain(child2));
        assertTrue(parent.canContain(child3));
        assertTrue(parent.canContain(child4));

        Tag invalidParent = new Tag("p");
        assertFalse(parent.canContain(invalidParent));
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.isBlock());

        tag = new Tag("p");
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("script");
        assertTrue(tag.isData());

        tag = new Tag("p");
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("img");
        assertFalse(tag.isEmpty());

        tag = new Tag("br");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("pre");
        assertTrue(tag.preserveWhitespace());

        tag = new Tag("p");
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        Tag tag3 = new Tag("p");

        assertTrue(tag1.equals(tag1));
        assertTrue(tag1.equals(tag2));
        assertFalse(tag1.equals(tag3));

        tag2.setTagName("p");
        assertFalse(tag1.equals(tag2));
    }

    @Test(expected = NullPointerException.class)
    public void testCanContainNull() {
        Tag parent = new Tag("div");
        Tag child = null;
        parent.canContain(child);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanContainSelf() {
        Tag parent = new Tag("div");
        parent.canContain(parent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidParentChild() {
        Tag parent = new Tag("div");
        Tag child = new Tag("span");
        parent.canContain(child);
        child.canContain(parent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidParentChild2() {
        Tag parent = new Tag("div");
        Tag child = new Tag("div");
        parent.canContain(child);
        child.canContain(parent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidParentChild3() {
        Tag parent = new Tag("div");
        Tag child = new Tag("div");
        parent.canContain(child);
        child.canContain(parent);
        child.canContain(parent);
    }

    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testSetAncestor() {
        Tag tag = new Tag("div");
        List<String> ancestors = Arrays.asList("html", "body");
        tag.setAncestor(ancestors.toArray(new String[ancestors.size()]));

        Tag ancestor = Tag.valueOf("html");
        assertEquals(ancestor, tag.getImplicitParent());

        ancestor = Tag.valueOf("body");
        assertTrue(Arrays.asList(ancestor, ancestor).containsAll(tag.ancestors));
    }

    @Test
    public void testIsValidParent() {
        Tag parent = new Tag("div");
        Tag child1 = new Tag("p");
        Tag child2 = new Tag("div");
        Tag child3 = new Tag("div");

        assertTrue(parent.isValidParent(child1));
        assertTrue(parent.isValidParent(child2));
        assertFalse(parent.isValidParent(child3));

        child3.ancestors = Arrays.asList(parent.getName()).asList();
        assertTrue(parent.isValidParent(child3));
    }
}