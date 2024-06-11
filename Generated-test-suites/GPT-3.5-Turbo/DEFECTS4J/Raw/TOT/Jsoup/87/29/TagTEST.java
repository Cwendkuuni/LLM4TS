import org.jsoup.helper.Validate;
import org.junit.Assert;
import org.junit.Test;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("p");
        Assert.assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOfWithSettings() {
        ParseSettings settings = ParseSettings.preserveCase;
        Tag tag = Tag.valueOf("p", settings);
        Assert.assertNotNull(tag);
        Assert.assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOfWithoutSettings() {
        Tag tag = Tag.valueOf("p");
        Assert.assertNotNull(tag);
        Assert.assertEquals("p", tag.getName());
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("p");
        Assert.assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("p");
        Assert.assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("p");
        Assert.assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("div");
        Assert.assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("span");
        Assert.assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("img");
        Assert.assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("img");
        Assert.assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag = new Tag("div");
        Assert.assertFalse(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithName() {
        Assert.assertTrue(Tag.isKnownTag("a"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("pre");
        Assert.assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag = Tag.valueOf("button");
        Assert.assertTrue(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = Tag.valueOf("input");
        Assert.assertTrue(tag.isFormSubmittable());
    }
}