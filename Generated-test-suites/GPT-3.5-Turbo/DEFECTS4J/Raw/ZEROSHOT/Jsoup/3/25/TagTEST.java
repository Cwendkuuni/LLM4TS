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
        Tag tag = Tag.valueOf("test");
        Assert.assertEquals("test", tag.getName());
    }

    @Test
    public void testCanContain() {
        Tag parentTag = new Tag("parent");
        Tag childTag = new Tag("child");
        Assert.assertTrue(parentTag.canContain(childTag));
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("test");
        tag.isBlock = true;
        Assert.assertTrue(tag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("test");
        tag.canContainBlock = true;
        Assert.assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("test");
        tag.isBlock = false;
        Assert.assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("test");
        tag.canContainInline = false;
        tag.empty = false;
        Assert.assertTrue(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("test");
        tag.empty = true;
        Assert.assertTrue(tag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("test");
        tag.preserveWhitespace = true;
        Assert.assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag tag = new Tag("test");
        Tag parentTag = new Tag("parent");
        tag.ancestors.add(parentTag);
        Assert.assertEquals(parentTag, tag.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag parentTag = new Tag("parent");
        Tag childTag = new Tag("child");
        Assert.assertTrue(parentTag.isValidParent(childTag));
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("test");
        Tag tag2 = new Tag("test");
        Assert.assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag = new Tag("test");
        Assert.assertEquals(-96335384, tag.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("test");
        Assert.assertEquals("test", tag.toString());
    }
}