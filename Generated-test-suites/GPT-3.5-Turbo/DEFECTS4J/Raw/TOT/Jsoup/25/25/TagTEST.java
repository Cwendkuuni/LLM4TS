
import org.jsoup.parser.Tag;
import org.junit.Test;
import org.junit.Assert;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        Assert.assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOfKnownTag() {
        Tag tag = Tag.valueOf("p");
        Assert.assertTrue(tag.isBlock());
        Assert.assertTrue(tag.formatAsBlock());
        Assert.assertTrue(tag.canContainBlock());
        Assert.assertTrue(tag.canContainInline());
        Assert.assertFalse(tag.isEmpty());
        Assert.assertFalse(tag.isSelfClosing());
        Assert.assertFalse(tag.preserveWhitespace());
        Assert.assertTrue(tag.isKnownTag());
    }

    @Test
    public void testValueOfUnknownTag() {
        Tag tag = Tag.valueOf("mytag");
        Assert.assertFalse(tag.isBlock());
        Assert.assertTrue(tag.formatAsBlock());
        Assert.assertTrue(tag.canContainBlock());
        Assert.assertTrue(tag.canContainInline());
        Assert.assertFalse(tag.isEmpty());
        Assert.assertFalse(tag.isSelfClosing());
        Assert.assertFalse(tag.preserveWhitespace());
        Assert.assertFalse(tag.isKnownTag());
    }

    @Test
    public void testIsBlock() {
        Tag blockTag = Tag.valueOf("p");
        Tag inlineTag = Tag.valueOf("a");

        Assert.assertTrue(blockTag.isBlock());
        Assert.assertFalse(inlineTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag blockTag = Tag.valueOf("div");
        Tag inlineTag = Tag.valueOf("a");

        Assert.assertTrue(blockTag.formatAsBlock());
        Assert.assertFalse(inlineTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        Assert.assertTrue(tag.canContainBlock());

        tag = Tag.valueOf("p");
        Assert.assertFalse(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag blockTag = Tag.valueOf("p");
        Tag inlineTag = Tag.valueOf("a");

        Assert.assertFalse(blockTag.isInline());
        Assert.assertTrue(inlineTag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("img");

        Assert.assertFalse(tag1.isData());
        Assert.assertFalse(tag2.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag1 = Tag.valueOf("img");
        Tag tag2 = Tag.valueOf("div");

        Assert.assertTrue(tag1.isEmpty());
        Assert.assertFalse(tag2.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag1 = Tag.valueOf("img");
        Tag tag2 = Tag.valueOf("div");

        Assert.assertTrue(tag1.isSelfClosing());
        Assert.assertFalse(tag2.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag knownTag = Tag.valueOf("div");
        Tag unknownTag = Tag.valueOf("mytag");

        Assert.assertTrue(knownTag.isKnownTag());
        Assert.assertFalse(unknownTag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithName() {
        Assert.assertTrue(Tag.isKnownTag("div"));
        Assert.assertFalse(Tag.isKnownTag("mytag"));
    }

    @Test
    public void testPreserveWhiteSpace() {
        Tag tag = Tag.valueOf("pre");
        Assert.assertTrue(tag.preserveWhitespace());

        tag = Tag.valueOf("div");
        Assert.assertFalse(tag.preserveWhitespace());
    }
}

