import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {
    
    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }
    
    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
        assertFalse(tag.isBlock());
        assertTrue(tag.canContainBlock());
    }
    
    @Test
    public void testCanContain() {
        Tag parentTag = Tag.valueOf("div");
        Tag childTag = Tag.valueOf("p");
        assertTrue(parentTag.canContain(childTag));
        
        parentTag = Tag.valueOf("p");
        childTag = Tag.valueOf("div");
        assertFalse(parentTag.canContain(childTag));
        
        parentTag = Tag.valueOf("div");
        childTag = Tag.valueOf("img");
        assertFalse(parentTag.canContain(childTag));
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("span");
        assertTrue(tag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("img");
        assertTrue(tag.isEmpty());
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
    }
    
    @Test
    public void testGetImplicitParent() {
        Tag tag = Tag.valueOf("p");
        assertNull(tag.getImplicitParent());
    }
    
    @Test
    public void testIsValidParent() {
        Tag parentTag = Tag.valueOf("div");
        Tag childTag = Tag.valueOf("span");
        assertTrue(parentTag.isValidParent(childTag));
        
        parentTag = Tag.valueOf("p");
        childTag = Tag.valueOf("div");
        assertFalse(parentTag.isValidParent(childTag));
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
    
    @Test
    public void testTagCreation() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("span");
        assertTrue(tag1.canContainInline());
        assertFalse(tag2.canContainBlock());
    }
}