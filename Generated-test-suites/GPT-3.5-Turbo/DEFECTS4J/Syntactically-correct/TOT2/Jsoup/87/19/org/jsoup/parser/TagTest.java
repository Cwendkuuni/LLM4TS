import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("testTag");
        assertEquals("testTag", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("span");
        assertEquals(false, tag1.isBlock());
        assertEquals(true, tag2.isBlock());
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("testTag");
        assertEquals(true, tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("testTag");
        assertEquals(true, tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("testTag");
        assertEquals(true, tag.canContainBlock());
    }

    // Add more test cases for other methods

}