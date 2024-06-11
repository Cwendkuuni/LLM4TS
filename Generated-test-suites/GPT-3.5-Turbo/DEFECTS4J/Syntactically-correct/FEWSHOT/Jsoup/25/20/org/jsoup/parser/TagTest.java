package org.jsoup.parser;
import org.jsoup.helper.Validate;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("p");
        Assert.assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("div");
        Tag tag3 = Tag.valueOf("span");
        Assert.assertTrue(tag1.isBlock());
        Assert.assertTrue(tag2.isBlock());
        Assert.assertTrue(tag3.isBlock());
        Assert.assertTrue(tag1.formatAsBlock());
        Assert.assertTrue(tag2.formatAsBlock());
        Assert.assertTrue(tag3.formatAsBlock());
        Assert.assertTrue(tag1.canContainBlock());
        Assert.assertTrue(tag2.canContainBlock());
        Assert.assertTrue(tag3.canContainBlock());
        Assert.assertFalse(tag1.isInline());
        Assert.assertFalse(tag2.isInline());
        Assert.assertFalse(tag3.isInline());
        Assert.assertFalse(tag1.isData());
        Assert.assertFalse(tag2.isData());
        Assert.assertFalse(tag3.isData());
        Assert.assertFalse(tag1.isEmpty());
        Assert.assertFalse(tag2.isEmpty());
        Assert.assertFalse(tag3.isEmpty());
        Assert.assertFalse(tag1.isSelfClosing());
        Assert.assertFalse(tag2.isSelfClosing());
        Assert.assertFalse(tag3.isSelfClosing());
        Assert.assertTrue(tag1.isKnownTag());
        Assert.assertFalse(tag2.isKnownTag());
        Assert.assertFalse(tag3.isKnownTag());
        Assert.assertFalse(tag1.preserveWhitespace());
        Assert.assertFalse(tag2.preserveWhitespace());
        Assert.assertFalse(tag3.preserveWhitespace());
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        Assert.assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("p");
        Assert.assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("p");
        Assert.assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("p");
        Assert.assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("p");
        Assert.assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("p");
        Assert.assertFalse(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("p");
        Assert.assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("div");
        Tag tag3 = Tag.valueOf("span");
        Assert.assertTrue(tag1.isKnownTag());
        Assert.assertFalse(tag2.isKnownTag());
        Assert.assertFalse(tag3.isKnownTag());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("p");
        Assert.assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = Tag.valueOf("p");
        tag.setSelfClosing();
        Assert.assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        Tag tag3 = Tag.valueOf("div");
        Assert.assertTrue(tag1.equals(tag2));
        Assert.assertFalse(tag1.equals(tag3));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        Assert.assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("p");
        Assert.assertEquals("p", tag.toString());
    }
}