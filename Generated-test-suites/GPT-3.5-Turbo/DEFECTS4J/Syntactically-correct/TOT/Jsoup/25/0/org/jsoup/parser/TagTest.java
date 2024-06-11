import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("span");
        assertFalse(tag.isBlock());
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());
        assertFalse(tag.isInline());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("em");
        assertFalse(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("i");
        assertFalse(tag.isBlock());
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag1 = Tag.valueOf("script");
        Tag tag2 = Tag.valueOf("img");
        assertFalse(tag1.isData());
        assertFalse(tag2.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("img");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag1 = Tag.valueOf("img");
        Tag tag2 = Tag.valueOf("div");
        assertTrue(tag1.isSelfClosing());
        assertFalse(tag2.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("input"));
        assertFalse(Tag.isKnownTag("custom"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("span");
        Tag tag2 = new Tag("span");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("span");
        Tag tag2 = new Tag("span");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.toString());
    }
}