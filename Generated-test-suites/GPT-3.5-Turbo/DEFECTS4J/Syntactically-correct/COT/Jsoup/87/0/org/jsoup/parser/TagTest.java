
import org.junit.Assert;
import org.junit.Test;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("tagName");
        Assert.assertEquals("tagName", tag.getName());
    }

    @Test
    public void testValueOfWithSettings() {
        Tag tag = Tag.valueOf("tagName", ParseSettings.preserveCase);
        Assert.assertNotNull(tag);
    }

    @Test
    public void testValueOfWithoutSettings() {
        Tag tag = Tag.valueOf("tagName");
        Assert.assertNotNull(tag);
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("tagName");
        Assert.assertTrue(tag.isBlock());
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
        Tag tag = new Tag("tagName");
        Assert.assertFalse(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithTagName() {
        boolean isKnownTag = Tag.isKnownTag("tagName");
        Assert.assertFalse(isKnownTag);
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("tagName");
        Assert.assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag = new Tag("tagName");
        Assert.assertFalse(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("tagName");
        Assert.assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("tagName");
        Tag tag2 = new Tag("tagName");
        Assert.assertEquals(tag1, tag2);
    }

    @Test
    public void testHashCode() {
        Tag tag = new Tag("tagName");
        Assert.assertEquals(-838832742, tag.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("tagName");
        Assert.assertEquals("tagName", tag.toString());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("tagName");
        Assert.assertTrue(tag.setSelfClosing().isSelfClosing());
    }
}
