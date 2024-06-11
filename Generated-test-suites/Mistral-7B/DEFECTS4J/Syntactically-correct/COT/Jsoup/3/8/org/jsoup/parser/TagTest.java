package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TagTest.java - Test cases for Tag class methods.
 *
 * @author [Your Name]
 */
public class TagTest {

    @Test
    public void testTagNameGettersAndSetters() {
        String tagName = "tagName";
        Tag tag = new Tag(tagName);

        assertEquals(tagName, tag.getName());

        tag.setName("newTagName");
        assertEquals("newTagName", tag.getName());
    }

    @Test
    public void testTagValueOf() {
        String tagName = "tagName";

        Tag tag = Tag.valueOf(tagName);
        assertEquals(tagName, tag.getName());
    }

    @Test(expected = AssertionError.class)
    public void testTagValueOfNull() {
        Tag tag = Tag.valueOf(null);
    }

    @Test
    public void testTagValueOfEmpty() {
        Tag tag = Tag.valueOf("");
        assertEquals("", tag.getName());
    }

    @Test
    public void testTagCanContain() {
        String parentTagName = "parent";
        String childTagName = "child";

        Tag parent = new Tag(parentTagName);
        Tag child = new Tag(childTagName);

        assertTrue(parent.canContain(child));

        parent.setCanContainBlock(false);
        assertFalse(parent.canContain(child));

        child.setIsBlock(false);
        assertTrue(parent.canContain(child));

        parent.setOptionalClosing(true);
        parent.setName(childTagName);
        assertFalse(parent.canContain(child));

        child.setIsEmpty(true);
        assertFalse(parent.canContain(child));
    }

    @Test
    public void testTagIsBlock() {
        String tagName = "tagName";
        Tag tag = new Tag(tagName);

        assertTrue(tag.isBlock());

        tag.setIsBlock(false);
        assertFalse(tag.isBlock());
    }

    @Test
    public void testTagIsInline() {
        String tagName = "tagName";
        Tag tag = new Tag(tagName);

        assertFalse(tag.isInline());

        tag.setIsBlock(false);
        assertTrue(tag.isInline());
    }

    @Test
    public void testTagIsData() {
        String tagName = "tagName";
        Tag tag = new Tag(tagName);

        assertFalse(tag.isData());

        tag.setCanContainInline(false);
        tag.setEmpty(false);
        assertTrue(tag.isData());
    }

    @Test
    public void testTagIsEmpty() {
        String tagName = "tagName";
        Tag tag = new Tag(tagName);

        assertFalse(tag.isEmpty());

        tag.setEmpty(true);
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testTagPreserveWhitespace() {
        String tagName = "tagName";
        Tag tag = new Tag(tagName);

        assertFalse(tag.preserveWhitespace());

        tag.setPreserveWhitespace(true);
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testTagGetImplicitParent() {
        String tagName = "tagName";
        Tag tag = new Tag(tagName);

        // Empty list should return null
        assertNull(tag.getImplicitParent());

        // Add ancestor
        Tag ancestor = new Tag("ancestor");
        tag.setAncestors(Collections.singletonList(ancestor));

        // Get ancestor
        Tag implicitParent = tag.getImplicitParent();
        assertEquals(ancestor.getName(), implicitParent.getName());
    }

    @Test
    public void testTagIsValidParent() {
        String parentTagName = "parent";
        String childTagName = "child";

        Tag parent = new Tag(parentTagName);
        Tag child = new Tag(childTagName);

        assertTrue(parent.isValidParent(child));

        child.setAncestors(Collections.singletonList(parent));
        assertFalse(parent.isValidParent(child));
    }

    @Test
    public void testTagEquals() {
        String tagName = "tagName";
        Tag tag1 = new Tag(tagName);
        Tag tag2 = new Tag(tagName);

        assertTrue(tag1.equals(tag2));

        tag2.setCanContainBlock(false);
        assertFalse(tag1.equals(tag2));

        tag2.setCanContainBlock(true);
        assertTrue(tag1.equals(tag2));

        tag2.setTagName("newTagName");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testTagHashcode() {
        String tagName = "tagName";
        Tag tag1 = new Tag(tagName);
        Tag tag2 = new Tag(tagName);

        int hashcode1 = tag1.hashCode();
        int hashcode2 = tag2.hashCode();

        assertEquals(hashcode1, hashcode2);

        tag1.setCanContainBlock(false);
        int hashcode3 = tag1.hashCode();
        assertNotEquals(hashcode1, hashcode3);
        assertNotEquals(hashcode2, hashcode3);

        tag2.setCanContainBlock(false);
        int hashcode4 = tag2.hashCode();
        assertEquals(hashcode3, hashcode4);
    }

    @Test
    public void testTagToString() {
        String tagName = "tagName";
        Tag tag = new Tag(tagName);

        assertEquals(tagName, tag.toString());
    }

    @Test(expected = AssertionError.class)
    public void testTagValueOfNullTagName() {
        Tag tag = Tag.valueOf(null);
    }

    @Test(expected = AssertionError.class)
    public void testTagValueOfEmptyTagName() {
        Tag tag = Tag.valueOf("");
    }

    @Test(expected = AssertionError.class)
    public void testTagCreateBlockNullTagName() {
        Tag tag = Tag.createBlock(null);
    }

    @Test(expected = AssertionError.class)
    public void testTagCreateBlockEmptyTagName() {
        Tag tag = Tag.createBlock("");
    }

    @Test(expected = AssertionError.class)
    public void testTagSetAncestorNull() {
        Tag tag = new Tag("tagName");
        tag.setAncestors(null);
    }

    @Test
    public void testTagSetAncestorEmptyList() {
        String tagName = "tagName";
        Tag tag = new Tag(tagName);

        List<Tag> ancestorList = Collections.emptyList();
        tag.setAncestors(ancestorList);
        assertTrue(tag.ancestors.isEmpty());
    }

    @Test
    public void testTagSetAncestorSingleTag() {
        String parentTagName = "parent";
        String tagName = "tagName";

        Tag parentTag = new Tag(parentTagName);
        Tag tag = new Tag(tagName);

        List<Tag> ancestorList = new ArrayList<>();
        ancestorList.add(parentTag);
        tag.setAncestors(ancestorList);

        assertEquals(1, tag.ancestors.size());
        assertEquals(parentTag.getName(), tag.ancestors.get(0).getName());
    }

    @Test
    public void testTagSetAncestorMultipleTags() {
        String parent1TagName = "parent1";
        String parent2TagName = "parent2";
        String tagName = "tagName";

        Tag parent1 = new Tag(parent1TagName);
        Tag parent2 = new Tag(parent2TagName);
        Tag tag = new Tag(tagName);

        List<Tag> ancestorList = new ArrayList<>();
        ancestorList.add(parent1);
        ancestorList.add(parent2);
        tag.setAncestors(ancestorList);

        assertEquals(2, tag.ancestors.size());
        assertEquals(parent1.getName(), tag.ancestors.get(0).getName());
        assertEquals(parent2.getName(), tag.ancestors.get(1).getName());
    }
}