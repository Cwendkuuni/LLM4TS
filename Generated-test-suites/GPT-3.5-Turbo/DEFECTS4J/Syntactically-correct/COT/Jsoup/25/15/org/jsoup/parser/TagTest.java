
import static org.junit.Assert.*;
import org.junit.Test;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("span");
        assertFalse(tag.formatAsBlock());
    }

    // ... test cases for other methods

    @Test
    public void testToString() {
        Tag tag = new Tag("a");
        assertEquals("a", tag.toString());
    }

    @Test
    public void testValueOfUnknownTag() {
        Tag tag = Tag.valueOf("unknown");
        assertEquals("unknown", tag.getName());
        assertFalse(tag.isBlock());
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testValueOfKnownTag() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    // Add more edge case test methods...

}
