package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for Tag class.
 */
public class TagTest {

    @Test
    public void testTagValueOf() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("P");
        Tag tag3 = Tag.valueOf("DIV");
        Tag tag4 = Tag.valueOf("UNKNOWN");

        assertEquals("p", tag1.getName());
        assertEquals("p", tag1.tagName);
        assertEquals("P", tag2.tagName);
        assertEquals("DIV", tag3.tagName);
        assertEquals("UNKNOWN", tag4.tagName);
    }

    @Test
    public void testTagIsBlock() {
        Tag tagBlock = Tag.valueOf("p");
        Tag tagInline = Tag.valueOf("span");

        assertTrue(tagBlock.isBlock());
        assertFalse(tagInline.isBlock());
    }

    @Test
    public void testTagCanContainBlock() {
        Tag tagBlock = Tag.valueOf("div");
        Tag tagInline = Tag.valueOf("span");
        Tag tagBlockLevel = Tag.valueOf("ul");

        assertTrue(tagBlock.canContainBlock());
        assertFalse(tagInline.canContainBlock());
        assertTrue(tagBlockLevel.canContainBlock());
    }

    @Test
    public void testTagCanContainInline() {
        Tag tagBlock = Tag.valueOf("div");
        Tag tagInline = Tag.valueOf("span");

        assertTrue(tagBlock.canContainInline());
        assertTrue(tagInline.canContainInline());
    }

    @Test
    public void testTagOptionalClosing() {
        Tag tagOptional = Tag.valueOf("br");
        Tag tagNotOptional = Tag.valueOf("p");

        assertTrue(tagOptional.isOptionalClosing());
        assertFalse(tagNotOptional.isOptionalClosing());
    }

    @Test
    public void testTagEmpty() {
        Tag tagEmpty = Tag.valueOf("img");
        Tag tagNotEmpty = Tag.valueOf("p");

        assertTrue(tagEmpty.isEmpty());
        assertFalse(tagNotEmpty.isEmpty());
    }

    @Test
    public void testTagPreserveWhitespace() {
        Tag tagPreserve = Tag.valueOf("pre");
        Tag tagNotPreserve = Tag.valueOf("p");

        assertTrue(tagPreserve.preserveWhitespace());
        assertFalse(tagNotPreserve.preserveWhitespace());
    }

    @Test
    public void testTagCanContain() {
        Tag tagParent = Tag.valueOf("div");
        Tag tagChild1 = Tag.valueOf("p");
        Tag tagChild2 = Tag.valueOf("span");
        Tag tagChild3 = Tag.valueOf("div");

        assertTrue(tagParent.canContain(tagChild1));
        assertTrue(tagParent.canContain(tagChild2));
        assertTrue(tagParent.canContain(tagChild3));
        assertFalse(tagChild1.canContain(tagParent));
        assertFalse(tagChild1.canContain(tagChild1));
        assertFalse(tagChild2.canContain(tagChild3));
    }

    @Test(expected = NullPointerException.class)
    public void testTagCanContainNull() {
        Tag tagParent = Tag.valueOf("div");
        Tag tagChild = null;

        tagParent.canContain(tagChild);
    }

    @Test
    public void testTagEquals() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("P");
        Tag tag3 = Tag.valueOf("p");
        Tag tag4 = new Tag("p");
        Tag tag5 = Tag.valueOf("DIV");

        assertTrue(tag1.equals(tag1));
        assertTrue(tag1.equals(tag3));
        assertFalse(tag1.equals(tag2));
        assertFalse(tag1.equals(tag4));
        assertFalse(tag1.equals(tag5));
    }

    @Test
    public void testTagHashCode() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("P");
        Tag tag3 = Tag.valueOf("p");
        Tag tag4 = new Tag("p");
        Tag tag5 = Tag.valueOf("DIV");

        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag1.hashCode();
        int hashCode3 = tag2.hashCode();
        int hashCode4 = tag3.hashCode();
        int hashCode5 = tag4.hashCode();
        int hashCode6 = tag5.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode5);
        assertNotEquals(hashCode1, hashCode6);
    }

    @Test
    public void testTagToString() {
        Tag tag = Tag.valueOf("p");

        assertEquals("p", tag.toString());
    }

    @Test
    public void testTagGetImplicitParent() {
        Tag tagParent = Tag.valueOf("div");
        Tag tagChild1 = Tag.valueOf("p");
        Tag tagChild2 = Tag.valueOf("span");

        assertNull(tagChild1.getImplicitParent());
        assertEquals(tagParent, tagChild2.getImplicitParent());
    }

    @Test
    public void testTagIsValidParent() {
        Tag tagParent1 = Tag.valueOf("div");
        Tag tagParent2 = Tag.valueOf("div");
        Tag tagChild1 = Tag.valueOf("p");
        Tag tagChild2 = Tag.valueOf("span");
        Tag tagChild3 = new Tag("div");

        assertTrue(tagParent1.isValidParent(tagChild1));
        assertTrue(tagParent1.isValidParent(tagChild2));
        assertTrue(tagParent1.isValidParent(tagChild3));
        assertTrue(tagParent2.isValidParent(tagChild1));
        assertTrue(tagParent2.isValidParent(tagChild2));
        assertFalse(tagParent1.isValidParent(tagChild1)); // different instances
        assertFalse(tagParent1.isValidParent(tagChild3)); // different class
    }

    @Test
    public void testTagCreateBlock() {
        Tag tagHtml = Tag.createBlock("HTML");
        Tag tagHead = Tag.createBlock("HEAD");
        Tag tagBody = Tag.createBlock("BODY");
        Tag tagFrameSet = Tag.createBlock("FRAMESET");

        assertEquals("HTML", tagHtml.getName());
        assertEquals("HEAD", tagHead.getName());
        assertEquals("BODY", tagBody.getName());
        assertEquals("FRAMESET", tagFrameSet.getName());
    }

    @Test
    public void testTagCreateInline() {
        Tag tagFontStyle = Tag.createInline("FONT");
        Tag tagTT = Tag.createInline("TT");
        Tag tagI = Tag.createInline("I");
        // ... other inline tags

        assertEquals("FONT", tagFontStyle.getName());
        assertEquals("TT", tagTT.getName());
        assertEquals("I", tagI.getName());
        // ... other inline tags
    }

    @Test
    public void testTagContains() {
        Tag tagAncestor = Tag.valueOf("div");
        Tag tagChild1 = Tag.valueOf("p");
        Tag tagChild2 = Tag.valueOf("span");

        List<Tag> ancestors = Arrays.asList(tagAncestor);

        Tag tag = new Tag("div");
        tag.setAncestor(ancestors.toArray(new String[ancestors.size()]));

        assertTrue(tag.contains(tagChild1));
        assertTrue(tag.contains(tagChild2));
        assertFalse(tagChild1.contains(tag));
        assertFalse(tagChild2.contains(tag));
    }

    @Test
    public void testTagSetEmpty() {
        Tag tag = new Tag("ul");

        assertTrue(tag.isEmpty());

        tag.setEmpty(false);

        assertFalse(tag.isEmpty());
    }

    @Test
    public void testTagSetOptionalClosing() {
        Tag tag = new Tag("br");

        assertTrue(tag.isOptionalClosing());

        tag.setOptionalClosing(false);

        assertFalse(tag.isOptionalClosing());
    }

    @Test
    public void testTagSetPreserveWhitespace() {
        Tag tag = new Tag("pre");

        assertTrue(tag.preserveWhitespace());

        tag.setPreserveWhitespace(false);

        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testTagSetAncestor() {
        Tag tagAncestor = Tag.valueOf("div");
        Tag tag = new Tag("div");

        tag.setAncestor();

        assertEmpty(tag.ancestors);

        tag.setAncestor(tagAncestor.getName());

        List<Tag> expectedAncestors = Collections.singletonList(tagAncestor);

        assertEquals(expectedAncestors, tag.ancestors);

        tag.setAncestor(new String[]{"div", "div", "div"});

        List<Tag> expectedAncestorsList = Arrays.asList(Tag.valueOf("div"), Tag.valueOf("div"), Tag.valueOf("div"));

        assertEquals(expectedAncestorsList, tag.ancestors);
    }

    @Test
    public void testTagSetContainDataOnly() {
        Tag tag = new Tag("script");

        tag.setContainDataOnly();

        assertFalse(tag.canContainBlock());
        assertFalse(tag.canContainInline());
        assertTrue(tag.preserveWhitespace);
    }

    @Test
    public void testTagSetContainInlineOnly() {
        Tag tag = new Tag("p");

        tag.setContainInlineOnly();

        assertFalse(tag.canContainBlock());
        assertTrue(tag.canContainInline());
    }

    @Test
    public void testTagSetOptionalClosingFalse() {
        Tag tag = new Tag("br");

        tag.setOptionalClosing(false);

        assertFalse(tag.isOptionalClosing());
    }

    @Test
    public void testTagSetPreserveWhitespaceFalse() {
        Tag tag = new Tag("pre");

        tag.setPreserveWhitespace(false);

        assertFalse(tag.preserveWhitespace);
    }

    private void assertEmpty(List<Tag> list) {
        assertTrue(list == null || list.isEmpty());
    }
}