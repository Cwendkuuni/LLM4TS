import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;

public class TagTest {

    private Tag tag;

    @Before
    public void setUp() {
        tag = new Tag("testTagName");
    }

    @Test
    public void testGetName() {
        Assert.assertEquals("testTagName", tag.getName());
    }

    @Test
    public void testValueOfWithTagNameAndSettings() {
        ParseSettings settings = ParseSettings.preserveCase;
        Tag newTag = Tag.valueOf("testTagName", settings);
        Assert.assertEquals(tag, newTag);
    }

    @Test
    public void testValueOfWithTagNameOnly() {
        Tag newTag = Tag.valueOf("testTagName");
        Assert.assertEquals(tag, newTag);
    }

    @Test
    public void testIsBlock() {
        Assert.assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Assert.assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Assert.assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Assert.assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        Assert.assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Assert.assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Assert.assertTrue(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithName() {
        Assert.assertTrue(Tag.isKnownTag("testTagName"));
    }

    @Test
    public void testPreserveWhitespace() {
        Assert.assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Assert.assertFalse(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Assert.assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        Assert.assertFalse(tag.isSelfClosing());
        tag.setSelfClosing();
        Assert.assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag newTag = new Tag("testTagName");
        Assert.assertEquals(tag, newTag);
    }

    @Test
    public void testHashCode() {
        Tag newTag = new Tag("testTagName");
        Assert.assertEquals(tag.hashCode(), newTag.hashCode());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("testTagName", tag.toString());
    }

    // Additional Test Cases

    @Test
    public void testCanContainInline() {
        tag = Tag.valueOf("img");
        Assert.assertFalse(tag.canContainInline());
    }

    @Test
    public void testIsKnownTagFalse() {
        Assert.assertFalse(Tag.isKnownTag("unknownTag"));
    }

    @Test
    public void testIsBlockFalse() {
        tag = Tag.valueOf("img");
        Assert.assertFalse(tag.isBlock());
    }

    @Test
    public void testIsBlockFalseWithUnknownTag() {
        tag = Tag.valueOf("unknownTag");
        Assert.assertFalse(tag.isBlock());
    }
}