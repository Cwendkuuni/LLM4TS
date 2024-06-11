package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.junit.Assert;
import org.junit.Test;

public class TagTest {

    @Test
    public void testTagValues() {
        Tag pTag = Tag.valueOf("p");
        Tag inputTag = Tag.valueOf("input");
        Tag divTag = Tag.valueOf("div");

        Assert.assertEquals("p", pTag.getName());
        Assert.assertEquals("input", inputTag.getName());
        Assert.assertEquals("div", divTag.getName());

        Assert.assertTrue(pTag.isBlock());
        Assert.assertFalse(inputTag.isBlock());
        Assert.assertTrue(divTag.isBlock());

        Assert.assertTrue(pTag.formatAsBlock());
        Assert.assertFalse(inputTag.formatAsBlock());
        Assert.assertTrue(divTag.formatAsBlock());

        Tag emptyTag = Tag.valueOf("meta");
        Assert.assertFalse(emptyTag.isBlock());
        Assert.assertFalse(emptyTag.formatAsBlock());
        Assert.assertTrue(emptyTag.isEmpty());
    }

    @Test
    public void testTagValueCaseSensitive() {
        Tag pTag = Tag.valueOf("p");
        Tag PTag = Tag.valueOf("P");

        Assert.assertEquals("p", pTag.getName());
        Assert.assertEquals("P", PTag.getName());
        Assert.assertEquals(pTag, PTag);
    }

    @Test
    public void testUnknownTagValue() {
        Tag unknownTag = Tag.valueOf("unknownTag");

        Assert.assertEquals("unknownTag", unknownTag.getName());
        Assert.assertFalse(unknownTag.isBlock());
        Assert.assertFalse(unknownTag.formatAsBlock());
        Assert.assertFalse(unknownTag.isEmpty());
    }

    @Test
    public void testSelfClosingTag() {
        Tag imgTag = Tag.valueOf("img");
        imgTag = imgTag.setSelfClosing();

        Assert.assertEquals("img", imgTag.getName());
        Assert.assertTrue(imgTag.isSelfClosing());
        Assert.assertFalse(imgTag.isEmpty());
    }

    @Test
    public void testIsKnownTag() {
        Assert.assertTrue(Tag.isKnownTag("p"));
        Assert.assertFalse(Tag.isKnownTag("unknownTag"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag preTag = Tag.valueOf("pre");
        Assert.assertTrue(preTag.preserveWhitespace());

        Tag plaintextTag = Tag.valueOf("plaintext");
        Assert.assertTrue(plaintextTag.preserveWhitespace());

        Tag titleTag = Tag.valueOf("title");
        Assert.assertFalse(titleTag.preserveWhitespace());
    }

    @Test
    public void testFormListed() {
        Tag buttonTag = Tag.valueOf("button");
        Assert.assertTrue(buttonTag.isFormListed());

        Tag textareaTag = Tag.valueOf("textarea");
        Assert.assertTrue(textareaTag.isFormListed());

        Tag pTag = Tag.valueOf("p");
        Assert.assertFalse(pTag.isFormListed());
    }

    @Test
    public void testFormSubmittable() {
        Tag inputTag = Tag.valueOf("input");
        Assert.assertTrue(inputTag.isFormSubmittable());

        Tag textareaTag = Tag.valueOf("textarea");
        Assert.assertTrue(textareaTag.isFormSubmittable());

        Tag pTag = Tag.valueOf("p");
        Assert.assertFalse(pTag.isFormSubmittable());
    }
}