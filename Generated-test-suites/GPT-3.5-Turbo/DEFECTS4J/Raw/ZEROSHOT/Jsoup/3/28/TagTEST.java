import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TagTest {
    private Tag tag;

    @Before
    public void setUp() {
        tag = Tag.valueOf("test");
    }

    @Test
    public void testGetName() {
        Assert.assertEquals("test", tag.getName());
    }

    @Test
    public void testCanContain() {
        Tag child = Tag.valueOf("child");
        Assert.assertTrue(tag.canContain(child));
    }

    @Test
    public void testIsBlock() {
        Assert.assertTrue(tag.isBlock());
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
    public void testPreserveWhitespace() {
        Assert.assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Assert.assertNull(tag.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag child = Tag.valueOf("child");
        Assert.assertTrue(tag.isValidParent(child));
    }

    @Test
    public void testEquals() {
        Tag other = Tag.valueOf("test");
        Assert.assertEquals(tag, other);
    }

    @Test
    public void testHashCode() {
        Tag other = Tag.valueOf("test");
        Assert.assertEquals(tag.hashCode(), other.hashCode());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("test", tag.toString());
    }
}