import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {
    
    @BeforeClass
    public static void setUpClass() {
        // Set up any necessary test data or configurations
    }
    
    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("example");
        assertEquals("example", tag.getName());
    }
    
    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("div");
        Tag tag3 = Tag.valueOf("example");
        
        assertTrue(tag1.isBlock());
        assertTrue(tag1.canContainBlock());
        
        assertTrue(tag2.isBlock());
        assertTrue(tag2.canContainBlock());
        
        assertFalse(tag3.isBlock());
        assertTrue(tag3.canContainBlock());
    }
    
    @Test
    public void testIsBlock() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("img");
        
        assertTrue(tag1.isBlock());
        assertFalse(tag2.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("img");
        
        assertTrue(tag1.formatAsBlock());
        assertFalse(tag2.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("img");
        
        assertTrue(tag1.canContainBlock());
        assertFalse(tag2.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("img");
        
        assertFalse(tag1.isInline());
        assertTrue(tag2.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("img");
        Tag tag3 = Tag.valueOf("example");
        
        assertFalse(tag1.isData());
        assertFalse(tag2.isData());
        assertFalse(tag3.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("img");
        
        assertFalse(tag1.isEmpty());
        assertFalse(tag2.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("img");
        
        assertFalse(tag1.isSelfClosing());
        assertFalse(tag2.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("example"));
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag1 = Tag.valueOf("pre");
        Tag tag2 = Tag.valueOf("div");
        
        assertTrue(tag1.preserveWhitespace());
        assertFalse(tag2.preserveWhitespace());
    }
}