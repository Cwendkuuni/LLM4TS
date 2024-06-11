import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {
    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }
    
    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertEquals(tag, Tag.valueOf("p"));
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());
        assertFalse(Tag.valueOf("a").isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.canContainBlock());
        assertFalse(Tag.valueOf("a").canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isInline());
        assertTrue(Tag.valueOf("a").isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isData());
        assertFalse(Tag.valueOf("a").isData());
        assertTrue(Tag.valueOf("span").isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isEmpty());
        assertTrue(Tag.valueOf("img").isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.preserveWhitespace());
        assertTrue(Tag.valueOf("pre").preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag tag = Tag.valueOf("p");
        assertNull(tag.getImplicitParent());
        Tag parent = Tag.valueOf("div");
        tag = Tag.valueOf("span");
        parent.setAncestor("div");
        tag.setAncestor("div");
        assertEquals(parent, tag.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag parent = Tag.valueOf("div");
        Tag child = Tag.valueOf("span");
        assertTrue(parent.isValidParent(child));
        
        parent = Tag.valueOf("p");
        assertFalse(parent.isValidParent(child));
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertEquals(tag1, tag2);
        
        Tag tag3 = Tag.valueOf("div");
        assertNotEquals(tag1, tag3);
    }

    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertEquals(tag1.hashCode(), tag2.hashCode());
        
        Tag tag3 = Tag.valueOf("div");
        assertNotEquals(tag1.hashCode(), tag3.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.toString());
    }
}