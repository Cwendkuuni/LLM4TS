package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Test class for Tag.
 */
public class TagTest {

    /**
     * Test method for {@link Tag#Tag(String)} with valid tag name.
     */
    @Test
    public void testTagConstructionWithValidTagName() {
        Tag tag = new Tag("div");
        String expectedTagName = "div";
        assertEquals(expectedTagName, tag.getName());
    }

    /**
     * Test method for {@link Tag#isBlock()} with a block tag.
     */
    @Test
    public void testIsBlockWithBlockTag() {
        Tag tag = new Tag("div");
        assertTrue(tag.isBlock());
    }

    /**
     * Test method for {@link Tag#isBlock()} with an inline tag.
     */
    @Test
    public void testIsBlockWithInlineTag() {
        Tag tag = new Tag("span");
        assertFalse(tag.isBlock());
    }

    /**
     * Test method for {@link Tag#formatAsBlock()} with a block tag.
     */
    @Test
    public void testFormatAsBlockWithBlockTag() {
        Tag tag = new Tag("div");
        assertTrue(tag.formatAsBlock());
    }

    /**
     * Test method for {@link Tag#formatAsBlock()} with an inline tag.
     */
    @Test
    public void testFormatAsBlockWithInlineTag() {
        Tag tag = new Tag("span");
        assertFalse(tag.formatAsBlock());
    }

    /**
     * Test method for {@link Tag#isData()} with a data only tag.
     */
    @Test
    public void testIsDataWithDataOnlyTag() {
        Tag tag = new Tag("script");
        assertTrue(tag.isData());
    }

    /**
     * Test method for {@link Tag#isEmpty()} with an empty tag.
     */
    @Test
    public void testIsEmptyWithEmptyTag() {
        Tag tag = new Tag("img");
        assertFalse(tag.isEmpty());
    }

    /**
     * Test method for {@link Tag#isSelfClosing()} with a self-closing tag.
     */
    @Test
    public void testIsSelfClosingWithSelfClosingTag() {
        Tag tag = new Tag("img");
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    /**
     * Test method for {@link Tag#isKnownTag(String)} with a known tag.
     */
    @Test
    public void testIsKnownTagWithKnownTag() {
        assertTrue(Tag.isKnownTag("div"));
    }

    /**
     * Test method for {@link Tag#isKnownTag(String)} with an unknown tag.
     */
    @Test
    public void testIsKnownTagWithUnknownTag() {
        assertFalse(Tag.isKnownTag("zxqw"));
    }

    /**
     * Test method for {@link Tag#preserveWhitespace()} with a tag that preserves whitespace.
     */
    @Test
    public void testPreserveWhitespaceWithTagThatPreservesWhitespace() {
        Tag tag = new Tag("pre");
        assertTrue(tag.preserveWhitespace());
    }

    /**
     * Test method for {@link Tag#isFormListed()} with a tag listed in forms.
     */
    @Test
    public void testIsFormListedWithFormListedTag() {
        Tag tag = new Tag("input");
        assertTrue(tag.isFormListed());
    }

    /**
     * Test method for {@link Tag#isFormSubmittable()} with a tag submittable with a form.
     */
    @Test
    public void testIsFormSubmittableWithFormSubmittableTag() {
        Tag tag = new Tag("input");
        assertTrue(tag.isFormSubmittable());
    }

    /**
     * Test method for {@link Tag#equals(Object)} with an equal Tag object.
     */
    @Test
    public void testEqualsWithEqualTagObject() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        assertTrue(tag1.equals(tag2));
    }

