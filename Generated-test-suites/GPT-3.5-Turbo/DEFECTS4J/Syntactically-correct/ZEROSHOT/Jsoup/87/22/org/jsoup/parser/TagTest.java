import org.jsoup.helper.Validate;
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.ParseSettings;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("testTag");
        Assert.assertEquals("testTag", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("testTag", ParseSettings.preserveCase);
        Assert.assertNotNull(tag);
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("testTag");
        Assert.assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("testTag");
        Assert.assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("testTag");
        Assert.assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("testTag");
        Assert.assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("testTag");
        Assert.assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("testTag");
        Assert.assertFalse(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("testTag");
        Assert.assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag = new Tag("testTag");
        Assert.assertFalse(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithTagName() {
        boolean result = Tag.isKnownTag("testTag");
        Assert.assertFalse(result);
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("testTag");
        Assert.assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag = new Tag("testTag");
        Assert.assertFalse(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("testTag");
        Assert.assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("testTag");
        Tag result = tag.setSelfClosing();
        Assert.assertEquals(tag, result);
        Assert.assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("testTag");
        Tag tag2 = new Tag("testTag");
        Assert.assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag = new Tag("testTag");
        Assert.assertEquals(tag.getName().hashCode(), tag.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("testTag");
        Assert.assertEquals("testTag", tag.toString());
    }
}