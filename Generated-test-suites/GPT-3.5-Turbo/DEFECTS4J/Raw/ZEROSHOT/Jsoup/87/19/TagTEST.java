import org.jsoup.helper.Validate;
import org.junit.Assert;
import org.junit.Test;

public class TagTest {
    @Test
    public void testGetName() {
        Tag tag = new Tag("span");
        Assert.assertEquals("span", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        Assert.assertTrue(tag.isBlock());
        Assert.assertTrue(tag.formatAsBlock());
        Assert.assertTrue(tag.canContainBlock());

        Tag unknownTag = Tag.valueOf("unknown");
        Assert.assertFalse(unknownTag.isBlock());
        Assert.assertFalse(unknownTag.formatAsBlock());
        Assert.assertFalse(unknownTag.canContainBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("h1");
        Assert.assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("i");
        Assert.assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("img");
        Assert.assertTrue(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("br");
        Assert.assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("img");
        Assert.assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Assert.assertTrue(Tag.isKnownTag("p"));
        Assert.assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("pre");
        Assert.assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag = new Tag("input");
        Assert.assertTrue(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("input");
        Assert.assertTrue(tag.isFormSubmittable());
    }

    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        Assert.assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("a");
        Assert.assertEquals("a", tag.toString());
    }
}