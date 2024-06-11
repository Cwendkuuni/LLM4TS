package org.jsoup.parser;

import org.apache.commons.lang3.Validate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class TagTest {

    @Test
    public void testTagCreation() {
        Tag tag = new Tag("TestTag");
        String tagName = tag.getName();
        assertTrue(tagName.equals("testtag"));
    }

    @Test
    public void testTagValueOf() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.canContainBlock());
        assertTrue(tag.getName().equalsIgnoreCase("p"));
    }

    @Test
    public void testUnknownTagValueOf() {
        Tag tag = Tag.valueOf("UnknownTag");
        assertTrue(tag.isInline());
        assertTrue(tag.getName().equalsIgnoreCase("unknowntag"));
    }

    @Test
    public void testContainBlockMethod() {
        Tag parentTag = new Tag("div");
        Tag childTag1 = new Tag("p");
        Tag childTag2 = new Tag("span");

        assertTrue(parentTag.canContain(childTag1));
        assertTrue(parentTag.canContain(childTag2));
        assertFalse(childTag1.canContain(childTag2));
        assertFalse(parentTag.canContain(parentTag));
    }

    @Test
    public void testIsBlockMethod() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("span");

        assertTrue(tag1.isBlock());
        assertFalse(tag2.isBlock());
    }

    @Test
    public void testCanContainBlockMethod() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("p");
        Tag tag3 = new Tag("span");

        assertTrue(tag1.canContainBlock());
        assertTrue(tag1.canContain(tag2));
        assertFalse(tag2.canContainBlock());
        assertFalse(tag2.canContain(tag3));
    }

    @Test
    public void testIsEmptyMethod() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("img");

        assertFalse(tag1.isEmpty());
        assertTrue(tag2.isEmpty());
    }

    @Test
    public void testPreserveWhitespaceMethod() {
        Tag tag1 = new Tag("pre");
        Tag tag2 = new Tag("p");

        assertTrue(tag1.preserveWhitespace());
        assertFalse(tag2.preserveWhitespace());
    }

    @Test
    public void testToStringMethod() {
        Tag tag = new Tag("TestTag");
        String tagName = tag.getName();

        assertTrue(tag.toString().equalsIgnoreCase("testtag"));
    }

    @Test
    public void testEqualsMethod() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        Tag tag3 = new Tag("span");

        assertTrue(tag1.equals(tag1));
        assertTrue(tag1.equals(tag2));
        assertFalse(tag1.equals(tag3));
    }

    @Test
    public void testHashCodeMethod() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        Tag tag3 = new Tag("span");

        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag2.hashCode();
        int hashCode3 = tag3.hashCode();

        assertTrue(hashCode1 == hashCode2);
        assertFalse(hashCode1 == hashCode3);
    }

    @Test
    public void testGetImplicitParentMethod() {
        Tag parentTag = new Tag("div");
        Tag childTag1 = new Tag("p");
        childTag1.setAncestor(parentTag);

        Tag implicitParent = childTag1.getImplicitParent();
        assertTrue(implicitParent != null && implicitParent.equals(parentTag));
    }

    @Test
    public void testIsValidParentMethod() {
        Tag parentTag1 = new Tag("div");
        Tag parentTag2 = new Tag("p");
        Tag childTag = new Tag("span");

        assertTrue(parentTag1.isValidParent(childTag));
        assertFalse(parentTag2.isValidParent(childTag));
    }
}