    /**
     * Test method for {@link Tag#equals(Object)} with a non-equal Tag object.
     */
    @Test
    public void testEqualsWithNonEqualTagObject() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("span");
        assertFalse(tag1.equals(tag2));
    }

    /**
     * Test method for {@link Tag#hashCode()} with equal Tag objects.
     */
    @Test
    public void testHashCodeWithEqualTagObjects() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    /**
     * Test method for {@link Tag#toString()} with a Tag object.
     */
    @Test
    public void testToStringWithTagObject() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.toString());
    }

    /**
     * Test method for {@link Tag#valueOf(String)} with a known tag.
     */
    @Test
    public void testValueOfWithKnownTag() {
        Tag tag = Tag.valueOf("div");
        String expectedTagName = "div";
        assertEquals(expectedTagName, tag.getName());
    }

    /**
     * Test method for {@link Tag#valueOf(String)} with an unknown tag.
     */
    @Test
    public void testValueOfWithUnknownTag() {
        Tag tag = Tag.valueOf("zxqw");
        String expectedTagName = "zxqw";
        assertEquals(expectedTagName, tag.getName());
        assertTrue(tag.isBlock()); // default settings
    }

    /**
     * Test method for {@link Tag#valueOf(String, ParseSettings)} with a known tag and default settings.
     */
    @Test
    public void testValueOfWithKnownTagAndDefaultSettings() {
        Tag tag = Tag.valueOf("div");
        String expectedTagName = "div";
        assertEquals(expectedTagName, tag.getName());
    }

    /**
     * Test method for {@link Tag#valueOf(String, ParseSettings)} with a known tag and custom settings.
     */
    @Test
    public void testValueOfWithKnownTagAndCustomSettings() {
        Tag tag = Tag.valueOf("div", new ParseSettings()); // custom settings
        String expectedTagName = "div";
        assertEquals(expectedTagName, tag.getName());
        assertFalse(tag.isBlock()); // custom settings
    }

    /**
     * Test method for {@link Tag#valueOf(String)} with an unknown tag and default settings.
     */
    @Test
    public void testValueOfWithUnknownTagAndDefaultSettings() {
        Tag tag = Tag.valueOf("zxqw");
        String expectedTagName = "zxqw";
        assertEquals(expectedTagName, tag.getName());
        assertTrue(tag.isBlock()); // default settings
    }

    /**
     * Test method for {@link Tag#valueOf(String, ParseSettings)} with an unknown tag and custom settings.
     */
    @Test
    public void testValueOfWithUnknownTagAndCustomSettings() {
        Tag tag = Tag.valueOf("zxqw", new ParseSettings()); // custom settings
        String expectedTagName = "zxqw";
        assertEquals(expectedTagName, tag.getName());
        assertTrue(tag.isBlock()); // custom settings
    }

    /**
     * Test method for {@link Tag#canContainBlock()} with a tag that can contain block tags.
     */
    @Test
    public void testCanContainBlockWithTagThatCanContainBlockTags() {
        Tag tag = new Tag("div");
        assertTrue(tag.canContainBlock());
    }

    /**
     * Test method for {@link Tag#canContainBlock()} with a tag that cannot contain block tags.
     */
    @Test
    public void testCanContainBlockWithTagThatCannotContainBlockTags() {
        Tag tag = new Tag("span");
        assertFalse(tag.canContainBlock());
    }

    /**
     * Test method for {@link Tag#isFormListed()} with a tag listed in forms but not submittable.
     */
    @Test
    public void testIsFormListedButNotSubmittable() {
        Tag tag = new Tag("label");
        assertTrue(tag.isFormListed());
        assertFalse(tag.isFormSubmittable());
    }

    /**
     * Test method for {@link Tag#isFormSubmittable()} with a tag submittable with a form but not listed.
     */
    @Test
    public void testIsFormSubmittableButNotListed() {
        Tag tag = new Tag("output");
        assertFalse(tag.isFormListed());
        assertTrue(tag.isFormSubmittable());
    }

    /**
     * Test method for {@link Tag#isFormListed()} with a tag neither listed nor submittable.
     */
    @Test
    public void testIsFormListedButNotListedNorSubmittable() {
        Tag tag = new Tag("abbr");
        assertFalse(tag.isFormListed());
        assertFalse(tag.isFormSubmittable());
    }

    /**
     * Test method for {@link Tag#isFormSubmittable()} with a tag both listed and submittable.
     */
    @Test
    public void testIsFormSubmittableAndListed() {
        Tag tag = new Tag("input");
        assertTrue(tag.isFormListed());
        assertTrue(tag.isFormSubmittable());
    }

    /**
     * Test method for {@link Tag#isFormSubmittable()} with a tag neither listed nor submittable.
     */
    @Test
    public void testIsFormSubmittableWithNeitherListedNorSubmittableTag() {
        Tag tag = new Tag("header");
        assertFalse(tag.isFormListed());
        assertFalse(tag.isFormSubmittable());
    }

    /**
     * Test method for {@link Tag#isFormSubmittable()} with a tag listed but not submittable.
     */
    @Test
    public void testIsFormSubmittableWithListedButNotSubmittableTag() {
        Tag tag = new Tag("head");
        assertFalse(tag.isFormListed());
        assertFalse(tag.isFormSubmittable());
    }
}