import org.junit.Assert;
import org.junit.Test;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("test");
        Assert.assertEquals("test", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("div");
        Assert.assertTrue(tag.isBlock());
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("test");
        tag.isBlock = true;
        Assert.assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("test");
        tag.formatAsBlock = true;
        Assert.assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("test");
        tag.canContainBlock = true;
        Assert.assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("test");
        tag.isBlock = false;
        Assert.assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("test");
        tag.canContainInline = false;
        tag.empty = false;
        Assert.assertTrue(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("test");
        tag.empty = true;
        Assert.assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("test");
        tag.empty = true;
        tag.selfClosing = false;
        Assert.assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag = new Tag("test");
        Assert.assertFalse(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithStringTag() {
        Assert.assertFalse(Tag.isKnownTag("test"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("test");
        tag.preserveWhitespace = true;
        Assert.assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("test");
        tag.setSelfClosing();
        Assert.assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("test");
        Tag tag2 = new Tag("test");
        Assert.assertEquals(tag1, tag2);
    }

    @Test
    public void testHashCode() {
        Tag tag = new Tag("test");
        Assert.assertEquals("test".hashCode(), tag.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("test");
        Assert.assertEquals("test", tag.toString());
    }
}