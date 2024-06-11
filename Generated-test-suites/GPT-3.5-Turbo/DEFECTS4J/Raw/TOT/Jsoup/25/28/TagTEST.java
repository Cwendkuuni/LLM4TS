import org.jsoup.helper.Validate;
import org.junit.Assert;
import org.junit.Test;

public class TagTest {

    // Test case for getName() method
    @Test
    public void testGetName() {
        Tag tag = new Tag("p");
        Assert.assertEquals("p", tag.getName());
    }

    // Test case for valueOf() method when tag name is a known tag
    @Test
    public void testValueOfKnownTag() {
        Tag tag = Tag.valueOf("p");
        Assert.assertNotNull(tag);
        Assert.assertTrue(tag.isKnownTag());
    }

    // Test case for valueOf() method when tag name is an unknown tag
    @Test
    public void testValueOfUnknownTag() {
        Tag tag = Tag.valueOf("customTag");
        Assert.assertNotNull(tag);
        Assert.assertFalse(tag.isKnownTag());
    }

    // Test case for isBlock() method
    @Test
    public void testIsBlock() {
        Tag tag = new Tag("h1");
        Assert.assertTrue(tag.isBlock());
        tag = Tag.valueOf("span");
        Assert.assertFalse(tag.isBlock());
    }

    // Test case for formatAsBlock() method
    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("p");
        Assert.assertTrue(tag.formatAsBlock());
        tag = Tag.valueOf("span");
        Assert.assertFalse(tag.formatAsBlock());
    }

    // Test case for canContainBlock() method
    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("div");
        Assert.assertTrue(tag.canContainBlock());
        tag = Tag.valueOf("span");
        Assert.assertFalse(tag.canContainBlock());
    }

    // Test case for isInline() method
    @Test
    public void testIsInline() {
        Tag tag = new Tag("span");
        Assert.assertTrue(tag.isInline());
        tag = Tag.valueOf("h1");
        Assert.assertFalse(tag.isInline());
    }

    // Test case for isData() method
    @Test
    public void testIsData() {
        Tag tag = new Tag("span");
        Assert.assertTrue(tag.isData());
        tag = Tag.valueOf("p");
        Assert.assertFalse(tag.isData());
        tag = Tag.valueOf("img");
        Assert.assertFalse(tag.isData());
    }

    // Test case for isEmpty() method
    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("img");
        Assert.assertTrue(tag.isEmpty());
        tag = new Tag("div");
        Assert.assertFalse(tag.isEmpty());
    }

    // Test case for isSelfClosing() method
    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("img");
        Assert.assertTrue(tag.isSelfClosing());
        tag = new Tag("p");
        Assert.assertFalse(tag.isSelfClosing());
    }

    // Test case for preserveWhitespace() method
    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("pre");
        Assert.assertTrue(tag.preserveWhitespace());
        tag = new Tag("div");
        Assert.assertFalse(tag.preserveWhitespace());
    }

    // Test case for equals() method when two tags are equal
    @Test
    public void testEqualsEqualTags() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        Assert.assertTrue(tag1.equals(tag2));
    }

    // Test case for equals() method when two tags are not equal
    @Test
    public void testEqualsNotEqualTags() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("div");
        Assert.assertFalse(tag1.equals(tag2));
    }

    // Test case for hashCode() method
    @Test
    public void testHashCode() {
        Tag tag = new Tag("p");
        Assert.assertEquals(tag.getName().hashCode(), tag.hashCode());
    }

    // Test case for toString() method
    @Test
    public void testToString() {
        Tag tag = new Tag("p");
        Assert.assertEquals("p", tag.toString());
    }
}