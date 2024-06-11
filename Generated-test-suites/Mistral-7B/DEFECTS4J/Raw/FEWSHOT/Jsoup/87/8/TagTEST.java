package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

    @Test
    public void testTagValues() {
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");
        Tag img = Tag.valueOf("img");

        Assert.assertEquals("html", html.getName());
        Assert.assertEquals("p", p.getName());
        Assert.assertEquals("img", img.getName());

        Assert.assertTrue(html.isBlock());
        Assert.assertTrue(p.isBlock());
        Assert.assertFalse(img.isBlock());

        Assert.assertTrue(html.formatAsBlock());
        Assert.assertTrue(p.formatAsBlock());
        Assert.assertFalse(img.formatAsBlock());

        Assert.assertTrue(html.canContainBlock());
        Assert.assertTrue(p.canContainBlock());
        Assert.assertFalse(img.canContainBlock());

        Assert.assertFalse(html.isEmpty());
        Assert.assertFalse(p.isEmpty());
        Assert.assertFalse(img.isEmpty());

        Assert.assertFalse(html.isSelfClosing());
        Assert.assertFalse(p.isSelfClosing());
        Assert.assertFalse(img.isSelfClosing());

        Assert.assertFalse(html.preserveWhitespace());
        Assert.assertFalse(p.preserveWhitespace());
        Assert.assertFalse(img.preserveWhitespace());

        Assert.assertFalse(html.isFormListed());
        Assert.assertFalse(p.isFormListed());
        Assert.assertFalse(img.isFormListed());

        Assert.assertFalse(html.isFormSubmittable());
        Assert.assertFalse(p.isFormSubmittable());
        Assert.assertFalse(img.isFormSubmittable());
    }

    @Test
    public void testTagValuesCaseInsensitive() {
        Tag HTML = Tag.valueOf("HTML");
        Tag P = Tag.getValue("p");
        Tag IMG = Tag.getValue("IMG");

        Assert.assertEquals("html", HTML.getName());
        Assert.assertEquals("p", P.getName());
        Assert.assertEquals("img", IMG.getName());

        Assert.assertTrue(HTML.isBlock());
        Assert.assertTrue(P.isBlock());
        Assert.assertFalse(IMG.isBlock());

        Assert.assertTrue(HTML.formatAsBlock());
        Assert.assertTrue(P.formatAsBlock());
        Assert.assertFalse(IMG.formatAsBlock());

        Assert.assertTrue(HTML.canContainBlock());
        Assert.assertTrue(P.canContainBlock());
        Assert.assertFalse(IMG.canContainBlock());

        Assert.assertFalse(HTML.isEmpty());
        Assert.assertFalse(P.isEmpty());
        Assert.assertFalse(IMG.isEmpty());

        Assert.assertFalse(HTML.isSelfClosing());
        Assert.assertFalse(P.isSelfClosing());
        Assert.assertFalse(IMG.isSelfClosing());

        Assert.assertFalse(HTML.preserveWhitespace());
        Assert.assertFalse(P.preserveWhitespace());
        Assert.assertFalse(IMG.preserveWhitespace());

        Assert.assertFalse(HTML.isFormListed());
        Assert.assertFalse(P.isFormListed());
        Assert.assertFalse(IMG.isFormListed());

        Assert.assertFalse(HTML.isFormSubmittable());
        Assert.assertFalse(P.isFormSubmittable());
        Assert.assertFalse(IMG.isFormSubmittable());
    }

    @Test
    public void testTagValuesUnknown() {
        Tag unknown = Tag.valueOf("unknown");

        Assert.assertEquals("unknown", unknown.getName());
        Assert.assertFalse(unknown.isBlock());
        Assert.assertFalse(unknown.formatAsBlock());
        Assert.assertFalse(unknown.canContainBlock());
        Assert.assertFalse(unknown.isEmpty());
        Assert.assertFalse(unknown.isSelfClosing());
        Assert.assertFalse(unknown.preserveWhitespace());
        Assert.assertFalse(unknown.isFormListed());
        Assert.assertFalse(unknown.isFormSubmittable());
    }

    @Test
    public void testIsKnownTag() {
        Assert.assertTrue(Tag.isKnownTag("html"));
        Assert.assertTrue(Tag.isKnownTag("p"));
        Assert.assertTrue(Tag.isKnownTag("img"));
        Assert.assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testEqualsAndHashCode() {
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag1");
        Tag tag3 = new Tag("tag2");

        Assert.assertEquals(tag1, tag1);
        Assert.assertEquals(tag1, tag2);
        Assert.assertNotEquals(tag1, tag3);

        Assert.assertEquals(tag1.hashCode(), tag2.hashCode());
        Assert.assertNotEquals(tag1.hashCode(), tag3.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("tag");
        Assert.assertEquals("tag", tag.toString());
    }
}