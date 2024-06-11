import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {
    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }
    
    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isKnownTag());
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());
        
        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.formatAsBlock());
        
        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());
        
        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isInline());
        
        Tag tag2 = Tag.valueOf("span");
        assertTrue(tag2.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isData());
        
        Tag tag2 = Tag.valueOf("br");
        assertFalse(tag2.isData());
        
        Tag tag3 = Tag.valueOf("span");
        assertFalse(tag3.isData());
        
        Tag tag4 = Tag.valueOf("pre");
        assertFalse(tag4.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isEmpty());
        
        Tag tag2 = Tag.valueOf("img");
        assertTrue(tag2.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isSelfClosing());
        
        Tag tag2 = Tag.valueOf("img");
        assertTrue(tag2.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        assertFalse(Tag.isKnownTag("randomTag"));
        
        assertTrue(Tag.isKnownTag("div"));
        assertTrue(Tag.isKnownTag("p"));
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
        
        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.preserveWhitespace());
    }
    
    @Test
    public void testEquals() {
        Tag tag = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertTrue(tag.equals(tag2));
        
        Tag tag3 = Tag.valueOf("span");
        assertFalse(tag.equals(tag3));
    }
    
    @Test
    public void testHashCode() {
        Tag tag = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertEquals(tag.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.toString());
    }
    
    @Test
    public void testStaticInitializers() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isKnownTag());
        
        Tag tag2 = Tag.valueOf("span");
        assertTrue(tag2.isKnownTag());
    }
    
    @Test
    public void testSetSelfClosing() {
        Tag tag = Tag.valueOf("div");
        Tag selfClosingTag = tag.setSelfClosing();
        
        assertTrue(selfClosingTag.isSelfClosing());
    }
}