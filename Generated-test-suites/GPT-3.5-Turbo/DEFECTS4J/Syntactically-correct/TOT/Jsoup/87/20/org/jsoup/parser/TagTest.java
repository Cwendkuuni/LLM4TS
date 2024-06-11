import org.jsoup.parser.Tag;
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
        Tag tag = Tag.valueOf("p");
        Assert.assertEquals("p", tag.getName());
        Assert.assertTrue(tag.isBlock());
        Assert.assertTrue(tag.formatAsBlock());
        Assert.assertTrue(tag.canContainBlock());

        tag = Tag.valueOf("test", ParseSettings.preserveCase);
        Assert.assertEquals("test", tag.getName());
        Assert.assertFalse(tag.isBlock());
        Assert.assertFalse(tag.formatAsBlock());
        Assert.assertFalse(tag.canContainBlock());
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("test");
        Assert.assertTrue(tag.isBlock());

        tag = Tag.valueOf("p");
        Assert.assertTrue(tag.isBlock());

        tag = Tag.valueOf("test", ParseSettings.preserveCase);
        Assert.assertFalse(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("test");
        Assert.assertTrue(tag.formatAsBlock());

        tag = Tag.valueOf("p");
        Assert.assertTrue(tag.formatAsBlock());

        tag = Tag.valueOf("test", ParseSettings.preserveCase);
        Assert.assertFalse(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("test");
        Assert.assertTrue(tag.canContainBlock());

        tag = Tag.valueOf("p");
        Assert.assertTrue(tag.canContainBlock());

        tag = Tag.valueOf("test", ParseSettings.preserveCase);
        Assert.assertFalse(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("test");
        Assert.assertFalse(tag.isInline());

        tag = Tag.valueOf("p");
        Assert.assertFalse(tag.isInline());

        tag = Tag.valueOf("test", ParseSettings.preserveCase);
        Assert.assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("test");
        Assert.assertFalse(tag.isData());

        tag = Tag.valueOf("p");
        Assert.assertFalse(tag.isData());

        tag = Tag.valueOf("test", ParseSettings.preserveCase);
        Assert.assertTrue(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("test");
        Assert.assertFalse(tag.isEmpty());

        tag = Tag.valueOf("p");
        Assert.assertFalse(tag.isEmpty());

        tag = Tag.valueOf("test", ParseSettings.preserveCase);
        Assert.assertFalse(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("test");
        Assert.assertFalse(tag.isSelfClosing());

        tag = Tag.valueOf("p");
        Assert.assertFalse(tag.isSelfClosing());

        tag = Tag.valueOf("test", ParseSettings.preserveCase);
        Assert.assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag = new Tag("test");
        Assert.assertFalse(tag.isKnownTag());

        tag = Tag.valueOf("p");
        Assert.assertTrue(tag.isKnownTag());

        tag = Tag.valueOf("test", ParseSettings.preserveCase);
        Assert.assertFalse(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithName() {
        Assert.assertFalse(Tag.isKnownTag("test"));
        Assert.assertTrue(Tag.isKnownTag("p"));
        Assert.assertFalse(Tag.isKnownTag("test", ParseSettings.preserveCase));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("test");
        Assert.assertFalse(tag.preserveWhitespace());

        tag = Tag.valueOf("p");
        Assert.assertFalse(tag.preserveWhitespace());

        tag = Tag.valueOf("test", ParseSettings.preserveCase);
        Assert.assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag = new Tag("test");
        Assert.assertFalse(tag.isFormListed());

        tag = Tag.valueOf("p");
        Assert.assertFalse(tag.isFormListed());

        tag = Tag.valueOf("test", ParseSettings.preserveCase);
        Assert.assertFalse(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("test");
        Assert.assertFalse(tag.isFormSubmittable());

        tag = Tag.valueOf("p");
        Assert.assertFalse(tag.isFormSubmittable());

        tag = Tag.valueOf("test", ParseSettings.preserveCase);
        Assert.assertFalse(tag.isFormSubmittable());
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
        Tag tag1 = new Tag("test");
        Tag tag2 = new Tag("test");
        Assert.assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("test");
        Assert.assertEquals("test", tag.toString());
    }
}