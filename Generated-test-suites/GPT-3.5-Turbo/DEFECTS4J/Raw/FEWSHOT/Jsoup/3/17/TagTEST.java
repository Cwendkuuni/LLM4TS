import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
        assertFalse(tag.canContainBlock());
        assertTrue(tag.canContainInline());
    }
    
    @Test
    public void testCanContain() {
        Tag parent = Tag.valueOf("p");
        Tag child1 = Tag.valueOf("div");
        Tag child2 = Tag.valueOf("a");
        assertTrue(parent.canContain(child1));
        assertFalse(parent.canContain(child2));
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("br");
        assertTrue(tag.isEmpty());
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
    }
    
    @Test
    public void testGetImplicitParent() {
        Tag child = Tag.valueOf("p");
        Tag parent = Tag.valueOf("body");
        parent.setAncestor("html");
        child.setAncestor(parent.getName());
        assertEquals(parent, child.getImplicitParent());
    }
    
    @Test
    public void testIsValidParent() {
        Tag parent = Tag.valueOf("body");
        parent.setAncestor("html");
        
        Tag child1 = Tag.valueOf("p");
        child1.setAncestor("body");
        
        Tag child2 = Tag.valueOf("img");
        child2.setAncestor("body");
        
        assertTrue(parent.isValidParent(child1));
        assertFalse(parent.isValidParent(child2));
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