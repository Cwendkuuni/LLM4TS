package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.Validate;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagTest {

    private static Tag tag1, tag2, tag3, tag4, tag5, tag6, tagDefaultAncestor;

    @BeforeClass
    public static void setup() {
        tagDefaultAncestor = Tag.valueOf("BODY");
        tag1 = Tag.valueOf("P");
        tag2 = Tag.valueOf("DIV");
        tag3 = Tag.valueOf("SPAN");
        tag4 = Tag.valueOf("IMG");
        tag5 = Tag.valueOf("TABLE");
        tag6 = Tag.valueOf("LI");
    }

    // Basic tests for Tag constructor and getters

    @Test
    public void testTagConstructor() {
        assertEquals("p", tag1.getName());
    }

    @Test
    public void testTagGetters() {
        assertEquals(true, tag1.isBlock());
        assertEquals(true, tag1.canContainBlock());
        assertEquals(true, tag1.canContainInline());
        assertEquals(false, tag1.isData());
        assertEquals(false, tag1.isEmpty());
        assertEquals(false, tag1.preserveWhitespace());
        assertNull(tag1.getImplicitParent());
        assertFalse(tag1.isValidParent(tag1));
        assertFalse(tag1.isValidParent(tag3));
        assertFalse(tag1.isValidParent(tag5));
        assertTrue(tag1.canContain(tag3));
    }

    // Testing canContain method with various child and parent combinations

    @Test
    public void testCanContainMethod() {
        assertTrue(tag1.canContain(tag3));
        assertTrue(tag2.canContain(tag3));
        assertTrue(tag2.canContain(tag1));
        assertTrue(tag4.canContain(tag3));
        assertTrue(tag5.canContain(tag3));
        assertTrue(tag5.canContain(tag6));
        assertTrue(tag6.canContain(tag3));
        assertFalse(tag1.canContain(tag1));
        assertFalse(tag1.canContain(tag5));
        assertFalse(tag5.canContain(tag1));
        assertFalse(tag5.canContain(tag5));
        assertFalse(tag6.canContain(tag6));
    }

    // Testing equals and hashCode methods

    @Test
    public void testEqualsAndHashCodeMethods() {
        Tag tag7 = new Tag("P");
        Tag tag8 = new Tag("DIV");
        Tag tag9 = new Tag("P");
        Tag tag10 = new Tag("P");

        assertEquals(tag1, tag1);
        assertEquals(tag1, tag1);
        assertEquals(tag1, tag9);
        assertEquals(tag1, tag10);
        assertNotEquals(tag1, tag2);
        assertNotEquals(tag1, tag3);
        assertNotEquals(tag1, tag4);
        assertNotEquals(tag1, tag5);
        assertNotEquals(tag1, tag6);
        assertNotEquals(tag1, tag7);
        assertNotEquals(tag1, tag8);
        assertNotEquals(tag1, tag10);
        assertTrue(tag1.equals(tag9));
        assertTrue(tag1.equals(tag10));
        assertEquals(tag1.hashCode(), tag9.hashCode());
        assertEquals(tag1.hashCode(), tag10.hashCode());
    }

    // Testing toString method

    @Test
    public void testToStringMethod() {
        assertEquals("p", tag1.toString());
        assertEquals("div", tag2.toString());
        assertEquals("img", tag4.toString());
        assertEquals("li", tag6.toString());
    }
}