package org.jsoup.parser;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TagTest {

    @Test
    public void testTagCreation() {
        Tag tag = Tag.valueOf("p");
        Assert.assertEquals("p", tag.getName());
        Assert.assertTrue(tag.isBlock());
        Assert.assertTrue(tag.canContainBlock());
        Assert.assertTrue(tag.canContainInline());
        Assert.assertFalse(tag.optionalClosing());
        Assert.assertFalse(tag.isEmpty());
        Assert.assertFalse(tag.preserveWhitespace());
        Assert.assertEquals(Arrays.asList(new String[]{"html"}), tag.ancestors);
    }

    @Test
    public void testTagEquals() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        Tag tag3 = Tag.valueOf("div");

        Assert.assertTrue(tag1.equals(tag2));
        Assert.assertFalse(tag1.equals(tag3));
        Assert.assertFalse(tag3.equals(tag1));
    }

    @Test
    public void testTagHashCode() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        Tag tag3 = Tag.valueOf("div");

        Assert.assertEquals(tag1.hashCode(), tag2.hashCode());
        Assert.assertNotEquals(tag1.hashCode(), tag3.hashCode());
        Assert.assertNotEquals(tag3.hashCode(), tag1.hashCode());
    }

    @Test
    public void testTagToString() {
        Tag tag = Tag.valueOf("p");
        Assert.assertEquals("p", tag.toString());
    }

    @Test
    public void testGetImplicitParent() {
        Tag body = Tag.valueOf("body");
        Tag p = Tag.valueOf("p");
        p.setAncestor(body);

        Assert.assertNull(p.getImplicitParent());
        Assert.assertEquals(body, p.getImplicitParent());
    }

    @Test
    public void testCanContain() {
        Tag body = Tag.valueOf("body");
        Tag p = Tag.valueOf("p");
        Tag div = Tag.valueOf("div");
        Tag img = Tag.valueOf("img");
        Tag head = Tag.valueOf("head");
        Tag script = Tag.valueOf("script");
        Tag ul = Tag.valueOf("ul");
        Tag li = Tag.valueOf("li");

        Assert.assertTrue(body.canContain(p));
        Assert.assertTrue(body.canContain(div));
        Assert.assertTrue(body.canContain(img));
        Assert.assertFalse(body.canContain(head));
        Assert.assertFalse(body.canContain(script));
        Assert.assertTrue(p.canContain(img));
        Assert.assertTrue(ul.canContain(li));
        Assert.assertFalse(li.canContain(p));
    }

    @Test
    public void testIsValidParent() {
        Tag body = Tag.valueOf("body");
        Tag p = Tag.valueOf("p");
        Tag div = Tag.valueOf("div");
        Tag ul = Tag.valueOf("ul");
        Tag li = Tag.valueOf("li");

        p.ancestors = Arrays.asList(new String[]{"body", "p"});
        div.ancestors = Arrays.asList(new String[]{"body"});
        ul.ancestors = Arrays.asList(new String[]{"ul"});
        li.ancestors = Arrays.asList(new String[]{"ul", "li"});

        Assert.assertTrue(body.isValidParent(p));
        Assert.assertTrue(body.isValidParent(div));
        Assert.assertFalse(body.isValidParent(p)); // this is a test of the equals check in isValidParent
        Assert.assertTrue(ul.isValidParent(li));
        Assert.assertFalse(li.isValidParent(ul));
    }

    @Test
    public void testGetTagByName() {
        Tag body = Tag.valueOf("body");
        Tag p = Tag.valueOf("p");
        Tag div = Tag.valueOf("div");

        Assert.assertEquals(body, Tag.valueOf("BODY"));
        Assert.assertEquals(p, Tag.valueOf("p"));
        Assert.assertEquals(div, Tag.valueOf("div"));
        Assert.assertNull(Tag.valueOf("UNKNOWN"));
    }

    @Test
    public void testCreateBlockTag() {
        Tag html = createBlock("HTML");
        Tag head = createBlock("HEAD");
        Tag body = createBlock("BODY");

        Assert.assertEquals("HTML", html.getName());
        Assert.assertEquals("HEAD", head.getName());
        Assert.assertEquals("BODY", body.getName());
        Assert.assertEquals(new String[0], html.ancestors.toArray(new String[0]));
        Assert.assertEquals(new String[]{"HTML"}, head.ancestors.toArray(new String[0]));
        Assert.assertEquals(new String[]{"HTML"}, body.ancestors.toArray(new String[0]));
    }

    @Test
    public void testCreateInlineTag() {
        Tag font = createInline("FONT");
        Tag tt = createInline("TT");
        Tag i = createInline("I");

        Assert.assertEquals("FONT", font.getName());
        Assert.assertEquals("TT", tt.getName());
        Assert.assertEquals("I", i.getName());
        Assert.assertEquals(new String[0], font.ancestors.toArray(new String[0]));
        Assert.assertEquals(new String[0], tt.ancestors.toArray(new String[0]));
        Assert.assertEquals(new String[0], i.ancestors.toArray(new String[0]));
    }

    private Tag createBlock(String tagName) {
        return Tag.createBlock(tagName);
    }

    private Tag createInline(String tagName) {
        return Tag.createInline(tagName);
    }
}