package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.jsoup.parser.Tag.blockTags;
import static org.jsoup.parser.Tag.emptyTags;
import static org.jsoup.parser.Tag.formatAsBlock;
import static org.jsoup.parser.Tag.formatAsInline;
import static org.jsoup.parser.Tag.isBlock;
import static org.jsoup.parser.Tag.isData;
import static org.jsoup.parser.Tag.isEmpty;
import static org.jsoup.parser.Tag.isFormListed;
import static org.jsoup.parser.Tag.isFormSubmittable;
import static org.jsoup.parser.Tag.isFormSubmitTag;
import static org.jsoup.parser.Tag.isInline;
import static org.jsoup.parser.Tag.isKnownTag;
import static org.jsoup.parser.Tag.isPreserveWhitespace;
import static org.jsoup.parser.Tag.preserveWhitespaceTags;

public class TagTest {

    @Test
    public void testBlockTagsAsBlock() {
        for (String tag : blockTags) {
            Tag t = Tag.valueOf(tag);
            Assert.assertTrue(isBlock(t));
            Assert.assertTrue(formatAsBlock(t));
        }
    }

    @Test
    public void testBlockTagsAsInline() {
        for (String tag : blockTags) {
            Tag t = Tag.valueOf(tag);
            Assert.assertFalse(isInline(t));
            Assert.assertTrue(isBlock(t));
        }
    }

    @Test
    public void testInlineTagsAsInline() {
        for (String tag : inlineTags) {
            Tag t = Tag.valueOf(tag);
            Assert.assertTrue(isInline(t));
            Assert.assertFalse(isBlock(t));
        }
    }

    @Test
    public void testEmptyTags() {
        for (String tag : emptyTags) {
            Tag t = Tag.valueOf(tag);
            Assert.assertTrue(isData(t));
            Assert.assertTrue(isEmpty(t));
        }
    }

    @Test
    public void testFormatAsBlockTags() {
        for (String tag : formatAsBlock) {
            Tag t = Tag.valueOf(tag);
            Assert.assertTrue(formatAsBlock(t));
        }
    }

    @Test
    public void testFormatAsInlineTags() {
        for (String tag : formatAsInline) {
            Tag t = Tag.valueOf(tag);
            Assert.assertFalse(formatAsBlock(t));
        }
    }

    @Test
    public void testPreserveWhitespaceTags() {
        for (String tag : preserveWhitespaceTags) {
            Tag t = Tag.valueOf(tag);
            Assert.assertTrue(isPreserveWhitespace(t));
        }
    }

    @Test
    public void testIsFormListedTags() {
        for (String tag : formListedTags) {
            Tag t = Tag.valueOf(tag);
            Assert.assertTrue(isFormListed(t));
        }
    }

    @Test
    public void testIsFormSubmittableTags() {
        for (String tag : formSubmitTags) {
            Tag t = Tag.valueOf(tag);
            Assert.assertTrue(isFormSubmittable(t));
        }
    }

    @Test
    public void testIsFormSubmitTag() {
        Assert.assertTrue(isFormSubmitTag("input"));
        Assert.assertTrue(isFormSubmitTag("textarea"));
        Assert.assertFalse(isFormSubmitTag("p"));
        Assert.assertFalse(isFormSubmitTag("div"));
    }

    @Test
    public void testIsKnownTag() {
        Assert.assertTrue(isKnownTag("html"));
        Assert.assertTrue(isKnownTag("p"));
        Assert.assertFalse(isKnownTag("foo"));
    }
}