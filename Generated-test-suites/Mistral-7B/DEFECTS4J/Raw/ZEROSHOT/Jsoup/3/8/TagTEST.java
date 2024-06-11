package org.jsoup.parser;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.junit.Test;

public class TagTest {

    @Test
    public void testTagValueOf() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
        tag = Tag.valueOf("DIV");
        assertEquals("div", tag.getName());
        tag = Tag.valueOf("UNKNOWN");
        assertNotSame(tag, Tag.valueOf("p"));
        assertEquals("UNKNOWN", tag.getName());
    }

    @Test
    public void testTagCanContain() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag imgTag = Tag.valueOf("img");
        Tag brTag = Tag.valueOf("br");
        Tag pTagInsidePTag = Tag.valueOf("p");
        Tag pTagInsideDivTag = Tag.valueOf("p");

        assertTrue(pTag.canContain(pTagInsidePTag));
        assertTrue(pTag.canContain(divTag));
        assertTrue(pTag.canContain(brTag));
        assertFalse(pTag.canContain(pTag));
        assertFalse(pTag.canContain(imgTag));

        assertTrue(divTag.canContain(pTag));
        assertTrue(divTag.canContain(divTag));
        assertFalse(divTag.canContain(imgTag));

        assertTrue(imgTag.canContain(pTagInsideImgTag));
        assertFalse(imgTag.canContain(pTag));
    }

    @Test
    public void testTagIsBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag imgTag = Tag.valueOf("img");
        Tag brTag = Tag.valueOf("br");

        assertTrue(pTag.isBlock());
        assertTrue(divTag.isBlock());
        assertFalse(imgTag.isBlock());
        assertFalse(brTag.isBlock());
    }

    @Test
    public void testTagCanContainBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag imgTag = Tag.valueOf("img");
        Tag brTag = Tag.valueOf("br");

        assertTrue(pTag.canContainBlock());
        assertTrue(divTag.canContainBlock());
        assertFalse(imgTag.canContainBlock());
        assertFalse(brTag.canContainBlock());
    }

    @Test
    public void testTagIsInline() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag imgTag = Tag.valueOf("img");
        Tag brTag = Tag.valueOf("br");

        assertFalse(pTag.isInline());
        assertFalse(divTag.isInline());
        assertFalse(imgTag.isInline());
        assertTrue(brTag.isInline());
    }

    @Test
    public void testTagIsData() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag imgTag = Tag.valueOf("img");
        Tag brTag = Tag.valueOf("br");

        assertFalse(pTag.isData());
        assertFalse(divTag.isData());
        assertTrue(imgTag.isData());
        assertFalse(brTag.isData());
    }

    @Test
    public void testTagIsEmpty() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag imgTag = Tag.valueOf("img");
        Tag brTag = Tag.valueOf("br");
        Tag imgTagEmpty = Tag.valueOf("img").setEmpty();

        assertFalse(pTag.isEmpty());
        assertFalse(divTag.isEmpty());
        assertFalse(imgTag.isEmpty());
        assertFalse(brTag.isEmpty());
        assertTrue(imgTagEmpty.isEmpty());
    }

    @Test
    public void testTagPreserveWhitespace() {
        Tag pTag = Tag.valueOf("p");
        Tag preTag = Tag.valueOf("pre");

        assertFalse(pTag.preserveWhitespace());
        assertTrue(preTag.preserveWhitespace());
    }

    @Test
    public void testTagGetImplicitParent() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag bodyTag = Tag.valueOf("body");

        assertNull(pTag.getImplicitParent());
        assertEquals("html", divTag.getImplicitParent().getName());
        assertEquals("body", bodyTag.getImplicitParent().getName());
    }

    @Test
    public void testTagIsValidParent() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag bodyTag = Tag.valueOf("body");
        Tag divTag2 = Tag.valueOf("div");

        assertTrue(pTag.isValidParent(pTag));
        assertTrue(pTag.isValidParent(divTag));
        assertTrue(divTag.isValidParent(pTag));
        assertFalse(divTag.isValidParent(divTag2));
        assertFalse(bodyTag.isValidParent(pTag));
    }

    @Test
    public void testTagEquals() {
        Tag pTag1 = Tag.valueOf("p");
        Tag pTag2 = Tag.valueOf("p");
        Tag divTag1 = Tag.valueOf("div");
        Tag divTag2 = Tag.valueOf("div");

        assertTrue(pTag1.equals(pTag2));
        assertNotEquals(pTag1, divTag1);
        assertNotEquals(pTag1, divTag2);
    }

    @Test
    public void testTagHashCode() {
        Tag pTag1 = Tag.valueOf("p");
        Tag pTag2 = Tag.valueOf("p");
        Tag divTag1 = Tag.valueOf("div");
        Tag divTag2 = Tag.valueOf("div");

        int hashCodePTag1 = pTag1.hashCode();
        int hashCodePTag2 = pTag2.hashCode();
        int hashCodeDivTag1 = divTag1.hashCode();
        int hashCodeDivTag2 = divTag2.hashCode();

        assertEquals(hashCodePTag1, hashCodePTag2);
        assertNotEquals(hashCodePTag1, hashCodeDivTag1);
        assertNotEquals(hashCodePTag1, hashCodeDivTag2);
    }

    @Test
    public void testTagToString() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        assertEquals("p", pTag.toString());
        assertEquals("div", divTag.toString());
    }
}