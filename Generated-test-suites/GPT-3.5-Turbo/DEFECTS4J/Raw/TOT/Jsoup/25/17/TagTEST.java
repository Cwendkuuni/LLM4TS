import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("p");
        Assert.assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("unknown");
        Assert.assertFalse(tag.isBlock());
        Assert.assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        Assert.assertTrue(tag.isBlock());

        tag = Tag.valueOf("h1");
        Assert.assertTrue(tag.isBlock());

        tag = Tag.valueOf("img");
        Assert.assertFalse(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("p");
        Assert.assertTrue(tag.formatAsBlock());

        tag = Tag.valueOf("h1");
        Assert.assertTrue(tag.formatAsBlock());

        tag = Tag.valueOf("img");
        Assert.assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("p");
        Assert.assertTrue(tag.canContainBlock());

        tag = Tag.valueOf("h1");
        Assert.assertTrue(tag.canContainBlock());

        tag = Tag.valueOf("img");
        Assert.assertFalse(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("p");
        Assert.assertFalse(tag.isInline());

        tag = Tag.valueOf("h1");
        Assert.assertFalse(tag.isInline());

        tag = Tag.valueOf("img");
        Assert.assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("p");
        Assert.assertFalse(tag.isData());

        tag = Tag.valueOf("img");
        Assert.assertFalse(tag.isData());

        tag = Tag.valueOf("pre");
        Assert.assertTrue(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("p");
        Assert.assertFalse(tag.isEmpty());

        tag = Tag.valueOf("img");
        Assert.assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("p");
        Assert.assertFalse(tag.isSelfClosing());

        tag = Tag.valueOf("img");
        Assert.assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag = Tag.valueOf("p");
        Assert.assertTrue(tag.isKnownTag());

        tag = Tag.valueOf("unknown");
        Assert.assertFalse(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithTagName() {
        Assert.assertTrue(Tag.isKnownTag("p"));
        Assert.assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("p");
        Assert.assertFalse(tag.preserveWhitespace());

        tag = Tag.valueOf("pre");
        Assert.assertTrue(tag.preserveWhitespace());
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
        Assert.assertEquals(tag1, tag2);

        tag2 = Tag.valueOf("h1");
        Assert.assertNotEquals(tag1, tag2);
    }

    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        Assert.assertEquals(tag1.hashCode(), tag2.hashCode());

        tag2 = Tag.valueOf("h1");
        Assert.assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("p");
        Assert.assertEquals("p", tag.toString());
    }

    @Test
    public void testBlockTags() {
        for (String tagName : Tag.blockTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertTrue(tag.isBlock());
            Assert.assertTrue(tag.formatAsBlock());
            Assert.assertTrue(tag.canContainBlock());
        }
    }

    @Test
    public void testInlineTags() {
        for (String tagName : Tag.inlineTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertFalse(tag.isBlock());
            Assert.assertFalse(tag.formatAsBlock());
            Assert.assertFalse(tag.canContainBlock());
        }
    }

    @Test
    public void testEmptyTags() {
        for (String tagName : Tag.emptyTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertFalse(tag.canContainBlock());
            Assert.assertFalse(tag.canContainInline());
            Assert.assertTrue(tag.isEmpty());
        }
    }

    @Test
    public void testFormatAsInlineTags() {
        for (String tagName : Tag.formatAsInlineTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertFalse(tag.formatAsBlock());
        }
    }

    @Test
    public void testPreserveWhitespaceTags() {
        for (String tagName : Tag.preserveWhitespaceTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertTrue(tag.preserveWhitespace());
        }
    }

    @Test
    public void testRegister() {
        Tag tag = new Tag("custom");
        Tag.register(tag);

        Map<String, Tag> tags = Tag.getTags();
        Assert.assertTrue(tags.containsKey("custom"));
        Assert.assertEquals(tag, tags.get("custom"));
    }
}