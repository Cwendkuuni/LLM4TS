import static org.junit.Assert.*;
import org.junit.Test;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("test");
        assertEquals("test", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("test");
        assertNotNull(tag);
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("test");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("test");
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("test"));
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("test");
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    // Add more test cases for other methods testing typical use cases, edge cases, and error scenarios

}