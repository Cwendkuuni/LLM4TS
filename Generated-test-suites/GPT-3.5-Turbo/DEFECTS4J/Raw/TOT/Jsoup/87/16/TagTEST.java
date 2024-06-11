import org.jsoup.parser.ParseSettings;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TagTest {

    @Before
    public void setup() {
        // Ensure that the tags HashMap for Tag class is empty before each test
        Tag.tags.clear();
    }

    @Test
    public void testGetName() {
        Tag tag = new Tag("p");
        Assert.assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOf() {
        ParseSettings settings = new ParseSettings(false, false);
        Tag tag = Tag.valueOf("unknownTag", settings);
        Assert.assertFalse(tag.isBlock());
        Assert.assertTrue(tag.isInline());
        Assert.assertFalse(tag.formatAsBlock());
        Assert.assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("section");
        Assert.assertTrue(tag.isBlock());
        tag = new Tag("span");
        Assert.assertFalse(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("p");
        Assert.assertTrue(tag.formatAsBlock());
        tag = new Tag("strong");
        Assert.assertFalse(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("p");
        Assert.assertTrue(tag.canContainBlock());
        tag = new Tag("strong");
        Assert.assertFalse(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("span");
        Assert.assertTrue(tag.isInline());
        tag = new Tag("section");
        Assert.assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("span");
        Assert.assertFalse(tag.isData());
        tag = new Tag("pre");
        Assert.assertTrue(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("span");
        Assert.assertFalse(tag.isEmpty());
        tag = new Tag("img");
        Assert.assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("span");
        Assert.assertFalse(tag.isSelfClosing());
        tag = new Tag("img");
        Assert.assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag = new Tag("p");
        Assert.assertTrue(tag.isKnownTag());
        tag = new Tag("unknownTag");
        Assert.assertFalse(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithName() {
        Assert.assertTrue(Tag.isKnownTag("p"));
        Assert.assertFalse(Tag.isKnownTag("unknownTag"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("pre");
        Assert.assertTrue(tag.preserveWhitespace());
        tag = new Tag("span");
        Assert.assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag = new Tag("input");
        Assert.assertTrue(tag.isFormListed());
        tag = new Tag("span");
        Assert.assertFalse(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("input");
        Assert.assertTrue(tag.isFormSubmittable());
        tag = new Tag("span");
        Assert.assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("span");
        tag.setSelfClosing();
        Assert.assertTrue(tag.isSelfClosing());
    }

}