import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {
    private Tag tag;

    @Before
    public void setup() {
        tag = new Tag("test");
    }

    @Test
    public void testGetName() {
        assertEquals("test", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag newTag = Tag.valueOf("test");
        assertEquals(tag, newTag);
    }

    @Test
    public void testIsBlock() {
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithString() {
        assertTrue(Tag.isKnownTag("test"));
    }

    @Test
    public void testPreserveWhitespace() {
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testSetSelfClosing() {
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag newTag = new Tag("test");
        assertTrue(tag.equals(newTag));
    }

    @Test
    public void testHashCode() {
        Tag newTag = new Tag("test");
        assertEquals(tag.hashCode(), newTag.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("test", tag.toString());
    }
}