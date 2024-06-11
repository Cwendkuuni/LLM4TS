import org.junit.*;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }
    
    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("nonexisting");
        assertEquals("nonexisting", tag.getName());
    }
    
    @Test
    public void testCanContain() {
        Tag parent = Tag.valueOf("p");
        Tag child = Tag.valueOf("em");
        assertTrue(parent.canContain(child));
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isEmpty());
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.preserveWhitespace());
    }
    
    @Test
    public void testGetImplicitParent() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("em");
        tag2.setAncestor("p");
        assertEquals(tag1, tag2.getImplicitParent());
    }
    
    @Test
    public void testIsValidParent() {
        Tag parent = Tag.valueOf("p");
        Tag child = Tag.valueOf("em");
        child.setAncestor("p");
        assertTrue(parent.isValidParent(child));
    }
    
    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertTrue(tag1.equals(tag2));
    }
    
    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.toString());
    }
}