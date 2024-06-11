package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TagTest {

    private Tag tag;

    @Before
    public void setup() {
        tag = new Tag("tag");
    }

    @Test
    public void getName_validTagName_returnTagName() {
        assertEquals("tag", tag.getName());
    }

    @Test
    public void valueOf_knownTagName_returnRegisteredTag() {
        Tag registeredTag = Tag.valueOf("p");
        assertTrue(registeredTag.isKnownTag());
        assertEquals("p", registeredTag.getName());
        assertTrue(registeredTag.isBlock());
        assertTrue(registeredTag.formatAsBlock());
        assertTrue(registeredTag.canContainBlock());
        assertFalse(registeredTag.canContainInline());
        assertFalse(registeredTag.isEmpty());
        assertFalse(registeredTag.isSelfClosing());
        assertFalse(registeredTag.preserveWhitespace());
    }

    @Test
    public void valueOf_unknownTagName_returnNewGenericTag() {
        Tag genericTag = Tag.valueOf("unknown");
        assertFalse(genericTag.isKnownTag());
        assertEquals("unknown", genericTag.getName());
        assertFalse(genericTag.isBlock());
        assertTrue(genericTag.formatAsBlock());
        assertTrue(genericTag.canContainBlock());
        assertTrue(genericTag.canContainInline());
        assertFalse(genericTag.isEmpty());
        assertFalse(genericTag.isSelfClosing());
        assertFalse(genericTag.preserveWhitespace());
    }

    @Test
    public void isBlock_blockTag_returnTrue() {
        assertTrue(tag.isBlock());
    }

    @Test
    public void isBlock_inlineTag_returnFalse() {
        tag = Tag.valueOf("a");
        assertFalse(tag.isBlock());
    }

    @Test
    public void formatAsBlock_blockTag_returnTrue() {
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void formatAsBlock_inlineTag_returnTrue() {
        tag = Tag.valueOf("a");
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void canContainBlock_tagThatCanContainBlock_returnTrue() {
        tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void canContainBlock_tagThatCannotContainBlock_returnFalse() {
        tag = Tag.valueOf("a");
        assertFalse(tag.canContainBlock());
    }

    @Test
    public void isInline_inlineTag_returnTrue() {
        tag = Tag.valueOf("a");
        assertTrue(tag.isInline());
    }

    @Test
    public void isInline_blockTag_returnFalse() {
        assertTrue(tag.isBlock());
    }

    @Test
    public void isData_tagThatCanContainInlineAndNotEmpty_returnFalse() {
        assertFalse(tag.isData());
    }

    @Test
    public void isData_tagThatCanContainInlineAndIsEmpty_returnFalse() {
        tag = Tag.valueOf("img");
        assertFalse(tag.isData());
    }

    @Test
    public void isEmpty_emptyTag_returnTrue() {
        tag = Tag.valueOf("img");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void isEmpty_notEmptyTag_returnFalse() {
        assertFalse(tag.isEmpty());
    }

    @Test
    public void isSelfClosing_emptyTag_returnTrue() {
        tag = Tag.valueOf("img");
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void isSelfClosing_tagThatCanSelfClose_returnTrue() {
        tag = Tag.valueOf("unknown");
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void isSelfClosing_tagThatCannotSelfClose_returnFalse() {
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void isKnownTag_knownTag_returnTrue() {
        tag = Tag.valueOf("p");
        assertTrue(tag.isKnownTag());
    }

    @Test
    public void isKnownTag_unknownTag_returnFalse() {
        assertFalse(tag.isKnownTag());
    }

    @Test
    public void isKnownTag_knownTagName_returnTrue() {
        assertTrue(Tag.isKnownTag("p"));
    }

    @Test
    public void isKnownTag_unknownTagName_returnFalse() {
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void preserveWhitespace_tagThatShouldPreserveWhitespace_returnTrue() {
        tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void preserveWhitespace_tagThatShouldNotPreserveWhitespace_returnFalse() {
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void setSelfClosing_setSelfClosingTag_returnSelfClosingTag() {
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void equals_sameTagInstance_returnTrue() {
        assertTrue(tag.equals(tag));
    }

    @Test
    public void equals_tagWithDifferentTagName_returnFalse() {
        Tag otherTag = new Tag("otherTag");
        assertFalse(tag.equals(otherTag));
    }

    @Test
    public void equals_tagWithDifferentCanBeBlock_returnFalse() {
        Tag otherTag = new Tag("tag");
        otherTag.isBlock = false;
        assertFalse(tag.equals(otherTag));
    }

    @Test
    public void equals_tagWithDifferentFormatAsBlock_returnFalse() {
        Tag otherTag = new Tag("tag");
        otherTag.formatAsBlock = false;
        assertFalse(tag.equals(otherTag));
    }

    @Test
    public void equals_tagWithDifferentCanContainBlock_returnFalse() {
        Tag otherTag = new Tag("tag");
        otherTag.canContainBlock = false;
        assertFalse(tag.equals(otherTag));
    }

    @Test
    public void equals_tagWithDifferentCanContainInline_returnFalse() {
        Tag otherTag = new Tag("tag");
        otherTag.canContainInline = false;
        assertFalse(tag.equals(otherTag));
    }

    @Test
    public void equals_tagWithDifferentIsEmpty_returnFalse() {
        Tag otherTag = new Tag("tag");
        otherTag.empty = true;
        assertFalse(tag.equals(otherTag));
    }

    @Test
    public void equals_tagWithDifferentIsSelfClosing_returnFalse() {
        Tag otherTag = new Tag("tag");
        otherTag.selfClosing = true;
        assertFalse(tag.equals(otherTag));
    }

    @Test
    public void equals_tagWithDifferentPreserveWhitespace_returnFalse() {
        Tag otherTag = new Tag("tag");
        otherTag.preserveWhitespace = true;
        assertFalse(tag.equals(otherTag));
    }

    @Test
    public void hashCode_sameTagInstance_returnSameHashCode() {
        assertEquals(tag.hashCode(), tag.hashCode());
    }

    @Test
    public void hashCode_tagWithDifferentTagName_returnDifferentHashCode() {
        Tag otherTag = new Tag("otherTag");
        assertFalse(tag.hashCode() == otherTag.hashCode());
    }

    @Test
    public void hashCode_tagWithDifferentCanBeBlock_returnDifferentHashCode() {
        Tag otherTag = new Tag("tag");
        otherTag.isBlock = false;
        assertFalse(tag.hashCode() == otherTag.hashCode());
    }

    @Test
    public void hashCode_tagWithDifferentFormatAsBlock_returnDifferentHashCode() {
        Tag otherTag = new Tag("tag");
        otherTag.formatAsBlock = false;
        assertFalse(tag.hashCode() == otherTag.hashCode());
    }

    @Test
    public void hashCode_tagWithDifferentCanContainBlock_returnDifferentHashCode() {
        Tag otherTag = new Tag("tag");
        otherTag.canContainBlock = false;
        assertFalse(tag.hashCode() == otherTag.hashCode());
    }

    @Test
    public void hashCode_tagWithDifferentCanContainInline_returnDifferentHashCode() {
        Tag otherTag = new Tag("tag");
        otherTag.canContainInline = false;
        assertFalse(tag.hashCode() == otherTag.hashCode());
    }

    @Test
    public void hashCode_tagWithDifferentIsEmpty_returnDifferentHashCode() {
        Tag otherTag = new Tag("tag");
        otherTag.empty = true;
        assertFalse(tag.hashCode() == otherTag.hashCode());
    }

    @Test
    public void hashCode_tagWithDifferentIsSelfClosing_returnDifferentHashCode() {
        Tag otherTag = new Tag("tag");
        otherTag.selfClosing = true;
        assertFalse(tag.hashCode() == otherTag.hashCode());
    }

    @Test
    public void hashCode_tagWithDifferentPreserveWhitespace_returnDifferentHashCode() {
        Tag otherTag = new Tag("tag");
        otherTag.preserveWhitespace = true;
        assertFalse(tag.hashCode() == otherTag.hashCode());
    }

    @Test
    public void toString_returnTagName() {
        assertEquals("tag", tag.toString());
    }

    // TODO: Additional tests for internal static initializers

}