import org.jsoup.helper.Validate;
import org.junit.Assert;
import org.junit.Test;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("tagName");
        Assert.assertEquals("tagName", tag.getName());
    }
    
    @Test
    public void testValueOf() {
        ParseSettings settings = new ParseSettings(false, false);
        Tag tag = Tag.valueOf("tagName", settings);
        Assert.assertNotNull(tag);
    }
    
    @Test
    public void testValueOfCaseSensitive() {
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
    public void testIsKnownTagWithName() {
        Assert.assertFalse(Tag.isKnownTag("tagName"));
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
    public void testSetSelfClosing() {
        Tag tag = new Tag("tagName");
        Assert.assertFalse(tag.isSelfClosing());
        tag.setSelfClosing();
        Assert.assertTrue(tag.isSelfClosing());
    }
    
    @Test
    public void testEquals() {
        Tag tag1 = new Tag("tagName");
        Tag tag2 = new Tag("tagName");
        Assert.assertEquals(tag1, tag2);
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
    
    @Test
    public void testBlockTags() {
        for (String tagName : Tag.blockTags) {
            Tag tag = new Tag(tagName);
            Assert.assertTrue(tag.isBlock());
            Assert.assertTrue(tag.formatAsBlock());
            Assert.assertTrue(tag.canContainBlock());
        }
    }
    
    @Test
    public void testInlineTags() {
        for (String tagName : Tag.inlineTags) {
            Tag tag = new Tag(tagName);
            Assert.assertFalse(tag.isBlock());
            Assert.assertFalse(tag.formatAsBlock());
            Assert.assertFalse(tag.canContainBlock());
        }
    }
    
    @Test
    public void testEmptyTags() {
        for (String tagName : Tag.emptyTags) {
            Tag tag = new Tag(tagName);
            Assert.assertFalse(tag.canContainInline());
            Assert.assertTrue(tag.isEmpty());
            Assert.assertTrue(tag.isSelfClosing());
        }
    }
    
    @Test
    public void testFormatAsInlineTags() {
        for (String tagName : Tag.formatAsInlineTags) {
            Tag tag = new Tag(tagName);
            Assert.assertFalse(tag.formatAsBlock());
        }
    }
    
    @Test
    public void testPreserveWhitespaceTags() {
        for (String tagName : Tag.preserveWhitespaceTags) {
            Tag tag = new Tag(tagName);
            Assert.assertTrue(tag.preserveWhitespace());
        }
    }
    
    @Test
    public void testFormListedTags() {
        for (String tagName : Tag.formListedTags) {
            Tag tag = new Tag(tagName);
            Assert.assertTrue(tag.isFormListed());
        }
    }
    
    @Test
    public void testFormSubmitTags() {
        for (String tagName : Tag.formSubmitTags) {
            Tag tag = new Tag(tagName);
            Assert.assertTrue(tag.isFormSubmittable());
        }
    }
}