import org.junit.Test;

import static org.junit.Assert.*;

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
        assertEquals("test", tag.getName());

        // Test for unknown tag
        Tag unknownTag = Tag.valueOf("unknown");
        assertTrue(unknownTag.isInline());
        assertTrue(unknownTag.canContainBlock());
    }

    @Test
    public void testCanContain() {
        Tag parentTag = new Tag("parent");
        Tag childTag = new Tag("child");
        assertTrue(parentTag.canContain(childTag));

        // Test edge case where parent can only contain specific child tags
        parentTag.setAncestor("specificChild");
        assertFalse(parentTag.canContain(childTag));
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("test");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("test");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("test");
        assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("test");
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("test");
        assertFalse(tag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("test");
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testIsValidParent() {
        Tag parentTag = new Tag("parent");
        Tag childTag = new Tag("child");
        assertTrue(parentTag.isValidParent(childTag));

        // Test invalid parent-child relationship
        assertFalse(childTag.isValidParent(parentTag));
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("test");
        Tag tag2 = new Tag("test");
        assertEquals(tag1, tag2);
    }

    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("test");
        Tag tag2 = new Tag("test");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("test");
        assertEquals("test", tag.toString());
    }
}