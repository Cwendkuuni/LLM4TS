package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

    @Test
    public void testTagValueOfWithNormalizedName() {
        Tag tag = Tag.valueOf("p", ParseSettings.preserveCase);
        assertEquals("p", tag.getName());
        assertTrue(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.isFormListed());
        assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void testTagValueOfWithUnknownName() {
        Tag tag = Tag.valueOf("foo", ParseSettings.preserveCase);
        assertEquals("foo", tag.getName());
        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertFalse(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.isFormListed());
        assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void testTagValueOfWithLowercaseName() {
        Tag tag = Tag.valueOf("p", ParseSettings.normalizeTag);
        assertEquals("p", tag.getName());
        assertTrue(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.isFormListed());
        assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void testTagIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertTrue(Tag.isKnownTag("div"));
        assertTrue(Tag.isKnownTag("span"));
        assertFalse(Tag.isKnownTag("bar"));
    }

    @Test
    public void testTagIsBlock() {
        assertTrue(Tag.valueOf("p", ParseSettings.preserveCase).isBlock());
        assertTrue(Tag.valueOf("div", ParseSettings.preserveCase).isBlock());
        assertFalse(Tag.valueOf("img", ParseSettings.preserveCase).isBlock());
        assertFalse(Tag.valueOf("input", ParseSettings.preserveCase).isBlock());
    }

    @Test
    public void testTagIsInline() {
        assertFalse(Tag.valueOf("p", ParseSettings.preserveCase).isInline());
        assertFalse(Tag.valueOf("div", ParseSettings.preserveCase).isInline());
        assertTrue(Tag.valueOf("img", ParseSettings.preserveCase).isInline());
        assertTrue(Tag.valueOf("input", ParseSettings.preserveCase).isInline());
    }

    @Test
    public void testTagIsData() {
        assertFalse(Tag.valueOf("p", ParseSettings.preserveCase).isData());
        assertFalse(Tag.valueOf("div", ParseSettings.preserveCase).isData());
        assertFalse(Tag.valueOf("img", ParseSettings.preserveCase).isData());
        assertTrue(Tag.valueOf("script", ParseSettings.preserveCase).isData());
        assertTrue(Tag.valueOf("style", ParseSettings.preserveCase).isData());
    }

    @Test
    public void testTagIsEmpty() {
        assertFalse(Tag.valueOf("p", ParseSettings.preserveCase).isEmpty());
        assertFalse(Tag.valueOf("div", ParseSettings.preserveCase).isEmpty());
        assertTrue(Tag.valueOf("img", ParseSettings.preserveCase).isEmpty());
        assertTrue(Tag.valueOf("input", ParseSettings.preserveCase).isEmpty());
        assertTrue(Tag.valueOf("br", ParseSettings.preserveCase).isEmpty());
    }

    @Test
    public void testTagIsSelfClosing() {
        assertFalse(Tag.valueOf("p", ParseSettings.preserveCase).isSelfClosing());
        assertFalse(Tag.valueOf("div", ParseSettings.preserveCase).isSelfClosing());
        assertTrue(Tag.valueOf("img", ParseSettings.preserveCase).isSelfClosing());
        assertTrue(Tag.valueOf("input", ParseSettings.preserveCase).isSelfClosing());
        assertTrue(Tag.valueOf("br", ParseSettings.preserveCase).isSelfClosing());
        assertTrue(Tag.valueOf("img", ParseSettings.preserveCase).setSelfClosing().isSelfClosing());
    }

    @Test
    public void testTagIsKnownTagWithNormalizedName() {
        assertTrue(Tag.isKnownTag("p"));
        assertTrue(Tag.isKnownTag("div"));
        assertTrue(Tag.isKnownTag("span"));
        assertFalse(Tag.isKnownTag("BAR"));
    }

    @Test
    public void testTagPreserveWhitespace() {
        Tag tagPre = Tag.valueOf("pre", ParseSettings.preserveCase);
        assertTrue(tagPre.preserveWhitespace());

        Tag tagTextarea = Tag.valueOf("textarea", ParseSettings.preserveCase);
        assertTrue(tagTextarea.preserveWhitespace());

        Tag tagScript = Tag.valueOf("script", ParseSettings.preserveCase);
        assertTrue(tagScript.preserveWhitespace());
    }

    @Test
    public void testTagIsFormListed() {
        Tag tagButton = Tag.valueOf("button", ParseSettings.preserveCase);
        assertTrue(tagButton.isFormListed());

        Tag tagInput = Tag.valueOf("input", ParseSettings.preserveCase);
        assertTrue(tagInput.isFormListed());

        Tag tagTextarea = Tag.valueOf("textarea", ParseSettings.preserveCase);
        assertTrue(tagTextarea.isFormListed());

        Tag tagSelect = Tag.valueOf("select", ParseSettings.preserveCase);
        assertTrue(tagSelect.isFormListed());

        Tag tagOutput = Tag.valueOf("output", ParseSettings.preserveCase);
        assertTrue(tagOutput.isFormListed());

        Tag tagLabel = Tag.valueOf("label", ParseSettings.preserveCase);
        assertFalse(tagLabel.isFormListed());
    }

    @Test
    public void testTagIsFormSubmittable() {
        Tag tagInput = Tag.valueOf("input", ParseSettings.preserveCase);
        assertTrue(tagInput.isFormSubmittable());

        Tag tagSelect = Tag.valueOf("select", ParseSettings.preserveCase);
        assertTrue(tagSelect.isFormSubmittable());

        Tag tagTextarea = Tag.valueOf("textarea", ParseSettings.preserveCase);
        assertTrue(tagTextarea.isFormSubmittable());

        Tag tagButton = Tag.valueOf("button", ParseSettings.preserveCase);
        assertFalse(tagButton.isFormSubmittable());
    }
}