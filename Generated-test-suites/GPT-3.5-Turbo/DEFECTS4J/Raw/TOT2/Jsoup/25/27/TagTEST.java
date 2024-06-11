import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("unknownTag");
        
        assertFalse(tag1.isBlock());
        assertTrue(tag2.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("script");
        
        assertTrue(tag1.formatAsBlock());
        assertFalse(tag2.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("span");
        
        assertTrue(tag1.canContainBlock());
        assertFalse(tag2.canContainBlock());
    }

    // Add more test cases for other methods

}