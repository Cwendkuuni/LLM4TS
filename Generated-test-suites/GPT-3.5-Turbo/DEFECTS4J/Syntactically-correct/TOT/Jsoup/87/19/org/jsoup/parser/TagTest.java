import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Tag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TagTest {
    private ParseSettings parseSettings;

    @Before
    public void setUp() {
        parseSettings = ParseSettings.preserveCase;
    }

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("div", parseSettings);

        Assert.assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("span", parseSettings);

        Assert.assertFalse(tag.isBlock());
        Assert.assertTrue(tag.isInline());
        Assert.assertFalse(tag.formatAsBlock());
        Assert.assertTrue(tag.canContainBlock());

        Tag tag2 = Tag.valueOf("customTag", parseSettings);

        Assert.assertFalse(tag2.isBlock());
        Assert.assertTrue(tag2.isInline());
        Assert.assertFalse(tag2.formatAsBlock());
        Assert.assertTrue(tag2.canContainBlock());
    }

    @Test
    public void testValueOfUnknownTag() {
        Tag tag = Tag.valueOf("unknownTag");

        Assert.assertTrue(tag.isInline());
        Assert.assertFalse(tag.formatAsBlock());
        Assert.assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p", parseSettings);

        Assert.assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("span", parseSettings);

        Assert.assertFalse(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("span", parseSettings);

        Assert.assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("div", parseSettings);

        Assert.assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("div", parseSettings);

        Assert.assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("div", parseSettings);

        Assert.assertFalse(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("img", parseSettings);

        Assert.assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag = Tag.valueOf("span", parseSettings);

        Assert.assertTrue(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagUnknownTag() {
        Tag tag = Tag.valueOf("unknownTag");

        Assert.assertFalse(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagCaseSensitive() {
        Tag tag = Tag.valueOf("script");

        Assert.assertFalse(tag.isKnownTag("SCRIPT"));
        Assert.assertFalse(tag.isKnownTag("script"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre", parseSettings);

        Assert.assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag = Tag.valueOf("input", parseSettings);

        Assert.assertTrue(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = Tag.valueOf("input", parseSettings);

        Assert.assertTrue(tag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("customTag");
        tag.setSelfClosing();

        Assert.assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("a", parseSettings);
        Tag tag2 = Tag.valueOf("a", parseSettings);

        Assert.assertEquals(tag1, tag2);
    }

    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("a", parseSettings);
        Tag tag2 = Tag.valueOf("a", parseSettings);

        Assert.assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("div", parseSettings);

        Assert.assertEquals("div", tag.toString());
    }
}