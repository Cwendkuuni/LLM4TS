import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Tag;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("tagName");
        Assert.assertEquals("tagName", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("tagName");
        Assert.assertTrue(tag instanceof Tag);
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("tagName");
        tag.setSelfClosing();
        Assert.assertFalse(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("tagName");
        Assert.assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("tagName");
        Assert.assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("tagName");
        Assert.assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("tagName");
        Assert.assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("tagName");
        Assert.assertFalse(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("tagName");
        Assert.assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Assert.assertFalse(Tag.isKnownTag("tagName"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("tagName");
        Assert.assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("tagName");
        tag.setSelfClosing();
        Assert.assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("tagName");
        Tag tag2 = new Tag("tagName");
        Assert.assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("tagName");
        Tag tag2 = new Tag("tagName");
        Assert.assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("tagName");
        Assert.assertEquals("tagName", tag.toString());
    }

    // Additional test cases for edge cases and exceptions

    @Test
    public void testValueOf_unknownTag() {
        Tag tag = Tag.valueOf("unknownTag");
        Assert.assertEquals("unknownTag", tag.getName());
        Assert.assertFalse(tag.isBlock());
        Assert.assertTrue(tag.canContainBlock());
    }

    @Test
    public void testValueOf_nullTagName() {
        try {
            Tag tag = Tag.valueOf(null);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("tagName must not be null", e.getMessage());
        }
    }

    @Test
    public void testValueOf_emptyTagName() {
        try {
            Tag tag = Tag.valueOf("");
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("tagName must not be empty", e.getMessage());
        }
    }

    @Test
    public void testIsKnownTag_knownTag() {
        Assert.assertTrue(Tag.isKnownTag("html"));
    }

    @Test
    public void testIsKnownTag_unknownTag() {
        Assert.assertFalse(Tag.isKnownTag("unknownTag"));
    }

    @Test
    public void testIsKnownTag_nullTagName() {
        try {
            boolean isKnownTag = Tag.isKnownTag(null);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("tagName must not be null", e.getMessage());
        }
    }
}