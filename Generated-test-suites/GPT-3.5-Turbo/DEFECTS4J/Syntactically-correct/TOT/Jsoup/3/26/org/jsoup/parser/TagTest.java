import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TagTest {

    private Tag tag;

    @Before
    public void setUp() {
        tag = new Tag("test");
    }

    @Test
    public void testGetName() {
        assertEquals("test", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("div");
        Tag tag3 = Tag.valueOf("unknown");

        assertTrue(tag1.isBlock());
        assertTrue(tag1.canContainBlock());
        assertFalse(tag1.isInline());

        assertTrue(tag2.isBlock());
        assertTrue(tag2.canContainBlock());
        assertFalse(tag2.isInline());

        assertFalse(tag3.isBlock());
        assertTrue(tag3.canContainBlock());
        assertTrue(tag3.isInline());
    }

    @Test
    public void testCanContain() {
        Tag blockTag = new Tag("block");
        Tag inlineTag = new Tag("inline");

        assertTrue(tag.canContain(blockTag));
        assertTrue(tag.canContain(inlineTag));
    }

    @Test
    public void testIsBlock() {
        assertFalse(tag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        assertTrue(tag.isInline());
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

        Tag parentTag = new Tag("parent");
        tag.getAncestors().add(parentTag);

        assertEquals(parentTag, tag.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag parentTag = new Tag("parent");
        assertFalse(tag.isValidParent(parentTag));

        tag.getAncestors().add(parentTag);
        assertTrue(tag.isValidParent(parentTag));
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("test");
        Tag tag2 = new Tag("test");

        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testEqualsDifferentClass() {
        assertFalse(tag.equals("test"));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("test");
        Tag tag2 = new Tag("test");

        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("test", tag.toString());
    }
}