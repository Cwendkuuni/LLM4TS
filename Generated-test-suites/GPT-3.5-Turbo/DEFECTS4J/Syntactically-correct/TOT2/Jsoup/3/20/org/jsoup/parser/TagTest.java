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
        Tag tag = Tag.valueOf("div");
        assertNotNull(tag);
    }
    
    @Test
    public void testCanContain() {
        Tag parent = new Tag("parent");
        Tag childBlock = new Tag("childBlock");
        Tag childInline = new Tag("childInline");
        
        parent.canContainBlock = true;
        assertFalse(parent.canContain(childInline));
        
        parent.canContainInline = true;
        assertTrue(parent.canContain(childInline));
        
        parent.optionalClosing = true;
        assertFalse(parent.canContain(parent));
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = new Tag("test");
        assertTrue(tag.isBlock());
    }
    
    // Add more test cases for other methods as well
    
}