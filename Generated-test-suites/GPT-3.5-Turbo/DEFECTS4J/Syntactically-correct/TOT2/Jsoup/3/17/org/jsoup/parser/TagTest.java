import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {
    
    @Test
    public void testName() {
        Tag tag = new Tag("test");
        assertEquals("test", tag.getName());
    }
    
    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("test");
        assertNotNull(tag);
    }
    
    @Test
    public void testCanContain() {
        Tag parent = new Tag("parent");
        Tag child1 = new Tag("child1");
        Tag child2 = new Tag("child2");
        
        parent.setAncestor("ancestor");
        assertTrue(parent.canContain(child1));
        assertFalse(parent.canContain(child2));
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
        assertTrue(tag.isData());
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
    public void testGetImplicitParent() {
        Tag tag = new Tag("test");
        assertNull(tag.getImplicitParent());
    }
    
    @Test
    public void testIsValidParent() {
        Tag parent = new Tag("parent");
        Tag child = new Tag("child");
        
        child.setAncestor("parent");
        assertTrue(parent.isValidParent(child));
    }
}