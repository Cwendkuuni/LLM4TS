import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Tag;

import static org.junit.Assert.*;

public class TagTest {

    private Tag tag;

    @Before
    public void setUp() {
        tag = new Tag("tag");
    }

    @Test
    public void testGetName() {
        assertEquals("tag", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tagName = Tag.valueOf("tagName");
        assertNotNull(tagName);
        assertEquals("tagName", tagName.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfNullTagName() {
        Tag.valueOf(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfEmptyTagName() {
        Tag.valueOf("");
    }

    @Test
    public void testCanContain() {
        Tag child = new Tag("child");
        assertTrue(tag.canContain(child));
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
    public void testPreserveWhitespace() {
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        assertNull(tag.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag child = new Tag("child");
        assertTrue(tag.isValidParent(child));
    }

    @Test
    public void testEquals() {
        assertEquals(tag, tag);
        assertNotEquals(tag, "tag");
        assertNotEquals(tag, null);
    }

    @Test
    public void testHashCode() {
        assertEquals(tag.hashCode(), tag.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("tag", tag.toString());
    }
}