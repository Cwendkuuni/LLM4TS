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
        Tag tag = Tag.valueOf("p");
        Assert.assertEquals("p", tag.getName());
    }

    @Test
    public void testCanContain() {
        Tag parent = new Tag("div");
        Tag child1 = new Tag("p");
        Assert.assertTrue(parent.canContain(child1));

        Tag child2 = new Tag("span");
        Assert.assertTrue(parent.canContain(child2));

        Tag child3 = new Tag("h1");
        Assert.assertFalse(parent.canContain(child3));
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("div");
        Assert.assertTrue(tag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("div");
        Assert.assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("span");
        Assert.assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("script");
        Assert.assertTrue(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("img");
        Assert.assertTrue(tag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("pre");
        Assert.assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag tag = new Tag("p");
        Assert.assertNull(tag.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag parent = new Tag("div");
        Tag child = new Tag("span");
        Assert.assertTrue(parent.isValidParent(child));
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        Assert.assertEquals(tag1, tag2);
    }

    @Test
    public void testHashCode() {
        Tag tag = new Tag("p");
        Assert.assertNotNull(tag.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("div");
        Assert.assertEquals("div", tag.toString());
    }
}