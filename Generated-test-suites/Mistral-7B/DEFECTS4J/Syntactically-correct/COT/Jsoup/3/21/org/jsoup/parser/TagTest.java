package org.jsoup.parser;

import org.apache.commons.lang3.Validate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TagTest {

    

    @Test
    public void testTagValueOf() {
        Tag tag = Tag.valueOf("p");
        String name = tag.getName();
        assertEquals("p", name);

        tag = Tag.valueOf("DIV");
        name = tag.getName();
        assertEquals("div", name);

        tag = Tag.valueOf("UNKNOWN");
        name = tag.getName();
        assertEquals("UNKNOWN", name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagValueOfNull() {
        Tag.valueOf(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagValueOfEmpty() {
        Tag.valueOf("");
    }

    @Test
    public void testTagCanContain() {
        Tag parent = new Tag("DIV");
        Tag child1 = new Tag("P");
        Tag child2 = new Tag("DIV");
        Tag child3 = new Tag("SPAN");
        Tag child4 = new Tag("DIV");

        assertTrue(parent.canContain(child1));
        assertTrue(parent.canContain(child2));
        assertTrue(parent.canContain(child3));
        assertTrue(parent.canContain(child4));

        Tag invalidChild1 = new Tag("DIV");
        assertFalse(parent.canContain(invalidChild1));

        Tag invalidChild2 = new Tag("P");
        assertFalse(child1.canContain(invalidChild2));
    }

    @Test
    public void testTagIsBlock() {
        Tag tag = new Tag("DIV");
        assertTrue(tag.isBlock());

        tag = new Tag("P");
        assertFalse(tag.isBlock());
    }

    @Test
    public void testTagCanContainBlock() {
        Tag tag = new Tag("DIV");
        assertTrue(tag.canContainBlock());

        tag = new Tag("SPAN");
        assertTrue(tag.canContainBlock());

        tag = new Tag("P");
        assertFalse(tag.canContainBlock());
    }

    @Test
    public void testTagIsInline() {
        Tag tag = new Tag("DIV");
        assertFalse(tag.isInline());

        tag = new Tag("SPAN");
        assertTrue(tag.isInline());
    }

    @Test
    public void testTagIsData() {
        Tag tag = new Tag("DIV");
        assertFalse(tag.isData());

        tag = new Tag("SCRIPT");
        assertTrue(tag.isData());
    }

    @Test
    public void testTagIsEmpty() {
        Tag tag = new Tag("IMG");
        assertTrue(tag.isEmpty());

        tag = new Tag("DIV");
        assertFalse(tag.isEmpty());
    }

    @Test
    public void testTagPreserveWhitespace() {
        Tag tag = new Tag("PRE");
        assertTrue(tag.preserveWhitespace());

        tag = new Tag("DIV");
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testTagGetImplicitParent() {
        Tag child = new Tag("DIV");
        child.ancestors = new ArrayList<>(Arrays.asList(new Tag("UL"), new Tag("BODY")));

        Tag parent = child.getImplicitParent();
        assertEquals("BODY", parent.getName());

        child.ancestors = Collections.emptyList();
        parent = child.getImplicitParent();
        assertNull(parent);
    }

    @Test
    public void testTagIsValidParent() {
        Tag child = new Tag("DIV");
        Tag parent1 = new Tag("UL");
        Tag parent2 = new Tag("DIV");

        assertTrue(child.isValidParent(parent1));
        assertFalse(child.isValidParent(parent2));

        parent2.ancestors = new ArrayList<>(Arrays.asList(new Tag("BODY")));
        assertTrue(child.isValidParent(parent2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagEqualsNull() {
        Tag tag = new Tag("DIV");
        assertNotEquals(tag, null);
    }

    @Test
    public void testTagEqualsSameInstance() {
        Tag tag = new Tag("DIV");
        assertTrue(tag.equals(tag));
    }

    @Test
    public void testTagEqualsSameClass() {
        Tag tag1 = new Tag("DIV");
        Tag tag2 = new Tag("DIV");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testTagEqualsDifferentClass() {
        Tag tag1 = new Tag("DIV");
        Object tag2 = new Object();
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testTagEqualsDifferentTagName() {
        Tag tag1 = new Tag("DIV");
        Tag tag2 = new Tag("SPAN");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testTagEqualsSameProperties() {
        Tag tag1 = new Tag("DIV").setBlock(false).setCanContainBlock(true).setCanContainInline(false).setEmpty(true).setPreserveWhitespace(true);
        Tag tag2 = new Tag("DIV").setBlock(false).setCanContainBlock(true).setCanContainInline(false).setEmpty(true).setPreserveWhitespace(true);
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testTagHashCode() {
        Tag tag1 = new Tag("DIV");
        Tag tag2 = new Tag("DIV");

        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag2.hashCode();

        assertEquals(hashCode1, hashCode2);

        tag1.setBlock(false);
        tag2.setBlock(false);

        hashCode1 = tag1.hashCode();
        hashCode2 = tag2.hashCode();

        assertEquals(hashCode1, hashCode2);

        tag1.setCanContainBlock(false);
        tag2.setCanContainBlock(false);

        hashCode1 = tag1.hashCode();
        hashCode2 = tag2.hashCode();

        assertEquals(hashCode1, hashCode2);

        tag1.setCanContainInline(false);
        tag2.setCanContainInline(false);

        hashCode1 = tag1.hashCode();
        hashCode2 = tag2.hashCode();

        assertEquals(hashCode1, hashCode2);

        tag1.setEmpty(true);
        tag2.setEmpty(true);

        hashCode1 = tag1.hashCode();
        hashCode2 = tag2.hashCode();

        assertEquals(hashCode1, hashCode2);

        tag1.setPreserveWhitespace(true);
        tag2.setPreserveWhitespace(true);

        hashCode1 = tag1.hashCode();
        hashCode2 = tag2.hashCode();

        assertEquals(hashCode1, hashCode2);

        tag1.setTagName("SPAN");
        tag2.setTagName("SPAN");

        hashCode1 = tag1.hashCode();
        hashCode2 = tag2.hashCode();

        assertEquals(hashCode1, hashCode2);

        tag1.setBlock(true);
        tag2.setBlock(true);

        hashCode1 = tag1.hashCode();
        hashCode2 = tag2.hashCode();

        assertNotEquals(hashCode1, hashCode2);

        tag1.setCanContainBlock(true);
        tag2.setCanContainBlock(false);

        hashCode1 = tag1.hashCode();
        hashCode2 = tag2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testTagToString() {
        Tag tag = new Tag("DIV");
        assertEquals("DIV", tag.toString());
    }

    
}