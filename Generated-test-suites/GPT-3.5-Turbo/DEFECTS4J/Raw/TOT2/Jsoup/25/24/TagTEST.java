import org.junit.Test;
import org.junit.Assert;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("test");
        Assert.assertEquals("Test if getName returns correct tag name", "test", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("test");
        Tag tag2 = Tag.valueOf("test");
        Tag tag3 = Tag.valueOf("different");

        Assert.assertEquals("Test if valueOf returns same tag for the same name", tag1, tag2);
        Assert.assertNotEquals("Test if valueOf returns different tag for different names", tag1, tag3);
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("test");
        Assert.assertTrue("Test if isBlock returns true by default", tag.isBlock());
    }

    // Add more test cases for other methods as needed

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("test");
        Tag tag2 = new Tag("test");

        Assert.assertEquals("Test if equals returns true for same tags", tag1, tag2);
    }

    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("test");
        Tag tag2 = new Tag("test");

        Assert.assertEquals("Test if hashCode returns same for equal tags", tag1.hashCode(), tag2.hashCode());
    }

}