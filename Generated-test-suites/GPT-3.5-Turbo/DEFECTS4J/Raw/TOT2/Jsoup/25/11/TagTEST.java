import static org.junit.Assert.*;
import org.junit.Test;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsBlock() {
        Tag tagBlock = new Tag("div");
        assertTrue(tagBlock.isBlock());

        Tag tagInline = new Tag("span");
        assertFalse(tagInline.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tagBlock = new Tag("div");
        assertTrue(tagBlock.formatAsBlock());

        Tag tagInline = new Tag("span");
        assertFalse(tagInline.formatAsBlock());
    }

    // Additional test cases for other methods can be added here

}