package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TagTest {

    @Test
    public void testTagNameIsSetCorrectly() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOfReturnsPredefinedTag() {
        Tag pTag = Tag.valueOf("p");
        assertEquals("p", pTag.getName());
        assertTrue(pTag.isBlock());
    }

    @Test
    public void testValueOfReturnsNewGenericTagForUnknownTag() {
        Tag unknownTag = Tag.valueOf("unknownTag");
        assertFalse(unknownTag.isBlock());
        assertEquals("unknownTag", unknownTag.getName());
    }

    @Test
    public void testCanContainReturnsTrueForValidChildTag() {
        Tag parentTag = new Tag("div");
        Tag childTag = new Tag("p");
        assertTrue(parentTag.canContain(childTag));
    }

    @Test
    public void testCanContainReturnsFalseForInvalidChildTag() {
        Tag parentTag = new Tag("div");
        Tag childTag = new Tag("div");
        assertFalse(parentTag.canContain(childTag));
    }

    @Test(expected = NullPointerException.class)
    public void testCanContainThrowsExceptionForNullChildTag() {
        Tag parentTag = new Tag("div");
        Tag childTag = null;
        parentTag.canContain(childTag);
    }

    @Test
    public void testIsBlockReturnsCorrectValueForBlockTag() {
        Tag pTag = new Tag("p");
        assertTrue(pTag.isBlock());
    }

    @Test
    public void testIsBlockReturnsCorrectValueForInlineTag() {
        Tag emTag = new Tag("em");
        assertFalse(emTag.isBlock());
    }

    @Test
    public void testCanContainBlockReturnsCorrectValueForTagThatCanContainBlock() {
        Tag divTag = new Tag("div");
        assertTrue(divTag.canContainBlock());
    }

    @Test
    public void testCanContainBlockReturnsCorrectValueForTagThatCannotContainBlock() {
        Tag spanTag = new Tag("span");
        assertFalse(spanTag.canContainBlock());
    }

    @Test
    public void testIsDataReturnsCorrectValueForDataOnlyTag() {
        Tag scriptTag = new Tag("script");
        assertTrue(scriptTag.isData());
    }

    @Test
    public void testIsDataReturnsCorrectValueForNonDataOnlyTag() {
        Tag divTag = new Tag("div");
        assertFalse(divTag.isData());
    }

    @Test
    public void testIsEmptyReturnsCorrectValueForEmptyTag() {
        Tag imgTag = new Tag("img");
        assertTrue(imgTag.isEmpty());
    }

    @Test
    public void testIsEmptyReturnsCorrectValueForNonEmptyTag() {
        Tag divTag = new Tag("div");
        assertFalse(divTag.isEmpty());
    }

    @Test
    public void testPreserveWhitespaceReturnsCorrectValueForPreservingTag() {
        Tag preTag = new Tag("pre");
        assertTrue(preTag.preserveWhitespace());
    }

    @Test
    public void testPreserveWhitespaceReturnsCorrectValueForNonPreservingTag() {
        Tag divTag = new Tag("div");
        assertFalse(divTag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParentReturnsCorrectValue() {
        Tag pTag = new Tag("p");
        Tag divTag = new Tag("div");
        divTag.ancestors = Arrays.asList(pTag);
        assertEquals(pTag, pTag.getImplicitParent());
        assertEquals(pTag, divTag.getImplicitParent());
    }

    @Test
    public void testGetImplicitParentReturnsNullForNoAncestors() {
        Tag spanTag = new Tag("span");
        assertEquals(null, spanTag.getImplicitParent());
    }

    @Test
    public void testIsValidParentReturnsTrueForValidParent() {
        Tag parentTag = new Tag("div");
        Tag childTag = new Tag("p");
        assertTrue(parentTag.isValidParent(childTag));
    }

    @Test
    public void testIsValidParentReturnsFalseForInvalidParent() {
        Tag parentTag = new Tag("div");
        Tag childTag = new Tag("div");
        assertFalse(parentTag.isValidParent(childTag));
    }

    @Test
    public void testEqualsReturnsCorrectValueForSameObjects() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testEqualsReturnsFalseForDifferentObjects() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("p");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testHashCodeReturnsConsistentValue() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToStringReturnsCorrectValue() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.toString());
    }
}