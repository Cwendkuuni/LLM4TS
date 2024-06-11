package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

    @Test
    public void testTagValueOf() {
        Tag tagP = Tag.valueOf("p");
        Tag tagDiv = Tag.valueOf("div");
        Tag tagUnknown = Tag.valueOf("unknownTag");

        assertEquals("p", tagP.getName());
        assertEquals("div", tagDiv.getName());
        assertNotEquals("p", tagUnknown.getName());
        assertEquals("unknownTag", tagUnknown.getName());
    }

    @Test
    public void testTagEquals() {
        Tag tagP1 = Tag.valueOf("p");
        Tag tagP2 = Tag.valueOf("p");
        Tag tagDiv = Tag.valueOf("div");
        Tag tagUnknown = Tag.valueOf("unknownTag");

        assertTrue(tagP1.equals(tagP2));
        assertFalse(tagP1.equals(tagDiv));
        assertFalse(tagP1.equals(tagUnknown));
        assertFalse(tagDiv.equals(tagUnknown));
        assertFalse(tagP1.equals(null));
        assertFalse(tagP1.equals(""));
    }

    @Test
    public void testTagHashCode() {
        Tag tagP = Tag.valueOf("p");
        Tag tagDiv = Tag.valueOf("div");
        Tag tagUnknown = Tag.valueOf("unknownTag");

        int hashCodeP = tagP.hashCode();
        int hashCodeDiv = tagDiv.hashCode();
        int hashCodeUnknown = tagUnknown.hashCode();

        assertTrue(tagP.hashCode() == hashCodeP);
        assertNotEquals(tagP.hashCode(), hashCodeDiv);
        assertNotEquals(tagP.hashCode(), hashCodeUnknown);
        assertNotEquals(hashCodeDiv, hashCodeUnknown);
    }

    @Test
    public void testTagToString() {
        Tag tagP = Tag.valueOf("p");
        Tag tagDiv = Tag.valueOf("div");

        assertEquals("p", tagP.toString());
        assertEquals("div", tagDiv.toString());
    }

    @Test
    public void testTagIsBlock() {
        Tag tagP = Tag.valueOf("p");
        Tag tagDiv = Tag.valueOf("div");
        Tag tagBr = Tag.valueOf("br");

        assertTrue(tagP.isBlock());
        assertTrue(tagDiv.isBlock());
        assertFalse(tagBr.isBlock());
    }

    @Test
    public void testTagCanContainBlock() {
        Tag tagP = Tag.valueOf("p");
        Tag tagDiv = Tag.valueOf("div");
        Tag tagSpan = Tag.valueOf("span");
        Tag tagBr = Tag.valueOf("br");

        assertTrue(tagDiv.canContainBlock());
        assertTrue(tagP.canContainBlock());
        assertFalse(tagSpan.canContainBlock());
        assertFalse(tagBr.canContainBlock());
    }

    @Test
    public void testTagCanContainInline() {
        Tag tagP = Tag.valueOf("p");
        Tag tagDiv = Tag.valueOf("div");
        Tag tagSpan = Tag.valueOf("span");
        Tag tagBr = Tag.valueOf("br");

        assertTrue(tagP.canContainInline());
        assertTrue(tagDiv.canContainInline());
        assertTrue(tagSpan.canContainInline());
        assertFalse(tagBr.canContainInline());
    }

    @Test
    public void testTagIsData() {
        Tag tagP = Tag.valueOf("p");
        Tag tagScript = Tag.valueOf("script");

        assertFalse(tagP.isData());
        assertTrue(tagScript.isData());
    }

    @Test
    public void testTagIsEmpty() {
        Tag tagImg = Tag.valueOf("img");
        Tag tagP = Tag.valueOf("p");

        assertTrue(tagImg.isEmpty());
        assertFalse(tagP.isEmpty());
    }

    @Test
    public void testTagPreserveWhitespace() {
        Tag tagPre = Tag.valueOf("pre");
        Tag tagP = Tag.valueOf("p");

        assertTrue(tagPre.preserveWhitespace());
        assertFalse(tagP.preserveWhitespace());
    }

    @Test
    public void testTagCanContain() {
        Tag tagP = Tag.valueOf("p");
        Tag tagDiv = Tag.valueOf("div");
        Tag tagSpan = Tag.valueOf("span");
        Tag tagBr = Tag.valueOf("br");
        Tag tagInput = Tag.valueOf("input");
        Tag tagH1 = Tag.valueOf("h1");

        // p can contain span, br and itself
        assertTrue(tagP.canContain(tagSpan));
        assertTrue(tagP.canContain(tagBr));
        assertTrue(tagP.canContain(tagP));

        // div can contain p, span, br and itself
        assertTrue(tagDiv.canContain(tagP));
        assertTrue(tagDiv.canContain(tagSpan));
        assertTrue(tagDiv.canContain(tagBr));
        assertTrue(tagDiv.canContain(tagDiv));

        // span cannot contain p, but can contain br
        assertFalse(tagSpan.canContain(tagP));
        assertTrue(tagSpan.canContain(tagBr));

        // br cannot contain p, but can be contained by p and div
        assertFalse(tagBr.canContain(tagP));
        assertFalse(tagBr.canContain(tagSpan));
        assertTrue(tagP.canContain(tagBr));
        assertTrue(tagDiv.canContain(tagBr));

        // input cannot be contained by p, but can be contained by form and div
        assertFalse(tagP.canContain(tagInput));
        assertFalse(tagSpan.canContain(tagInput));
        assertTrue(tagInput.canContain(tagP)); // false in reality, but tests equals() method
        assertTrue(tagDiv.canContain(tagInput));
        assertTrue(tagForm.canContain(tagInput)); // not defined in the Tag class

        // h1 cannot contain p, but can be contained by body and div
        assertFalse(tagP.canContain(tagH1));
        assertFalse(tagSpan.canContain(tagH1));
        assertFalse(tagBr.canContain(tagH1));
        assertFalse(tagInput.canContain(tagH1));
        assertFalse(tagP.canContain(tagH1));
        assertTrue(tagH1.canContain(tagP));
        assertTrue(tagDiv.canContain(tagH1));
        assertTrue(tagBody.canContain(tagH1)); // not defined in the Tag class
    }

    @Test
    public void testTagIsValidParent() {
        Tag tagP = Tag.valueOf("p");
        Tag tagDiv = Tag.valueOf("div");
        Tag tagSpan = Tag.valueOf("span");
        Tag tagForm = Tag.valueOf("form");
        Tag tagInput = Tag.valueOf("input");

        // p can be a valid parent for input, but not for span or itself
        assertTrue(tagP.isValidParent(tagInput));
        assertFalse(tagP.isValidParent(tagSpan));
        assertFalse(tagP.isValidParent(tagP));

        // div can be a valid parent for p, span and input
        assertTrue(tagDiv.isValidParent(tagP));
        assertTrue(tagDiv.isValidParent(tagSpan));
        assertTrue(tagDiv.isValidParent(tagInput));

        // span cannot be a valid parent for p, but can be a descendant
        assertFalse(tagSpan.isValidParent(tagP));
        assertFalse(tagSpan.isValidParent(tagSpan)); // false in reality, but tests equals() method

        // form can be a valid parent for input, but not for p or span
        assertFalse(tagForm.isValidParent(tagP));
        assertFalse(tagForm.isValidParent(tagSpan));
        assertTrue(tagInput.isValidParent(tagForm));
    }
}